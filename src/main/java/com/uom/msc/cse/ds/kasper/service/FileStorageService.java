package com.uom.msc.cse.ds.kasper.service;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.application.exception.FileStorageException;
import com.uom.msc.cse.ds.kasper.application.exception.MyFileNotFoundException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class FileStorageService {

    @Getter
    private final Path localFileStorageLocation;
    @Getter
    private final Path downloadFileStorageLocation;

    YAMLConfig yamlConfig;

    List<String> randomFileLst;

    @Autowired
    public FileStorageService(YAMLConfig yamlConfig) {
        this.yamlConfig = yamlConfig;
        this.randomFileLst = new ArrayList();

        this.localFileStorageLocation = Paths.get(yamlConfig.getUploadDir())
                .toAbsolutePath().normalize();
        this.downloadFileStorageLocation = Paths.get(yamlConfig.getDownloadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.localFileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the local files will be stored.", ex);
        }

        try {
            Files.createDirectories(this.downloadFileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the downloaded files will be stored.", ex);
        }


        updateRandomFilesFromLocal();

        log.info("Contains Files : {}", randomFileLst);
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.localFileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


            return "SUCCESS";
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.localFileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }



    public void updateRandomFilesFromLocal() {

        randomFileLst.clear();
        List<String> fileList =  Stream.of(new File(yamlConfig.getUploadDir()).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());

        log.info("DEFAULT FILES : {}",fileList.toString());

//        Random rnd = new Random();
//
//        List<String> list = new ArrayList<String>(fileList);
//        Collections.shuffle(list);
//        list = list.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
////        int desiredFileCount = rnd.nextInt(2)+3;
//        int desiredFileCount = 3;
//        int subLstFileCount = Integer.min(desiredFileCount,list.size());
//        List<String> fileNames =  list.subList(0, subLstFileCount);
//        fileNames.parallelStream().forEach(f -> randomFileLst.add(f));
//        fileList.parallelStream().forEach(f -> randomFileLst.add(f));
        randomFileLst = fileList;

        log.info("Local Drive Updated {} ",randomFileLst.toString());

    }

    public List<String> getAvailableFilesByKeyword(String keyword){

        log.info("RAND LIST : {}",randomFileLst.toString());
        return randomFileLst.parallelStream().filter(fileName -> fileName.contains(keyword)).collect(Collectors.toList());
    }
}
