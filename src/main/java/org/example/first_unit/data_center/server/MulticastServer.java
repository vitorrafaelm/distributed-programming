package org.example.first_unit.data_center.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.first_unit.data_center.database.entities.Weather;
import org.example.first_unit.data_center.database.services.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MulticastServer implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MulticastServer.class.getSimpleName());

    private String multicastServerPort;
    private String getMulticastServerHost;

    public MulticastServer(String multicastServerPort, String getMulticastServerHost) {
        this.multicastServerPort = multicastServerPort;
        this.getMulticastServerHost = getMulticastServerHost;
    }

    @Override
    public void run() {
        boolean flag = true;

        try (MulticastSocket ms = new MulticastSocket(Integer.parseInt(multicastServerPort))) {
            InetAddress multicastIP = InetAddress.getByName(getMulticastServerHost);

            InetSocketAddress group = new InetSocketAddress(multicastIP, Integer.parseInt(multicastServerPort));
            NetworkInterface networkInterface = NetworkInterface.getByName("en0");
            ms.joinGroup(group, networkInterface);

            while (flag) {
                byte[] messageReceived = CaptureMessage(ms);
                LOG.info("Message received: " + new String(messageReceived).trim());

                String receivedData = new String(messageReceived).trim();
                JsonObject message = JsonParser.parseString(receivedData).getAsJsonObject();

                String data = message.get("data").getAsString();
                String operation = message.get("operation").getAsString();

                if ("process_data".equals(operation)) {
                    initiateDataProcessing(data);
                } else {
                    final var host = message.get("host").getAsString();
                    final var port = message.get("port").getAsInt();
                    sendDataToClient(host, port);
                }
            }

            ms.leaveGroup(group, networkInterface);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting multicast server: " + e.getMessage());
        }
    }

    private byte[] CaptureMessage(MulticastSocket ms) throws Exception {
        byte bufferReceive[] = new byte[50000];
        DatagramPacket pacoteRecepcao = new DatagramPacket(
                bufferReceive,
                bufferReceive.length);

        ms.receive(pacoteRecepcao);
        return bufferReceive;
    }

    private void initiateDataProcessing(String data) {
        WeatherService weatherService = new WeatherService();

        List<String> originalData = Arrays.asList(data.replace("[", "").replace("]", "").split(", "));

        originalData.forEach(item -> {
            LOG.info("Processing item: " + item);

            Weather weather = new Weather();
            weather.setWeatherData(item.replaceAll("[,#;\\-]", "//"));

            try {
                weatherService.add(weather);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void sendDataToClient(final String host, final Integer port) {
        // Obter dados
        final var weatherService = new WeatherService();
        final var weatherDataList = weatherService.list().stream()
                .map(Weather::getWeatherData)
                .toList();

        // Criar JSON
        final var jsonObject = new JsonObject();
        jsonObject.addProperty("data", weatherDataList.toString());
        final var bufferSend = jsonObject.toString().getBytes();

        // Enviar dados para o cliente
        try (final var client = new Socket(host, port)) {
            final var writer = client.getOutputStream();

            writer.write(bufferSend);
            writer.flush();
        } catch (Exception e) {
            System.err.println("Error sending data to the client: " + e.getMessage());
        }
    }

}
