package com.uom.msc.cse.ds.kasper.application.socket;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.dto.FileSearchResponse;
import com.uom.msc.cse.ds.kasper.external.response.ResponseHandler;
import com.uom.msc.cse.ds.kasper.service.RoutingTableService;
import com.uom.msc.cse.ds.kasper.service.SearchFileService;
import com.uom.msc.cse.ds.kasper.service.SearchResultService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.*;
import java.io.*;


@Service
@Log4j2
public class SocketServer{


    DatagramSocket datagramSocket;

    @Autowired
    RoutingTableService routingTableService;

    @Autowired
    SearchFileService searchFileService;

    @Autowired
    YAMLConfig yamlConfig;

    @Autowired
    SearchResultService searchResultService;

    @Autowired
    ResponseHandler responseHandler;

    private static int reqCount = 0;


    @Async("threadPoolExecutor")
    public void init(int port, String ip) {
        try {
            this. datagramSocket = new DatagramSocket(port,InetAddress.getByName(ip));
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            while(true){
                byte[] buffer = new byte[65536];
                DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(incoming);

                byte[] data = incoming.getData();
                String msg = new String(data, 0, incoming.getLength());

                log.info("REQ RECEIVED : {}",msg);

                log.info("CUMULATIVE REQ COUNT : "+ ++reqCount);

                String[] msgData = msg.split(" ");
                String command = msgData[1];
                boolean alreadyReplied = false;
                String reply = "";
                boolean isSuccess;
                switch (command){
                    case "JOIN":
                        isSuccess = routingTableService.addToRouteTable(msgData[2],Integer.parseInt(msgData[3]));
                        reply = UriComponentsBuilder.fromPath(yamlConfig.getJoinReply()).buildAndExpand(isSuccess?0:9999).toString();
                        break;
                    case "LEAVE":
                        isSuccess =routingTableService.removeFromRouteTable(msgData[2],Integer.parseInt(msgData[3]));
                        reply = UriComponentsBuilder.fromPath(yamlConfig.getLeaveReply()).buildAndExpand(isSuccess?0:9999).toString();
                        break;
                    case "SER": //search: "SER {ip} {port} {file name} {hops}"
                        //SearchFileService searchFileService; //= new SearchFileService();

                        reply = "SERNOTIFY";
                        reply = String.format("%04d %s", reply.length() + 5 ,reply);
                        DatagramPacket dpReply = new DatagramPacket(reply.getBytes() , reply.getBytes().length , incoming.getAddress() , incoming.getPort());
                        datagramSocket.send(dpReply);

                        boolean isNewReq =  searchFileService.isNewRequest(msgData[6]);
//                        isSuccess = searchFileService.searchFileInCurrentNode(msgData[4], tmp, port, ip, Integer.parseInt(msgData[5]));

                        if(isNewReq) {

                            String searchResultFromCurrentNode = searchFileService.searchFileInCurrentNode(msgData[4],port, ip, Integer.parseInt(msgData[5]));
                            if(searchResultFromCurrentNode != null){

                                searchFileService.sendSearchData(searchResultFromCurrentNode, msgData[2], Integer.parseInt(msgData[3]));
                                alreadyReplied = true;
                            }else{

                                searchFileService.forwardSearchRequest(msgData[2],
                                        Integer.parseInt(msgData[3]),
                                        msgData[4],
                                        Integer.parseInt(msgData[5]) - 1,
                                        msgData[6],
                                        incoming.getAddress().getHostAddress(),
                                        incoming.getPort());

                            }

                        }

                        break;

                    case "SEROK": //search-reply: "SEROK {No of Files} {ip} {port} {hops} {file names}"

                        FileSearchResponse fileSearchResponseTmp = this.responseHandler.handleSearchResponse(msg);

                        if(searchResultService.getFileSearchResponse().isEmpty()){
                            searchResultService.addToSearchResult(fileSearchResponseTmp);
                        }

                        reply = "SEROKRECEIVED";
                        break;
                }
                 if(!alreadyReplied) {
                    reply = String.format("%04d %s", reply.length() + 5, reply);
                    DatagramPacket dpReply = new DatagramPacket(reply.getBytes(), reply.getBytes().length, incoming.getAddress(), incoming.getPort());
                    datagramSocket.send(dpReply);
                }

            }
        }catch (Exception ex){

            log.error("Socket Server Exception ", ex);
        }

    }

}