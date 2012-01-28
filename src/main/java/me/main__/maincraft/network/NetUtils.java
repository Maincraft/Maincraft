package me.main__.maincraft.network;

import java.io.IOException;

import me.main__.maincraft.packet.PacketClient;

public final class NetUtils {
    private NetUtils() {
    }

    public static String readString(PacketClient client, int maxlength) throws IOException {
        short recvlength = client.getDataInputStream().readShort();
        if (recvlength > maxlength)
            throw new IOException("String longer than allowed length!");
        if (recvlength < 0)
            throw new IOException("A string shorter than 0???");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recvlength; i++) {
            sb.append(client.getDataInputStream().readChar());
        }

        return sb.toString();
    }

    public static void writeString(PacketClient client, String string) throws IOException {
        client.getDataOutputStream().writeShort(string.length());
        client.getDataOutputStream().writeChars(string);
    }
}
