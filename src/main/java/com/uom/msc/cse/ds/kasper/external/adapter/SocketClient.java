package com.uom.msc.cse.ds.kasper.external.adapter;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import org.springframework.stereotype.Service;

import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;

@Service
public class SocketClient{

    DatagramSocket datagramSocket;

    YAMLConfig yamlConfig;

    SocketClient(YAMLConfig yamlConfig) throws SocketException {
        datagramSocket = new DatagramSocket();
        this.yamlConfig = yamlConfig;
    }


    public String sendAndReceive(String targetIp, int targetPort, String request) throws IOException {
        DatagramPacket sendingPacket = new DatagramPacket(request.getBytes(),
                request.length(), InetAddress.getByName(targetIp),targetPort);

        datagramSocket.setSoTimeout(yamlConfig.getTimeout());

        datagramSocket.send(sendingPacket);

        byte[] buffer = new byte[65536];

        DatagramPacket received = new DatagramPacket(buffer, buffer.length);

        datagramSocket.receive(received);

        return new String(received.getData(), 0, received.getLength());
    }

}