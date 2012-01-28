package me.main__.maincraft.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.easymock.PowerMock.*;
import static org.easymock.EasyMock.expect;

import java.util.List;

import me.main__.maincraft.MainServer;
import me.main__.maincraft.Maincraft;
import me.main__.maincraft.util.config.ChatSettings;
import me.main__.maincraft.util.config.MaincraftConfig;

import org.bukkit.ChatColor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ TextWrap.class, Maincraft.class })
public class TestTextWrap {

    @Before
    public void setUp() {
        MainServer mockServer = mock(MainServer.class);
        MaincraftConfig mockConfig = mock(MaincraftConfig.class);
        when(mockConfig.getChatSettings()).thenReturn(new ChatSettings());
        when(mockServer.getConfig()).thenReturn(mockConfig);

        mockStatic(Maincraft.class);
        expect(Maincraft.getServer()).andReturn(mockServer);
        expect(Maincraft.getServer()).andReturn(mockServer);

        replay(Maincraft.class);
    }

    @Test
    public void test() {
        String testString = "testString " + ChatColor.GREEN + "more Testing " + ChatColor.RED
                + "useless text to trigger a linebreak "
                + /* new line should begin here */"of awesome";
        List<String> ret = TextWrap.wrapText(testString);

        assertEquals(2, ret.size());
        String line1 = ret.get(0);
        String line2 = ret.get(1);

        assertEquals("testString " + ChatColor.GREEN + "more Testing " + ChatColor.RED
                + "useless text to trigger a linebreak", line1);
        assertEquals(ChatColor.RED /* it's important that we don't forget this color */
                + "of awesome", line2);
    }

}
