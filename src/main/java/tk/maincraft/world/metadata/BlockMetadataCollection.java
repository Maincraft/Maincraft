package tk.maincraft.world.metadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class BlockMetadataCollection implements MetadataCollection {
    private Map<EMetadataType, Object> metadata = new ConcurrentHashMap<EMetadataType, Object>();

    public BlockMetadataCollection() {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getMetadata(EMetadataType type) throws InvalidMetadataTypeException {
        T val;
        try {
            val = (T) metadata.get(type);
        } catch (ClassCastException e) {
            throw new InvalidMetadataTypeException("Sorry, but this Metadata is using another type.", e);
        }
        return (T) ((val == null) ? type.getDefaultValue() : val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Object> void setMetadata(EMetadataType type, T data) throws InvalidMetadataTypeException {
        metadata.put(type, data);
    }
}
