package com.uom.msc.cse.ds.kasper.external.response;

import com.uom.msc.cse.ds.kasper.dto.File;
import com.uom.msc.cse.ds.kasper.dto.FileSearchResponse;
import com.uom.msc.cse.ds.kasper.util.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
@Log4j2
public class ResponseHandler {

    private static final String REGOK = "REGOK";
    private static final String UNROK = "UNROK";
    private static final String JOIN = "JOINOK";
    private static final String LEAVE = "LEAVEOK";
    private static final String SEROK = "SEROK";

    public List<InetSocketAddress> handleRegisterResponse(String response){

        String[] msgData = response.split(" ");

        if (!REGOK.equals(msgData[1])) {
            throw new IllegalStateException(REGOK + " not received");
        }

        int nodesCount = Integer.parseInt(msgData[2]);

        List<InetSocketAddress> neighbourNodes = neighbourNodes = new ArrayList<>();

        switch (nodesCount) {
            case 0:
                log.info("Successful - No other nodes in the network");
                break;

            case 1:
                log.info("No of nodes found : 1");

                neighbourNodes.add(new InetSocketAddress(msgData[3], Integer.parseInt(msgData[4])));
                break;

            case 2:
                log.info("No of nodes found : 2");

                neighbourNodes.add(new InetSocketAddress(msgData[3], Integer.parseInt(msgData[4])));
                neighbourNodes.add(new InetSocketAddress(msgData[5], Integer.parseInt(msgData[6])));
                break;

            case 9999:
                log.error("Failed, there is some error in the command");
                break;
            case 9998:
                log.error("Failed, already registered to you, unregister first");
                break;
            case 9997:
                log.error("Failed, registered to another user, try a different IP and port");
                break;
            case 9996:
                log.error("Failed, can’t register. BS full");
                break;
            default:
                throw new IllegalStateException("Invalid Response");
        }

        return neighbourNodes;
    }

    public boolean handleUnregisterResponse(String response){

        String[] msgData = response.split(" ");

        if (!UNROK.equals(msgData[1])) {
            throw new IllegalStateException(UNROK + " not received");
        }

        int code = Integer.parseInt(msgData[2]);

        switch (code) {
            case 0:
                log.error("Successfully Unregistered");
                return true;
            case 9999:
                log.error("Error while unregistering. IP and port may not be in the registry or command is incorrect");
                break;
            default:
                throw new IllegalStateException("Invalid Response");
        }
        return false;
    }


    public boolean handleJoinResponse(String response){

        String[] msgData = response.split(" ");

        if (!JOIN.equals(msgData[1])) {
            throw new IllegalStateException(JOIN + " not received");
        }

        int code = Integer.parseInt(msgData[2]);

        switch (code) {
            case 0:
                log.error("Successfully Joined");
                return true;
            case 9999:
                log.error("Error while adding new node to routing table");
                break;
            default:
                throw new IllegalStateException("Invalid Response");
        }
        return false;
    }

    public boolean handleLeaveResponse(String response){

        String[] msgData = response.split(" ");

        if (!LEAVE.equals(msgData[1])) {
            throw new IllegalStateException(LEAVE + " not received");
        }

        int code = Integer.parseInt(msgData[2]);

        switch (code) {
            case 0:
                log.error("Successfully LEAVE");
                return true;
            case 9999:
                log.error("Error Error while removing the node from routing tablet");
                break;
            default:
                throw new IllegalStateException("Invalid Response");
        }
        return false;
    }

    public FileSearchResponse handleSearchResponse(String response){

        String[] msgData = response.split(" ");

        if (!SEROK.equals(msgData[1])) {
            throw new IllegalStateException(SEROK + " not received");
        }

        int code = Integer.parseInt(msgData[2]);

        FileSearchResponse fileSearchResponse = new FileSearchResponse();
        List<String> files = new ArrayList<>();

        if(1 <= code && code < 9998){
            fileSearchResponse.setIp(msgData[3]);
            fileSearchResponse.setPort(Integer.parseInt(msgData[4]));
            fileSearchResponse.setHops(Integer.parseInt(msgData[5]));

            for(int i=1 ; i<=code; i++ ){
               files.add(msgData[5 + i]);
            }
            fileSearchResponse.setFiles(files);
            return fileSearchResponse;
        }


        switch (code) {
            case 0:
                log.info("No matching results. Searched key is not in key table");
                fileSearchResponse.setIp(msgData[3]);
                fileSearchResponse.setPort(Integer.parseInt(msgData[4]));
                fileSearchResponse.setHops(Integer.parseInt(msgData[5]));
                fileSearchResponse.setFiles(files);
                return fileSearchResponse;
            case 9999:
                log.error("Failure due to node unreachable");
                break;
            case 9998:
                log.error("Some other error");
                break;
            default:
                throw new IllegalStateException("Invalid Response");
        }
        return null;
    }
}
