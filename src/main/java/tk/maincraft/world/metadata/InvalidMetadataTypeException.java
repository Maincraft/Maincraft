package tk.maincraft.world.metadata;

public class InvalidMetadataTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidMetadataTypeException() {
        super();
    }

    public InvalidMetadataTypeException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public InvalidMetadataTypeException(String arg0) {
        super(arg0);
    }

    public InvalidMetadataTypeException(Throwable arg0) {
        super(arg0);
    }
}
