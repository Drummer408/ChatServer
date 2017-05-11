package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(int port) {
        startServer(port);
    }

    private void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server listening for clients...");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void run() {
        try {
            long clientId = 0;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client #" + clientId + " connected.");
                ClientThread clientThread = new ClientThread(clientSocket, clientId);
                clientThread.start();
                clientId++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(4444);
        server.run();
    }
}
