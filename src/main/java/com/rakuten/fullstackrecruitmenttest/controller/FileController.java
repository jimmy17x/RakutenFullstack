package com.rakuten.fullstackrecruitmenttest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rakuten.fullstackrecruitmenttest.payload.UploadFileResponse;
import com.rakuten.fullstackrecruitmenttest.service.EmlpFileStorageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private EmlpFileStorageService fileStorageService;

	@PostMapping("/uploadEmployeeRecord")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile employeeRecordFile) {
		String fileName = fileStorageService.storeFile(employeeRecordFile);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadEmployeeRecord/")
				.path(fileName).toUriString();

		return new UploadFileResponse(fileName, fileDownloadUri, employeeRecordFile.getContentType(),
				employeeRecordFile.getSize());
	}

	@PostMapping("/uploadMultipleEmployee")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
	}

	@GetMapping("/downloadEmployeeRecord/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}