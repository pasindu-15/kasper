package com.uom.msc.cse.ds.kasper.application.socket;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.service.RoutingTableService;
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
    YAMLConfig yamlConfig;


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

                String reply = "";
                boolean isSuccess;
                switch (command){
                    case "JOIN":
                        isSuccess = routingTableService.addToRouteTable(msgData[1],Integer.parseInt(msgData[2]));
                        reply = UriComponentsBuilder.fromPath(yamlConfig.getJoinReply()).buildAndExpand(isSuccess?0:9999).toString();
                        break;
                    case "LEAVE":
                        isSuccess =routingTableService.removeFromRouteTable(msgData[1],Integer.parseInt(msgData[2]));
                        reply = UriComponentsBuilder.fromPath(yamlConfig.getLeaveReply()).buildAndExpand(isSuccess?0:9999).toString();
                        break;
                }

                reply = String.format("%04d %s", reply.length() + 5 ,reply);

                DatagramPacket dpReply = new DatagramPacket(reply.getBytes() , reply.getBytes().length , incoming.getAddress() , incoming.getPort());
                datagramSocket.send(dpReply);

            }
        }catch (IOException ex){
            System.err.println("Socket Server IOException " + ex);
        }

    }

}