package is.hgo2.reviewSearchHelper;

import is.hgo2.reviewSearchHelper.amazonMessages.Item;
import is.hgo2.reviewSearchHelper.amazonMessages.ItemSearchResponse;
import is.hgo2.reviewSearchHelper.amazonMessages.Items;
import is.hgo2.reviewSearchHelper.entities.*;
import is.hgo2.reviewSearchHelper.entityManagers.AsinEntityManager;
import is.hgo2.reviewSearchHelper.entityManagers.BooksEntityManager;
import is.hgo2.reviewSearchHelper.entityManagers.BrowsenodesAsinEntityManager;
import is.hgo2.reviewSearchHelper.entityManagers.ChildbrowsenodestosearchEntityManager;
import is.hgo2.reviewSearchHelper.util.Util;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Helga
 * Date: 20.9.2013
 * Time: 18:02
 * To change this template use File | Settings | File Templates.
 */
public class ExtractAsin {

    private AmazonClient client;
    private Util util;
    private BinSearch binSearch;

    public ExtractAsin()throws Exception{
        util = new Util();
        client = new AmazonClient(util);
        binSearch = new BinSearch();
    }

    public void setSearchResults() throws Exception{

        ChildbrowsenodestosearchEntityManager em = new ChildbrowsenodestosearchEntityManager();
        List<Childbrowsenodestosearch> items = em.getAllNotResultFetched();
        for(Childbrowsenodestosearch item : items){
            setChildBrowseNodesAsin(item);
        }

    }

    /**
     * Gets all the ASIN from the browseNodes responses, result pages 1-10, and saves into databasetable browsenodesAsin
     * and database table asin (if it doesn't exist there already).
     * The amazon product advertising api only allows fetching of the first 100 results.
     * @param bn the childbrowsenodestosearch object
     * @throws Exception
     */
    private void setChildBrowseNodesAsin(Childbrowsenodestosearch bn) throws Exception{

        String keyword = bn.getKeyword();
        String endpoint = bn.getEndpoint();
        String browsenodeId = bn.getBrowseNodeId();

        ItemSearchResponse response = getBrowseNodeAsins(browsenodeId, bn, keyword, endpoint, null);

        for(int i=1; i < getTotalPages(response); i++){

            if(i > 10){
                break; //Amazon only allows fetching results from the first 10 pages.
            }
            getBrowseNodeAsins(browsenodeId, bn, keyword, endpoint, String.valueOf(i));

        }
        ChildbrowsenodestosearchEntityManager em = new ChildbrowsenodestosearchEntityManager();
        em.setResultFetched(bn);
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
    private ItemSearchResponse getBrowseNodeAsins(String browseNodesId, Childbrowsenodestosearch browsenodes, String keyword, String endpoint, String page) throws Exception {
        ItemSearchResponse response = client.sendBinSearchRequest(keyword, browseNodesId, page, endpoint);
        BinsearchResults bin = binSearch.setBinSearchResults(response, keyword, endpoint, Boolean.FALSE);
        setBrowseNodeAsin(browsenodes, response, bin, endpoint);
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
    private void setBrowseNodeAsin(Childbrowsenodestosearch browsenodes, ItemSearchResponse response, BinsearchResults bin, String endpoint) throws Exception{

        String asinNumber;
        Asin asin;
        BookLookup bookLookup = new BookLookup();
        for(Items items : response.getItems()){
            for(Item item : items.getItem()){
                BrowsenodesAsinEntityManager browsenodesAsinEm = new BrowsenodesAsinEntityManager();
                AsinEntityManager asinEm = new AsinEntityManager();

                asinNumber = item.getASIN();
                asin = asinEm.getAsin(asinNumber);
                if(asin == null){
                    asin = asinEm.asin(asinNumber);
                    asinEm.persist(asin);
                    bookLookup.setBookDetail(endpoint, asin);
                }else{
                    BooksEntityManager booksEm = new BooksEntityManager();
                    if(booksEm.getBooks(asinNumber) == null){
                        bookLookup.setBookDetail(endpoint, asin);
                    }
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
     *  Amazon Product Advertising API only allows to get the top 100 results of a search, therefore the top 100 results for each child browse nodes (book category) are extracted. The categories are organized in a hierarchical order so the search can be narrowed down the tree.
     *     the reviewSearchHelper traverses down this hierarchy until there are fewer than 100 results or the node has no more children.
     * @throws Exception
     */
    public static void main(String [] args)throws Exception{

        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = "Start datetime: " + fm.format(System.currentTimeMillis());
        long startTime = System.currentTimeMillis();
        System.out.println(start);

        ExtractAsin extractAsin = new ExtractAsin();
        extractAsin.setSearchResults();
        String extractAsinFinished = "Extract asin and book info, finished datetime: " + fm.format(System.currentTimeMillis());

        long endTime = System.currentTimeMillis();
        System.out.println("DateTime of Search");
        System.out.println(start);
        System.out.println(extractAsinFinished);
        long total = endTime - startTime;
        System.out.println("Total time" + total);

    }
}
