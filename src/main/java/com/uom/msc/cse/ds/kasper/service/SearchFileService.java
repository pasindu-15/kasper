package com.uom.msc.cse.ds.kasper.service;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import com.uom.msc.cse.ds.kasper.external.adapter.SocketClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;
import java.util.stream.Collectors;



@Service
@Log4j2
public class SearchFileService {

        @Autowired
        RouteTable routeTable;

        @Autowired
        FileStorage fileStorage;

        @Autowired
        YAMLConfig yamlConfig;

    public SearchFileService(Set<String> hash_Set) {
        this.hash_Set = hash_Set;
    }

    @Autowired
        SocketClient socketClient;

        Set<String> hash_Set;

        public boolean isNewRequest(String uniqId){
            if(hash_Set.contains(uniqId)){
                return false;
            }
            hash_Set.add(uniqId);
            return true;
        }

        public boolean searchFileInCurrentNode(String fileName, String[] replyTmp, int port, String ip, int hops) throws Exception {

            //Node myNode = nodeHandler.getMyNode();

            log.info("Search FileIn Current Node Request: {}", fileName);
            if (fileStorage.isFileAvailable(fileName)) {
                //  search-reply: "SEROK {No of Files} {ip} {port} {hops} {file names}"
                String msg = UriComponentsBuilder.fromPath(yamlConfig.getSearchReply()).buildAndExpand(1, ip, port, hops, fileName).toString();
                log.info("Success Reply: {}", msg);
                replyTmp[0] = msg;
                return true;
            }
            return false;
        }

        public boolean sendSearchData(String msg, String targetIp, int targetPort){
            try{
//            String res = restClient.send(neighbourNode.getIpAddress(), Integer.toString(neighbourNode.getPort()),msg);
                String reply = socketClient.sendAndReceive(targetIp,targetPort,msg);

                log.info(reply.toString());
            }catch (Exception e){
                log.error("Failed to SEARCH Recursive");
            }
            return true;

        }


        public String ReqursiveSearchCall(String requestIp, int requestPort, String keyword, int hops, String uniqId){
            String replyG = "Sent to all neigours";

            if(hops <= 0) {
                return replyG;
            }
            for (Node n: routeTable.getNeighbours()) {
                String reply;
                reply = searchWithStringReply(requestIp, requestPort, keyword, hops, n.getIpAddress(), n.getPort(), uniqId);
                //"SEROK {No of Files} {ip} {port} {hops} {file names}"
                /*if(reply == null){
                    continue;
                }
                String[] msgData = reply.split(" ");
                String command = msgData[1];
                if(command == "SEROK"){
                    return reply;
                }*/
            }
            return replyG;
        }


    public String searchWithStringReply(String requestIp, int requestPort, String keyword,int hops, String targetIp, int targetPort, String uniqId){

        if(targetIp == null || targetPort == 0){
            return null;
        }
        String msg = UriComponentsBuilder.fromPath(yamlConfig.getSearchMsg()).buildAndExpand(requestIp,requestPort,keyword,hops, uniqId).toString();
        msg = String.format("%04d %s",msg.length() + 5,msg);
        log.info("search msg Reqursive: {} to {}:{} with {} ", msg,  targetIp, targetPort, uniqId);
        try{
//            String res = restClient.send(neighbourNode.getIpAddress(), Integer.toString(neighbourNode.getPort()),msg);
            String reply = socketClient.sendAndReceive(targetIp,targetPort,msg);

            log.info(reply.toString());

            return reply;


        }catch (Exception e){
            log.error("Failed to SEARCH Recursive");
        }
        return null;

    }
}