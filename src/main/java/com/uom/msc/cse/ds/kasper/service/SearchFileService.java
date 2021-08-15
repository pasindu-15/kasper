package com.uom.msc.cse.ds.kasper.service;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import com.uom.msc.cse.ds.kasper.external.adapter.SocketClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;


@Service
@Log4j2
public class SearchFileService {

    @Autowired
    RouteTable routeTable;

    @Autowired
    FileStorageService fileStorage;

    @Autowired
    YAMLConfig yamlConfig;

    @Autowired
    SocketClient socketClient;

    Set<String> hash_Set;

    public SearchFileService(Set<String> hash_Set) {
        this.hash_Set = hash_Set;
    }


    public boolean isNewRequest(String uniqId){
        if(hash_Set.contains(uniqId)){
            return false;
        }
        hash_Set.add(uniqId);
        return true;
    }

    public List<String> searchFileInCurrentNode(String fileName) throws Exception {

        log.info("Search FileIn Current Node Request: {}", fileName);

        return fileStorage.getAvailableFilesByKeyword(fileName);


    }

    public boolean searchFileInCurrentNode(String fileName, String[] replyTmp, int port, String ip, int hops) throws Exception {

            log.info("Search FileIn Current Node Request: {}", fileName);

            List<String> availableFiles = fileStorage.getAvailableFilesByKeyword(fileName);

            if (!availableFiles.isEmpty()) {
                String fileListStr = "";
                for (String file: availableFiles) {
                    fileListStr += file+" ";
                }
                fileListStr = fileListStr.substring(0,fileListStr.length()-1);

                hops = yamlConfig.getHops()+1 - hops;

                //  search-reply: "SEROK {No of Files} {ip} {port} {hops} {file names}"
                String msg = UriComponentsBuilder.fromPath(yamlConfig.getSearchReply()).buildAndExpand(availableFiles.size(), ip, port, hops, fileListStr).toString();
                log.info("Success Reply: {}", msg);
                replyTmp[0] = msg;
                return true;
            }
            return false;
        }

        public boolean sendSearchData(String msg, String targetIp, int targetPort){
            try{
                String reply = socketClient.sendAndReceive(targetIp,targetPort,msg);
                log.info(reply);
            }catch (Exception e){
                log.error("Failed to SEARCH Recursive");
            }
            return true;

        }


        public String recursiveSearchCall(String requestIp, int requestPort, String keyword, int hops, String uniqId, String incomingIP, int incomingPort){
            String reply = "Sent to all neighbours";

            if(hops <= 0) {
                return reply;
            }
            for (Node n: routeTable.getNeighbours()) {
                if(n.getIpAddress() == incomingIP && n.getPort() == incomingPort){
                    continue;
                }
                reply = searchWithStringReply(requestIp, requestPort, keyword, hops, n.getIpAddress(), n.getPort(), uniqId);
            }
            return reply;
        }


    public String searchWithStringReply(String requestIp, int requestPort, String keyword,int hops, String targetIp, int targetPort, String uniqId){

        if(targetIp == null || targetPort == 0){
            return null;
        }
        String msg = UriComponentsBuilder.fromPath(yamlConfig.getSearchMsg()).buildAndExpand(requestIp,requestPort,keyword,hops, uniqId).toString();
        msg = String.format("%04d %s",msg.length() + 5,msg);
        log.info("search msg Recursive: {} to {}:{} with {} ", msg,  targetIp, targetPort, uniqId);
        try{
            String reply = socketClient.sendAndReceive(targetIp,targetPort,msg);
            log.info(reply);
            return reply;

        }catch (Exception e){
            log.error("Failed to SEARCH Recursive");
            e.printStackTrace();
        }
        return null;

    }
}
