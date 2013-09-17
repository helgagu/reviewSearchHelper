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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
     * Find a BrowseNodeId name by finding the name of the bin for that browseNodeId.
     *
     * @param response     the response object of the ItemSearch with responseGroup = BinSearch
     * @param browseNodeId the id of a specific browseNode (category for a subject)
     * @return string with the browseNodeId name
     */
    public String getBrowseNodeIdName(ItemSearchResponse response, String browseNodeId) {

        if (response != null) {
            for (Items items : response.getItems()) {
                for (SearchBinSet bin : items.getSearchBinSets().getSearchBinSet()) {
                    for (Bin binDetails : bin.getBin()) {
                        for (Bin.BinParameter param : binDetails.getBinParameter()) {
                            if (browseNodeId.equalsIgnoreCase(param.getValue())) {
                                return binDetails.getBinName();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * This fetches all the browseNodesIds in a bin list returned by an ItemSearch with responseGroup = BinSearch
     * if the binItemCount is more than 100 and needs to be narrowed more. <p><p>
     * <p/>
     * Exclusion criteria can also be applied, the exclusion criteria applied are defined in ExclusionCriteria.excludeBrowseNodeId(bin name).
     * The exclusion criteria were analyzed manually.
     *
     * @param response the response object of the ItemSearch with responseGroup = BinSearch
     */
    public void setBrowseNodeIds(ItemSearchResponse response, BinsearchResults binsearchResults) throws Exception{

        for (Items items : response.getItems()) {
            for (SearchBinSet bin : items.getSearchBinSets().getSearchBinSet()) {
                if(bin.getBin().size() == 0){
                    String searchParamBrowseNode = binsearchResults.getSearchParamsBrowseNodeId();

                    BrowsenodesEntityManager browsenodesEm = new BrowsenodesEntityManager();
                    Browsenodes bn = browsenodesEm.getBrowsenodes(searchParamBrowseNode);

                    if(bn.getExclusionReason() == null){
                        setAllBrowseNodesAsin(searchParamBrowseNode, binsearchResults, bn);
                    }

                }

                for (Bin binDetails : bin.getBin()) {
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

                    if(resultsLessThanHundred(binItemCount)){
                        if(exclusionReason == null){
                          setAllBrowseNodesAsin(browseNodeId, binsearchResults, browsenodes);
                        }
                    } else{
                        getAllBrowseNodes(binsearchResults.getKeyword(), binsearchResults.getAmazonLocale(), browseNodeId);
                    }
                }
            }
        }
    }

    public void setAllBrowseNodesAsin(String browseNodesId, BinsearchResults binsearchResults, Browsenodes browsenodes) throws Exception{

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

    private ItemSearchResponse getBrowseNodeAsins(String browseNodesId, Browsenodes browsenodes, String keyword, String endpoint, String page) throws Exception {
        ItemSearchResponse response = amazonClient.sendBinSearchRequest(keyword, browseNodesId, page, endpoint);
        setBinSearchResults(response, keyword, endpoint, Boolean.FALSE);
        setBrowseNodeAsin(browsenodes, response);
        return response;
    }

    private void setBrowseNodeAsin(Browsenodes browsenodes, ItemSearchResponse response) {

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
                BrowsenodesAsin browsenodesAsin = browsenodesAsinEm.asin(asinNumber, asin, browsenodes);
                browsenodesAsinEm.persist(browsenodesAsin);
            }
        }
    }

    public int getTotalPages(ItemSearchResponse response){

        for(Items item: response.getItems()){
            return item.getTotalPages().intValue();
        }
        return 0;
    }

    /**
     * Compares the binItemCount to see if it is less than 100.
     *
     * @param binItemCount the amount of result items in a specific bin
     * @return true=binItemCount is less than 100, false=binItemCount is equal or smaller than 100
     */
    public Boolean resultsLessThanHundred(Long binItemCount) {
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
     * @throws Exception
     */
    public void getAllBrowseNodes(String keyword, String endpoint, String browseNodeId) throws Exception{

        ItemSearchResponse response = amazonClient.sendBinSearchRequest(keyword, browseNodeId, null, endpoint);
        setBinSearchResults(response, keyword, endpoint, Boolean.TRUE);
    }


    /**
     * Set the values in a binsearch object
     * @param response the amazon itemSearchResponse
     * @param keyword the serach keyword
     * @param endpoint the amazon locale, the ending of the url http://amazon. , e.g. com, co.uk etc...
     * @throws Exception
     */
    public void setBinSearchResults(ItemSearchResponse response, String keyword, String endpoint, Boolean getBrowseNodes) throws Exception{

        BinsearchResultsEntityManager bin = new BinsearchResultsEntityManager();
        String availability = "";
        String merchantId = "";
        String sort = "";
        String searchIndex = "";
        String responseGroup = "";
        String browseNodeId = "";
        String powerSearch = "";
        String timestamp = "";
        Long totalResults = null;
        Long totalPages = null;

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
           }
        }

        for(Items item: response.getItems()){
            totalPages = item.getTotalPages().longValue();
            totalResults = item.getTotalResults().longValue();
        }

        BinsearchResults binsearchresults = bin.binsearchResults(endpoint, keyword, util.unmarshalResponse(response), availability,
                browseNodeId, merchantId, powerSearch, responseGroup, searchIndex, sort, totalResults, totalPages, timestamp);
        bin.persist(binsearchresults);
        if(getBrowseNodes){
            setBrowseNodeIds(response, binsearchresults);
        }
    }
}