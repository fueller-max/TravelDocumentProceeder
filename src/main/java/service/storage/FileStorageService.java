package service.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import util.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.stream.Stream;

@Service
public class FileStorageService implements StorageService{

    private final Path rootLocation;
    private Logger logger = new Logger();

    @Autowired
    public FileStorageService(StorageProperties storageProperties){
        this.rootLocation = Paths.get(storageProperties.getLocation());
    }


    @Override
    public void init() {
        try{
            Files.createDirectories(rootLocation);
        }catch (IOException exception){
            throw new StorageException("Could not initialize storage", exception);
        }
    }

    @Override
    public void store(MultipartFile file) {
        try{

            if(file.isEmpty()){
                throw new StorageException("Cannot store an empty file");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(file.getOriginalFilename()))
                                                     .normalize().toAbsolutePath();

            if(!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())){
                throw new StorageException("Cannot store the file outside current directory");
            }

            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

        }catch (IOException exception){
            throw new StorageException("Could not store the file", exception);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
         return Files.walk(this.rootLocation, 1)
                 .filter(path -> !path.equals(this.rootLocation))
                 .map(this.rootLocation::relativize);
         }catch (IOException exception){
         throw new StorageException("Could not read storied files");
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) throws StorageFileNotFoundException {
        try{
         Path file = load(filename);
         Resource resource = new UrlResource(file.toUri());
         if(resource.exists() || resource.isReadable()){
             return resource;
         }else
         {
             logger.writeToConsole("Exception \"StorageFileNotFoundException\" has been triggered");
             throw new StorageFileNotFoundException("Could not read the file: " + filename);
         }
        }
        catch (MalformedURLException exception) {
            throw new StorageFileNotFoundException("Could not read the file: " + filename,exception);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
