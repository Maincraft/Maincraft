package tk.maincraft;

import java.lang.management.ManagementFactory;

public class Main {

    public static void main(String[] args) {
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());
        // TODO Auto-generated method stub
        MainServer server = new MainServer();
        server.start();
    }

}
