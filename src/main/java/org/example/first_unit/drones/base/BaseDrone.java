package org.example.first_unit.drones.base;

import org.example.first_unit.drones.connection.LoadBalanceConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BaseDrone {

    private static final Logger LOG = LoggerFactory.getLogger(BaseDrone.class.getSimpleName());

    private String csv_file_name;
    private String connection_port;
    private String connection_host;

    public BaseDrone(String csv_file_name, String connection_port, String connection_host) {
        this.csv_file_name = csv_file_name;
        this.connection_port = connection_port;
        this.connection_host = connection_host;
    }

    public void processAndSendData(LoadBalanceConnection loadBalanceConnection) {
        try {
            List<String> batch = new ArrayList<>();
            FileReader fileReader = new FileReader(
                    Paths.get("src/main/java/org/example/first_unit/drones/files", csv_file_name).toAbsolutePath()
                            .toString());

            try (BufferedReader reader = new BufferedReader(fileReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    batch.add(line);
                    if (batch.size() == 1000) {
                        sendBatch(batch);
                        batch.clear();

                        Thread.sleep(5000);
                    }
                }

                // Send remaining data if batch is not empty
                if (!batch.isEmpty()) {
                    sendBatch(batch);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            LOG.error("Error reading the CSV file", e);
        }
    }

    private void sendBatch(List<String> batch) {
        try {
            BaseExecutor baseExecutor = new BaseExecutor(connection_host, connection_port, batch);
            baseExecutor.run();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
