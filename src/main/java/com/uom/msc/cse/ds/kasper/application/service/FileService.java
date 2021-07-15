package com.uom.msc.cse.ds.kasper.application.service;

import com.uom.msc.cse.ds.kasper.dto.File;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    public List<File> getFileList(){

        List<File> files = new ArrayList<File>();

        files.add(new File("FileA"));
        files.add(new File("FileB"));

        return files;

    }
}
