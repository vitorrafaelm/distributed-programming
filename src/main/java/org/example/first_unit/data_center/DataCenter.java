package org.example.first_unit.data_center;

import org.example.first_unit.data_center.server.MulticastExecutor;

public class DataCenter {

    private static String DataCenterPort = "54330";
    private static String DataCenterHost = "225.7.8.9";

    public static void main(String[] args) {
        try {
            MulticastExecutor multicastExecutor = new MulticastExecutor(DataCenterPort, DataCenterHost);
            multicastExecutor.run();

            System.out.println("Data Center is running on host: " + DataCenterHost + " and port: " + DataCenterPort);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
