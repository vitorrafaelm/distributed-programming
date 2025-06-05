package org.example.first_unit.load_balance.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ClientHandler.class.getSimpleName());

    private final Socket clientSocket;
    private final List<String> dataServers;
    private final boolean useRandomBalancing;
    private static int currentProxyIndex = 0;

    public ClientHandler(Socket clientSocket, List<String> dataServers, boolean useRandomBalancing) {
        this.clientSocket = clientSocket;
        this.dataServers = dataServers;
        this.useRandomBalancing = useRandomBalancing;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            in.readLine();
            String proxyAddress = getNextProxyAddress();

            out.println(proxyAddress);
            LOG.info("Cliente direcionado para o servidor de dados: " + proxyAddress);

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Erro ao processar requisição do cliente: " + e.getMessage());
        }
    }

    private synchronized String getNextProxyAddress() {
        if (useRandomBalancing) {
            // Balanceamento aleatório
            return dataServers.get(new Random().nextInt(dataServers.size()));
        } else {
            // Balanceamento Round-Robin
            String proxy = dataServers.get(currentProxyIndex);
            currentProxyIndex = (currentProxyIndex + 1) % dataServers.size();
            return proxy;
        }
    }
}