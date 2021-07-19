
package com.uom.msc.cse.ds.kasper.application.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "msce")
@Getter
@Setter
public class YAMLConfig {
    @Value("${log.identifierKey}")
    private String logIdentifierKey;


    @Value("${bootstrap.bip}")
    private String BSIPAddress;

    @Value("${bootstrap.bport}")
    private int BSPort;

    @Value("${node.req-url}")
    private String url;

    @Value("${search.hops}")
    private int hops;

    @Value("${msg.register}")
    private String regMsg;

    @Value("${msg.un-register}")
    private String unRegMsg;

    @Value("${msg.join}")
    private String joinMsg;

    @Value("${msg.join-reply}")
    private String joinReply;

    @Value("${msg.leave}")
    private String leaveMsg;

    @Value("${msg.leave-reply}")
    private String leaveReply;

    @Value("${msg.search}")
    private String searchMsg;

    @Value("${msg.search-reply}")
    private String searchReply;




}
