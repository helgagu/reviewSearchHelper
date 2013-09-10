package is.hgo2.reviewSearchHelper;

import com.sun.jersey.api.client.ClientResponse;
import is.hgo2.reviewSearchHelper.amazonMessages.ItemLookupResponse;
import is.hgo2.reviewSearchHelper.amazonMessages.ItemSearchResponse;
import is.hgo2.reviewSearchHelper.entityManagers.AsinEntityManager;
import is.hgo2.reviewSearchHelper.util.HttpClient;
import is.hgo2.reviewSearchHelper.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static is.hgo2.reviewSearchHelper.util.Constants.*;

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
    public AmazonClient(Util util) throws Exception{
        this.httpClient = new HttpClient();
        this.util = util;
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

    public Map<String, String> createStandardLookupRequest(String ASIN){

        Map<String, String> params = createParameterMap(ITEMLOOKUP_OPERATION_VALUE);
        params.put(RESPONSEGROUP_PARAMETER, MEDIUM_RESPONSEGROUP_PARAMETER);
        params.put(ITEMID_PARAMETER, ASIN);
        return params;

    }

    public Map<String, String> createEditorialLookupRequest(String ASIN){

        Map<String, String> params = createParameterMap(ITEMLOOKUP_OPERATION_VALUE);
        params.put(RESPONSEGROUP_PARAMETER, EDITORIALREVIEW_RESPONSEGROUP_PARAMETER);
        params.put(ITEMID_PARAMETER, ASIN);
        return params;

    }

    public ItemSearchResponse sendLargeResponseGroupRequest(String keyword, String endpoint)throws Exception{

        Map<String, String> params = createStandardSearchRequest(keyword);
        params.put(RESPONSEGROUP_PARAMETER, LARGE_RESPONSEGROUP_PARAMETER);

        return sendSearchRequest(params, endpoint);
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
    public Map<String, String> getBinSearchRequest(String keyword, String browseNodeId, String searchPageNumber){

        Map<String, String> params = createStandardSearchRequest(keyword);
        params.put(RESPONSEGROUP_PARAMETER, SEARCHBINS_RESPONSEGROUP_PARAMETER);
        if(browseNodeId != null){
            params.put(BROWSENODE_PARAMETER, browseNodeId);
        }
        if(searchPageNumber != null){
            params.put(ITEMPAGE_PARAMETER, searchPageNumber);
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
    public ItemSearchResponse sendSearchRequest(Map<String, String> params, String endpoint) throws Exception{

        String request = util.getRequest(params, endpoint);
        System.out.println(request);
        ClientResponse response = httpClient.sendGetRequest(request);
        ItemSearchResponse itemSearchResponse = response.getEntity(ItemSearchResponse.class);
        util.writeOriginalResponseToFile(itemSearchResponse);
        util.putAsin(itemSearchResponse);
        return itemSearchResponse;

    }

    public ItemLookupResponse sendItemLookupResponse(String ASIN, String endpoint) throws Exception{

        Map<String, String> originalRequest = createStandardLookupRequest(ASIN);
        String request = util.getRequest(originalRequest, endpoint);
        System.out.println(request);
        ClientResponse response = httpClient.sendGetRequest(request);
        ItemLookupResponse itemLookupResponse = response.getEntity(ItemLookupResponse.class);
        util.writeOriginalResponseToFile(itemLookupResponse);
        return itemLookupResponse;

    }

    public ItemLookupResponse sendEditorialLookupResponse(String ASIN, String endpoint) throws Exception{

        Map<String, String> originalRequest = createEditorialLookupRequest(ASIN);
        String request = util.getRequest(originalRequest, endpoint);
        System.out.println(request);
        ClientResponse response = httpClient.sendGetRequest(request);
        ItemLookupResponse itemLookupResponse = response.getEntity(ItemLookupResponse.class);
        util.writeOriginalResponseToFile(itemLookupResponse);
        return itemLookupResponse;

    }

    public ItemSearchResponse sendBinSearchRequest(String keyword, String browseNodeId, String itemPageNumber) throws Exception{
        Map<String, String> originalRequest = getBinSearchRequest(keyword, browseNodeId, itemPageNumber);

        return sendSearchRequest(originalRequest, ENDPOINT_US);
    }

    public static void main(String [] args) throws Exception{

        Util util1 = new Util();
        AmazonClient amazonClient = new AmazonClient(util1);

       amazonClient.sendBinSearchRequest("Productivity", "4744", "2");
       //amazonClient.sendItemLookupResponse("0743269519", ENDPOINT_US);
        //amazonClient.sendEditorialLookupResponse("0743269519", ENDPOINT_US);
    }

}
