package com.example;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

public class GardenManagerTest2 {
  
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
        Garden[] expected = new Garden[]{garden1};
        expect(mock.addGarden(garden1)).andReturn(new ArrayList<Garden>().add(garden1));
        expect(mock.getGarden()).andReturn(new ArrayList<Garden>().toArray(new Garden[1]));
        replay(mock);
        assertArrayEquals(expected, manager.getGarden());
        verify(mock);
    }

}
