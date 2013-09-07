package is.hgo2.reviewSearchHelper;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.sun.jersey.core.util.Base64;
import is.hgo2.reviewSearchHelper.amazonMessages.*;

import static is.hgo2.reviewSearchHelper.Constants.*;

/**
 * This is a util class for creating and extracting information from requests and responses
 *
 * @author Helga
 * @since 2013-03
 */
public class Util {

    private static final String UTF8_CHARSET = "UTF-8";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private SecretKeySpec secretKeySpec = null;
    private Mac mac = null;

    /**
     * Initializer
     *
     * @throws Exception
     */
    public Util() throws Exception{
        byte[] secretyKeyBytes = AWSACCESS_SECRET.getBytes(UTF8_CHARSET);
        secretKeySpec =
                new SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM);
        mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(secretKeySpec);
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
        return hmac(toSign);
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
    private String hmac(String stringToSign) {
        String signature = null;
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
    private String canonicalize(Map<String, String> params)
    {
        SortedMap<String, String> sortedParamMap =
                new TreeMap<String, String>(params);

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
     * To create a string with results
     *
     * @param results hashmap of values to create a string (key = description; value = the result value)
     * @param newline should append a newline between hashmap values
     * @return string which can be printed in the console or in a text file
     */
    public String writeResults(Map<String, String> results, Boolean newline){

        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iter =
                results.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, String> kvpair = iter.next();
            builder.append(kvpair.getKey());
            builder.append(kvpair.getValue());
            if(newline){
                builder.append(NEWLINE);
            }

        }
        return builder.toString();

    }

    /**
     * To create a string with the total results and pages of an ItemSearch as well as a list of book titles
     *
     * @param itemSearchResponse the response object from an ItemSearch
     */
    public void writeTitleList(ItemSearchResponse itemSearchResponse) {
        StringBuilder result = new StringBuilder();
        for (Items items: itemSearchResponse.getItems()){

            Map<String, String> itemsResults = new HashMap<>();
            itemsResults.put("Total Results: ", items.getTotalResults().toString());
            itemsResults.put("Total Pages: ", items.getTotalPages().toString());

            for(Item item: items.getItem()){
                Map<String, String> itemResults = new HashMap<>();
                itemResults.put("Title: ", item.getItemAttributes().getTitle());
                result.append(writeResults(itemResults, Boolean.TRUE));
            }
            result.append(writeResults(itemsResults, Boolean.TRUE));

            System.out.println(result.toString());
        }
    }

    /**
     * To create a string with the total results and pages of an ItemSearch with responseGroup = BinSearch
     * as well as a list of all the bin names and their binItemCount.
     *
     * @param response the response object of the ItemSearch with responseGroup = BinSearch
     */
    public void writeBinList(ItemSearchResponse response) {
        StringBuilder result = new StringBuilder();
        for (Items items: response.getItems()){
            Map<String, String> itemsResults = new HashMap<>();
            itemsResults.put("Total Results: ", items.getTotalResults().toString());
            itemsResults.put("Total Pages: ", items.getTotalPages().toString());

            for(SearchBinSet bin: items.getSearchBinSets().getSearchBinSet()){


                itemsResults.put("NarrowBy: ", bin.getNarrowBy());

                for(Bin binDetails: bin.getBin()) {
                    Map<String, String> itemResults = new HashMap<>();
                    itemResults.put("BinName: ", binDetails.getBinName());
                    itemResults.put("BinItemCount: ", binDetails.getBinItemCount().toString());
                    result.append(writeResults(itemResults, Boolean.TRUE));
                }

                result.append(writeResults(itemsResults, Boolean.TRUE));
            }


            System.out.println(result.toString());
        }
    }

    /**
     * Find a BrowseNodeId name by finding the name of the bin for that browseNodeId.
     *
     * @param response the response object of the ItemSearch with responseGroup = BinSearch
     * @param browseNodeId the id of a specific browseNode (category for a subject)
     * @return string with the browseNodeId name
     */
    public String getBrowseNodeIdName(ItemSearchResponse response, String browseNodeId){

        if(response != null){
            for (Items items: response.getItems()){
                for(SearchBinSet bin: items.getSearchBinSets().getSearchBinSet()){
                    for(Bin binDetails: bin.getBin()) {
                        for(Bin.BinParameter param: binDetails.getBinParameter()){
                            if(param.getValue().equalsIgnoreCase(browseNodeId)){
                                return binDetails.getBinName();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * This fetches all the browseNodesIds in a bin list returned by an ItemSearch with responseGroup = BinSearch
     * if the binItemCount is more than 100 and needs to be narrowed more.
     *
     * Exclusion criteria can also be applied, the exclusion criteria applied are defined in ExclusionCriteria.excludeBrowseNodeId(bin name).
     * The exclusion criteria were analyzed manually.
     *
     * @param response the response object of the ItemSearch with responseGroup = BinSearch
     * @param useExclusionCriteria true=use the exclusion criteria specified in method ExclusionCriteria.excludeBrowseNodeId(bin name), false=no exclusion criteria
     * @return List of browseNodeIds
     */
    public List<String> getBrowseNodeIds(ItemSearchResponse response, Boolean useExclusionCriteria) {

        List<String> browseNodeIds = new ArrayList();
        for (Items items: response.getItems()){
            for(SearchBinSet bin: items.getSearchBinSets().getSearchBinSet()){
                for(Bin binDetails: bin.getBin()) {
                    if(useExclusionCriteria){
                        if(!ExclusionCriteria.excludeBrowseNodeId(binDetails.getBinName())){
                            if(resultsMoreThanHundred(binDetails.getBinItemCount())) {
                                for(Bin.BinParameter param: binDetails.getBinParameter()) {
                                    browseNodeIds.add(param.getValue());
                                }
                            }
                        }
                    }
                    else{
                        if(resultsMoreThanHundred(binDetails.getBinItemCount())) {
                            for(Bin.BinParameter param: binDetails.getBinParameter()) {
                                browseNodeIds.add(param.getValue());
                            }
                        }
                    }

                }
            }
        }
        return browseNodeIds;
    }

    /**
     * Compares the binItemCount to see if it is greater than 100.
     *
     * @param binItemCount the amount of result items in a specific bin
     * @return true=binItemCount is greater than 100, false=binItemCount is equal or smaller than 100
     */
    public Boolean resultsMoreThanHundred(BigInteger binItemCount){
          if(binItemCount.compareTo(BigInteger.valueOf(100)) == 1){
            //If binItemCount is greater than 100 return true. CompareTo returns 1 if greater than.
            return Boolean.TRUE;
          }

          return Boolean.FALSE;
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
}

