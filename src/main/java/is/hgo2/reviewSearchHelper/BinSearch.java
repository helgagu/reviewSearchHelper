package is.hgo2.reviewSearchHelper;

import is.hgo2.reviewSearchHelper.amazonMessages.*;
import is.hgo2.reviewSearchHelper.entities.Asin;
import is.hgo2.reviewSearchHelper.entities.BinsearchResults;
import is.hgo2.reviewSearchHelper.entities.Browsenodes;
import is.hgo2.reviewSearchHelper.entities.BrowsenodesAsin;
import is.hgo2.reviewSearchHelper.entityManagers.AsinEntityManager;
import is.hgo2.reviewSearchHelper.entityManagers.BinsearchResultsEntityManager;
import is.hgo2.reviewSearchHelper.entityManagers.BrowsenodesAsinEntityManager;
import is.hgo2.reviewSearchHelper.entityManagers.BrowsenodesEntityManager;
import is.hgo2.reviewSearchHelper.util.Constants;
import is.hgo2.reviewSearchHelper.util.ExclusionCriteria;
import is.hgo2.reviewSearchHelper.util.Util;

/**
 * Class for the binSearch, browseNodes and ASIN extraction from the Amazon Product Advertising API
 * @author Helga Gudrun Oskarsdottir
 */
public class BinSearch {
    private final Util util;
    private final AmazonClient amazonClient;

    /**
     * Constructor which initiates util and amazonClient
     * @throws Exception
     */
    public BinSearch() throws Exception{
        this.util = new Util();
        this.amazonClient = new AmazonClient(util);
    }


    /**
     * This fetches all the browseNodesIds in a bin list returned by an ItemSearch with responseGroup = BinSearch
     * if the binItemCount is more than 100 and needs to be narrowed more. <p><p>
     * <p/>
     * If there are no more browseNode children or if the binItemCount is less than 100, then the first 100 ASIN is extracted (amazon standard item number)
     * from the result set. Amazon Product Advertising API does not allow fetching more results than that.
     *
     * After this method these database tables have been filled with data <p>
     *     - browsenodes <p>
     *     - browsenodesAsin <p>
     *     - asin <p>
     *
     * @param response the response object of the ItemSearch with responseGroup = BinSearch
     * @param binsearchResults the saved binsearchResults object for this response.
     */
    private void setBrowseNodeIds(ItemSearchResponse response, BinsearchResults binsearchResults) throws Exception{

        for (Items items : response.getItems()) {
            for (SearchBinSet bin : items.getSearchBinSets().getSearchBinSet()) {
                if(bin.getBin().size() == 0){
                    setAllBrowseNodesAsinForNoChildBrowseNodes(binsearchResults);
                }

                for (Bin binDetails : bin.getBin()) {
                    Browsenodes browsenodes = setBrowsenode(response, binsearchResults, binDetails);

                    if(resultsLessThanHundred(browsenodes.getBinItemCount())){
                        if(browsenodes.getExclusionReason() == null){
                          setAllBrowseNodesAsin(browsenodes.getBrowseNodeId(), binsearchResults, browsenodes);
                        }
                    } else{
                        getAllBrowseNodes(binsearchResults.getKeyword(), binsearchResults.getAmazonLocale(), browsenodes.getBrowseNodeId());
                    }
                }
            }
        }
    }

    /**
     * Inserts into the browsenodes database table.
     * @param response the itemSearchResponse from amazon
     * @param binsearchResults the object from the database table binsearch_results for the response
     * @param binDetails the bin from the itemSearchResponse to be saved in browsenodes
     * @return browsenodes object that has been saved in the database
     */
    private Browsenodes setBrowsenode(ItemSearchResponse response, BinsearchResults binsearchResults, Bin binDetails) {
        BrowsenodesEntityManager browseNodesEm = new BrowsenodesEntityManager();

        Long binItemCount = binDetails.getBinItemCount().longValue();
        String binName = binDetails.getBinName();
        String browseNodeId = "";
        String parentBrowseNodeId = "";

        for (Bin.BinParameter param : binDetails.getBinParameter()) {
            browseNodeId = param.getValue();
        }
        for(Arguments.Argument argument: response.getOperationRequest().getArguments().getArgument()){
            if (argument.getName().equalsIgnoreCase(Constants.BROWSENODE_PARAMETER)) {
                parentBrowseNodeId = argument.getValue();
            }
        }
        String exclusionReason = ExclusionCriteria.excludeBrowseNodeId(binName);

        Browsenodes browsenodes = browseNodesEm.browsenodes(binItemCount, binName, browseNodeId, parentBrowseNodeId, exclusionReason, binsearchResults);
        browseNodesEm.persist(browsenodes);
        return browsenodes;
    }

    /**
     * When a browsenode has no more children, then extract the ASIN from the response.
     *
     * Get the first 100 results by sending a request for result pages 1-10
     * @param binsearchResults the binsearch_results object saved in the database for the response
     * @throws Exception
     */
    private void setAllBrowseNodesAsinForNoChildBrowseNodes(BinsearchResults binsearchResults) throws Exception {
        String searchParamBrowseNode = binsearchResults.getSearchParamsBrowseNodeId();

        BrowsenodesEntityManager browsenodesEm = new BrowsenodesEntityManager();
        Browsenodes bn = browsenodesEm.getBrowsenodes(searchParamBrowseNode);

        if(bn.getExclusionReason() == null){
            setAllBrowseNodesAsin(searchParamBrowseNode, binsearchResults, bn);
        }
    }

    /**
     * Gets all the ASIN from the browseNodes responses, result pages 1-10, and saves into databasetable browsenodesAsin
     * and database table asin (if it doesn't exist there already).
     * The amazon product advertising api only allows fetching of the first 100 results.
     * @param browseNodesId the browsenodesId needed as a search parameter
     * @param binsearchResults the binsearchResults for the resposne
     * @param browsenodes the browsenodes object of the response
     * @throws Exception
     */
    private void setAllBrowseNodesAsin(String browseNodesId, BinsearchResults binsearchResults, Browsenodes browsenodes) throws Exception{

        String keyword = binsearchResults.getKeyword();
        String endpoint = binsearchResults.getAmazonLocale();

        ItemSearchResponse response = getBrowseNodeAsins(browseNodesId, browsenodes, keyword, endpoint, null);

        for(int i=1; i < getTotalPages(response); i++){

            if(i > 10){
                break; //Amazon only allows fetching results from the first 10 pages.
            }
            getBrowseNodeAsins(browseNodesId, browsenodes, keyword, endpoint, String.valueOf(i));

        }
    }

    /**
     * Sends a search request for each result page for a specific browsenodeId. This inserts each search response in binSearch_results
     * and extracts the aain to insert into browseNodesAsin
     * @param browseNodesId the browsenodesId to use as a search parameter
     * @param browsenodes the browsenodes object of the response
     * @param keyword the search keyword
     * @param endpoint the search endpoint
     * @param page the result page number
     * @return ItemSearchResponse
     * @throws Exception
     */
    private ItemSearchResponse getBrowseNodeAsins(String browseNodesId, Browsenodes browsenodes, String keyword, String endpoint, String page) throws Exception {
        ItemSearchResponse response = amazonClient.sendBinSearchRequest(keyword, browseNodesId, page, endpoint);
        BinsearchResults bin = setBinSearchResults(response, keyword, endpoint, Boolean.FALSE);
        setBrowseNodeAsin(browsenodes, response, bin);
        return response;
    }

    /**
     * Inserts into the database tables, browseNodesAsin and ASIN.
     *  - ASIN is a table for a list of unique ASIN's
     *  - browseNodesAsin is a table for all the ASIN's per browseNodes search
     * @param browsenodes browsenodes object which the ASINs are being extracted from
     * @param response the search response
     * @param bin the binsearch_results object for the search request
     */
    private void setBrowseNodeAsin(Browsenodes browsenodes, ItemSearchResponse response, BinsearchResults bin) {

        String asinNumber;
        Asin asin;
        for(Items items : response.getItems()){
            for(Item item : items.getItem()){
                BrowsenodesAsinEntityManager browsenodesAsinEm = new BrowsenodesAsinEntityManager();
                AsinEntityManager asinEm = new AsinEntityManager();

                asinNumber = item.getASIN();
                asin = asinEm.getAsin(asinNumber);
                if(asin == null){
                    asin = asinEm.asin(asinNumber);
                    asinEm.persist(asin);
                }
                BrowsenodesAsin browsenodesAsin = browsenodesAsinEm.asin(asinNumber, asin, browsenodes, bin);
                browsenodesAsinEm.persist(browsenodesAsin);
            }
        }
    }

    /**
     * Gets the totalpages value for a specific search response
     * @param response the response to get the total pages for
     * @return totalpages as int
     */
    private int getTotalPages(ItemSearchResponse response){

        int totalpages = 0;
        for(Items item: response.getItems()){
             totalpages = item.getTotalPages().intValue();
        }
        return totalpages;
    }

    /**
     * Compares the binItemCount to see if it is less than 100.
     *
     * @param binItemCount the amount of result items in a specific bin
     * @return true=binItemCount is less than 100, false=binItemCount is equal or smaller than 100
     */
    private Boolean resultsLessThanHundred(Long binItemCount) {
        if (binItemCount < 100) {
            //If binItemCount is greater than 100 return true. CompareTo returns 1 if greater than.
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    /**
     * This fetches a bin list for a standard search request for a specific keyword using the binSearch request.
     * For each bin in the bin list the browseNodeIds are extracted (the subject categories e.g. Business & Investing, Computers & Technology)  <p><p>
     *
     * A binSearch request is sent for each browseNodeId, extracting the child browseNodeIds, going down the hierarchical organization of the categories (browseNodes) until there
     * are no more child browseNodes or the binItemCount is less than 100.   <p><p>
     *
     * This is done because Amazon does not allow to fetch more than 100 results from an ItemSearch request. The categories are used to narrow the search for each request to keep the result set below 100.
     * A request will then be sent for each child browseNode which is not excluded by the exclusion criteria.
     *
     * @param keyword the search keyword, this is either Productivity, Personal Productivity, Efficient, Effective(ness) or knowledge worker productivity
     * @param endpoint the amazon locale endooint e.g. com, co.uk
     * @param browseNodeId the browseNodeId search parameter
     * @throws Exception
     */
    public void getAllBrowseNodes(String keyword, String endpoint, String browseNodeId) throws Exception{

        ItemSearchResponse response = amazonClient.sendBinSearchRequest(keyword, browseNodeId, null, endpoint);
        setBinSearchResults(response, keyword, endpoint, Boolean.TRUE);
    }


    /**
     * Set the values in a binsearch object
     * @param response the amazon itemSearchResponse
     * @param keyword the search keyword
     * @param endpoint the amazon locale, the ending of the url http://amazon. , e.g. com, co.uk etc...
     * @param getBrowseNodes true=call method setBrowseNodeIds to get the browsenodes children, false = do not call the method, the binItemCount < 100 we do not need to narrow the search more.
     * @throws Exception
     */
    private BinsearchResults setBinSearchResults(ItemSearchResponse response, String keyword, String endpoint, Boolean getBrowseNodes) throws Exception{

        BinsearchResultsEntityManager bin = new BinsearchResultsEntityManager();
        Long totalResults = null;
        Long totalPages = null;

        for(Items item: response.getItems()){
            totalPages = item.getTotalPages().longValue();
            totalResults = item.getTotalResults().longValue();
        }

        BinsearchResults binsearchresults = bin.binsearchResults(endpoint, keyword, util.unmarshalResponse(response), totalResults, totalPages);
        binsearchresults = setSearchParamsInBinSearchResults(response, bin, binsearchresults);
        bin.persist(binsearchresults);

        if(getBrowseNodes){
            setBrowseNodeIds(response, binsearchresults);
        }
        return binsearchresults;
    }

    /**
     * Add search parameters from argument in the response to the binsearch_result object
     * @param response the search response
     * @param bin the binsearchResultsEntityManager to work with the same binsearchresults object
     * @param binsearchresults the binsearchresults object to add the search parameters to
     * @return binsearchresults with the added values
     */
    private BinsearchResults setSearchParamsInBinSearchResults(ItemSearchResponse response, BinsearchResultsEntityManager bin, BinsearchResults binsearchresults) {
        String availability = "";
        String merchantId = "";
        String sort = "";
        String searchIndex = "";
        String responseGroup = "";
        String browseNodeId = "";
        String powerSearch = "";
        String timestamp = "";
        String itemPage = "";

        for(Arguments.Argument argument: response.getOperationRequest().getArguments().getArgument()){
            if (argument.getName().equalsIgnoreCase(Constants.TIMESTAMP_PARAMETER)) {
                timestamp = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.SEARCHINDEX_PARAMETER)) {
                searchIndex = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.SORT_PARAMETER)) {
                sort = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.POWER_PARAMETER)) {
                powerSearch = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.AVAILABILITY_PARAMETER)) {
                availability = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.MERCHANTID_PARAMETER)) {
                merchantId = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.BROWSENODE_PARAMETER)) {
                browseNodeId = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.RESPONSEGROUP_PARAMETER)) {
                responseGroup = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.ITEMPAGE_PARAMETER)){
                itemPage = argument.getValue();
            }
        }
        binsearchresults = bin.addSearchParamsBinsearchResults(binsearchresults, availability, browseNodeId, merchantId, powerSearch, responseGroup, searchIndex, sort, itemPage, timestamp);
        return binsearchresults;
    }
}