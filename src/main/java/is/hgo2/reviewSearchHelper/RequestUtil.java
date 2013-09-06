package is.hgo2.reviewSearchHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.sun.jersey.core.util.Base64;

import static is.hgo2.reviewSearchHelper.Constants.*;

/**
 * All requests sent to the amazon interface need to be authenticated using
 * HMAC. This Jersey client filter calculates the HMAC and adds it to the request.
 *
 * @author Helga
 * @since 2013-03
 */
public class RequestUtil {

    private static final String UTF8_CHARSET = "UTF-8";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private SecretKeySpec secretKeySpec = null;
    private Mac mac = null;

    public RequestUtil() throws Exception{
        byte[] secretyKeyBytes = AWSACCESS_SECRET.getBytes(UTF8_CHARSET);
        secretKeySpec =
                new SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM);
        mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(secretKeySpec);
    }

    public String getSignature(Map<String, String> params, String endpoint) {

        String canonicalQS = canonicalize(params);
        String toSign = getStringToSign(endpoint, canonicalQS);
        return hmac(toSign);
    }

    public String getRequest(Map<String, String> params, String endpoint){

        params.put(PARAMETER_SIGNATURE, getSignature(params, endpoint));
        String canonicalQS = canonicalize(params);

        StringBuilder url = new StringBuilder();
        url.append(HTTP);
        url.append(endpoint);
        url.append(REQUEST_URI);
        url.append(ENDPOINT_SEPARATOR);
        url.append(canonicalQS);

        return url.toString();
    }

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
}
