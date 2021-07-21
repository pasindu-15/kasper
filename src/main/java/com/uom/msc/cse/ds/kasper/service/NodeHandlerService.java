package com.uom.msc.cse.ds.kasper.service;

import com.uom.msc.cse.ds.kasper.application.socket.SocketServer;
import com.uom.msc.cse.ds.kasper.dto.FileSearchResponse;
import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import com.uom.msc.cse.ds.kasper.external.request.RequestHandlerInterface;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class NodeHandlerService {

    Node node;
    RequestHandlerInterface requestHandler;
    RouteTable routeTable;

    SocketServer socketServer;

    public NodeHandlerService(RouteTable routeTable, RequestHandlerInterface requestHandler, SocketServer socketServer) {
        this.routeTable =routeTable;
        this.requestHandler = requestHandler;
        this.socketServer = socketServer;

    }

    public Node getMyNode() throws Exception{
        return this.node;
    }

    public void init(int myPort) throws Exception {

        this.node = new Node(myPort);
        this.socketServer.init(myPort,node.getIpAddress());
        addToOwnRouteTable();

    }

    private void addToOwnRouteTable(){
        List<Node> neighbours = new ArrayList<>();
        List<InetSocketAddress> targets = targets = requestHandler.register(this.node);


        if(targets != null) {
            for (InetSocketAddress target: targets) {
                String ip = target.getAddress().toString().substring(1);
                int port = target.getPort();

                boolean isJoined = requestHandler.join(node,ip,port);
                if(isJoined) neighbours.add(new Node(ip,port));

            }
            routeTable.setNeighbours(neighbours);
            System.out.println(routeTable.getNeighbours().toString());
        }
    }

    public void removeFromOwnRouteTable() {

        boolean res = requestHandler.unRegister(this.node);

        if(res){
            routeTable.getNeighbours().parallelStream().forEach(neighbour -> requestHandler.leave(node,neighbour.getIpAddress(),neighbour.getPort()));
        }

    }

    public FileSearchResponse doSearch(String keyword, int hops){

        log.info("Input Search String {}", keyword);
        String uniqueID = UUID.randomUUID().toString();
        String uniqIdForSearch = "search"+uniqueID;

        for (Node n: routeTable.getNeighbours()) {
            FileSearchResponse fr  = requestHandler.search(this.node,keyword,hops,n.getIpAddress(),n.getPort());
            if(fr.getFiles() != null){
                break;
            }
        }
        return null;
    }


    public void download(String ip, String port, String fileName){
        requestHandler.fileDownload(fileName,ip,Integer.parseInt(port));
    }

    public String searchWithStringReply(String requestIp, int requestPort, String keyword,int hops, String targetIp, int targetPort) {
        return requestHandler.searchWithStringReply( requestIp,  requestPort,  keyword, hops,  targetIp,  targetPort);
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
