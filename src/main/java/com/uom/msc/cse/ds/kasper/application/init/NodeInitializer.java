package com.uom.msc.cse.ds.kasper.application.init;


import com.uom.msc.cse.ds.kasper.service.NodeHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;


@Configuration
public class NodeInitializer {


    @Autowired
    NodeHandlerService nodeHandlerService;

    @EventListener(WebServerInitializedEvent.class)
    public void onWebServerReady(WebServerInitializedEvent event) throws Exception {

        WebServerApplicationContext applicationContext = event.getApplicationContext();
        int port = applicationContext.getWebServer().getPort();

        nodeHandlerService.init(port);
    }

}
