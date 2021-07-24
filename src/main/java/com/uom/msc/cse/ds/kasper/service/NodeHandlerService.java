package com.uom.msc.cse.ds.kasper.service;

import com.uom.msc.cse.ds.kasper.application.socket.SocketServer;
import com.uom.msc.cse.ds.kasper.dto.FileSearchResponse;
import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import com.uom.msc.cse.ds.kasper.external.request.RequestHandlerInterface;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    SearchFileService searchFileService;

    @Autowired
    SearchResultService searchResultService;

    public NodeHandlerService(RouteTable routeTable, RequestHandlerInterface requestHandler, SocketServer socketServer) {
        this.routeTable =routeTable;
        this.requestHandler = requestHandler;
        this.socketServer = socketServer;

    }

    public Node getMyNode() throws Exception{
        return this.node;
    }

    /**
     *
     * @param myPort
     * @throws Exception
     */
    public void init(int myPort) throws Exception {

        this.node = new Node(myPort);
        this.socketServer.init(myPort,node.getIpAddress());
        addToOwnRouteTable();

    }

    /**
     *
     */
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

    /**
     *
     */
    public void removeFromOwnRouteTable() {

        boolean res = requestHandler.unRegister(this.node);

        if(res){
            routeTable.getNeighbours().parallelStream().forEach(neighbour -> requestHandler.leave(node,neighbour.getIpAddress(),neighbour.getPort()));
        }

    }




    /**
     *
     * @param keyword
     * @return
     */
    public List<String> searchOwn(String keyword){
        try {
            return searchFileService.searchFileInCurrentNode(keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void doSearch(String keyword, int hops){

        log.info("Input Search String {}", keyword);
        boolean isFoundInLocal = searchFilesOnLocalDrive(keyword);

        if(!isFoundInLocal){
            String uniqueID = UUID.randomUUID().toString();
            String uniqIdForSearch = "search"+uniqueID;
            searchResultService.cleanSearchResponse();
            searchFileService.isNewRequest(uniqIdForSearch); //If Own request Received Ignore
            for (Node n: routeTable.getNeighbours()) {
                requestHandler.search(this.node,keyword,hops,n.getIpAddress(),n.getPort(),uniqIdForSearch);

//            if(fr != null && fr.getFiles() != null){
//                break;
//            }
            }
        }

//        String uniqueID = UUID.randomUUID().toString();
//        String uniqIdForSearch = "search"+uniqueID;
//        searchResultService.cleanSearchResponse();
//        searchFileService.isNewRequest(uniqIdForSearch); //If Own request Received Ignore
//        for (Node n: routeTable.getNeighbours()) {
//            requestHandler.search(this.node,keyword,hops,n.getIpAddress(),n.getPort(),uniqIdForSearch);
//
////            if(fr != null && fr.getFiles() != null){
////                break;
////            }
//        }
    }


    public void download(String ip, String port, String fileName){
        requestHandler.fileDownload(fileName,ip,Integer.parseInt(port));
    }

    public String searchWithStringReply(String requestIp, int requestPort, String keyword,int hops, String targetIp, int targetPort) {
        return requestHandler.searchWithStringReply( requestIp,  requestPort,  keyword, hops,  targetIp,  targetPort);
    }

    private boolean searchFilesOnLocalDrive(String keyword){
        List<String> fileNames = null;
        try {
            fileNames = searchFileService.searchFileInCurrentNode(keyword);

            if (fileNames != null && !fileNames.isEmpty()) {
                FileSearchResponse fr = new FileSearchResponse();
                fr.setIp(getMyNode().getIpAddress());
                fr.setPort(getMyNode().getPort());
                fr.setFiles(fileNames);
                List<FileSearchResponse> searchResponses = new ArrayList<>();
                searchResponses.add(fr);
                searchResultService.setFileSearchResponse(searchResponses);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
