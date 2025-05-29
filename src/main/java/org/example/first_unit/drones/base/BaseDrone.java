package org.example.first_unit.drones.base;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BaseDrone {
    private String csv_file_name;
    private String connection_port;
    private String connection_host;

    public BaseDrone(String csv_file_name, String connection_port, String connection_host) {
        this.csv_file_name = csv_file_name;
        this.connection_port = connection_port;
        this.connection_host = connection_host;
    }

    public void processAndSendData() {
        try {
            List<String> batch = new ArrayList<>();
            FileReader fileReader = new FileReader(
                    Paths.get("src/main/java/org/example/first_unit/drones/files", csv_file_name).toAbsolutePath().toString()
            );

            try (BufferedReader reader = new BufferedReader(fileReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    batch.add(line);
                    if (batch.size() == 1000) {
                        sendBatch(batch);
                        batch.clear();
                    }
                }

                // Send remaining data if batch is not empty
                if (!batch.isEmpty()) {
                    sendBatch(batch);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
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
