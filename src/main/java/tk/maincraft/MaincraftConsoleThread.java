package tk.maincraft;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MaincraftConsoleThread extends Thread {
    private final MainServer server;
    private final Object lock = new Object();

    private boolean stopping = false;

    public MaincraftConsoleThread(MainServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        synchronized (lock) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!stopping) {
                try {
                    lock.wait(100);
                } catch (InterruptedException e) {
                    return;
                }
                try {
                    String line = reader.readLine();
                    if ((line != null) && (line != "")) {
                        server.dispatchCommand(server.getConsoleSender(), line);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public void shutdown() {
        this.interrupt();
        stopping = true;
    }
}
