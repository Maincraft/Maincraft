package tk.maincraft.world.metadata;

import java.io.Serializable;

public abstract class Metadata<T extends Serializable> {
    protected T data;
}
