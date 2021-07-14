
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


}
