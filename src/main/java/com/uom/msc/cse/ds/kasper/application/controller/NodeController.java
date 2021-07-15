package com.uom.msc.cse.ds.kasper.application.controller;


import com.uom.msc.cse.ds.kasper.application.service.NodeHandler;
import com.uom.msc.cse.ds.kasper.application.service.RoutingTableService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${base-url.context}/node")
@Log4j2
public class NodeController {


    @Autowired
    RoutingTableService routingTableService;

    @Autowired
    NodeHandler nodeHandler;

    @PostMapping(value="/",produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> request(@Validated @RequestBody(required = true) String msg, HttpServletRequest request)throws Exception{

        log.info("REQ RECEIVED : {}",msg);


        String[] data = msg.split(" ");
        String command = data[0];

        switch (command){
            case "JOIN":
                routingTableService.addToRouteTable(data[1],Integer.parseInt(data[2]));
                break;
            case "LEAVE":
                routingTableService.removeFromRouteTable(data[1],Integer.parseInt(data[2]));


        }

        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @PostMapping(value="/un-reg",produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> unReg(@Validated @RequestBody(required = true) String msg, HttpServletRequest request)throws Exception{

        log.info("REQ RECEIVED : {}",msg);

        String[] data = msg.split(" ");
        String command = data[0];

        switch (command) {
            case "UNREG":
                nodeHandler.unRegAndLeave();
        }

        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

}
