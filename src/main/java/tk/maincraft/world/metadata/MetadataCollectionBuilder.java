package tk.maincraft.world.metadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetadataCollectionBuilder {
    private MetadataCollectionBuilder() {
    }

    public static MetadataCollection buildBlockMetadataCollection() {
        return new BasicMetadataCollection(Metadatable.BLOCK);
    }

    private static final class BasicMetadataCollection implements MetadataCollection {
        private final Map<EMetadataType, Object> metadata;
        private final Metadatable type;

        private BasicMetadataCollection(Metadatable type) {
            this.metadata = new ConcurrentHashMap<EMetadataType, Object>();
            this.type = type;
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
            if (!type.getClazz().isAssignableFrom(data.getClass()))
                throw new InvalidMetadataTypeException();
            metadata.put(type, data);
        }
    }
}
