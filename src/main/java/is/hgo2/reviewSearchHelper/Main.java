package is.hgo2.reviewSearchHelper;

import is.hgo2.reviewSearchHelper.util.Constants;

import java.text.SimpleDateFormat;

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

        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = "Start datetime: " + fm.format(System.currentTimeMillis());
        long startTime = System.currentTimeMillis();
        System.out.println(start);

        BinSearch binSearch = new BinSearch();
//        binSearch.getAllBrowseNodes("Productivity", Constants.ENDPOINT_US, null);
//        String productivityFinished = "Productivity keyword browsenodes, finished datetime: " + fm.format(System.currentTimeMillis());
//        System.out.println(productivityFinished);
//
//        binSearch.getAllBrowseNodes("Personal Productivity", Constants.ENDPOINT_US, null);
//        String personalProductivityFinished = "Personal Productivity keyword browsenodes, finished datetime: " + fm.format(System.currentTimeMillis());
//        System.out.println(personalProductivityFinished);

        binSearch.getAllBrowseNodes("Knowledge Worker Productivity", Constants.ENDPOINT_US, null);
        String kwProductivityFinished = "Knowledge Worker Productivity keyword browsenodes, finishied datetime: " + fm.format(System.currentTimeMillis());
        System.out.println(kwProductivityFinished);

//        binSearch.getAllBrowseNodes("Efficient", Constants.ENDPOINT_US, null);
//        String efficientProductivityFinished = "Efficient keyword browsenodes, finishied datetime: " + fm.format(System.currentTimeMillis());
//        System.out.println(efficientProductivityFinished);
//
//        binSearch.getAllBrowseNodes("Effective*", Constants.ENDPOINT_US, null);
//        String effectiveFinished = "Effective keyword browsenodes, finished datetime: " + fm.format(System.currentTimeMillis());
//        System.out.println(effectiveFinished);

        long endTime = System.currentTimeMillis();
        System.out.println("DateTime of Search");
        System.out.println(start);
//        System.out.println(productivityFinished);
//        System.out.println(personalProductivityFinished);
        System.out.println(kwProductivityFinished);
//        System.out.println(efficientProductivityFinished);
//        System.out.println(effectiveFinished);
        long total = endTime - startTime;
        System.out.println("Total time" + total);

    }
}
