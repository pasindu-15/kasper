package com.uom.msc.cse.ds.kasper.external.request;

import com.uom.msc.cse.ds.kasper.dto.FileSearchResponse;
import com.uom.msc.cse.ds.kasper.dto.Node;

import java.net.InetSocketAddress;
import java.util.List;

public interface RequestHandlerInterface {
    public boolean unRegister(Node myNode);
    public List<InetSocketAddress> register(Node myNode);
    public boolean join(Node myNode,String targetIp, int targetPort);
    public boolean leave(Node myNode,String targetIp, int targetPort);
    public void fileDownload(String fileName, String targetIp, int targetPort);
    public FileSearchResponse search(Node myNode, String keyword, int hops, String targetIp, int targetPort, String uniqIdForSearch);
//    public String searchWithStringReply(String requestIp, int requestPort, String keyword,int hops, String targetIp, int targetPort);
//    public boolean sendSearchData(String msg, String targetIp, int targetPort);


}
