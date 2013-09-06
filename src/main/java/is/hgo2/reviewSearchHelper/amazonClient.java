package is.hgo2.reviewSearchHelper;

import com.sun.jersey.api.client.ClientResponse;
import is.hgo2.reviewSearchHelper.amazonMessages.ItemLookupResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static is.hgo2.reviewSearchHelper.Constants.*;

/**
 *
 * @author Helga
 */
public class AmazonClient {

    private static DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private HttpClient httpClient;
    private JaxbMessageConverter messageConverter;
    private RequestUtil requestUtil;

    public AmazonClient() throws Exception{
        this.httpClient = new HttpClient();
        this.messageConverter = new JaxbMessageConverter();
        this.requestUtil = new RequestUtil();

    }

    public Map<String, String> addConstantParameters(Map<String, String> params){

        params.put(PARAMETER_AWSACCESSKEYID, AWSACCESS_KEYID);
        params.put(PARAMETER_TIMESTAMP, dfm.format(new Date()));
        params.put(PARAMETER_ASSOCIATETAG, ASSOCIATE_TAG);

        return params;
    }

    public void sendItemLookupRequest(){

        Map<String, String> params = new HashMap<>();
        params.put(PARAMETER_OPERATION, VALUE_ITEMLOOKUP);
        params.put(PARAMETER_ITEMID, "0679722769");
        params = addConstantParameters(params);

        String request = requestUtil.getRequest(params, ENDPOINT_US);
        System.out.println(request);
        ClientResponse response = httpClient.sendGetRequest(request);
        ItemLookupResponse convertedResponse = response.getEntity(ItemLookupResponse.class);
        System.out.println(convertedResponse.getOperationRequest().getRequestProcessingTime());
        System.out.println(convertedResponse.getItems().get(0).getItem().get(0).getASIN());

    }

    public static void main(String [] args) throws Exception{

        AmazonClient client = new AmazonClient();
        client.sendItemLookupRequest();

    }

}
