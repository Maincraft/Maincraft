package tk.maincraft.util.doublekeymap;

import java.util.Collection;

/**
 * Like a normal {@link java.util.Map}, but with 2 Objects as key.
 * 
 * @author main()
 * 
 * @param <S> Type of Key 1
 * @param <T> Type of Key 2
 * @param <U> Type of Object
 */
public interface DoubleKeyMap<S, T, U> {

    /**
     * Adds an object to this {@link DoubleKeyMap}.
     * 
     * @param key1 Key-Object 1
     * @param key2 Key-Object 2
     * @param value Value-Object
     */
    void put(S key1, T key2, U value);

    /**
     * Gets an object from this {@link DoubleKeyMap}.
     * 
     * @param key1 Key-Object 1
     * @param key2 Key-Object 2
     * 
     * @return The Value-Object or null, if this {@link DoubleKeyMap} doesn't contain any objects for the specified keys.
     */
    U get(S key1, T key2);

    /**
     * Clears this {@link DoubleKeyMap}.
     */
    void clear();

    /**
     * Looks if this {@link DoubleKeyMap} contains an object for specified keys.
     * 
     * @param key1 Key-Object 1
     * @param key2 Key-Object 2
     * 
     * @return Whether this {@link DoubleKeyMap} contains an object for the specified keys.
     */
    boolean contains(S key1, T key2);

    /**
     * Looks if this {@link DoubleKeyMap} contains the specified value.
     * 
     * @param val The Value-Object.
     * 
     * @return Whether this {@link DoubleKeyMap} contains the specified value.
     */
    boolean contains(U val);

    /**
     * Gets the size of this {@link DoubleKeyMap}.
     * 
     * @return The size.
     */
    int size();

    /**
     * Looks if this {@link DoubleKeyMap} is empty.
     * 
     * @return True if it's empty, false if it isn't.
     */
    boolean isEmpty();

    /**
     * Removes the object mapped to the specified keys, if present.
     * 
     * @param key1 Key-Object 1
     * @param key2 Key-Object 2
     * 
     * @return The object that the keys were formerly mapped to or null if no object existed for the given keys.
     */
    U remove(S key1, T key2);

    /**
     * Gets an <b>unmodifiable</b> {@link Collection} of all values in this {@link DoubleKeyMap}.
     * 
     * @return The values in this {@link DoubleKeyMap}.
     */
    Collection<U> getValues();
}