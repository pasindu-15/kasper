package com.uom.msc.cse.ds.kasper.application.service;

import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RoutingTableService {

    @Autowired
    RouteTable routeTable;

    public void addToRouteTable(String ip, int port){

        System.out.println("BEFORE UPDATE :"+routeTable.getNeighbours().toString());

        if(!isPresent(ip,port)){
            routeTable.getNeighbours().add(new Node(ip,port));
        }

        System.out.println("AFTER UPDATE :"+routeTable.getNeighbours().toString());
    }

    public void removeFromRouteTable(String ip, int port){
        System.out.println("BEFORE REMOVE :"+routeTable.getNeighbours().toString());

        routeTable.getNeighbours().removeIf(n-> n.getIpAddress().equals(ip) && n.getPort() == port);
//        routeTable.getNeighbours().parallelStream().filter(n -> !n.getIpAddress().equals(ip) || n.getPort() != port).collect(Collectors.toList());

        System.out.println("AFTER REMOVE :"+routeTable.getNeighbours().toString());
    }

    private boolean isPresent(String ip, int port){

        return !routeTable.getNeighbours().parallelStream().filter(n -> n.getIpAddress().equals(ip) && n.getPort() == port).collect(Collectors.toList()).isEmpty();
    }
}
