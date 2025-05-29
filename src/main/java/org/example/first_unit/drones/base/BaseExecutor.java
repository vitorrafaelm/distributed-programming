package org.example.first_unit.drones.base;

import org.example.first_unit.drones.connection.BaseConnection;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BaseExecutor {
    private String connection_host;
    private String connection_port;
    private List<String> batch;

    public BaseExecutor(String connectionHost, String connectionPort, List<String> batch) {
        this.connection_host = connectionHost;
        this.connection_port = connectionPort;
        this.batch = batch;
    }

    public void run() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new BaseConnection(connection_host, connection_port, batch));

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
