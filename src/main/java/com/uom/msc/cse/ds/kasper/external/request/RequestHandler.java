package com.uom.msc.cse.ds.kasper.external.request;

import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.util.Constants;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestHandler implements RequestHandlerInterface{
    @Autowired
    RestClient restClient;
    public void ping(Node myNode, Node neighbourNode){

        String msg = String.format(Constants.PING_FORMAT, myNode.getIpAddress() , myNode.getPort());
//                String msg = String.format(Constants.MSG_FORMAT, payload.length() + 5,payload);

        String res = restClient.send(neighbourNode.getIpAddress(), Integer.toString(neighbourNode.getPort()),msg);

    }
}
