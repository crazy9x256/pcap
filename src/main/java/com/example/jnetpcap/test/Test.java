package com.example.jnetpcap.test;


import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Test {
    public static void main(String[] args) {
        try {

            String targetIpAddress = "192.168.1.7"; // Replace with the desired IP address
            InetAddress inetAddress = null; // Change "eth0" to your desired network interface

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address.getHostAddress().equals(targetIpAddress)) {
                        inetAddress = address;
                        System.out.println("Interface Name: " + networkInterface.getName());
                    }
                }
            }

            PcapNetworkInterface networkInterface = Pcaps.getDevByAddress(inetAddress);
            int snapshotLength = 65536; // Maximum number of bytes to capture in a single packet
            int timeoutMillis = 1000; // Timeout value in milliseconds

            PcapHandle handle = networkInterface.openLive(snapshotLength, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, timeoutMillis);
            while (true) {
                try {
                    Packet packet = handle.getNextPacketEx();
                    if (packet != null) {
                        // Process the captured packet
                        System.out.println(packet);
                    }
                } catch (PcapNativeException | NotOpenException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception exception) {
            System.out.print(exception);
        }
    }
}
