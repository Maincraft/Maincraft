package tk.maincraft.world.metadata;

public interface MetadataCollection {
    /**
     * Gets metadata.
     *
     * @param type The {@link EMetadataType} of the metadata we want to get.
     * @return The metadata.
     * @throws InvalidMetadataTypeException This method will automatically try to cast the metadata
     * to the right type. You will get this exception if that operation fails.
     */
    <T> T getMetadata(EMetadataType type) throws InvalidMetadataTypeException;

    /**
     * Sets metadata.
     *
     * @param type The {@link EMetadataType} of the metadata we want to set.
     * @param data The metadata.
     * @throws InvalidMetadataTypeException This method will automatically try to cast the metadata
     * to the right type. You will get this exception if that operation fails.
     */
    <T extends Object> void setMetadata(EMetadataType type, T data) throws InvalidMetadataTypeException;
}