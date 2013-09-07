package is.hgo2.reviewSearchHelper;

import com.sun.jersey.api.client.ClientResponse;
import is.hgo2.reviewSearchHelper.amazonMessages.ItemSearchResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static is.hgo2.reviewSearchHelper.Constants.*;

/**
 * This class creates and sends requests to the Amazon Product Advertising API.
 * The Product Advertising API, Developer Guide (API Version 2011-08-01) was used integrate to the API.
 * (see : http://docs.aws.amazon.com/AWSECommerceService/latest/DG/Welcome.html)
 *
 * @author Helga Gudrun Oskarsdottir
 */
public class AmazonClient {

    private static DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private HttpClient httpClient;
    private Util util;

    /**
     * Initializer
     * @throws Exception
     */
    public AmazonClient() throws Exception{
        this.httpClient = new HttpClient();
        this.util = new Util();

    }

    /**
     * Creates the base API request with common mandatory parameters:  <p>
     * - AWS Access KeyID  <p>
     * - Timestamp (now)  <p>
     * - Associate tag   <p>
     * - Service       <p>
     * - Operation     <p>
     * @param operation which operation the request should execute e.g. ItemSearch, ItemLookup
     * @return Hashmap with the common mandatory parameters (key = parameter name; value = parameter value)
     */
    public Map<String, String> createParameterMap(String operation){

        Map<String, String> params = new HashMap<>();
        params.put(AWSACCESSKEYID_PARAMETER, AWSACCESSKEYID_VALUE);
        params.put(TIMESTAMP_PARAMETER, dfm.format(new Date()));
        params.put(ASSOCIATETAG_PARAMETER, ASSOCIATETAG_VALUE);
        params.put(SERVICE_PARAMETER, SERVICE_VALUE);
        params.put(OPERATION_PARAMETER, operation);

        return params;
    }

    /**
     * Creates the standard search request used in this project
     * according to the selection strategy for the systematic review. <p><p>
     *
     * SearchIndex = Books <p>
     * Power search parameters=keywords:%s and language:english  <p>
     * Sort = relevancerank, sorted by how often and where the keyword appears, how closely multiple keywords occur in descriptions and how often customers purchased the products they found using the keyword {Amazon 2011}. <p>
     * MerchantId = Amazon, only look at available books that are sold by Amazon - excludes books sold by third party sellers <p>
     * Availability = available, excludes unavailable books   <p>
     *
     * @param keyword the search keyword which replaces %s (see above), this is either Productivity, Personal Productivity, Efficient, Effective(ness) or knowledge worker productivity
     * @return Hashmap with the common mandatory parameters and the standard search request parameters (key = parameter name; value = parameter value)
     */
    public Map<String, String> createStandardSearchRequest(String keyword){

        Map<String, String> params = createParameterMap(ITEMSEARCH_OPERATION_VALUE);
        params.put(SEARCHINDEX_PARAMETER, BOOKS_SEARCHINDEX_VALUE);
        params.put(POWER_PARAMETER, util.addKeywordToPowerSearch(keyword));
        params.put(SORT_PARAMETER, RELEVANCERANK_SORT_VALUE);
        params.put(MERCHANTID_PARAMETER, MERCHANTID_VALUE);
        params.put(AVAILABILITY_PARAMETER, AVAILABILITY_VALUE);
        return params;

    }

    /**
     * Creates a binSearch request using the standard search request with the addition of these parameters:  <p>
     *
     * ResponseGroup = SearchBins, this tells the request to return a list of bins, which are categorized lists by subject connected to a specific browseNodeId.  <p>
     * BrowseNodeId = this is the id of a browseNode which is specified in a bin, narrowing the results to just the ones in this browseNode. A browseNode is a book category which is part of an organizational hierarchy.  <p>
     *
     * @param keyword the search keyword, this is either Productivity, Personal Productivity, Efficient, Effective(ness) or knowledge worker productivity
     * @param browseNodeId the id of a specific browseNode (bin)
     * @return Hashmap with the common mandatory parameters, the standard search parameters and the binSearch parameters (key = parameter name; value = parameter value)
     */
    public Map<String, String> getBinSearchRequest(String keyword, String browseNodeId){

        Map<String, String> params = createStandardSearchRequest(keyword);
        params.put(RESPONSEGROUP_PARAMETER, SEARCHBINS_RESPONSEGROUP_PARAMETER);
        if(browseNodeId != null){
            params.put(BROWSENODE_PARAMETER, browseNodeId);
        }
        return params;
    }


    /**
     * Sends an ItemSearch request to Amazon.
     *
     * @param params hashmap with the request parameters (key = parameter name; value = parameter value) must include the mandatory common parameters
     * @param endpoint the Amazon locale to search, either; CA, CN, DE, ES, FR, IN, IT, JP, UK, US. Be aware of different requirements for different locales
     * @return the response as an ItemSearchResponse java object
     */
    public ItemSearchResponse sendSearchRequest(Map<String, String> params, String endpoint){

        String request = util.getRequest(params, endpoint);
        System.out.println(request);
        ClientResponse response = httpClient.sendGetRequest(request);
        return response.getEntity(ItemSearchResponse.class);

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

        Map<String, String> originalRequest = getBinSearchRequest(keyword, null);
        ItemSearchResponse originalResponse = sendSearchRequest(originalRequest, ENDPOINT_US);
        System.out.println("List for original request");
        util.getStringWithBinListResults(originalResponse);
        List<String> originalBrowseNodeIds = util.getBrowseNodeIds(originalResponse, useBinExclusionCriteria);
        getAllBrowseNodes(keyword, originalBrowseNodeIds, originalResponse, useBinExclusionCriteria);


    }

    /**
     * Recursive method to loop through all the bin list results, find their children going down the hierarchical organization of the categories (browseNodes) until there are no more children or the binItemCount is less than 100.
     *
     * @param keyword the search keyword, this is either Productivity, Personal Productivity, Efficient, Effective(ness) or knowledge worker productivity
     * @param browseNodeIds the browseNodeIds of the parent node
     * @param originalResponse the response of the binSearch request of the parent
     * @param useBinExclusionCriteria true = exclude categories by using the exclusion criteria in the method ExclusionCriteria.excludeBrowseNodeId(name of bin), false = do not exclude any categories
     * @throws Exception
     */
    public void getAllBrowseNodes(String keyword, List<String> browseNodeIds, ItemSearchResponse originalResponse, Boolean useBinExclusionCriteria) throws Exception{


        for(String browseNodeId: browseNodeIds){

                Map<String, String> browseNodeRequest = getBinSearchRequest(keyword, browseNodeId);
                ItemSearchResponse browseNodeResponse = sendSearchRequest(browseNodeRequest, ENDPOINT_US);
                System.out.println("List for browseNodeId: " + util.getBrowseNodeIdName(originalResponse, browseNodeId));
                util.getStringWithBinListResults(browseNodeResponse);
                List<String> browseNodeRequestBrowseNodeIds = util.getBrowseNodeIds(browseNodeResponse, useBinExclusionCriteria);
                if(!browseNodeRequestBrowseNodeIds.isEmpty()){
                    TimeUnit.SECONDS.sleep(5);
                    getAllBrowseNodes(keyword, browseNodeRequestBrowseNodeIds, browseNodeResponse, useBinExclusionCriteria);
                }
        }

    }


    public static void main(String [] args) throws Exception{

        AmazonClient client = new AmazonClient();
        client.getAllBrowseNodes("Productivity", Boolean.FALSE);

    }

}
