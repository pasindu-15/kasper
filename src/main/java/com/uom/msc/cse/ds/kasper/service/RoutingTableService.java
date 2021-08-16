package com.uom.msc.cse.ds.kasper.service;

import com.uom.msc.cse.ds.kasper.dto.Node;
import com.uom.msc.cse.ds.kasper.dto.RouteTable;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Log4j2
public class RoutingTableService {

    @Autowired
    private RouteTable routeTable;

    public boolean addToRouteTable(String ip, int port){

        log.info("BEFORE UPDATE :"+routeTable.getNeighbours().toString());
        try{
            if(!isPresent(ip,port)){
                routeTable.getNeighbours().add(new Node(ip,port));
            }

        }catch (Exception e){
            return false;
        }finally {
            log.info("AFTER UPDATE :"+routeTable.getNeighbours().toString());
        }

        log.info("NEIGHBOURS COUNT : {}",routeTable.getNeighbours().size());
        return true;
    }

    public boolean removeFromRouteTable(String ip, int port){
        log.info("BEFORE REMOVE :"+routeTable.getNeighbours().toString());

        try {
            routeTable.getNeighbours().removeIf(n-> n.getIpAddress().equals(ip) && n.getPort() == port);

        }catch (Exception e){
            return false;
        }finally {
            log.info("AFTER REMOVE :"+routeTable.getNeighbours().toString());
        }

        log.info("NEIGHBOURS COUNT : {}",routeTable.getNeighbours().size());
        return true;
    }

    private boolean isPresent(String ip, int port){

        return !routeTable.getNeighbours().parallelStream().filter(n -> n.getIpAddress().equals(ip) && n.getPort() == port).collect(Collectors.toList()).isEmpty();
    }
}
