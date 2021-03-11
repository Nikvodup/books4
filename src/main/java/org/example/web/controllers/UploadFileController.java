package org.example.web.controllers;

import org.example.app.services.FileStorage;
import org.example.app.services.FileStorageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UploadFileController {
	
	@Resource
	FileStorage fileStorage;

	@Autowired
	public UploadFileController(FileStorage fileStorage) {
		this.fileStorage = fileStorage;
	}

	@PostMapping("/books/shelf")
    public String uploadMultipartFile(@RequestParam("files") MultipartFile[] files, Model model) {
    	List<String> fileNames = null;
    	
		try {
	        fileNames = Arrays.asList(files)
	                .stream()
	                .map(file -> {
	                	fileStorage.store(file);
	                	return file.getOriginalFilename();
	                })
	                .collect(Collectors.toList());
			
			model.addAttribute("message", "Files uploaded successfully!");
			model.addAttribute("files", fileNames);
		} catch (Exception e) {
			model.addAttribute("message", "Fail!");
			model.addAttribute("files", fileNames);
		}
		
        return "book_shelf";
    }
    
}