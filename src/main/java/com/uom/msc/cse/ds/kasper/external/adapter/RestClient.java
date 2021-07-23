package com.uom.msc.cse.ds.kasper.external.adapter;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

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

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<byte[]> response = restTemplate.build()
//                .exchange("http://localhost:8080/downloadFile", HttpMethod.GET, entity, byte[].class);
//        Files.write(Paths.get("e:\\download-files\\demo1.pdf"), response.getBody());





        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ResponseEntity<Resource> responseEntity = restTemplate.exchange( uri, HttpMethod.GET, entity, Resource.class );

        Resource resource = responseEntity.getBody();

        return resource;



    }

}
