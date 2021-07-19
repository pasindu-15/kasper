package com.uom.msc.cse.ds.kasper.external.adapter;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.application.init.FileStorageInitializer;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
@Log4j2
public class RestClient {
    @Autowired
    YAMLConfig yamlConfig;

    @Autowired
    RestTemplate restTemplate;



    public Resource send(String url, String fileName){



//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application","text"));
//
//
//        log.info("MSG SENT: {}",msg);
//
//        String url = UriComponentsBuilder.fromHttpUrl(yamlConfig.getUrl()).buildAndExpand(ip,port).toString();
//
//
//        HttpEntity<?> entity = new HttpEntity<>(msg,headers);
//
//        String response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
//
//        return response;


        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ResponseEntity<Resource> responseEntity = restTemplate.exchange( uri, HttpMethod.GET, null, Resource.class );

        Resource resource = responseEntity.getBody();

        return resource;







    }
}
