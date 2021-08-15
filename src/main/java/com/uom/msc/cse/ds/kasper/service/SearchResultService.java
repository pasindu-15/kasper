package com.uom.msc.cse.ds.kasper.service;


import com.uom.msc.cse.ds.kasper.application.controller.UIController;
import com.uom.msc.cse.ds.kasper.dto.FileSearchResponse;
import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import com.uom.msc.cse.ds.kasper.external.response.ResponseHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@Getter
@Setter
public class SearchResultService {

    private ResponseHandler responseHandler;


    private List<FileSearchResponse> fileSearchResponse;

    public SearchResultService(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        this.fileSearchResponse = new ArrayList<>();
    }


    public void cleanSearchResponse(){
        this.fileSearchResponse.clear();
    }

    public boolean  addToSearchResult(String reply){

        UIController.RECEIVED_TIME_MILLS = System.currentTimeMillis();
        System.err.println("Search File Response : "+reply);
        try{
            FileSearchResponse fileSearchResponseTmp = this.responseHandler.handleSearchResponse(reply);


            this.fileSearchResponse.add(fileSearchResponseTmp);
        }catch (Exception e){
            log.error("Search Failed {}",e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }



}
