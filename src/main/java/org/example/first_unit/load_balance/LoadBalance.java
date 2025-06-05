package org.example.first_unit.load_balance;

import org.example.first_unit.load_balance.base.ClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LoadBalance {

    private static final Logger LOG = LoggerFactory.getLogger(LoadBalance.class.getSimpleName());

    private static final int PORT = 9876;

    private static final List<String> dataServers = new ArrayList<>();
    private static final boolean useRandomBalancing = false;

    public static void main(String[] args) {

        setupDataServersAddresses();

        try (ServerSocket serverSocket = new ServerSocket(PORT, 0, InetAddress.getByName(System.getenv("DB_IP")))) {
            LOG.info("Load balance iniciado na porta " + PORT);

            LOG.info("Proxies registrados: " + dataServers);
            LOG.info(
                    "Load balance iniciado no IP " + serverSocket.getLocalPort() + "/" + serverSocket.getInetAddress());

            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();

            LOG.info(ipAddress);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    LOG.info("Nova conexão de cliente: " + clientSocket.getInetAddress().getHostAddress());

                    ClientHandler clientHandler = new ClientHandler(clientSocket, dataServers, useRandomBalancing);
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    LOG.error("Erro ao aceitar conexão", e);
                }
            }
        } catch (IOException e) {
            LOG.error("Erro ao iniciar o servidor", e);
        }
    }

    private static void setupDataServersAddresses() {
        dataServers.add("225.7.8.9:54329");
        dataServers.add("225.7.8.9:54330");
    }
}
