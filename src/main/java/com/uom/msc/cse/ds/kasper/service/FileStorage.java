package com.uom.msc.cse.ds.kasper.service;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.application.init.FileStorageInitializer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
public class FileStorage {
    List<String> randomData;

    @Autowired
    FileStorageInitializer fileStorageInitializer;


    public FileStorage(YAMLConfig yamlConfig) {
        randomData = new ArrayList();
//        for(int i = 0; i<3; i = i + 1) {
//            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//            StringBuilder salt = new StringBuilder();
//            Random rnd = new Random();
//            while (salt.length() < 18) { // length of the random string.
//                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
//                salt.append(SALTCHARS.charAt(index));
//            }
//            String saltStr = salt.toString();
//            randomData.add(saltStr);
//        }
//
//        System.out.println("Contains Files " + randomData);


        List<String> fileNames = listFilesFromLoacal(yamlConfig.getUploadDir());

        fileNames.parallelStream().forEach(lst ->randomData.add(lst));

        log.info("Contains Files : {}", randomData);



    }


    public List<String> listFilesFromLoacal(String dir) {
        dir = "/Users/pasindu/Documents/Msc/Sem-2/Distributed Computing/Project/kasper/drive";
        Set<String> fileList =  Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());

        Random rnd = new Random();

        List<String> list = new ArrayList<String>(fileList);
        Collections.shuffle(list);
        return list.subList(0, rnd.nextInt(2)+3);


    }

    public List<String> getAvailableFiles(String keyword){
        return randomData.parallelStream().filter(fileName -> fileName.contains(keyword)).collect(Collectors.toList());
    }

}
