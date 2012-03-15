package tk.maincraft.world.metadata;

import static tk.maincraft.world.metadata.Metadatable.BLOCK;

public enum EMetadataType {
    DATA_VALUE(byte.class, (byte) 0, BLOCK);

    private final Metadatable[] types;
    private final Class<?> clazz;
    private final Object def;

    private EMetadataType(Class<?> clazz, Metadatable type0, Metadatable... types) {
        this.types = new Metadatable[types.length + 1];
        this.types[0] = type0;
        for (int i = 0; i < types.length; i++) {
            this.types[i + 1] = types[i];
        }
        this.clazz = clazz;
        this.def = null;
    }

    private <T> EMetadataType(Class<T> clazz, T def, Metadatable type0, Metadatable... types) {
        this.types = new Metadatable[types.length + 1];
        this.types[0] = type0;
        for (int i = 0; i < types.length; i++) {
            this.types[i + 1] = types[i];
        }
        this.clazz = clazz;
        this.def = def;
    }

    /**
     * Gets the type of elements that are supposed to support this metadata.
     * @return The type of elements that are supposed to support this metadata.
     */
    public Metadatable[] getTypes() {
        return types;
    }
    
    /**
     * Gets the type of this metadata.
     *
     * @return The type of this metadata.
     */
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * Gets the default value for this metadata.
     * <p>
     * May be {@code null}!
     *
     * @return The default value for this metadata.
     */
    @SuppressWarnings("unchecked")
    public <T> T getDefaultValue() {
        return (T) def;
    }
}
