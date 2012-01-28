package me.main__.maincraft.util.doublekeymap;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * A {@link DoubleKeyMap}-implementation
 */
public class GenericDoubleKeyMap<S, T, U> implements DoubleKeyMap<S, T, U> {

    private final static class Key<S, T> {
        protected S valS;
        protected T valT;

        public Key(S valS, T valT) {
            this.valS = valS;
            this.valT = valT;
        }

        @Override
        public String toString() {
            return "Key [val1=" + valS + ", val2=" + valT + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((valS == null) ? 0 : valS.hashCode());
            result = prime * result + ((valT == null) ? 0 : valT.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (!(obj instanceof Key))
                return false;

            Key<?, ?> other = (Key<?, ?>) obj;
            if (valS == null) {
                if (other.valS != null)
                    return false;
            }
            else if (!valS.equals(other.valS))
                return false;

            if (valT == null) {
                if (other.valT != null)
                    return false;
            }
            else if (!valT.equals(other.valT))
                return false;

            return true;
        }
    }

    private final Map<Key<S, T>, U> map;

    public GenericDoubleKeyMap(@SuppressWarnings("rawtypes") Class<? extends Map> mapclass) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends Map<Key<S, T>, U>> myClass = (Class<? extends Map<Key<S, T>, U>>) mapclass;
            Constructor<? extends Map<Key<S, T>, U>> ctor = myClass.getConstructor();
            map = ctor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(S key1, T key2, U value) {
        map.put(new Key<S, T>(key1, key2), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public U get(S key1, T key2) {
        return map.get(new Key<S, T>(key1, key2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        map.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(S key1, T key2) {
        return map.containsKey(new Key<S, T>(key1, key2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(U val) {
        return map.containsValue(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public U remove(S key1, T key2) {
        return map.remove(new Key<S, T>(key1, key2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<U> getValues() {
        return Collections.unmodifiableCollection(map.values());
    }

    @Override
    public String toString() {
        return "DoubleKeyMap " + map;
    }
}
