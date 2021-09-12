package com.uom.msc.cse.ds.kasper.external.adapter;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import org.springframework.stereotype.Service;

import java.net.*;
import java.io.*;

@Service
public class SocketClient{


    private final YAMLConfig yamlConfig;
    private final DatagramSocket datagramSocket;
    private final DatagramSocket datagramSocketBssServer;

    SocketClient(YAMLConfig yamlConfig) throws SocketException {

        this.datagramSocket = new DatagramSocket();
        this.datagramSocketBssServer = new DatagramSocket();
        this.yamlConfig = yamlConfig;
    }


    public String sendAndReceive(String targetIp, int targetPort, String request) throws IOException {

//        DatagramSocket datagramSocket = new DatagramSocket();
//        DatagramSocket datagramSocket = new DatagramSocket(targetPort,InetAddress.getByName(targetIp));


        DatagramPacket sendingPacket = new DatagramPacket(request.getBytes(),
                request.length(), InetAddress.getByName(targetIp),targetPort);

        datagramSocket.setSoTimeout(yamlConfig.getTimeout());

        datagramSocket.send(sendingPacket);

        byte[] buffer = new byte[65536];

        DatagramPacket received = new DatagramPacket(buffer, buffer.length);

        datagramSocket.receive(received);

        return new String(received.getData(), 0, received.getLength());
    }




    public String sendAndReceiveBssServer(String targetIp, int targetPort, String request) throws IOException {

//        DatagramSocket datagramSocket = new DatagramSocket();
//        DatagramSocket datagramSocket = new DatagramSocket(targetPort,InetAddress.getByName(targetIp));


        DatagramPacket sendingPacket = new DatagramPacket(request.getBytes(),
                request.length(), InetAddress.getByName(targetIp),targetPort);

        datagramSocketBssServer.setSoTimeout(yamlConfig.getTimeout());

        datagramSocketBssServer.send(sendingPacket);

        byte[] buffer = new byte[65536];

        DatagramPacket received = new DatagramPacket(buffer, buffer.length);

        datagramSocketBssServer.receive(received);

        return new String(received.getData(), 0, received.getLength());
    }

}