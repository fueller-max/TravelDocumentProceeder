package service;



import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import service.storage.StorageProperties;
import service.storage.StorageService;


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@ComponentScan
public class Application {
    public static void main(String[] args) throws IOException {

        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner init(StorageService storageService){
        return args -> {
          storageService.deleteAll();
          storageService.init();
        };
    }
}

