package org.example.first_unit.drones.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BaseDrone {

    private static final Logger LOG = LoggerFactory.getLogger(BaseDrone.class.getSimpleName());

    private final String csvFileName;
    private final String connectionPort;
    private final String connectionHost;

    public void processAndSendData() {
        try {
            final List<String> batch = new ArrayList<>();
            final var fileReader = new FileReader(
                    Paths.get("src/main/java/org/example/first_unit/drones/files", csvFileName).toAbsolutePath()
                            .toString());

            try (final var reader = new BufferedReader(fileReader)) {
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
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (final IOException e) {
            LOG.error("Error reading the CSV file", e);
        }
    }

    private void sendBatch(final List<String> batch) {
        try {
            final var baseExecutor = new BaseExecutor(connectionHost, connectionPort, batch);
            baseExecutor.run();
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
