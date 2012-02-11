package tk.maincraft;

public class Maincraft {
    private static MainServer server = null;

    public static MainServer getServer() {
        if (server == null)
            throw new IllegalStateException("The server wasn't set!");

        return server;
    }

    public static void setServer(MainServer newserver) {
        if (newserver == null)
            throw new IllegalArgumentException("can't use a null server!");
        if (server != null)
            throw new IllegalStateException("The server has already been set!");

        server = newserver;
    }

    public static void unsetServer() {
        if (server == null)
            throw new IllegalStateException("The server wasn't set!");

        server = null;
    }
}
