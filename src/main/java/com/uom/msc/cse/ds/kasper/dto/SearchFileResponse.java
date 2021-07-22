package com.uom.msc.cse.ds.kasper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SearchFileResponse {

    private String fileName;

    private String ipAddress;

    private String portId;


}

