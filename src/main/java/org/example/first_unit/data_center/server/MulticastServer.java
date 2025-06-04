package org.example.first_unit.data_center.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.first_unit.data_center.database.entities.Weather;
import org.example.first_unit.data_center.database.services.WeatherService;

import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MulticastServer implements Runnable {
    private String multicastServerPort;
    private String getMulticastServerHost;

    public MulticastServer(String multicastServerPort, String getMulticastServerHost) {
        this.multicastServerPort = multicastServerPort;
        this.getMulticastServerHost = getMulticastServerHost;
    }

    @Override
    public void run() {
        boolean flag = true;

        try(MulticastSocket ms = new MulticastSocket(Integer.parseInt(multicastServerPort))) {
            InetAddress multicastIP = InetAddress.getByName(getMulticastServerHost);

            InetSocketAddress group = new InetSocketAddress(multicastIP, Integer.parseInt(multicastServerPort));
            NetworkInterface networkInterface = NetworkInterface.getByName("en0");
            ms.joinGroup(group, networkInterface);

            while(flag) {
                byte[] messageReceived = CaptureMessage(ms);
                System.out.println("Message received: " + new String(messageReceived).trim());

                String receivedData = new String(messageReceived).trim();
                JsonObject message = JsonParser.parseString(receivedData).getAsJsonObject();

                String data = message.get("data").getAsString();
                String operation = message.get("operation").getAsString();

                if ("process_data".equals(operation)) {
                    initiateDataProcessing(data);
                } else {
                    String port = message.get("port").getAsString();
                    sendDataToClient(port);
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
                bufferReceive.length
        );

        ms.receive(pacoteRecepcao);
        return bufferReceive;
    }

    private void initiateDataProcessing(String data) {
        WeatherService weatherService = new WeatherService();

        List<String> originalData = Arrays.asList(data.replace("[", "").replace("]", "").split(", "));

        originalData.forEach(item -> {
            System.out.println("Processing item: " + item);

            Weather weather = new Weather();
            weather.setWeatherData(item.replaceAll("[,#;\\-]", "//"));

            try {
                weatherService.add(weather);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void sendDataToClient(String port) {
        WeatherService weatherService = new WeatherService();

        List<String> weatherDataList = new ArrayList<>();

        JsonObject jsonObject = new JsonObject();

        weatherService.list().forEach(weather -> {
            weatherDataList.add(weather.getWeatherData());
        });

        jsonObject.addProperty("data", weatherDataList.toString());
        byte bufferSend[] = jsonObject.toString().getBytes();

        try (DatagramSocket ds = new DatagramSocket()) {
            DatagramPacket packageToSend = new DatagramPacket(
                    bufferSend,
                    bufferSend.length,
                    InetAddress.getByName(getMulticastServerHost),
                    Integer.parseInt(port)
            );

            ds.send(packageToSend);
        } catch (Exception e) {
            System.err.println("Error sending data to the client: " + e.getMessage());
        }
    }
}
