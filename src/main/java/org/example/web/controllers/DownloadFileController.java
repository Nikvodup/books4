package org.example.web.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


import org.example.web.dto.FileInfo;

@Controller
public class DownloadFileController {



    private final Path rootLocation =
            Paths.get(System.getProperty("catalina.home") + File.separator + "external_uploads");


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

    public Resource loadFile(String filename) {
        try {
            Path file = this.rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error! -> message = " + e.getMessage());
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


    @GetMapping("/files")
    public String getListFiles(Model model) {
        List<FileInfo> fileInfos = loadFiles().map(
                path ->	{
                    String filename = path.getFileName().toString();
                    String url = MvcUriComponentsBuilder.fromMethodName(DownloadFileController.class,
                            "downloadFile", path.getFileName().toString()).build().toString();
                    return new FileInfo(filename, url);
                }
        )
                .collect(Collectors.toList());

        model.addAttribute("files", fileInfos);
        return "listfiles";
    }

    /*
     * Download Files
     */
    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource file = this.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
