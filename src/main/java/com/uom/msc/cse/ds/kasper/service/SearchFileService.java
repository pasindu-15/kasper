package com.uom.msc.cse.ds.kasper.service;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.stream.Collectors;



@Service
@Log4j2
public class SearchFileService {
        @Autowired
        NodeHandler nodeHandler;

        @Autowired
        RouteTable routeTable;

        @Autowired
        FileStorage fileStorage;

        @Autowired
        YAMLConfig yamlConfig;

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

        public String ReqursiveSearchCall(String requestIp, int requestPort, String keyword, int hops){
            String reply = "";
            for (Node n: routeTable.getNeighbours()) {
                reply = nodeHandler.searchWithStringReply(requestIp, requestPort, keyword, hops, n.getIpAddress(), n.getPort());
            }
            return reply;
        }



}
