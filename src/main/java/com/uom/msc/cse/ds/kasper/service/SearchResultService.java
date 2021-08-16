package com.uom.msc.cse.ds.kasper.service;


import com.uom.msc.cse.ds.kasper.application.controller.UIController;
import com.uom.msc.cse.ds.kasper.dto.FileSearchResponse;
import com.uom.msc.cse.ds.kasper.external.response.ResponseHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@Getter
@Setter

public class SearchResultService {

    private ResponseHandler responseHandler;


    private List<FileSearchResponse> fileSearchResponse;


    private boolean isListening;

    public SearchResultService(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        this.fileSearchResponse = new ArrayList<>();
    }


    public synchronized void cleanSearchResponse(){
        this.fileSearchResponse.clear();
        this.setListening(true);
    }

//    public synchronized boolean  addToSearchResult(String reply){
    public synchronized boolean  addToSearchResult(FileSearchResponse fileSearchResponse){

        while (!isListening) {
            try {
                System.out.println("BFR");
                wait();
                System.out.println("AFT");
            } catch (InterruptedException e) {
                log.error("addToSearchResult Failed {}",e.getMessage());
            }
        }
        isListening = false;

        UIController.RECEIVED_TIME_MILLS = System.currentTimeMillis();
        log.error("Search File Response : "+fileSearchResponse.toString());

        this.fileSearchResponse.add(fileSearchResponse);

        notifyAll();

        return true;
    }


    public synchronized List<FileSearchResponse> receivedFileSearch(){

        while (isListening){
            try {
                wait(1000);

            } catch (InterruptedException e) {
                log.error("receivedFileSearch Failed {}",e.getMessage());
            }
        }

        isListening = true;
        notifyAll();
        return fileSearchResponse;
    }



}
