package com.uom.msc.cse.ds.kasper.external.rest;

import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.util.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RequestHandler implements RequestHandlerInterface{
    @Autowired
    RestClient restClient;
    public void ping(Node myNode, Node neighbourNode){

        String msg = String.format(Constants.PING_FORMAT, myNode.getIpAddress() , myNode.getPort());
//                String msg = String.format(Constants.MSG_FORMAT, payload.length() + 5,payload);

        try{
            String res = restClient.send(neighbourNode.getIpAddress(), Integer.toString(neighbourNode.getPort()),msg);
        }catch (Exception e){
            log.error("Failed to PING");
        }



    }
}
