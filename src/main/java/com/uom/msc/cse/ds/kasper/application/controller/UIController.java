package com.uom.msc.cse.ds.kasper.application.controller;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.application.exception.FileStorageException;
import com.uom.msc.cse.ds.kasper.dto.*;
import com.uom.msc.cse.ds.kasper.service.FileStorageService;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("${base-url.context}")
public class UIController {

    private static final Logger logger = LoggerFactory.getLogger(UIController.class);

    @Autowired
    NodeHandlerService nodeHandlerService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;


    @Autowired
    RouteTable routeTable;

    @Autowired
    YAMLConfig yamlConfig;

    @Autowired
    SearchResultService searchResultService;


    @PostMapping("/searchFile")
    public FileSearchResponse searchFile(@RequestParam("fileName") String fileName) throws Exception {

        List<String> fileNames = nodeHandlerService.searchOwn(fileName);
        if (fileNames != null && !fileNames.isEmpty()) {
            FileSearchResponse fr = new FileSearchResponse();
            fr.setIp(nodeHandlerService.getMyNode().getIpAddress());
            fr.setPort(nodeHandlerService.getMyNode().getPort());
            fr.setFiles(fileNames);
            return fr;
        }

        nodeHandlerService.doSearch(fileName, yamlConfig.getHops());
        while (true) {
            if (!searchResultService.getFileSearchResponse().isEmpty()) {
                break;
            }
        }

        FileSearchResponse fr = searchResultService.getFileSearchResponse().get(0);

        log.info("Search Result : {}", fr.getFiles().toString());
        if (fr != null) {
            return fr;
        }

        return null;

    }

    @PostMapping("/downloadFile")
    public SearchFileResponse downloadRemoteFile(@RequestParam("fileName") String fileName, @RequestParam("ipAddress") String ip, @RequestParam("portID") String port) {
        log.info("DOWNLOAD REQUEST RECEIVED | {} | {} | {}", ip, port, fileName);

        nodeHandlerService.download(ip, port, fileName);

        return new SearchFileResponse(fileName, "", "");

    }


    @PostMapping("/uploadMultipleFiles")
    public ResponseEntity<String> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {

        String status;
        try {
            Arrays.asList(files)
                    .parallelStream()
                    .forEach(file -> fileStorageService.storeFile(file));
            fileStorageService.updateRandomFilesFromLoacal();
        } catch (FileStorageException e) {
            return ResponseEntity.badRequest().body("FAIL");
        }

        return ResponseEntity.ok().body("SUCCESS");

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
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/leave")
    public String leaveClient(@RequestParam("msg") String msg) {
        nodeHandlerService.removeFromOwnRouteTable();
        System.exit(0);
        return null;
    }
}


