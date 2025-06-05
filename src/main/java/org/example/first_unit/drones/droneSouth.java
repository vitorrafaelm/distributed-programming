package org.example.first_unit.drones;

import org.example.first_unit.drones.base.BaseDrone;
import org.example.first_unit.drones.connection.LoadBalanceConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class DroneSouth {

    private static final Logger LOG = LoggerFactory.getLogger(DroneSouth.class.getSimpleName());

    public static void main(final String[] args) {
        final var csvFileFame = "drone_south.csv";

        final var loadBalanceConnection = new LoadBalanceConnection("localhost", 9876);
        loadBalanceConnection.connectToLocationServer();

        final var dataServerPort = loadBalanceConnection.getDataServerPort();
        final var dataServerHost = loadBalanceConnection.getDataServerHost();

        final var baseDrone = new BaseDrone(csvFileFame, dataServerPort, dataServerHost);

        final var scanner = new Scanner(System.in);
        var running = true;

        while (running) {
            System.out.println("Choose an option:");
            System.out.println("1. Call processAndSendData");
            System.out.println("2. Exit");

            final var userInput = scanner.nextLine().trim();

            switch (userInput) {
                case "1":
                    baseDrone.processAndSendData();
                    LOG.info("processAndSendData has been executed.");
                    break;
                case "2":
                    LOG.info("Exiting the application.");
                    running = false;
                    break;
                default:
                    LOG.info("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
