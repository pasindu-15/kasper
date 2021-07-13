
package com.uom.msc.cse.ds.kasper.application.controller;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class BaseController {
    @Autowired
    private YAMLConfig yamlConfig;

    public void setLogIdentifier(HttpServletRequest request) {

        String logIdentifier = request.getHeader(yamlConfig.getLogIdentifierKey());
        if (logIdentifier != null) {
            MDC.put(yamlConfig.getLogIdentifierKey(), logIdentifier);
        } else {
            MDC.put(yamlConfig.getLogIdentifierKey(), UUID.randomUUID().toString());
        }
    }
}
