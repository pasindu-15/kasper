package com.uom.msc.cse.ds.kasper.application.service;

import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import com.uom.msc.cse.ds.kasper.external.rest.RequestHandlerInterface;
import com.uom.msc.cse.ds.kasper.external.udp.BSClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class NodeHandler {

    Node node;
    BSClient bsClient;
    RequestHandlerInterface requestHandler;
    RouteTable routeTable;

    public NodeHandler(BSClient bsClient, RouteTable routeTable, RequestHandlerInterface requestHandler) throws Exception {
        this.bsClient = bsClient;
        this.routeTable =routeTable;
        this.requestHandler = requestHandler;

    }

    public void init(int myPort) throws Exception {

        this.node = new Node(myPort);

        List<Node> neighbours = new ArrayList<>();
        List<InetSocketAddress> targets = null;
        try {
            targets = bsClient.register(node.getUserName(), node.getIpAddress(), node.getPort());
        } catch (IOException e) {
            log.error("Node Registering failed");
            e.printStackTrace();
        }

        if(targets != null) {
            for (InetSocketAddress target: targets) {
                String ip = target.getAddress().toString().substring(1);
                int port = target.getPort();
                Node neighbourNode = new Node(ip,port);
                neighbours.add(neighbourNode);

                requestHandler.join(node,neighbourNode);

            }
            routeTable.setNeighbours(neighbours);
            System.out.println(routeTable.getNeighbours().toString());
        }
    }


    //
    public void unRegAndLeave() {
        try{
            this.bsClient.unRegister(this.node.getUserName(), this.node.getIpAddress(), this.node.getPort());

            routeTable.getNeighbours().parallelStream().forEach(neighbour -> requestHandler.leave(node,neighbour));

        } catch (IOException e) {
            log.error("Un-Registering Node failed");
            e.printStackTrace();
        }
    }

    public void testSearch() {

    }
//
//    public int doSearch(String keyword){
//        return this.searchManager.doSearch(keyword);
//    }
//
//    public List<String> doUISearch(String keyword) {
//        return this.searchManager.doUISearch(keyword);
//    }
//
//    public void getFile(int fileOption) {
//        try {
//            SearchResult fileDetail = this.searchManager.getFileDetails(fileOption);
//            System.out.println("The file you requested is " + fileDetail.getFileName());
//            FTPClient ftpClient = new FTPClient(fileDetail.getAddress(), fileDetail.getTcpPort(),
//                    fileDetail.getFileName());
//
//            System.out.println("Waiting for file download...");
//            Thread.sleep(Constants.FILE_DOWNLOAD_TIMEOUT);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void getFile(int fileOption, TextArea textArea) {
//        try {
//            SearchResult fileDetail = this.searchManager.getFileDetails(fileOption);
//            System.out.println("The file you requested is " + fileDetail.getFileName());
//            FTPClient ftpClient = new FTPClient(fileDetail.getAddress(), fileDetail.getTcpPort(),
//                    fileDetail.getFileName(),textArea);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//


//
//    public void printRoutingTable(){
//        this.messageBroker.getRoutingTable().print();
//    }
//
//    public String getRoutingTable() {
//       return this.messageBroker.getRoutingTable().toString();
//    }
//
//    public String getFileNames() {
//        return this.messageBroker.getFiles();
//    }
}
