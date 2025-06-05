package org.example.first_unit.drones.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

public class BaseConnection implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(BaseConnection.class.getSimpleName());

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
        LOG.info("Connecting to " + connection_host_group + " on port " + connection_port);

        try (DatagramSocket ds = new DatagramSocket()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("operation", "process_data");
            jsonObject.addProperty("data", batch.toString());

            byte bufferSend[] = jsonObject.toString().getBytes();

            LOG.info(bufferSend.length + " bytes to send");

            DatagramPacket packageToSend = new DatagramPacket(
                    bufferSend,
                    bufferSend.length,
                    InetAddress.getByName(connection_host_group),
                    Integer.parseInt(connection_port));

            ds.send(packageToSend);
            ds.close();
        } catch (IOException e) {
            System.err.println("Error sending data to the server: " + e.getMessage());
        }

    }
}
