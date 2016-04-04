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

    private List<Garden> garden = new ArrayList<Garden>();

    @Before
    public void setUp() {
        mock = createMock(IGardens.class);
        manager = new GardenManager(mock);
    }

    @Test
    public void addCheck() {
        final Garden garden1 = new Garden("Roza", 3);
        expect(mock.add(garden1)).andReturn(true);
        expect(mock.get(1)).andReturn(garden1);
        replay(mock);
        manager.addGarden(garden1);
        assertEquals("Roza", manager.getGarden(1).getName());
        verify(mock);
    }

    @Test
    public void findGardenByFlowerCheck() {
        final Garden garden1 = new Garden("Fiolek", 3);
        expect(mock.findGardenByFlower("Fiolek")).andReturn(garden1).atLeastOnce();
        replay(mock);
        assertEquals("Fiolek", manager.findGardenByFlower("Fiolek").getName());
        verify(mock);
    }
    
    @Test
    public void findGardenByNumberCheck() {
    	final Garden garden2 = new Garden("Storczyk", 5);
    	garden.add(garden2);
        expect(mock.findGardenByNumber(5)).andReturn(garden).once();
        replay(mock);
        assertEquals(garden2, manager.findGardenByNumber(5).get(0));
        verify(mock);
    }
}
