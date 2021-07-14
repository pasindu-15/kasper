package com.uom.msc.cse.ds.kasper.external.rest;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.util.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Log4j2
public class RequestHandler implements RequestHandlerInterface{
    @Autowired
    RestClient restClient;

    @Autowired
    YAMLConfig yamlConfig;
    public void join(Node myNode, Node neighbourNode){

        String msg = UriComponentsBuilder.fromPath(yamlConfig.getJoinMsg()).buildAndExpand(myNode.getIpAddress(),myNode.getPort()).toString();

        try{
            String res = restClient.send(neighbourNode.getIpAddress(), Integer.toString(neighbourNode.getPort()),msg);
        }catch (Exception e){
            log.error("Failed to JOIN");
        }

    }
    public void leave(Node myNode, Node neighbourNode){

        String msg = UriComponentsBuilder.fromPath(yamlConfig.getLeaveMsg()).buildAndExpand(myNode.getIpAddress(),myNode.getPort()).toString();

        try{
            String res = restClient.send(neighbourNode.getIpAddress(), Integer.toString(neighbourNode.getPort()),msg);
        }catch (Exception e){
            log.error("Failed to LEAVE");
        }

    }
}
