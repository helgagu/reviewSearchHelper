package is.hgo2.reviewSearchHelper;

import au.com.bytecode.opencsv.CSVWriter;
import is.hgo2.reviewSearchHelper.amazonMessages.*;
import is.hgo2.reviewSearchHelper.entities.BinsearchResults;
import is.hgo2.reviewSearchHelper.entities.Browsenodes;
import is.hgo2.reviewSearchHelper.entityManagers.BinsearchResultsEntityManager;
import is.hgo2.reviewSearchHelper.entityManagers.BrowsenodesEntityManager;
import is.hgo2.reviewSearchHelper.util.Constants;
import is.hgo2.reviewSearchHelper.util.ExclusionCriteria;
import is.hgo2.reviewSearchHelper.util.JaxbMessageConverter;
import is.hgo2.reviewSearchHelper.util.Util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                            if (param.getValue().equalsIgnoreCase(browseNodeId)) {
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
     * @param response             the response object of the ItemSearch with responseGroup = BinSearch
     * @return List of browseNodeIds
     */
    public void setBrowseNodeIds(ItemSearchResponse response, BinsearchResults binsearchResults) {

        BrowsenodesEntityManager browseNodes = new BrowsenodesEntityManager();
        List<Browsenodes> rows = new ArrayList<>();

        for (Items items : response.getItems()) {
            for (SearchBinSet bin : items.getSearchBinSets().getSearchBinSet()) {
                for (Bin binDetails : bin.getBin()) {
                    Long binItemCount = binDetails.getBinItemCount().longValue();
                    String binName = binDetails.getBinName();
                    String browseNodeId = "";
                    for (Bin.BinParameter param : binDetails.getBinParameter()) {
                        browseNodeId = param.getValue();
                    }

                    rows.add(browseNodes.browsenodes(binItemCount, binName, browseNodeId, binsearchResults));
                }
            }
        }

        browseNodes.persist(rows);
    }

    /**
     * Compares the binItemCount to see if it is greater than 100.
     *
     * @param binItemCount the amount of result items in a specific bin
     * @return true=binItemCount is greater than 100, false=binItemCount is equal or smaller than 100
     */
    public Boolean resultsMoreThanHundred(BigInteger binItemCount) {
        if (binItemCount.compareTo(BigInteger.valueOf(100)) == 1) {
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
    public void getAllBrowseNodes(String keyword, String endpoint) throws Exception{

        ItemSearchResponse response = amazonClient.sendBinSearchRequest(keyword, null, null, endpoint);
        setBinSearchResults(response, keyword, endpoint);
    }


    /**
     * Set the values in a binsearch object
     * @param response the amazon itemSearchResponse
     * @param keyword the serach keyword
     * @param endpoint the amazon locale, the ending of the url http://amazon. , e.g. com, co.uk etc...
     * @throws Exception
     */
    public void setBinSearchResults(ItemSearchResponse response, String keyword, String endpoint) throws Exception{

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
        setBrowseNodeIds(response, binsearchresults);
    }

    public static void main(String [] args) throws Exception{

        BinSearch binSearch = new BinSearch();
        binSearch.getAllBrowseNodes("Productivity", Constants.ENDPOINT_US);
        binSearch.getAllBrowseNodes("Personal Productivity", Constants.ENDPOINT_US);
        binSearch.getAllBrowseNodes("Knowledge Worker Productivity", Constants.ENDPOINT_US);
        binSearch.getAllBrowseNodes("Efficient", Constants.ENDPOINT_US);
        binSearch.getAllBrowseNodes("Effective*", Constants.ENDPOINT_US);

    }
}