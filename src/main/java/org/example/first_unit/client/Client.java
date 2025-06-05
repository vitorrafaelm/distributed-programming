package org.example.first_unit.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Client implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Client.class.getSimpleName());

    private final String multicastHost;
    private final Integer multicastPort;
    private final String host;
    private final Integer port;

    @Override
    public void run() {
        LOG.info("Iniciando client {}:{}...", host, port);

        final var getDataTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    final var data = getData();
                    LOG.info("Dados recebidos: {}", data);
                } catch (final Exception e) {
                    LOG.error("Erro ao obter dados do servidor", e);
                }
            }
        };

        // Executa a cada 5 segundos
        final var timer = new Timer();
        timer.scheduleAtFixedRate(getDataTask, 0, 5000);
    }

    private String getData() {
        // Construir requisição
        final var request = new JsonObject();
        request.addProperty("operation", "get_data");
        request.addProperty("host", host);
        request.addProperty("port", port);
        final var bufferSend = request.toString().getBytes();

        // Iniciar Socket
        try (final var socket = new Socket(multicastHost, multicastPort)) {
            LOG.info("Conectado ao servidor");
            final var output = new ObjectOutputStream(socket.getOutputStream());
            final var input = new ObjectInputStream(socket.getInputStream());

            LOG.info("Enviando requisição...");
            output.write(bufferSend);
            output.flush();

            LOG.info("Aguardando resposta...");
            final var responseJson = (String) input.readObject();
            final var response = JsonParser.parseString(responseJson).getAsJsonObject();

            input.close();
            output.close();

            LOG.info("Conexão encerrada");
            return response.get("data").getAsString();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException("A resposta não pôde ser interpretada", e);
        }
    }

}
