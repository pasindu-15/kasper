package com.uom.msc.cse.ds.kasper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class FileSearchResponse {

    private String ip;
    private int port;
    private int hops;
    List<String> files;
}
