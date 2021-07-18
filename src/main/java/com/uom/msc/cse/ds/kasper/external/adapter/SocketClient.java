package com.uom.msc.cse.ds.kasper.external.adapter;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;

@Service
public class SocketClient{


    DatagramSocket datagramSocket;



    SocketClient() throws SocketException {
        datagramSocket = new DatagramSocket();
    }




    public String sendAndReceive(String targetIp, int targetPort, String request) throws IOException {
        DatagramPacket sendingPacket = new DatagramPacket(request.getBytes(),
                request.length(), InetAddress.getByName(targetIp),targetPort);

        datagramSocket.setSoTimeout(Constants.TIMEOUT_REG);

        datagramSocket.send(sendingPacket);

        byte[] buffer = new byte[65536];

        DatagramPacket received = new DatagramPacket(buffer, buffer.length);

        datagramSocket.receive(received);

        return new String(received.getData(), 0, received.getLength());
    }

}