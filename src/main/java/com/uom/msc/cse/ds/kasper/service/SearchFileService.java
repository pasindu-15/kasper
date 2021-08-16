package com.uom.msc.cse.ds.kasper.service;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import com.uom.msc.cse.ds.kasper.external.adapter.SocketClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;


@Service
@Log4j2
public class SearchFileService {


    private final RouteTable routeTable;

    private final FileStorageService fileStorage;

    private final YAMLConfig yamlConfig;

    private final SocketClient socketClient;

    private final Set<String> hashSet;

    public SearchFileService(RouteTable routeTable, FileStorageService fileStorage, YAMLConfig yamlConfig, SocketClient socketClient, Set<String> hashSet) {
        this.routeTable = routeTable;
        this.fileStorage = fileStorage;
        this.yamlConfig = yamlConfig;
        this.socketClient = socketClient;
        this.hashSet = hashSet;
    }

    public boolean isNewRequest(String uniqId){
        if(hashSet.contains(uniqId)){
            return false;
        }
        hashSet.add(uniqId);
        return true;
    }

    public List<String> searchFileInCurrentNode(String fileName) throws Exception {

        log.info("Search FileIn Current Node Request: {}", fileName);

        return fileStorage.getAvailableFilesByKeyword(fileName);


    }

    public String searchFileInCurrentNode(String fileName, int port, String ip, int hops) throws Exception {

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
                return msg;

            }
            return null;
        }

        public boolean sendSearchData(String msg, String targetIp, int targetPort){
            try{
                String successMsg = String.format("%04d %s", msg.length() + 5 ,msg);
                socketClient.sendAndReceive(targetIp,targetPort,successMsg);

            }catch (Exception e){
                log.error("Failed to SEARCH Recursive");
            }
            return true;

        }

        public void forwardSearchRequest(String requestIp, int requestPort, String keyword, int hops, String uniqId, String incomingIP, int incomingPort){

            if(hops <= 0) {
                log.info("Reached the maximum hop count");
            }
            for (Node n: routeTable.getNeighbours()) {
                if(n.getIpAddress() == incomingIP && n.getPort() == incomingPort){
                    continue;
                }

                String msg = UriComponentsBuilder.fromPath(yamlConfig.getSearchMsg()).buildAndExpand(requestIp,requestPort,keyword,hops, uniqId).toString();
                msg = String.format("%04d %s",msg.length() + 5,msg);
                log.info("search msg Recursive: {} to {}:{} with {} ", msg,  n.getIpAddress(), n.getPort(), uniqId);
                try{
                    String reply = socketClient.sendAndReceive( n.getIpAddress(),n.getPort(),msg);
                    log.info(reply);

                }catch (Exception e){
                    log.error("Failed to SEARCH Recursive");
                    e.printStackTrace();
                }
            }

        }
}
