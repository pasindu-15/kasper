package com.uom.msc.cse.ds.kasper.application.domain.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.*;

@Service
public class FileStorage {
    Set<String> randomData;

    public FileStorage() {
        randomData = new HashSet<String>();
        for(int i = 0; i<3; i = i + 1) {
            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 18) { // length of the random string.
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            String saltStr = salt.toString();
            randomData.add(saltStr);
        }

        System.out.println("Contains Files " + randomData);
    }

    public boolean isFileAvailable(String fileName){
        return randomData.contains(fileName);
    }

}
