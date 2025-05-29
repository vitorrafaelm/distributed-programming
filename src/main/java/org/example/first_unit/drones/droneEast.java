package org.example.first_unit.drones;

import org.example.first_unit.drones.base.BaseDrone;

public class droneEast {
    public static void main(String[] args) {
        String csv_file_name = "drone_east.csv";

        // Connection details for the drone
        String connection_port = "51789"; // Port to send data to the multicast data center server
        String connection_host = "225.7.8.9"; // Ip group to send data to the multicast data center server

        // Aqui ainda deve adicionar balanceamento de carga a partir da listagem dos servidores disponíveis;

        BaseDrone baseDrone = new BaseDrone(csv_file_name, connection_port, connection_host);
        baseDrone.processAndSendData();

    }
}
