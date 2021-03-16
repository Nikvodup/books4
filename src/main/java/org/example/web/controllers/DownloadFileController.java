package org.example.web.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.example.app.services.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


import org.example.web.dto.FileInfo;

@Controller
public class DownloadFileController {

	@Autowired
    FileStorage fileStorage;
	private final List<FileInfo> fileInfos;

	private final Path rootLocation =
			Paths.get(System.getProperty("catalina.home") + File.separator + "external_uploads");


	public DownloadFileController(List<FileInfo> fileInfos) {
		this.fileInfos = fileInfos;
	}


	List<FileInfo> getFileInfos(){
		List<FileInfo> fileInfos = fileStorage.loadFiles().map(
				path -> {
					String filename = path.getFileName().toString();
					String url = MvcUriComponentsBuilder.fromMethodName(DownloadFileController.class,
							"downloadFile", path.getFileName().toString()).build().toString();
					return new FileInfo(filename, url);
				}
		)
				.collect(Collectors.toList());

		  return fileInfos;
	}


	@GetMapping("/files")
	public String getListFiles(Model model) {

		model.addAttribute("files", getFileInfos());
		return "listfiles";
	}

	/*
	 * Download Files
	 */
	@GetMapping("/files{filename}")
	public ResponseEntity<Resource> downloadFile(String filename) throws IOException {
		Path file = rootLocation.resolve(filename);
		Resource resource = new UrlResource(file.toUri());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	}
