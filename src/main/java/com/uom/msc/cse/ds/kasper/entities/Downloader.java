package com.uom.msc.cse.ds.kasper.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Downloader {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String ipAddress;
    private String port;
    private String fileName;

    public Downloader(){}
    public Downloader(String ipAddress, String port, String fileName){
        this.ipAddress = ipAddress;
        this.port = port;
        this.fileName = fileName;
    }

    public long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
