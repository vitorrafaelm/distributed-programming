package org.example.first_unit.data_center;

import org.example.first_unit.data_center.server.MulticastExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCenter {

    private static final Logger LOG = LoggerFactory.getLogger(DataCenter.class.getSimpleName());

    private static String DataCenterPort = "54330";
    private static String DataCenterHost = "225.7.8.9";

    public static void main(String[] args) {
        try {
            MulticastExecutor multicastExecutor = new MulticastExecutor(DataCenterPort, DataCenterHost);
            multicastExecutor.run();

            LOG.info("Data Center is running on host: " + DataCenterHost + " and port: " + DataCenterPort);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
