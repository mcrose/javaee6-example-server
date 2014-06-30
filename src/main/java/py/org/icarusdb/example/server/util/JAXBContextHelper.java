/**
 * 
 */
package py.org.icarusdb.example.server.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * @author rgamarra
 *
 */
public class JAXBContextHelper
{
    public static Object getEntity(Class<?> clazz, String entity) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        InputStream is = new ByteArrayInputStream(entity.getBytes());
        
        return unmarshaller.unmarshal(is); 
    }

}
