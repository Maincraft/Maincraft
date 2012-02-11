package tk.maincraft.util.config;

import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;

import me.main__.util.SerializationConfig.Property;
import me.main__.util.SerializationConfig.SerializationConfig;

@SerializableAs("ChatSettings")
public class ChatSettings extends SerializationConfig {

    @Property
    private String allowedChars;
    @Property
    private int chatWrapLength;

    public ChatSettings() {
        super();
    }

    public ChatSettings(Map<String, Object> values) {
        super(values);
    }

    @Override
    public void setDefaults() {
        allowedChars = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghi"
                + "jklmnopqrstuvwxyz{|}~⌂ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«» ";
        chatWrapLength = 65;
    }

    public String getAllowedChars() {
        return allowedChars;
    }

    public void setAllowedChars(String allowedChars) {
        this.allowedChars = allowedChars;
    }

    public int getChatWrapLength() {
        return chatWrapLength;
    }

    public void setChatWrapLength(int chatWrapLength) {
        this.chatWrapLength = chatWrapLength;
    }

}
