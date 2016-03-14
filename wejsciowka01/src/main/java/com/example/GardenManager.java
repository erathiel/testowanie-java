package com.example;

import java.util.*;

public class GardenManager {
	private List<Garden> m_gardens = new ArrayList<Garden>();
	
    public GardenManager(IGardens garden) {
        this.m_gardens = new ArrayList<Garden>(Arrays.asList(garden.getGarden()));
    }

	public void addGarden(Garden garden) {
		m_gardens.add(garden);
	}
	
	public void removeGarden(Garden garden) {
		m_gardens.remove(garden);
	}
	
	public Garden[] getGarden() {
		Garden[] array = m_gardens.toArray(new Garden[m_gardens.size()]);
		return array;
	}
}
