package org.example.first_unit.data_center.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MulticastExecutor {
    private String connection_host;
    private String connection_port;

    public MulticastExecutor(String connectionHost, String connectionPort) {
        this.connection_host = connectionHost;
        this.connection_port = connectionPort;
    }

    public void run() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new MulticastServer(connection_host, connection_port));
        // executor.shutdown();
        // executor.awaitTermination(1, TimeUnit.SECONDS);
    }
}
