package com.uom.msc.cse.ds.kasper.application.domain.service;


import com.uom.msc.cse.ds.kasper.service.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainFileSearch {

    @Autowired
    FileStorage fileStorage;

    public boolean searchFileFromNetwork(String serchFileName)  {

        //Check For Current Node
        boolean isFileInCurrentNode = fileStorage.isFileAvailable(serchFileName);
        if(isFileInCurrentNode) {
            return true;
        }
        return false;
    }
}
