package org.example.first_unit.client;

import java.io.IOException;
import java.util.concurrent.Executors;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClientExecutor implements Runnable {

    private String multicastHost;
    private Integer multicastPort;
    private Integer port;

    @Override
    public void run() {
        try (final var executor = Executors.newSingleThreadExecutor();) {
            executor.submit(new Client(multicastHost, multicastPort, port));
        } catch (final IOException e) {
            throw new RuntimeException("Erro ao iniciar o cliente", e);
        }
    }
}
