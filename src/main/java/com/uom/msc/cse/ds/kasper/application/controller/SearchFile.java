package com.uom.msc.cse.ds.kasper.application.controller;


import com.uom.msc.cse.ds.kasper.application.domain.inputdata.InputDataForFileSearch;
import com.uom.msc.cse.ds.kasper.application.domain.service.DomainFileSearch;
import com.uom.msc.cse.ds.kasper.application.domain.service.FileStorage;
import com.uom.msc.cse.ds.kasper.application.service.NodeHandler;
import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Log4j2
@Controller
@RequestMapping(value = "${base-url.context}/search-file")
public class SearchFile extends BaseController{

    @Autowired
    DomainFileSearch domainFileSearch;

    @Autowired
    RouteTable routeTable;

    @Autowired
    NodeHandler nodeHandler;

    @PostMapping(value = "/direct-find", produces = MediaType.APPLICATION_JSON_VALUE)
    public void searchFileFromNetwork(@Validated @RequestBody InputDataForFileSearch serchFileInput) throws Exception {

        String searchFileName = serchFileInput.getFileName();
        log.info("Search File Name {}", searchFileName);
        boolean isFileAvailble = domainFileSearch.searchFileFromNetwork(searchFileName);
        List<Node> visitedNodes = serchFileInput.getVisitedNodes();
        if(visitedNodes.isEmpty()){
            serchFileInput.setRequester(nodeHandler.getMyNode());
        }
        visitedNodes.add(nodeHandler.getMyNode());

        log.info( "File Availability: {}", isFileAvailble);
        if(!isFileAvailble) {
            List<Node> neighbours = routeTable.getNeighbours();
            //Flood the call
            for (Node neighbourNode : neighbours) {
                if(!visitedNodes.contains(neighbourNode)) {

                }
            }
        }
    }

}
