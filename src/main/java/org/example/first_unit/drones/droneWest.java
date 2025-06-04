package org.example.first_unit.drones;

import org.example.first_unit.drones.base.BaseDrone;
import org.example.first_unit.drones.connection.LoadBalanceConnection;

import java.util.Scanner;

public class droneWest {

    public static void main(String[] args) {
        String csv_file_name = "drone_west.csv";

        LoadBalanceConnection loadBalanceConnection = new LoadBalanceConnection("localhost", 9876);
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
                    System.out.println("processAndSendData has been executed.");
                    break;
                case "2":
                    System.out.println("Exiting the application.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
