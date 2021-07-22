package com.uom.msc.cse.ds.kasper.service;


import com.uom.msc.cse.ds.kasper.dto.FileSearchResponse;
import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import com.uom.msc.cse.ds.kasper.external.response.ResponseHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class SearchResult {

    @Autowired
    ResponseHandler responseHandler;

    public SearchResult(ResponseHandler responseHandler, ArrayList<FileSearchResponse> fileSearchResponse) {
        this.responseHandler = responseHandler;
        this.fileSearchResponse = fileSearchResponse;
    }

    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }

    public void setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public ArrayList<FileSearchResponse> getFileSearchResponse() {
        return fileSearchResponse;
    }

    public void setFileSearchResponse(ArrayList<FileSearchResponse> fileSearchResponse) {
        this.fileSearchResponse = fileSearchResponse;
    }

    ArrayList<FileSearchResponse> fileSearchResponse;

    public void cleanSearchResponse(){
        fileSearchResponse.clear();
    }

    public boolean  addToSearchResult(String reply){

        log.info("Search File Response : {} " , reply);
        try{
            FileSearchResponse fileSearchResponseTmp = responseHandler.handleSearchResponse(reply);
            fileSearchResponse.add(fileSearchResponseTmp);
        }catch (Exception e){
            return false;
        }

        return true;
    }



}
