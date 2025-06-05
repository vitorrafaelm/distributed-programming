package org.example.first_unit.data_center.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.first_unit.data_center.database.entities.Weather;
import org.example.first_unit.data_center.database.services.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MulticastServer implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MulticastServer.class.getSimpleName());

    private final WeatherService weatherService = new WeatherService();

    private final String multicastServerPort;
    private final String getMulticastServerHost;

    private boolean flag = false;

    public MulticastServer(final String multicastServerPort, final String getMulticastServerHost) {
        this.multicastServerPort = multicastServerPort;
        this.getMulticastServerHost = getMulticastServerHost;
    }

    @Override
    public void run() {
        flag = true;

        try (final var ms = new MulticastSocket(Integer.parseInt(multicastServerPort))) {
            final var multicastIP = InetAddress.getByName(getMulticastServerHost);

            final var group = new InetSocketAddress(multicastIP, Integer.parseInt(multicastServerPort));
            final var networkInterface = NetworkInterface.getByName("en0");
            ms.joinGroup(group, networkInterface);

            ms.setTimeToLive(128);

            while (flag) {
                final var messageReceived = captureMessage(ms);
                final var receivedData = new String(messageReceived).trim();

                LOG.info("Message received: {}", receivedData);

                final var message = JsonParser.parseString(receivedData).getAsJsonObject();

                final var data = message.get("data").getAsString();
                final var operation = message.get("operation").getAsString();

                if ("process_data".equals(operation)) {
                    initiateDataProcessing(data);
                } else {
                    final var host = message.get("host").getAsString();
                    final var port = message.get("port").getAsInt();
                    sendDataToClient(host, port);
                }
            }

            ms.leaveGroup(group, networkInterface);
        } catch (final Exception e) {
            LOG.error("Error starting multicast server", e);
        }
    }

    private byte[] captureMessage(final MulticastSocket ms) throws IOException {
        final var bufferReceive = new byte[50000];
        final var pacoteRecepcao = new DatagramPacket(
                bufferReceive,
                bufferReceive.length);

        ms.receive(pacoteRecepcao);
        return bufferReceive;
    }

    private void initiateDataProcessing(final String data) {

        final List<String> originalData = Arrays.asList(data.replace("[", "").replace("]", "").split(", "));

        originalData.forEach(item -> {
            LOG.info("Processing item: {}", item);

            final var weather = new Weather();
            weather.setWeatherData(item.replaceAll("[,#;\\-]", "//"));

            try {
                weatherService.add(weather);
            } catch (final SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void sendDataToClient(final String host, final Integer port) {
        // Obter dados
        final var weatherDataList = weatherService.list().stream()
                .map(Weather::getWeatherData)
                .toList();

        // Criar JSON
        final var jsonObject = new JsonObject();
        jsonObject.addProperty("data", weatherDataList.toString());
        final var bufferSend = jsonObject.toString().getBytes();

        LOG.info("Host: {}, Port: {}", host, port);

        // Enviar dados para o cliente
        try (final var client = new Socket(System.getenv("CLIENT_IP"), port)) {
            final var writer = client.getOutputStream();

            writer.write(bufferSend);
            writer.flush();
        } catch (final Exception e) {
            LOG.error("Error sending data to the client", e);
        }
    }

    public void stop() {
        flag = false;
        LOG.info("Multicast server stopped.");
    }

}
