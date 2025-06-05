package org.example.first_unit.client;

import java.util.concurrent.Executors;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClientExecutor {

    private String multicastHost;
    private Integer multicastPort;
    private String host;
    private Integer port;

    public void run() {
        final var executor = Executors.newSingleThreadExecutor();
        executor.submit(new Client(multicastHost, multicastPort, host, port));
    }
}
