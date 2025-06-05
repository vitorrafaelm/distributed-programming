package org.example.first_unit.data_center;

import org.example.first_unit.data_center.server.MulticastExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCenter {

    private static final Logger LOG = LoggerFactory.getLogger(DataCenter.class.getSimpleName());

    private static String dataCenterPort = "54330";
    private static String dataCenterHost = "225.7.8.9";

    public static void main(String[] args) {
        final var multicastExecutor = new MulticastExecutor(dataCenterPort, dataCenterHost);
        multicastExecutor.run();

        LOG.info("Data Center is running on host: {} and port: {}", dataCenterHost, dataCenterPort);
    }
}
