package com.uom.msc.cse.ds.kasper.dto;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import java.net.*;
import java.util.UUID;

@Log4j2
@ToString
public class Node {

    private String userName;
    private String ipAddress;
    private int port;

    public Node(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public Node(int port){

        String uniqueID = UUID.randomUUID().toString();
        this.userName = "node_"+uniqueID;
        this.port = port;

        this.ipAddress = getIp();

        log.info("Node initiated on IP :" + ipAddress + " and Port :" + port);

    }

    public String getUserName() {
        return userName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    private  String getIp(){

        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Getting address failed");
            throw new RuntimeException("Getting address failed");
        }

    }

}
