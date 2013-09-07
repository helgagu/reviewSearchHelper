package is.hgo2.reviewSearchHelper;

/**
 * Static class with constants needed in multiple classes.
 *
 * @author Helga
 */
public class Constants {

    public static final String AWSACCESS_SECRET = "WlnMwQrOICBTEaOMEHr7CCZYlcnKXRDKSjDZxAnX";
    public static final String REQUEST_URI = "/onca/xml";
    public static final String REQUEST_METHOD_GET = "GET";
    public static final String NEWLINE = "\n";
    public static final String HTTP = "http://webservices.amazon.";

    public static final String ENDPOINT_US = "com";
    public static final String ENDPOINT_CA = "ca";
    public static final String ENDPOINT_CN = "cn";
    public static final String ENDPOINT_DE = "de";
    public static final String ENDPOINT_ES = "es";
    public static final String ENDPOINT_FR = "fr";
    public static final String ENDPOINT_IT = "it";
    public static final String ENDPOINT_JP = "co.jp";
    public static final String ENDPOINT_UK = "co.uk";

    public static final String URL_ENCODED_SPACE = "%20";
    public static final String ENDPOINT_SEPARATOR = "?";
    public static final String PARAMETER_SEPARATOR = "&";

    //Common parameters for every type of request
    public static final String OPERATION_PARAMETER = "Operation";
    public static final String AWSACCESSKEYID_PARAMETER = "AWSAccessKeyId";
    public static final String TIMESTAMP_PARAMETER = "Timestamp";
    public static final String ASSOCIATETAG_PARAMETER = "AssociateTag";
    public static final String SERVICE_PARAMETER = "Service";
    public static final String SIGNATURE_PARAMETER = "Signature";

    //Constant values of common parameters
    public static final String ASSOCIATETAG_VALUE = "helgagudrun-20";
    public static final String AWSACCESSKEYID_VALUE = "AKIAJDNPK4ZI5JYS32AQ";
    public static final String SERVICE_VALUE = "AWSECommerceService";

    //Parameters for ItemSearch
    public static final String KEYWORDS_PARAMETER = "Keywords";
    public static final String ITEMPAGE_PARAMETER = "ItemPage";
    public static final String SEARCHINDEX_PARAMETER = "SearchIndex";
    public static final String SORT_PARAMETER = "Sort";
    public static final String POWER_PARAMETER = "Power";
    public static final String AVAILABILITY_PARAMETER = "Availability";
    public static final String RESPONSEGROUP_PARAMETER = "ResponseGroup";
    public static final String MERCHANTID_PARAMETER = "MerchantId";
    public static final String BROWSENODE_PARAMETER = "BrowseNode";

    //Parameters for ItemLookup
    public static final String ITEMID_PARAMETER = "ItemId";

    //Constant values for paramaters
    public static final String ITEMSEARCH_OPERATION_VALUE = "ItemSearch";
    public static final String ITEMLOOKUP_OPERATION_VALUE = "ItemLookup";
    public static final String BOOKS_SEARCHINDEX_VALUE = "Books";
    public static final String POWER_SEARCH_VALUE = "keywords:%s and language:english";

    /* Relevance rank info:
       Items ranked according to the following criteria: how often the keyword appears in the description,
       where the keyword appears (for example, the ranking is higher when keywords are found in titles),
       how closely they occur in descriptions (if there are multiple keywords),
       and how often customers purchased the products they found using the keyword.  {Amazon, 2011 #34}
     */
    public static final String RELEVANCERANK_SORT_VALUE = "relevancerank";
    public static final String SEARCHBINS_RESPONSEGROUP_PARAMETER = "SearchBins";
    public static final String AVAILABILITY_VALUE = "Available";
    public static final String MERCHANTID_VALUE = "Amazon";

    //Browse Id's to exclude
    public static final String HEALTHFITNESSDIETING_BROWSENODE = "Health, Fitness & Dieting";
    public static final String MEDICALBOOKS_BROWSENODE  = "Medical Books";
    public static final String RELIGIONSPIRITUALITY_BROWSENODE = "Religion & Spirituality";
    public static final String ARTPHOTOGRAPHY_BROWSENODE = "Art & Photography";


}

