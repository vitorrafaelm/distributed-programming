package org.example.first_unit.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

public class Client implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Client.class.getSimpleName());

    private final ServerSocket receiver;

    private final String multicastHost;
    private final Integer multicastPort;
    private final String host;
    private final Integer port;

    public Client(String multicastHost, Integer multicastPort, Integer port) throws IOException {
        this.multicastHost = multicastHost;
        this.multicastPort = multicastPort;
        this.port = port;

        LOG.info("Iniciando ServerSocket na porta {}", port);
        this.receiver = new ServerSocket(port);
        this.host = InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    public void run() {
        LOG.info("Iniciando client {}:{}...", host, port);

        final var dataListener = new Thread(this::listenDatacenter);
        dataListener.start();

        final var getDataTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    requestData();
                    LOG.info("Dados solicitados");
                } catch (final Exception e) {
                    LOG.error("Erro ao obter dados do servidor", e);
                }
            }
        };

        // Executa a cada 5 segundos
        final var timer = new Timer();
        timer.scheduleAtFixedRate(getDataTask, 0, 5000);
    }

    private void listenDatacenter() {
        try {
            while (true) {
                LOG.info("Aguardando conexão do servidor de datacenter na porta {}", port);
                final var datacenterSocket = receiver.accept();

                try (final var input = new ObjectInputStream(datacenterSocket.getInputStream())) {
                    final var data = (String) input.readObject();
                    LOG.info("Dados recebidos do servidor: {}", data);
                } catch (final ClassNotFoundException e) {
                    LOG.error("Erro ao interpretar os dados recebidos", e);
                } finally {
                    datacenterSocket.close();
                }
            }
        } catch (final IOException e) {
            LOG.error("Erro ao aceitar conexão do cliente", e);
        }
        ;
    }

    private void requestData() {
        // Construir requisição
        final var request = new JsonObject();
        request.addProperty("operation", "get_data");
        request.addProperty("data", "");
        request.addProperty("host", host);
        request.addProperty("port", port);
        final var bufferSend = request.toString().getBytes();

        // Enviar requisição para o servidor
        try (final var ds = new DatagramSocket()) {
            final var packageToSend = new DatagramPacket(
                    bufferSend, bufferSend.length,
                    InetAddress.getByName(multicastHost), multicastPort);

            ds.send(packageToSend);
            ds.close();
        } catch (IOException e) {
            System.err.println("Error sending data to the server: " + e.getMessage());
        }
    }

}
