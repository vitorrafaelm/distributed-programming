package org.example.first_unit.load_balance;

import org.example.first_unit.load_balance.base.ClientHandler;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LoadBalance {
    private static final int PORT = 9876;

    private static final List<String> dataServers = new ArrayList<>();
    private static final boolean useRandomBalancing = false;

    public static void main(String[] args) {
        setupDataServersAddresses();

        try (ServerSocket serverSocket = new ServerSocket(PORT, 0, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Load balance iniciado na porta " + PORT);

            System.out.println("Proxies registrados: " + dataServers);
            System.out.println("Load balance iniciado no IP " + serverSocket.getLocalPort() + "/" + serverSocket.getInetAddress());

            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();

            System.out.println(ipAddress);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Nova conexão de cliente: " + clientSocket.getInetAddress().getHostAddress());

                    ClientHandler clientHandler = new ClientHandler(clientSocket, dataServers, useRandomBalancing);
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    private static void setupDataServersAddresses() {
        // dataServers.add("225.7.8.9:54329");
        dataServers.add("225.7.8.9:54330");
    }
}
