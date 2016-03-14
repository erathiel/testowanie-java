package com.example;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

public class GardenManagerTest {
  
    //SUT
    private GardenManager manager;
    private IGardens mock;

    @Before
    public void setUp() {
        mock = createMock(IGardens.class);
        manager = new GardenManager(mock);
    }

    @Test
    public void addCheck() {
        Garden garden1 = new Garden("Roza", 3);
        expect(mock.add(garden1)).andReturn(true);
        expect(mock.get(1)).andReturn(garden1);
        replay(mock);
        manager.addGarden(garden1);
        assertEquals("Roza", manager.getGarden(1).getName());
        verify(mock);
    }
}
