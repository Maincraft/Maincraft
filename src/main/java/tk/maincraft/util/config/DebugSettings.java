package tk.maincraft.util.config;

import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;

import me.main__.util.SerializationConfig.Property;
import me.main__.util.SerializationConfig.SerializationConfig;

@SerializableAs("DebugSettings")
public class DebugSettings extends SerializationConfig {

    @Property
    private boolean detailedPacketDebug;
    @Property
    private boolean dumpPackets;

    public DebugSettings() {
        super();
    }

    public DebugSettings(Map<String, Object> values) {
        super(values);
    }

    @Override
    public void setDefaults() {
        detailedPacketDebug = false;
        dumpPackets = false;
    }

    public boolean getDetailedPacketDebug() {
        return detailedPacketDebug;
    }

    public void setDetailedPacketDebug(boolean detailedPacketDebug) {
        this.detailedPacketDebug = detailedPacketDebug;
    }

    public boolean getDumpPackets() {
        return dumpPackets;
    }

    public void setDumpPackets(boolean dumpPackets) {
        this.dumpPackets = dumpPackets;
    }
}
