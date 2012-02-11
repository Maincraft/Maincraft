package tk.maincraft.util.doublekeymap;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tk.maincraft.util.doublekeymap.DoubleKeyMap;
import tk.maincraft.util.doublekeymap.GenericDoubleKeyMap;

public class DoubleKeyMapTest {

    DoubleKeyMap<UUID, UUID, UUID> dkm;

    @Before
    public void setUp() throws Exception {
        dkm = new GenericDoubleKeyMap<UUID, UUID, UUID>(HashMap.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        // add some dummy-data
        UUID key1_1 = UUID.randomUUID();
        UUID key1_2 = UUID.randomUUID();
        UUID val1 = UUID.randomUUID();
        UUID key2_1 = UUID.randomUUID();
        UUID key2_2 = UUID.randomUUID();
        UUID val2 = UUID.randomUUID();
        dkm.put(key1_1, key1_2, val1);
        dkm.put(key2_1, key2_2, val2);

        // test
        assertEquals(val1, dkm.get(key1_1, key1_2));
        assertTrue(dkm.contains(key1_1, key1_2));
        assertTrue(dkm.contains(val1));
        assertEquals(val2, dkm.get(key2_1, key2_2));
        assertTrue(dkm.contains(key2_1, key2_2));
        assertTrue(dkm.contains(val2));
        assertEquals(2, dkm.size());
        assertFalse(dkm.isEmpty());

        // test removing
        assertEquals(val1, dkm.remove(key1_1, key1_2));

        assertNull(dkm.get(key1_1, key1_2));
        assertFalse(dkm.contains(key1_1, key1_2));
        assertFalse(dkm.contains(val1));
        assertEquals(1, dkm.size());

        // test clearing
        dkm.clear();

        assertNull(dkm.get(key1_1, key1_2));
        assertFalse(dkm.contains(key1_1, key1_2));
        assertFalse(dkm.contains(val1));
        assertNull(dkm.get(key2_1, key2_2));
        assertFalse(dkm.contains(key2_1, key2_2));
        assertFalse(dkm.contains(val2));
        assertEquals(0, dkm.size());
        assertTrue(dkm.isEmpty());
    }
}
