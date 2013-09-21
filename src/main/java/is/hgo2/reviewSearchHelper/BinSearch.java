package is.hgo2.reviewSearchHelper;

import is.hgo2.reviewSearchHelper.amazonMessages.*;
import is.hgo2.reviewSearchHelper.entities.*;
import is.hgo2.reviewSearchHelper.entityManagers.*;
import is.hgo2.reviewSearchHelper.util.Constants;
import is.hgo2.reviewSearchHelper.util.ExclusionCriteria;
import is.hgo2.reviewSearchHelper.util.Util;

import java.text.SimpleDateFormat;

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
     * If there are no more browseNode children or if the binItemCount is less than 100, then the browse node is marked as to be searched by inserting into childbrowsenodestosearch.
     *
     *
     * After this method these database tables have been filled with data <p>
     *     - browsenodes <p>
     *     - childbrowsenodestoSearch
     *
     * @param response the response object of the ItemSearch with responseGroup = BinSearch
     * @param binsearchResults the saved binsearchResults object for this response.
     */
    private void setBrowseNodeIds(ItemSearchResponse response, BinsearchResults binsearchResults) throws Exception{

        for (Items items : response.getItems()) {
            for (SearchBinSet bin : items.getSearchBinSets().getSearchBinSet()) {
                if(bin.getBin().size() == 0){
                   setChildbrowsenodetosearchForNoChildBrowseNode(binsearchResults);
                }

                for (Bin binDetails : bin.getBin()) {

                    Browsenodes browsenodes = setBrowsenode(binsearchResults, binDetails);

                    if(resultsLessThanHundred(browsenodes.getBinItemCount())){
                        if(browsenodes.getExclusionReason() == null){
                          setChildbrowsenodetosearch(binsearchResults, browsenodes);
                        }
                    } else{
                        getAllBrowseNodes(binsearchResults.getKeyword(), binsearchResults.getAmazonLocale(), browsenodes.getBrowseNodeId());
                    }
                }
            }
        }
    }

    private void setChildbrowsenodetosearch(BinsearchResults bin, Browsenodes bn){

        ChildbrowsenodestosearchEntityManager em = new ChildbrowsenodestosearchEntityManager();
        if(!hasBeenMarked(bn.getBrowseNodeId(), bin.getKeyword(), bin.getAmazonLocale())){
            em.persist(em.childbrowsenodestosearch(bn.getBinItemCount(), bn.getBinName(), bn.getBrowseNodeId(), bn.getParentBrowseNode(), bin.getAmazonLocale(), bin.getKeyword(), bn));
        }
    }

    private Boolean hasBeenMarked(String browsenodeId, String keyword, String endpoint){
        ChildbrowsenodestosearchEntityManager em = new ChildbrowsenodestosearchEntityManager();
        Childbrowsenodestosearch item = em.getChildbrowsenodestosearch(browsenodeId, keyword, endpoint);
        if(item != null){
            System.out.println("Item already exists in childbrowsenodestosearch");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Inserts into the browsenodes database table.
     * @param binsearchResults the object from the database table binsearch_results for the response
     * @param binDetails the bin from the itemSearchResponse to be saved in browsenodes
     * @return browsenodes object that has been saved in the database
     */
    private Browsenodes setBrowsenode(BinsearchResults binsearchResults, Bin binDetails) {
        BrowsenodesEntityManager browseNodesEm = new BrowsenodesEntityManager();

        Long binItemCount = binDetails.getBinItemCount().longValue();
        String binName = binDetails.getBinName();
        String browseNodeId = "";
        String parentBrowseNodeId = binsearchResults.getSearchParamsBrowseNodeId();
        String keyword = binsearchResults.getKeyword();

        for (Bin.BinParameter param : binDetails.getBinParameter()) {
            browseNodeId = param.getValue();
        }

        String exclusionReason = ExclusionCriteria.excludeBrowseNodeId(browseNodeId);

        Browsenodes existsAlready = browseNodesEm.getBrowseNode(browseNodeId, keyword);
        if(existsAlready == null){
            Browsenodes browsenodes = browseNodesEm.browsenodes(binItemCount, binName, browseNodeId, parentBrowseNodeId, exclusionReason, keyword, binsearchResults);
            browseNodesEm.persist(browsenodes);
            return browsenodes;
        }else{
            return existsAlready;
        }

    }

    /**
     * When a browsenode has no more children, then extract the ASIN from the response.
     *
     * Get the first 100 results by sending a request for result pages 1-10
     * @param binsearchResults the binsearch_results object saved in the database for the response
     * @throws Exception
     */
    private void setChildbrowsenodetosearchForNoChildBrowseNode(BinsearchResults binsearchResults) throws Exception {
        String searchParamBrowseNode = binsearchResults.getSearchParamsBrowseNodeId();

        BrowsenodesEntityManager browsenodesEm = new BrowsenodesEntityManager();
        Browsenodes bn = browsenodesEm.getBrowseNode(searchParamBrowseNode, binsearchResults.getKeyword());
        if(bn != null){
            if(bn.getExclusionReason() == null){
                setChildbrowsenodetosearch(binsearchResults, bn);
            }
        } else {
            Browsenodes bnNull = browsenodesEm.browsenodes(null, "CannotFindBrowseNodeObject", searchParamBrowseNode, null, null, binsearchResults.getKeyword(), binsearchResults);
            browsenodesEm.persist(bnNull);
            setChildbrowsenodetosearch(binsearchResults, bnNull);
        }
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
    public BinsearchResults setBinSearchResults(ItemSearchResponse response, String keyword, String endpoint, Boolean getBrowseNodes) throws Exception{

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

    /**
     * Main method - used to run the binsearch for the reviewSearchHelper.
     *
     * The reviewSearchHelper: <p>
     *     - Inserts data from Amazon Product Advertising API into the reviewSearchResults database  <p>
     *     - Does searches for these keywords (%s in power search below) = Productivity, Personal Productivity, Knowledge Worker Productivity, Efficient and Effective* <p> <p>
     *     - Standard search parameters are:  <p>
     *          SearchIndex = Books <p>
     *          Power search parameters=keywords:%s and language:english  <p>
     *          Sort = relevancerank, sorted by how often and where the keyword appears, how closely multiple keywords occur in descriptions and how often customers purchased the products they found using the keyword {Amazon 2011}. <p>
     *          MerchantId = Amazon, only look at available books that are sold by Amazon - excludes books sold by third party sellers <p>
     *          Availability = available, excludes unavailable books   <p> <p>
     *
     *     - Exclusion criteria on the browsenode book categories are applied. The name of every book category in the pilot search was read through and a decision made if they should be excluded.
     *     - Amazon Product Advertising API does not support Kindle books, so the search is also restricted to other bindings. So Kindle only books are not in scope.
     *     - The browse nodes (categories) that results should be extracted from are saved in table childbrowsenodestosearch table. The results are extracted in class extractAsin
     *
     * @param  args string array of arguments - arg[0] = search keyword
     */
    public static void main(String [] args) throws Exception{

            SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String start = "Start datetime: " + fm.format(System.currentTimeMillis());
            long startTime = System.currentTimeMillis();
            System.out.println(start);

            BinSearch binSearch = new BinSearch();
            binSearch.getAllBrowseNodes(args[0], Constants.ENDPOINT_US, null);
            String kwProductivityFinished = args[0] + " keyword browsenodes, finishied datetime: " + fm.format(System.currentTimeMillis());
            System.out.println(kwProductivityFinished);


            long endTime = System.currentTimeMillis();
            System.out.println("DateTime of Search");
            System.out.println(start);
            System.out.println(kwProductivityFinished);
            long total = endTime - startTime;
            System.out.println("Total time" + total);

        }
}