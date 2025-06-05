package org.example.first_unit.drones.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadBalanceConnection {

    private static final Logger LOG = LoggerFactory.getLogger(LoadBalanceConnection.class.getSimpleName());

    private String loadBalanceHost;
    private int loadBalancePort;
    private String dataServerHost;
    private String dataServerPort;

    public LoadBalanceConnection(String loadBalanceHost, int loadBalancePort) {
        this.loadBalanceHost = loadBalanceHost;
        this.loadBalancePort = loadBalancePort;
    }

    public void connectToLocationServer() {
        try (Socket socket = new Socket(loadBalanceHost, loadBalancePort)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("GET_APPLICATION_SERVER");
            String response = in.readLine();
            String[] serverInfo = response.split(":");
            this.dataServerHost = serverInfo[0];
            this.dataServerPort = serverInfo[1];
            LOG.info("Servidor de dados encontrado em " + dataServerHost + ":" + dataServerPort);
        } catch (IOException e) {
            LOG.error("Erro ao conectar ao servidor de localização", e);
            System.exit(1);
        }
    }

    public String getDataServerHost() {
        return dataServerHost;
    }

    public String getDataServerPort() {
        return dataServerPort;
    }
}
