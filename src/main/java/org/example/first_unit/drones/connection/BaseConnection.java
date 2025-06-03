package org.example.first_unit.drones.connection;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class BaseConnection implements Runnable {
    private String connection_host_group;
    private String connection_port;
    private List<String> batch;

    public BaseConnection(String connection_host_group, String connectionPort, List<String> batch) {
        this.connection_host_group = connection_host_group;
        this.connection_port = connectionPort;

        this.batch = batch;
    }

    @Override
    public void run() {
        System.out.println("Connecting to " + connection_host_group + " on port " + connection_port);

        try (DatagramSocket ds = new DatagramSocket()) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("operation", "process_data");
            jsonObject.addProperty("data", batch.toString());

            byte bufferSend[] = jsonObject.toString().getBytes();

            System.out.println(bufferSend.length + " bytes to send");

            DatagramPacket packageToSend = new DatagramPacket(
                    bufferSend,
                    bufferSend.length,
                    InetAddress.getByName(connection_host_group),
                    Integer.parseInt(connection_port)
            );

            ds.send(packageToSend);
            ds.close();
        } catch (IOException e) {
            System.err.println("Error sending data to the server: " + e.getMessage());
        }

    }
}
