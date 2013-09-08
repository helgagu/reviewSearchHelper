package is.hgo2.reviewSearchHelper.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Generic class used for message conversion when using jaxb
 *
 * @author fridrik
 * @since 12/12/12 10:53 AM
 */
public class JaxbMessageConverter {
    protected <T> T convert(Class<T> messageType, String message) throws JAXBException {
        return convert(messageType, new StringReader(message));
    }

    /**
     * Converts a String to a object of type T. Note, object must be Entity class.
     *
     * @param messageType type of class to convert to
     * @param reader
     * @param <T>         type of class
     * @return new object of type T
     * @throws JAXBException
     */
    protected <T> T convert(Class<T> messageType, Reader reader) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(messageType);
        Unmarshaller u = jc.createUnmarshaller();
        return (T) u.unmarshal(reader);
    }

    /**
     * Creates a xml string from an object of type T. Note, object must be Entity class.
     *
     * @param messageType
     * @param object
     * @param <T>
     * @return
     * @throws JAXBException
     */
    protected <T> String getMessage(Class<T> messageType, Object object) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(messageType);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        m.marshal(object, writer);
        return writer.toString();
    }
}
