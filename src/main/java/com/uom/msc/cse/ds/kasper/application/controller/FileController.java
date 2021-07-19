package com.uom.msc.cse.ds.kasper.application.controller;

import com.uom.msc.cse.ds.kasper.application.init.Initializer;
import com.uom.msc.cse.ds.kasper.application.payload.SearchFileResponse;
import com.uom.msc.cse.ds.kasper.application.payload.UploadFileResponse;
import com.uom.msc.cse.ds.kasper.application.service.FileStorageService;
import com.uom.msc.cse.ds.kasper.application.service.NodeHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    Initializer initializer;

    @Autowired
    NodeHandler nodeHandler;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @PostMapping("/searchFile")
    public SearchFileResponse searchFile(@RequestParam("fileName") String fileName) {

        InetAddress localHost = null;
        try {
            localHost = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(),e);
        }

        int port = initializer.getPort();

        return new SearchFileResponse(fileName, localHost.getHostAddress(), Integer.toString(port));
    }

    @PostMapping("/downloadRemoteFile")
    public SearchFileResponse downloadRemoteFile(@RequestParam("fileName") String fileName, @RequestParam("ipAddress") String ipAddress, @RequestParam("portID") String portID) {
        log.info("File Name: " + fileName);
        log.info("IP Address: " + ipAddress);
        log.info("Port ID: " + portID);

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        String URL = "http://" + ipAddress + ":" + portID + "/downloadFile/" + fileName;
        URI uri = null;
        try {
            uri = new URI(URL);
        } catch ( URISyntaxException ex) {
            logger.error(ex.getMessage());
        }

        log.info("URL: " + uri.toString());

        ResponseEntity<Resource> responseEntity = restTemplate.exchange( uri, HttpMethod.GET, null, Resource.class );

        InputStream responseInputStream;
        try {
            responseInputStream = responseEntity.getBody().getInputStream();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        File targetFile = new File(fileStorageService.getDownloadFileStorageLocation().toString() + "/" + fileName);

        try {
            java.nio.file.Files.copy(responseInputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IOUtils.closeQuietly(responseInputStream);

        log.info("File downloaded");

        return new SearchFileResponse(fileName, ipAddress, portID);
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        log.info("downloadFile called");
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
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
