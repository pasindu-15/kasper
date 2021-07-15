package com.uom.msc.cse.ds.kasper.application.domain.service;


import com.uom.msc.cse.ds.kasper.application.domain.inputdata.InputDataForFileSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class DomainFileSearch {

    @Autowired
    FileStorage fileStorage;

    public boolean searchFileFromNetwork(String serchFileName)  {

        //Check For Current Node
        boolean isFileInCurrentNode = fileStorage.isFileAvailable(serchFileName);
        if(isFileInCurrentNode) {
            //Return Info to the Requester
        } else {
            //Call Neighbour
        }

        return false;
    }
}
