package org.example.first_unit.drones;

import org.example.first_unit.drones.base.BaseDrone;
import org.example.first_unit.drones.connection.LoadBalanceConnection;

public class droneEast {
    public static void main(String[] args) {
        String csv_file_name = "drone_east.csv";

        LoadBalanceConnection loadBalanceConnection = new LoadBalanceConnection("localhost", 9876);
        loadBalanceConnection.connectToLocationServer();

        String dataServerPort = loadBalanceConnection.getDataServerPort();
        String dataServerHost = loadBalanceConnection.getDataServerHost();

        BaseDrone baseDrone = new BaseDrone(csv_file_name, dataServerPort, dataServerHost);
        baseDrone.processAndSendData(loadBalanceConnection);
    }
}
