package is.hgo2.reviewSearchHelper;

import is.hgo2.reviewSearchHelper.util.Constants;

/**
 * Main class - used to run the reviewSearchHelper.
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
 *     - Amazon Product Advertising API only allows to get the top 100 results of a search, therefore the top 100 results for each child book category are extracted. The categories are organized in a hierarchical order so the search can be narrowed down the tree.
 *     the reviewSearchHelper traverses down this hierarchy until there are fewer than 100 results or the node has no more children.
 *
 * @author Helga Gudrun Oskarsdottir
 */
public class Main {

    public static void main(String [] args) throws Exception{

        BinSearch binSearch = new BinSearch();
        binSearch.getAllBrowseNodes("Productivity", Constants.ENDPOINT_US, null);
        binSearch.getAllBrowseNodes("Personal Productivity", Constants.ENDPOINT_US, null);
        binSearch.getAllBrowseNodes("Knowledge Worker Productivity", Constants.ENDPOINT_US, null);
        binSearch.getAllBrowseNodes("Efficient", Constants.ENDPOINT_US, null);
        binSearch.getAllBrowseNodes("Effective*", Constants.ENDPOINT_US, null);

    }
}
