package com.uom.msc.cse.ds.kasper.dto;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.*;
import java.util.UUID;

@Log4j2
@ToString
public class Node {

//
//    @Autowired
//    private BSClient bsClient;

    private String userName;
    private String ipAddress;


    private int port;
//    private MessageBroker messageBroker;
//    private SearchManager searchManager;
//    private FTPServer ftpServer;




    public Node(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public Node(int port) throws Exception {
//        this.bsClient = bsClient;

        String uniqueID = UUID.randomUUID().toString();
        this.userName = "node_"+uniqueID;
        this.port = port;

//        try{

//        this.port = assignPort();
        this.ipAddress = getIp();

//        SocketServer server = new SocketServer(port);
//        server.start();

//        } catch (Exception e){
//            throw new RuntimeException("Could not find host address");
//        }



//        FileManager fileManager = FileManager.getInstance(userName);
//        this.ftpServer = new FTPServer(this.port + Constants.FTP_PORT_OFFSET, userName);
//        t.start();
//
//        this.bsClient = new BSClient();
//        this.messageBroker = new MessageBroker(ipAddress, port);
//
//        this.searchManager = new SearchManager(this.messageBroker);
//
//        messageBroker.start();

        log.info("Node initiated on IP :" + ipAddress + " and Port :" + port);

        System.out.println("DOne");


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
//            final DatagramSocket socket = new DatagramSocket();
//            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
////            socket.connect(InetAddress.getLocalHost(), 10002);
//            String address =  socket.getLocalAddress().getHostAddress();
            return InetAddress.getLocalHost().getHostAddress();
//            return address;
        } catch (UnknownHostException e) {
            log.error("Getting address failed");
            throw new RuntimeException("Getting address failed");
        }

    }
    private int assignPort() {
        try {
            ServerSocket socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            int port = socket.getLocalPort();
            socket.close();
            return port;
        } catch (IOException e) {
            log.error("Getting port failed");
            throw new RuntimeException("Getting port failed");
        }
    }




}
