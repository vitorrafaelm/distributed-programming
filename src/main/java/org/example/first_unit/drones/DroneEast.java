package org.example.first_unit.drones;

import org.example.first_unit.drones.base.BaseDrone;
import org.example.first_unit.drones.connection.LoadBalanceConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class DroneEast {

    private static final Logger LOG = LoggerFactory.getLogger(DroneEast.class.getSimpleName());

    public static void main(String[] args) {
        String csv_file_name = "drone_east.csv";

        LoadBalanceConnection loadBalanceConnection = new LoadBalanceConnection(System.getenv("LOAD_BALANCE_IP"), 9876);
        loadBalanceConnection.connectToLocationServer();

        String dataServerPort = loadBalanceConnection.getDataServerPort();
        String dataServerHost = loadBalanceConnection.getDataServerHost();

        BaseDrone baseDrone = new BaseDrone(csv_file_name, dataServerPort, dataServerHost);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Choose an option:");
            System.out.println("1. Call processAndSendData");
            System.out.println("2. Exit");

            String userInput = scanner.nextLine().trim();

            switch (userInput) {
                case "1":
                    baseDrone.processAndSendData(loadBalanceConnection);
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
