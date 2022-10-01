package service.DocumentProceederService.pdfXmlService.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
public class pdfToXMLSConfig {

    @Value("${pdfToXMLSConfig.basicXMLSPath}")
    private String basicXMLSPath;

    @Value("${pdfToXMLSConfig.modifiedXMLSPath}")
    private String modifiedXMLSPath;

    public String getBasicXMLSPath() {
        return basicXMLSPath;
    }

    public void setBasicXMLSPath(String basicXMLSPath) {
        this.basicXMLSPath = basicXMLSPath;
    }

    public String getModifiedXMLSPath() {
        return modifiedXMLSPath;
    }

    public void setModifiedXMLSPath(String modifiedXMLSPath) {
        this.modifiedXMLSPath = modifiedXMLSPath;
    }
}
