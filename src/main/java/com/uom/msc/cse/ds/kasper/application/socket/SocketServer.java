package com.uom.msc.cse.ds.kasper.application.socket;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.application.controller.UIController;
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

    private static int reqCount = 0;



    @Async("threadPoolExecutor")
    public void init(int port, String ip) {
        try {
            this. datagramSocket = new DatagramSocket(port,InetAddress.getByName(ip));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
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

                System.err.println("CUMULATIVE REQ COUNT : "+ ++reqCount);

                String[] msgData = msg.split(" ");
                String command = msgData[1];
                boolean allreadyRepiled = false;
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
                        String[] tmp = new String[1]; tmp[0] = "";
                        String uniqIdForSearch = msgData[6];
                        reply = "SERNOTIFY";
                        reply = String.format("%04d %s", reply.length() + 5 ,reply);
                        DatagramPacket dpReply = new DatagramPacket(reply.getBytes() , reply.getBytes().length , incoming.getAddress() , incoming.getPort());
                        datagramSocket.send(dpReply);

                        boolean isNewReq =  searchFileService.isNewRequest(uniqIdForSearch);
                        isSuccess = searchFileService.searchFileInCurrentNode(msgData[4], tmp, port, ip, Integer.parseInt(msgData[5]));


                        if(isNewReq && isSuccess) {
                            reply = tmp[0];
                            reply = String.format("%04d %s", reply.length() + 5 ,reply);
                            searchFileService.sendSearchData(reply, msgData[2], Integer.parseInt(msgData[3]));
                            allreadyRepiled = true;
                        } else if(isNewReq){
                            String requestIP = msgData[2];
                            int requestPort = Integer.parseInt(msgData[3]);
                            String fileName = msgData[4];
                            int hops = Integer.parseInt(msgData[5]) - 1;
                            String searchUniqId = msgData[6];
                            String incomingIp;
                            int incomingPort;
                            try {
                                incomingIp =  incoming.getAddress().getLocalHost().getHostAddress();
                                incomingPort = incoming.getPort();
                                reply = searchFileService.recursiveSearchCall(requestIP, requestPort, fileName, hops, searchUniqId, incomingIp, incomingPort);
                            } catch (UnknownHostException e) {
                                log.error("Getting address failed");
                                throw new RuntimeException("Getting address failed");
                            }
                        } else{
                            reply = "Stopped Recursive";
                        }
                        break;
                    case "SEROK": //search-reply: "SEROK {No of Files} {ip} {port} {hops} {file names}"
                        searchResultService.addToSearchResult(msg);

                        reply = "SEROKRECEIVED";
                        break;
                }
                 if(!allreadyRepiled) {
                    reply = String.format("%04d %s", reply.length() + 5, reply);
                    DatagramPacket dpReply = new DatagramPacket(reply.getBytes(), reply.getBytes().length, incoming.getAddress(), incoming.getPort());
                    datagramSocket.send(dpReply);
                }

            }
        }catch (IOException ex){
            System.err.println("Socket Server IOException " + ex);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}