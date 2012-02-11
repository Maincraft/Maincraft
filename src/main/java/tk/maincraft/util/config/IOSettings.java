package tk.maincraft.util.config;

import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;

import me.main__.util.SerializationConfig.Property;
import me.main__.util.SerializationConfig.SerializationConfig;

@SerializableAs("IOSettings")
public class IOSettings extends SerializationConfig {

    @Property
    private int chunkViewDistance;
    @Property
    private int keepChunkInMemoryTime;
    @Property
    private int entityNoRelVal;

    public IOSettings() {
        super();
    }

    public IOSettings(Map<String, Object> values) {
        super(values);
    }

    @Override
    public void setDefaults() {
        chunkViewDistance = 5;
        keepChunkInMemoryTime = 5;
    }

    public int getViewDistance() {
        return chunkViewDistance;
    }

    public void setViewDistance(int viewDistance) {
        this.chunkViewDistance = viewDistance;
    }

    public int getEntityNoRelVal() {
        return entityNoRelVal;
    }

    public void setEntityNoRelVal(int entityNoRelVal) {
        this.entityNoRelVal = entityNoRelVal;
    }

    public int getKeepChunkInMemoryTime() {
        return keepChunkInMemoryTime;
    }

    public void setKeepChunkInMemoryTime(int keepChunkInMemoryTime) {
        this.keepChunkInMemoryTime = keepChunkInMemoryTime;
    }
}
