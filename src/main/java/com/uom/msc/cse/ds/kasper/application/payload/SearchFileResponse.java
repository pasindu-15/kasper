package com.uom.msc.cse.ds.kasper.application.payload;

import lombok.Getter;
import lombok.Setter;

public class SearchFileResponse {

    @Setter @Getter
    private String fileName;

    @Setter @Getter
    private String ipAddress;

    @Setter @Getter
    private String portId;

    public SearchFileResponse(String fileName, String ipAddress, String portId) {
        this.fileName = fileName;
        this.ipAddress = ipAddress;
        this.portId = portId;
    }
}

