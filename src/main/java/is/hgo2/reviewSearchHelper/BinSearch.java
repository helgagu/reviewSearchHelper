package is.hgo2.reviewSearchHelper;

import au.com.bytecode.opencsv.CSVWriter;
import is.hgo2.reviewSearchHelper.amazonMessages.Bin;
import is.hgo2.reviewSearchHelper.amazonMessages.ItemSearchResponse;
import is.hgo2.reviewSearchHelper.amazonMessages.Items;
import is.hgo2.reviewSearchHelper.amazonMessages.SearchBinSet;
import is.hgo2.reviewSearchHelper.util.Constants;
import is.hgo2.reviewSearchHelper.util.ExclusionCriteria;
import is.hgo2.reviewSearchHelper.util.Util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinSearch {
    private final Util util;
    private final AmazonClient amazonClient;


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
     * @param useExclusionCriteria true=use the exclusion criteria specified in method ExclusionCriteria.excludeBrowseNodeId(bin name), false=no exclusion criteria
     * @return List of browseNodeIds
     */
    public List<String> getBrowseNodeIds(ItemSearchResponse response, Boolean useExclusionCriteria) {

        List<String> browseNodeIds = new ArrayList();
        for (Items items : response.getItems()) {
            for (SearchBinSet bin : items.getSearchBinSets().getSearchBinSet()) {
                for (Bin binDetails : bin.getBin()) {
                    if (useExclusionCriteria) {
                        if (!ExclusionCriteria.excludeBrowseNodeId(binDetails.getBinName())) {
                            if (resultsMoreThanHundred(binDetails.getBinItemCount())) {
                                for (Bin.BinParameter param : binDetails.getBinParameter()) {
                                    browseNodeIds.add(param.getValue());
                                }
                            }
                        }
                    } else {
                        if (resultsMoreThanHundred(binDetails.getBinItemCount())) {
                            for (Bin.BinParameter param : binDetails.getBinParameter()) {
                                browseNodeIds.add(param.getValue());
                            }
                        }
                    }

                }
            }
        }
        return browseNodeIds;
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
    public void getAllBrowseNodes(String keyword) throws Exception{

        ItemSearchResponse response = amazonClient.sendBinSearchRequest(keyword, null, null);
        String filename = Constants.BINLIST_FILENAME + Constants.CSV_FILEEND;

    }

    public static void main(String [] args) throws Exception{

        BinSearch binSearch = new BinSearch();
        binSearch.getAllBrowseNodes("Productivity");
        binSearch.getAllBrowseNodes("Personal Productivity");
        binSearch.getAllBrowseNodes("Knowledge Worker Productivity");
        binSearch.getAllBrowseNodes("Efficient");
        binSearch.getAllBrowseNodes("Effective*");

    }
}