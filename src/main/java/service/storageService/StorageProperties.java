package service.storageService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties("storage")
@PropertySource("application.properties")
public class StorageProperties {
    /**
     * Folder location for storing files
     */

    @Value("${storageProperties.uploadLocation}")
    private String location;

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }
}
