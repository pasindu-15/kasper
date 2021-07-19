package com.uom.msc.cse.ds.kasper.application.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    @Getter
    @Setter
    private String uploadDir;

    @Getter
    @Setter
    private String downloadDir;

}
