package org.example;

import org.example.first_unit.client.ClientLauncher;
import org.example.first_unit.data_center.DataCenter;
import org.example.first_unit.data_center.DataCenterBackup;
import org.example.first_unit.drones.droneEast;
import org.example.first_unit.drones.droneNorth;
import org.example.first_unit.drones.droneSouth;
import org.example.first_unit.drones.droneWest;
import org.example.first_unit.load_balance.LoadBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class.getSimpleName());

    public static void main(String[] args) {
        System.out.println("Qual serviço deseja executar?");
        System.out.println("1. Load Balancer");
        System.out.println("2. Data Center");
        System.out.println("3. Data Center Backup");
        System.out.println("4. Drone East");
        System.out.println("5. Drone West");
        System.out.println("6. Drone North");
        System.out.println("7. Drone South");
        System.out.println("8. Client");

        final var scanner = new java.util.Scanner(System.in);
        final var userInput = scanner.nextInt();
        scanner.close();

        switch (userInput) {
            case 1:
                LoadBalance.main(args);
                break;
            case 2:
                DataCenter.main(args);
                break;
            case 3:
                DataCenterBackup.main(args);
                break;
            case 4:
                droneEast.main(args);
                break;
            case 5:
                droneWest.main(args);
                break;
            case 6:
                droneNorth.main(args);
                break;
            case 7:
                droneSouth.main(args);
                break;
            case 8:
                ClientLauncher.main(args);
                break;
            default:
                LOG.info("Opção inválida. Por favor, escolha um número de 1 a 8.");
        }
    }
}
