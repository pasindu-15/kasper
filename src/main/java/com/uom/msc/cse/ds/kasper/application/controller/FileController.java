package com.uom.msc.cse.ds.kasper.application.controller;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.dto.*;
import com.uom.msc.cse.ds.kasper.application.init.FileStorageInitializer;
import com.uom.msc.cse.ds.kasper.service.NodeHandlerService;
import com.uom.msc.cse.ds.kasper.service.SearchResultService;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("${base-url.context}")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    NodeHandlerService nodeHandlerService;

    @Autowired
    private FileStorageInitializer fileStorageService;

    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;


    @Autowired
    RouteTable routeTable;

    @Autowired
    YAMLConfig yamlConfig;

    @Autowired
    SearchResultService searchResultService;


    @PostMapping("/searchFile")
    public SearchFileResponse searchFile(@RequestParam("fileName") String fileName) throws Exception {

        List<String> fileNames = nodeHandlerService.searchOwn(fileName);
        if(fileNames != null && !fileNames.isEmpty()){
            return new SearchFileResponse(fileNames.get(0), nodeHandlerService.getMyNode().getIpAddress(), Integer.toString(nodeHandlerService.getMyNode().getPort()));
        }

        nodeHandlerService.doSearch(fileName,yamlConfig.getHops());
        while (true){
            if(!searchResultService.getFileSearchResponse().isEmpty()){
                break;
            }
        }

        FileSearchResponse fr = searchResultService.getFileSearchResponse().get(0);

        log.info("Search Result : {}",fr.getFiles().toString());
        if(fr != null){
            return new SearchFileResponse(fr.getFiles().get(0), fr.getIp(), Integer.toString(fr.getPort()));
        }

        return null;

    }

    @PostMapping("/downloadFile")
    public SearchFileResponse downloadRemoteFile(@RequestParam("fileName") String fileName, @RequestParam("ipAddress") String ip, @RequestParam("portID") String port) {
        log.info("DOWNLOAD REQUEST RECEIVED | {} | {} | {}",ip,port,fileName);

        nodeHandlerService.download(ip,port,fileName);

        return new SearchFileResponse(fileName, "", "");

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

    @GetMapping("/download/{fileName:.+}")
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
