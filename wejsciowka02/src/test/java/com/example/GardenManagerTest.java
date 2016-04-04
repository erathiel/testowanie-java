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

    private List<Garden> garden;

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

    @Test
    public void findGardenByFlowerCheck() {
        Garden garden1 = new Garden("Fiolek", 3);
        expect(mock.findGardenByFlower("Fiolek")).andReturn(garden1).atLeastOnce();
        replay(mock);
        assertEquals(garden1, manager.findGardenByFlower("Fiolek"));
        verify(mock);
    }
    
    @Test
    public void findGardenByNumberCheck() {
        Garden garden2 = new Garden("Storczyk", 5);
        expect(mock.findGardenByNumber(5)).andReturn(garden).atLeastOnce();
        replay(mock);
        assertEquals(garden, manager.findGardenByNumber(5));
        verify(mock);
    }
}
