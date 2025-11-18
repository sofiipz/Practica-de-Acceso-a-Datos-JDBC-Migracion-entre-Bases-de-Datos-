package logger;

import javax.xml.bind.*;
import java.io.InputStream;

public class ConfigLoader {

    public static ConfigLog cargar(InputStream input) {
        try {
            JAXBContext context = JAXBContext.newInstance(ConfigLog.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ConfigLog) unmarshaller.unmarshal(input);
        } catch (JAXBException e) {
            throw new RuntimeException("Error al cargar configuración XML", e);
        }
    }
}