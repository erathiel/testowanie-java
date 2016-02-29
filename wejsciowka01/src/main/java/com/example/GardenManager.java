package com.example;

import java.util.*;

public class GardenManager {
	private List<Garden> m_gardens = new ArrayList<Garden>();
	
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
