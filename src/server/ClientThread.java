package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientThread extends Thread {
    private static ConcurrentMap<Long, ClientThread> clientMap = new ConcurrentHashMap<>();

    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private long clientId;

    private List<ClientThread> peers;

    public ClientThread(Socket socket, long clientId) {
        this.socket = socket;
        this.clientId = clientId;
        clientMap.put(clientId, this);

        try {
            din = new DataInputStream(this.socket.getInputStream());
            dout = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        peers = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = din.readUTF();
                for (long id : clientMap.keySet()) {
                    if (clientId != id) {
                        ClientThread ct = clientMap.get(id);
                        ct.sendToClient(ct, message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToClient(ClientThread clientThread, String message) {
        try {
            clientThread.getDout().writeUTF(message);
            clientThread.getDout().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataInputStream getDin() {
        return din;
    }

    public DataOutputStream getDout() {
        return dout;
    }
}
