package org.example.first_unit.load_balance.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.example.first_unit.load_balance.LoadBalanceAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ClientHandler.class.getSimpleName());

    private static AtomicInteger currentProxyIndex = new AtomicInteger(0);

    private final Socket clientSocket;
    private final List<String> dataServers;
    private final LoadBalanceAlgorithm loadBalanceAlgorithm;

    public ClientHandler(final Socket clientSocket, final List<String> dataServers,
            final LoadBalanceAlgorithm loadBalanceAlgorithm) {
        this.clientSocket = clientSocket;
        this.dataServers = dataServers;
        this.loadBalanceAlgorithm = loadBalanceAlgorithm;
    }

    @Override
    public void run() {
        try (final var out = new PrintWriter(clientSocket.getOutputStream())) {
            final String proxyAddress = getNextProxyAddress();

            out.println(proxyAddress);
            LOG.info("Cliente direcionado para o servidor de dados: {}", proxyAddress);

            clientSocket.close();
        } catch (final IOException e) {
            LOG.error("Erro ao processar requisição do cliente", e);
        }
    }

    private synchronized String getNextProxyAddress() {
        return loadBalanceAlgorithm.get(dataServers, currentProxyIndex);
    }

}
