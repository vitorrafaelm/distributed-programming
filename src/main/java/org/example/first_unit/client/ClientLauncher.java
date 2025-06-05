package org.example.first_unit.client;

public class ClientLauncher {

    private static final String MULTICAST_HOST = "225.7.8.9";
    private static final Integer MULTICAST_PORT = 54330;

    public static void main(String[] args) {
        // Gerar porta aleatória baseada no timestamp atual (entre 7000 e 7999)
        final var deciseconds = (int) (System.currentTimeMillis() / 100) % 1000;
        final var port = 7000 + deciseconds;

        // Iniciar o executor do cliente com a porta gerada
        final var multicastExecutor = new ClientExecutor(MULTICAST_HOST, MULTICAST_PORT, port);
        multicastExecutor.run();
    }

}
