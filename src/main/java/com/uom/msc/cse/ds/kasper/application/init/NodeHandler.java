package com.uom.msc.cse.ds.kasper.application.init;

import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.undo.AbstractUndoableEdit;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class NodeHandler {

    Node node;
    BSClient bsClient;

//    @Autowired
    RouteTable routeTable;

    public NodeHandler(BSClient bsClient,RouteTable routeTable) throws Exception {
        this.bsClient = bsClient;
        this.routeTable =routeTable;
        this.node = new Node();
        init();
    }

    private void init() {
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
                System.out.println("REP: "+target);
                neighbours.add(new Node(target.getAddress().toString(),target.getPort()));
//                messageBroker.sendPing(target.getAddress().toString().substring(1), target.getPort());
            }
            routeTable.setNeighbours(neighbours);
            System.out.println(routeTable.getNeighbours().toString());
        }
    }


    //
//    public void unRegister() {
//        try{
//            this.bsClient.unRegister(this.userName, this.ipAddress, this.port);
//            this.messageBroker.sendLeave();
//
//        } catch (IOException e) {
//            LOG.severe("Un-Registering Gnode failed");
//            e.printStackTrace();
//        }
//    }
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
