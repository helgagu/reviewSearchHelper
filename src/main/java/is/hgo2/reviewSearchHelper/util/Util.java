package is.hgo2.reviewSearchHelper.util;

import com.sun.jersey.core.util.Base64;
import is.hgo2.reviewSearchHelper.amazonMessages.ItemLookupResponse;
import is.hgo2.reviewSearchHelper.amazonMessages.ItemSearchResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static is.hgo2.reviewSearchHelper.util.Constants.*;

/**
 * This is a util class for creating and extracting information from requests and responses
 *
 * @author Helga
 * @since 2013-03
 */
public class Util {

    private static final String UTF8_CHARSET = "UTF-8";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    static DateFormat dateStamp = new SimpleDateFormat("yyyyMMddHHmmss");
    private JaxbMessageConverter messageConverter;
    private Mac mac = null;

    /**
     * Initializer
     *
     * @throws Exception
     */
    public Util() throws Exception{
        byte[] secretKeyBytes = AWSACCESS_SECRET.getBytes(UTF8_CHARSET);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, HMAC_SHA256_ALGORITHM);
        mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(secretKeySpec);
        messageConverter = new JaxbMessageConverter();
    }

    /**
     * Creates the signature string for the request to Amazon Product Advertising API.
     * Uses hmac-sha256.
     *
     * @param params hashmap with the parameters for the request (key=parameter name; value=parameter value)
     * @param endpoint the Amazon locale to search, either; CA, CN, DE, ES, FR, IN, IT, JP, UK, US. Be aware of different requirements for different locales
     * @return value for the signature parameter for the request with the parameters params
     */
    public String getSignature(Map<String, String> params, String endpoint) {

        String canonicalQS = canonicalize(params);
        String toSign = getStringToSign(endpoint, canonicalQS);
        return calculateHmac(toSign);
    }

    /**
     * This adds the signature to the request, canonicalizes (formats request, escape special characters etc...) and creates the URI request string from the request parameters params
     *
     * @param params hashmap with the parameters for the request (key=parameter name; value=parameter value)
     * @param endpoint the Amazon locale to search, either; CA, CN, DE, ES, FR, IN, IT, JP, UK, US. Be aware of different requirements for different locales
     * @return the URI request string which can be sent to the Amazon API
     */
    public String getRequest(Map<String, String> params, String endpoint){

        params.put(SIGNATURE_PARAMETER, getSignature(params, endpoint));
        String canonicalQS = canonicalize(params);

        StringBuilder url = new StringBuilder();
        url.append(HTTP);
        url.append(AMAZON_URI);
        url.append(endpoint);
        url.append(REQUEST_URI);
        url.append(ENDPOINT_SEPARATOR);
        url.append(canonicalQS);

        return url.toString();
    }

    /**
     * Creates the string to sign with hmac with the canonicalized (formated request, escaped special characters etc...) request.
     *
     * @param endpoint the Amazon locale to search, either; CA, CN, DE, ES, FR, IN, IT, JP, UK, US. Be aware of different requirements for different locales
     * @param canonicalQS   the canonicalized (escaped special characters etc...) request
     * @return string to sign with hmac-sha256
     */
    private String getStringToSign(String endpoint, String canonicalQS) {
        StringBuilder toSign = new StringBuilder();
        toSign.append(REQUEST_METHOD_GET);
        toSign.append(NEWLINE);
        toSign.append(AMAZON_URI);
        toSign.append(endpoint);
        toSign.append(NEWLINE);
        toSign.append(REQUEST_URI);
        toSign.append(NEWLINE);
        toSign.append(canonicalQS);
        return toSign.toString();
    }

    /**
     * Calculates the hmac
     *
     * @param stringToSign the string to apply the hmac to
     * @return value for the signature parameter
     */
    private String calculateHmac(String stringToSign) {
        String signature;
        byte[] data;
        byte[] rawHmac;
        try {
            data = stringToSign.getBytes(UTF8_CHARSET);
            rawHmac = mac.doFinal(data);
            Base64 encoder = new Base64();
            signature = new String(encoder.encode(rawHmac));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(UTF8_CHARSET + " is unsupported!", e);
        }
        return signature;
    }

    /**
     * This escapes special characters and formats the request string.
     *
     * @param params hashmap with the parameters for the request (key=parameter name; value=parameter value)
     * @return request string in the right format
     */
    private String canonicalize(Map<String, String> params){
        SortedMap<String, String> sortedParamMap =
                new TreeMap<>(params);

        if (sortedParamMap.isEmpty()) {
            return "";
        }

        StringBuilder buffer = new StringBuilder();
        Iterator<Map.Entry<String, String>> iter =
                sortedParamMap.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, String> kvpair = iter.next();
            buffer.append(percentEncodeRfc3986(kvpair.getKey()));
            buffer.append("=");
            buffer.append(percentEncodeRfc3986(kvpair.getValue()));
            if (iter.hasNext()) {
                buffer.append("&");
            }
        }
        return buffer.toString();
    }

    /**
     * Escape special characters
     *
     * @param s string to escape
     * @return encoded string
     */
    private String percentEncodeRfc3986(String s) {
        String out;
        try {
            out = URLEncoder.encode(s, UTF8_CHARSET)
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            out = s;
        }
        return out;
    }

    /**
     * Insert the keyword to the power search parameter value.
     * Power search = keywords:%s and language:english
     *
     * @param keyword the search keyword to replace %s, this is either Productivity, Personal Productivity, Efficient, Effective(ness) or knowledge worker productivity
     * @return the power search value with the keyword
     */
    public String addKeywordToPowerSearch(String keyword){

        return String.format(POWER_SEARCH_VALUE, keyword);

    }

    /**
     * Writes an itemSearchResponse to a file
     * @param response itemSearchResponse
     * @throws Exception
     */
    public void writeOriginalResponseToFile(ItemSearchResponse response) throws Exception{

        String filename =  AMAZON_RESPONSES_ITEMSEARCH_FILENAME + getDateTimeStamp();
        FileWriter responseFile = new FileWriter(filename);
        responseFile.write(messageConverter.getMessage(ItemSearchResponse.class, response));
        responseFile.flush();
        responseFile.close();
    }

    /**
     * Marshal the response to xml bytes to insert into database
     * @param response itemSearchResponse
     * @return byte[] response xml
     * @throws Exception
     */
    public byte[] unmarshalResponse(ItemSearchResponse response) throws Exception{
        String xml = messageConverter.getMessage(ItemSearchResponse.class, response);
        return xml.getBytes();
    }

    /**
     * Marshal the response to xml bytes to insert into database
     * @param response itemSearchResponse
     * @return byte[] response xml
     * @throws Exception
     */
    public byte[] unmarshalResponse(ItemLookupResponse response) throws Exception{
        String xml = messageConverter.getMessage(ItemLookupResponse.class, response);
        return xml.getBytes();
    }

    /**
     * Write itemLookupResponse to file
     * @param response itemLookupResponse
     * @throws Exception
     */
    public void writeOriginalResponseToFile(ItemLookupResponse response) throws Exception{

        String filename =  AMAZON_RESPONSE_ITEMLOOKUP_FILENAME + getDateTimeStamp();
        FileWriter responseFile = new FileWriter(filename);
        responseFile.write(messageConverter.getMessage(ItemLookupResponse.class, response));
        responseFile.flush();
        responseFile.close();
    }

    /**
     * DateTimeStamp for filenames
     * @return yyyyMMddHHMMss
     */
    public String getDateTimeStamp(){
        return dateStamp.format(new Date());
    }
}

