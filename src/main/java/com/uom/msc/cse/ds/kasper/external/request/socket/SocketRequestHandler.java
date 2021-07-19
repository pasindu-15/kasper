package com.uom.msc.cse.ds.kasper.external.request.socket;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.dto.File;
import com.uom.msc.cse.ds.kasper.dto.FileSearchResponse;
import com.uom.msc.cse.ds.kasper.external.request.RequestHandlerInterface;
import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.external.adapter.SocketClient;
import com.uom.msc.cse.ds.kasper.external.response.ResponseHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetSocketAddress;
import java.util.List;


@Log4j2
@Service
public class SocketRequestHandler implements RequestHandlerInterface {
    @Autowired
    YAMLConfig yamlConfig;

    @Autowired
    ResponseHandler responseHandler;

    @Autowired
    SocketClient socketClient;


    public List<InetSocketAddress> register(Node myNode){

        String msg = UriComponentsBuilder.fromPath(yamlConfig.getRegMsg()).buildAndExpand(myNode.getIpAddress(),myNode.getPort(),myNode.getUserName()).toString();
        msg = String.format("%04d %s",msg.length() + 5,msg);
        try{
//            String res = restClient.send(neighbourNode.getIpAddress(), Integer.toString(neighbourNode.getPort()),msg);
            String reply = socketClient.sendAndReceive(yamlConfig.getBSIPAddress(),yamlConfig.getBSPort(),msg);

            return  responseHandler.handleRegisterResponse(reply);

        }catch (Exception e){
            log.error("Failed to REG");
            e.printStackTrace();
        }
        return null;
    }

    public boolean unRegister(Node myNode){

        String msg = UriComponentsBuilder.fromPath(yamlConfig.getUnRegMsg()).buildAndExpand(myNode.getIpAddress(),myNode.getPort(),myNode.getUserName()).toString();
        msg = String.format("%04d %s",msg.length() + 5,msg);
        try{
//            String res = restClient.send(neighbourNode.getIpAddress(), Integer.toString(neighbourNode.getPort()),msg);
            String reply = socketClient.sendAndReceive(yamlConfig.getBSIPAddress(),yamlConfig.getBSPort(),msg);

            return  responseHandler.handleUnregisterResponse(reply);

        }catch (Exception e){
            log.error("Failed to UNREG");
            e.printStackTrace();
        }
        return false;
    }


    public boolean join(Node myNode,String targetIp, int targetPort){

        String msg = UriComponentsBuilder.fromPath(yamlConfig.getJoinMsg()).buildAndExpand(myNode.getIpAddress(),myNode.getPort()).toString();
        msg = String.format("%04d %s",msg.length() + 5,msg);
        try{
//            String res = restClient.send(neighbourNode.getIpAddress(), Integer.toString(neighbourNode.getPort()),msg);
            String reply = socketClient.sendAndReceive(targetIp,targetPort,msg);

            return responseHandler.handleJoinResponse(reply);
//            System.out.println(reply);
        }catch (Exception e){
            log.error("Failed to JOIN");
            e.printStackTrace();
        }
        return false;

    }
    public boolean leave(Node myNode,String targetIp, int targetPort){

        String msg = UriComponentsBuilder.fromPath(yamlConfig.getLeaveMsg()).buildAndExpand(myNode.getIpAddress(),myNode.getPort()).toString();
        msg = String.format("%04d %s",msg.length() + 5,msg);
        try{
//            String res = restClient.send(neighbourNode.getIpAddress(), Integer.toString(neighbourNode.getPort()),msg);
            String reply = socketClient.sendAndReceive(targetIp,targetPort,msg);
            responseHandler.handleLeaveResponse(reply);
        }catch (Exception e){
            log.error("Failed to LEAVE");
        }
        return false;

    }


    public FileSearchResponse search(Node myNode, String keyword,int hops, String targetIp, int targetPort){

        if(targetIp == null || targetPort == 0){
            return null;
        }
        String msg = UriComponentsBuilder.fromPath(yamlConfig.getSearchMsg()).buildAndExpand(myNode.getIpAddress(),myNode.getPort(),keyword,hops).toString();
        msg = String.format("%04d %s",msg.length() + 5,msg);
        log.info("search msg: {}", msg);
        try{
//            String res = restClient.send(neighbourNode.getIpAddress(), Integer.toString(neighbourNode.getPort()),msg);
            String reply = socketClient.sendAndReceive(targetIp,targetPort,msg);
            FileSearchResponse fileSearchResponse = responseHandler.handleSearchResponse(reply);

            log.info(fileSearchResponse.toString());

            return fileSearchResponse;


        }catch (Exception e){
            log.error("Failed to SEARCH");
        }
        return null;

    }





}
