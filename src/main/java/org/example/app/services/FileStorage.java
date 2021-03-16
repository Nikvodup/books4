package org.example.app.services;

import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.example.web.dto.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorage {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());


    private final Path rootLocation =
            Paths.get(System.getProperty("catalina.home") + File.separator + "external_uploads");



    public void store(MultipartFile file){
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }




    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }


    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }


    public Stream<Path> loadFiles() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new RuntimeException("\"Failed to read stored file");
        }
    }
}