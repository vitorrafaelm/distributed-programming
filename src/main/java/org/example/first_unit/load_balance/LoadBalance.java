package org.example.first_unit.load_balance;

import org.example.first_unit.load_balance.base.ClientHandler;
import org.example.first_unit.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class LoadBalance {

    private static final Logger LOG = LoggerFactory.getLogger(LoadBalance.class.getSimpleName());

    private static final int PORT = 9876;
    private static final LoadBalanceAlgorithm DEFAULT_ALGORITHM = LoadBalanceAlgorithm.ROUND_ROBIN;

    private static final List<String> dataServers = new ArrayList<>();

    public static void main(final String[] args) {
        setupDataServersAddresses();

        try (ServerSocket serverSocket = new ServerSocket(PORT, 0, InetAddress.getByName(Constants.DB_IP))) {
            LOG.info("Load balance iniciado na porta {}", PORT);

            LOG.info("Proxies registrados: {}", dataServers);
            LOG.info("Load balance iniciado no IP {}/{}", serverSocket.getLocalPort(), serverSocket.getInetAddress());

            final var localHost = InetAddress.getLocalHost();
            final var ipAddress = localHost.getHostAddress();

            LOG.info(ipAddress);

            while (true) {
                handleNextMessage(serverSocket);
            }
        } catch (final IOException e) {
            LOG.error("Erro ao iniciar o servidor", e);
        }
    }

    private static void handleNextMessage(final ServerSocket serverSocket) {
        try {
            final var clientSocket = serverSocket.accept();
            LOG.info("Nova conexão de cliente: {}", clientSocket.getInetAddress().getHostAddress());

            final var clientHandler = new ClientHandler(clientSocket, dataServers, DEFAULT_ALGORITHM);
            new Thread(clientHandler).start();
        } catch (final IOException e) {
            LOG.error("Erro ao aceitar conexão", e);
        }
    }

    private static void setupDataServersAddresses() {
        dataServers.add("225.7.8.9:54329");
        dataServers.add("225.7.8.9:54330");
    }
}
