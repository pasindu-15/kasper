package com.uom.msc.cse.ds.kasper.application.controller;


import lombok.extern.log4j.Log4j2;
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



    @PostMapping(value="/",produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> request(@Validated @RequestBody(required = true) String msg, HttpServletRequest request)throws Exception{

        log.info("REQ RECEIVED : {}",msg);


        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
