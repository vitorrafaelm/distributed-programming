package org.example.first_unit.data_center.server;

import java.net.*;

public class MulticastServer implements Runnable {
    private String multicastServerPort;
    private String getMulticastServerHost;

    public MulticastServer(String multicastServerPort, String getMulticastServerHost) {
        this.multicastServerPort = multicastServerPort;
        this.getMulticastServerHost = getMulticastServerHost;
    }

    @Override
    public void run() {

        try(MulticastSocket ms = new MulticastSocket(Integer.parseInt(multicastServerPort))) {
            InetAddress multicastIP = InetAddress.getByName(getMulticastServerHost);

            InetSocketAddress group = new InetSocketAddress(multicastIP, Integer.parseInt(multicastServerPort));
            NetworkInterface networkInterface = NetworkInterface.getByName("wlo1");
            ms.joinGroup(group, networkInterface);

            byte[] messageReceived = CaptureMessage(ms);

            initiateDataProcessing(messageReceived);

            ms.leaveGroup(group, networkInterface);
            ms.close();
        } catch (Exception e) {
            System.err.println("Error starting multicast server: " + e.getMessage());
        }
    }

    private byte[] CaptureMessage(MulticastSocket ms) throws Exception {
        // Como saber qual o tamanho do buffer de recebimento?
        byte bufferReceive[] = new byte[1024];
        DatagramPacket pacoteRecepcao = new DatagramPacket(
                bufferReceive,
                bufferReceive.length
        );

        ms.receive(pacoteRecepcao);
        return bufferReceive;
    }

    private void initiateDataProcessing(byte[] data) {
        // Aqui deve iniciar o processamento dos dados recebidos
        // Isso pode incluir salvar em um banco de dados, processar e enviar para outro serviço, etc.
        System.out.println("Data processing initiated.");
    }
}
