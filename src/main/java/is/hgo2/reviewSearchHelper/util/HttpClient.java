package is.hgo2.reviewSearchHelper.util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * Class used for sending requests to REST web services
 *
 * @author Helga
 */
public class HttpClient {

    Client client;

    public HttpClient() {
        client = Client.create();
    }


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    /**
     * Sends http post request to a specified url and returns the response as a byte array
     *
     *
     * @param url  where to send the request
     * @param body the request body to send
     * @return response as byte[] if response is not null, else return null
     * @throws UniformInterfaceException
     */
    public ClientResponse sendPostRequest(String url, byte[] body) throws UniformInterfaceException {
        WebResource.Builder builder = getBuilder(url);
        return builder.post(ClientResponse.class, body);
    }

    /**
     * Sends http get request to a specified url and returns the response as a byte array
     *
     * @param url where to send the request
     * @return response as byte[] if response is not null, else return null
     * @throws UniformInterfaceException
     */
    public ClientResponse sendGetRequest(String url) throws UniformInterfaceException {
        WebResource.Builder builder = getBuilder(url);
        return builder.get(ClientResponse.class);
    }

    /**
     * Sends http delete request to a specified url and returns the response as a byte array
     *
     * @param url where to send the request
     * @return response as byte[] if response is not null, else return null
     * @throws UniformInterfaceException
     */
    public ClientResponse sendDeleteRequest(String url) throws UniformInterfaceException {
        WebResource.Builder builder = getBuilder(url);
        return builder.delete(ClientResponse.class);
    }

    /**
     * Sends http put request to a specified url and returns the response as a byte array
     *
     * @param url  where to send the request
     * @param body the request body to send
     * @return response as byte[] if response is not null, else return null
     * @throws UniformInterfaceException
     */
    public ClientResponse sendPutRequest(String url, byte[] body) throws UniformInterfaceException {
        WebResource.Builder builder = getBuilder(url);
        return builder.put(ClientResponse.class, body);
    }


    private WebResource.Builder getBuilder(String url) {
        WebResource webResource = client.resource(url);

        return webResource.getRequestBuilder();
    }

}

