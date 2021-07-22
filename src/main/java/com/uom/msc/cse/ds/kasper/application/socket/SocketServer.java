package com.uom.msc.cse.ds.kasper.application.socket;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.service.RoutingTableService;
import com.uom.msc.cse.ds.kasper.service.SearchFileService;
import com.uom.msc.cse.ds.kasper.service.SearchResult;
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
    SearchResult searchResult;

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
                        allreadyRepiled = true;
                        isSuccess = searchFileService.searchFileInCurrentNode(msgData[4], tmp, port, ip, Integer.parseInt(msgData[5]));
                        if(isSuccess) {
                            reply = tmp[0];
                            reply = String.format("%04d %s", reply.length() + 5 ,reply);
                            searchFileService.sendSearchData(reply, msgData[2], Integer.parseInt(msgData[3]));
                        } else if(searchFileService.isNewRequest(uniqIdForSearch)){
                            reply = searchFileService.ReqursiveSearchCall(msgData[2], Integer.parseInt(msgData[3]), msgData[4], Integer.parseInt(msgData[5])-1, msgData[6]);
                        } else{
                            reply = "Stopped";
                        }
                    case "SEROK" : //search-reply: "SEROK {No of Files} {ip} {port} {hops} {file names}"
                        searchResult.addToSearchResult(msg);
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