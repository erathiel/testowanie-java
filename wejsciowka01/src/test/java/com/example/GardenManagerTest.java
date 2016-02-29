package com.example;

import static org.junit.Assert.*;

import org.junit.Test;

public class GardenManagerTest
{
    private GardenManager gm = new GardenManager();
    private GardenManager gmfilled = new GardenManager();
	private Garden garden1 = new Garden("Fiolek", 5);
	private Garden garden2 = new Garden("Roza", 3);
	
	@Test
	public void addCheck() {
		gm.addGarden(garden1);
		gmfilled.addGarden(garden1);
		assertArrayEquals(gm.getGarden(), gmfilled.getGarden());
	}
	
	@Test
    public void removeCheck() {
		gm.addGarden(garden1);
		gm.addGarden(garden2);
		gmfilled.addGarden(garden1);
		gm.removeGarden(garden2);
		assertArrayEquals(gm.getGarden(), gmfilled.getGarden());
    }

    @Test
    public void addCheckThorough() {
        gm.addGarden(garden1);
        Garden[] expected = new Garden[]{garden1};
        assertArrayEquals(expected, gm.getGarden());
    }

    @Test
    public void removeCheckThorough() {
       gm.addGarden(garden1);
       gm.addGarden(garden2);
       Garden[] expected = new Garden[]{garden2};
       gm.removeGarden(garden1);
       assertArrayEquals(expected, gm.getGarden());
    }
}
