package is.hgo2.reviewSearchHelper;

import au.com.bytecode.opencsv.CSVWriter;
import is.hgo2.reviewSearchHelper.amazonMessages.Bin;
import is.hgo2.reviewSearchHelper.amazonMessages.ItemSearchResponse;
import is.hgo2.reviewSearchHelper.amazonMessages.Items;
import is.hgo2.reviewSearchHelper.amazonMessages.SearchBinSet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BinSearch {
    private final Util util;
    private final AmazonClient amazonClient;

    public BinSearch() throws Exception{
        this.util = new Util();
        this.amazonClient = new AmazonClient(util);
    }

    /**
     * To create a string with the total results and pages of an ItemSearch with responseGroup = BinSearch
     * as well as a list of all the bin names and their binItemCount.
     *
     * @param response the response object of the ItemSearch with responseGroup = BinSearch
     */
    public void getStringWithBinListResults(ItemSearchResponse response) {
        StringBuilder result = new StringBuilder();
        for (Items items : response.getItems()) {
            Map<String, String> itemsResults = new HashMap<String, String>();
            itemsResults.put("Total Results: ", items.getTotalResults().toString());
            itemsResults.put("Total Pages: ", items.getTotalPages().toString());

            for (SearchBinSet bin : items.getSearchBinSets().getSearchBinSet()) {


                itemsResults.put("NarrowBy: ", bin.getNarrowBy());

                for (Bin binDetails : bin.getBin()) {
                    Map<String, String> itemResults = new HashMap<String, String>();
                    itemResults.put("BinName: ", binDetails.getBinName());
                    itemResults.put("BinItemCount: ", binDetails.getBinItemCount().toString());
                    result.append(util.getFormattedResultString(itemResults, Boolean.TRUE));
                }

                result.append(util.getFormattedResultString(itemsResults, Boolean.TRUE));
            }


            System.out.println(result.toString());
        }
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

    public void writeBinListResultHeadersToFile(CSVWriter pw) throws Exception {
        //Same values for each line in file
        String[] headers = new String[Constants.BINLIST_CSV_ARRAYSIZE];
        headers[Constants.BROWSENODEID_ARRAYINDEX_BINLIST_CSV] = "Browse Node Id";
        headers[Constants.BINNAME_ARRAYINDEX_BINLIST_CSV] = "Bin Name";
        headers[Constants.BINITEMCOUNT_ARRAYINDEX_BINLIST_CSV] = "Bin Item Count";
        headers[Constants.PARENTBROWSENODEID_ARRAYINDEX_BINLIST_CSV] = "Parent Browse Id";
        headers[Constants.PARENTBINNAME_ARRAYINDEX_BINLIST_CSV] = "Parent Bin Name";
        headers[Constants.SEARCHKEYWORD_ARRAYINDEX_BINLIST_CSV] = "Search Keyword";
        headers[Constants.NARROWEDBY_ARRAYINDEX_BINLIST_CSV] = "Narrowed by";
        headers[Constants.SEARCHINDEX_ARRAYINDEX_BINLIST_CSV] = "Search Param: Search Index";
        headers[Constants.SORT_ARRAYINDEX_BINLIST_CSV] = "Search Param: Sorted By";
        headers[Constants.POWERSEARCH_ARRAYINDEX_BINLIST_CSV] = "Search Param: Power Search String";
        headers[Constants.AVAILABILITY_ARRAYINDEX_BINLIST_CSV] = "Search Param: Only Available";
        headers[Constants.MERCHANTID_ARRAYINDEX_BINLIST_CSV] = "Search Param: MerchantId (seller)";
        headers[Constants.RESULTTIMESTAMP_ARRAYINDEX_BINLIST_CSV] = "Result Timestamp";

        pw.writeNext(headers);
    }

    /**
     * To create a string with the total results and pages of an ItemSearch with responseGroup = BinSearch
     * as well as a list of all the bin names and their binItemCount.
     *
     * @param response the response object of the ItemSearch with responseGroup = BinSearch
     */
    public void writeBinListResultsToCSV(CSVWriter pw, ItemSearchResponse response, ItemSearchResponse parentResponse, String parentBrowseNodeId, String keyword) throws Exception {

        for (Items items : response.getItems()) {
            for (SearchBinSet bin : items.getSearchBinSets().getSearchBinSet()) {
                if (!bin.getBin().isEmpty()) {
                    String[] row = new String[Constants.BINLIST_CSV_ARRAYSIZE];

                    CsvFileMaker.addTimestampAndSearchParametersToRow(row, response);
                    row[Constants.SEARCHKEYWORD_ARRAYINDEX_BINLIST_CSV] = keyword;
                    row[Constants.NARROWEDBY_ARRAYINDEX_BINLIST_CSV] = bin.getNarrowBy();

                    for (Bin binDetails : bin.getBin()) {
                        for (Bin.BinParameter param : binDetails.getBinParameter()) {
                            row[Constants.BROWSENODEID_ARRAYINDEX_BINLIST_CSV] = param.getValue();
                        }
                        row[Constants.BINNAME_ARRAYINDEX_BINLIST_CSV] = binDetails.getBinName();
                        row[Constants.BINITEMCOUNT_ARRAYINDEX_BINLIST_CSV] = binDetails.getBinItemCount().toString();
                    }


                    if (parentResponse != null && parentBrowseNodeId != null) {
                        row[Constants.PARENTBROWSENODEID_ARRAYINDEX_BINLIST_CSV] = parentBrowseNodeId;
                        row[Constants.PARENTBINNAME_ARRAYINDEX_BINLIST_CSV] = getBrowseNodeIdName(parentResponse, parentBrowseNodeId);
                    }
                    pw.writeNext(row);
                }

            }

        }


    }

    /**
     *
     * Recursive method to loop through all the bin list results, find their children going down the hierarchical organization of the categories (browseNodes) until there are no more children or the binItemCount is less than 100.
     *
     * @param counter
     * @param keyword
     * @param binList
     * @param pw
     * @param binListResponse
     * @param useBinExclusionCriteria
     * @throws Exception
     */
    private void goThroughBrowseNodes(int counter, String keyword, List<String> binList, CSVWriter pw, ItemSearchResponse binListResponse, Boolean useBinExclusionCriteria) throws Exception {

        if(counter == 0){
            return;
        }else{
            TimeUnit.SECONDS.sleep(7);
            ItemSearchResponse childBinListResponse = amazonClient.sendBinSearchRequest(keyword, binList.get(counter).toString());
            writeBinListResultsToCSV(pw, binListResponse, childBinListResponse, binList.get(counter).toString(), keyword);
            List<String> fetchChildBinList = getBrowseNodeIds(childBinListResponse, useBinExclusionCriteria);
            goThroughBrowseNodes(counter--, keyword, fetchChildBinList, pw, childBinListResponse, useBinExclusionCriteria);

            return;
        }
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
     * @param useBinExclusionCriteria true = exclude categories by using the exclusion criteria in the method ExclusionCriteria.excludeBrowseNodeId(name of bin), false = do not exclude any categories
     * @throws Exception
     */
    public void getAllBrowseNodes(String keyword, Boolean useBinExclusionCriteria) throws Exception{

    }

    public static void main(String [] args) throws Exception{

        BinSearch binSearch = new BinSearch();
        //add method to call on binSearch run....
    }
}