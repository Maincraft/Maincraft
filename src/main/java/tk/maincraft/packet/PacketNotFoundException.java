package tk.maincraft.packet;

import java.io.IOException;

@SuppressWarnings("serial")
public class PacketNotFoundException extends IOException {

    public PacketNotFoundException() {
    }

    public PacketNotFoundException(String message) {
        super(message);
    }

    public PacketNotFoundException(Throwable cause) {
        super(cause);
    }

    public PacketNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
