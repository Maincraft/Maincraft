package tk.maincraft.util.config;

import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;

import me.main__.util.SerializationConfig.Property;
import me.main__.util.SerializationConfig.SerializationConfig;

@SerializableAs("MaincraftConfig")
public class MaincraftConfig extends SerializationConfig {

    @Property
    private DebugSettings debugSettings;
    @Property
    private ChatSettings chatSettings;
    @Property
    private IOSettings IOSettings;

    public MaincraftConfig() {
        super();
    }

    public MaincraftConfig(Map<String, Object> values) {
        super(values);
    }

    @Override
    public void setDefaults() {
        debugSettings = new DebugSettings();
        chatSettings = new ChatSettings();
        IOSettings = new IOSettings();
    }

    public DebugSettings getDebugSettings() {
        return debugSettings;
    }

    public ChatSettings getChatSettings() {
        return chatSettings;
    }

    public IOSettings getIOSettings() {
        return IOSettings;
    }
}
