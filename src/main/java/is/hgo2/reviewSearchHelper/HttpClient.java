package is.hgo2.reviewSearchHelper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used for sending requests to REST web services
 *
 * @author Helga
 */
public class HttpClient {

    Map<String, String> httpHeaders;
    Client client;

    public HttpClient() {
        client = Client.create();
        httpHeaders = new HashMap<>();
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Adds a new http header to the httpHeaders map.
     * If the httpHeaders map is null a new instance is created.
     *
     * @param key   the header key
     * @param value the header value
     */
    public void addHttpHeader(String key, String value) {
        if (httpHeaders == null) {
            httpHeaders = new HashMap<>();
        }
        httpHeaders.put(key, value);
    }

    public void resetHeaders() {
        httpHeaders = new HashMap<>();
    }

    /**
     * Sends http post request to a specified url and returns the response as a byte array
     *
     * @param url  where to send the request
     * @param body the request body to send
     * @return response as byte[] if response is not null, else return null
     * @throws UniformInterfaceException
     */
    public ClientResponse sendPostRequest(String url, byte[] body, String terminalDateTime) throws UniformInterfaceException {
        WebResource.Builder builder = getBuilder(url);
        ClientResponse response = builder.post(ClientResponse.class, body);
        return response;
    }

    /**
     * Sends http get request to a specified url and returns the response as a byte array
     *
     * @param url where to send the request
     * @return response as byte[] if response is not null, else return null
     * @throws UniformInterfaceException
     */
    public ClientResponse sendGetRequest(String url, String terminalDateTime) throws UniformInterfaceException {
        WebResource.Builder builder = getBuilder(url);
        ClientResponse response = builder.get(ClientResponse.class);
        return response;
    }

    /**
     * Sends http delete request to a specified url and returns the response as a byte array
     *
     * @param url where to send the request
     * @return response as byte[] if response is not null, else return null
     * @throws UniformInterfaceException
     */
    public ClientResponse sendDeleteRequest(String url, String terminalDateTime) throws UniformInterfaceException {
        WebResource.Builder builder = getBuilder(url);
        ClientResponse response = builder.delete(ClientResponse.class);
        return response;
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
        ClientResponse response = builder.put(ClientResponse.class, body);
        return response;
    }


    private WebResource.Builder getBuilder(String url) {
        WebResource webResource = client.resource(url);
        WebResource.Builder builder = webResource.getRequestBuilder();
        for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
            builder = builder.header(entry.getKey(), entry.getValue());
        }
        return builder;
    }

    /**
     * Builder to create HttpClient instance.
     */
    public static final class Builder {
        private HttpClient httpClient;

        private Builder() {
            httpClient = new HttpClient();
        }

        public Builder addFilter(ClientFilter filter) {
            httpClient.getClient().addFilter(filter);
            return this;
        }

        public Builder addHttpHeader(String key, String value) {
            httpClient.addHttpHeader(key, value);
            return this;
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder addLoggingFilter() {
            httpClient.getClient().addFilter(new LoggingFilter());
            return this;
        }

        public Builder addHmacFilter(String sharedSecret) {
            httpClient.getClient().addFilter(new HmacFilter(sharedSecret));
            return this;
        }

        public HttpClient build() {
            return httpClient;
        }
    }
}

