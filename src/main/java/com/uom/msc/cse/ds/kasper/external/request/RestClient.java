package com.uom.msc.cse.ds.kasper.external.request;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.swing.*;
import java.util.Arrays;

@Service
@Log4j2
public class RestClient {
    @Autowired
    YAMLConfig yamlConfig;

    public String send(String ip, String port, String msg){
        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

//        HttpHeaders headers = new HttpHeaders();
////        headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
//        headers.setContentType(MediaType.TEXT_PLAIN);
//        headers.set("UUID", MDC.get("UUID"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","text"));


        log.info("MSG SENT: {}",msg);

        String url = UriComponentsBuilder.fromHttpUrl(yamlConfig.getUrl()).buildAndExpand(ip,port).toString();
//
//        System.out.println(url);
//        url = "http://192.168.8.241:53462/kasper/v1/node";
//        System.out.println(url);

        HttpEntity<?> entity = new HttpEntity<>(msg,headers);

        String response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();

        return response;

    }
}
