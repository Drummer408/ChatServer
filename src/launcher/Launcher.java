package launcher;

import server.Server;

public class Launcher {
    public static void main(String[] args) {
        Server server = new Server(4444);
        server.run();
    }
}
