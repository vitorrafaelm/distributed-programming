package org.example.first_unit.data_center.server;

import java.util.concurrent.Executors;

public class MulticastExecutor {
    private final String connectionHost;
    private final String connectionPort;

    public MulticastExecutor(final String connectionHost, final String connectionPort) {
        this.connectionHost = connectionHost;
        this.connectionPort = connectionPort;
    }

    public void run() {
        try (var executor = Executors.newSingleThreadExecutor()) {
            executor.submit(new MulticastServer(connectionHost, connectionPort));
            // executor.shutdown();
            // executor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }
}
