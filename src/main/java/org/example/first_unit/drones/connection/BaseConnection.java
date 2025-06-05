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

    private final String connectionHostGroup;
    private final String connectionPort;
    private final List<String> batch;

    public BaseConnection(final String connectionHostGroup, final String connectionPort, final List<String> batch) {
        this.connectionHostGroup = connectionHostGroup;
        this.connectionPort = connectionPort;

        this.batch = batch;
    }

    @Override
    public void run() {
        LOG.info("Connecting to {} on port {}", connectionHostGroup, connectionPort);

        try (final var ds = new DatagramSocket()) {
            final var jsonObject = new JsonObject();
            jsonObject.addProperty("operation", "process_data");
            jsonObject.addProperty("data", batch.toString());

            final var bufferSend = jsonObject.toString().getBytes();
            LOG.info("{} bytes to send", bufferSend.length);

            final var packageToSend = new DatagramPacket(
                    bufferSend,
                    bufferSend.length,
                    InetAddress.getByName(connectionHostGroup),
                    Integer.parseInt(connectionPort));

            ds.send(packageToSend);
        } catch (final IOException e) {
            LOG.error("Error sending data to the server", e);
        }

    }
}
