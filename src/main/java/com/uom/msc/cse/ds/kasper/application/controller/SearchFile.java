package com.uom.msc.cse.ds.kasper.application.controller;


import com.uom.msc.cse.ds.kasper.application.domain.inputdata.InputDataForFileSearch;
import com.uom.msc.cse.ds.kasper.application.domain.service.DomainFileSearch;
import com.uom.msc.cse.ds.kasper.application.domain.service.FileStorage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@Log4j2
@RequestMapping(value = "${base-url.context}/search-file")
public class SearchFile extends BaseController{

    @Autowired
    DomainFileSearch domainFileSearch;


    @GetMapping(value = "/direct-find", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean searchFileFromNetwork(@Validated @RequestBody InputDataForFileSearch serchFileInput) throws Exception {

        String searchFileName = serchFileInput.getFileName();
        log.info("Search File Name", searchFileName);
        domainFileSearch.searchFileFromNetwork(searchFileName);



        return false;

    }

}
