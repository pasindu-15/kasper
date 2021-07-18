package com.uom.msc.cse.ds.kasper.service;

import com.uom.msc.cse.ds.kasper.dto.File;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Service
public class FileService {

  /*  public List<File> getFileList(){

        List<File> files = new ArrayList<File>();

        for(int i = 0; i<3; i = i + 1) {
            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 18) { // length of the random string.
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            String saltStr = salt.toString();
            files.add(new File(saltStr));
        }
        return files;

    }*/
}
