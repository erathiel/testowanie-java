package com.example;

import java.util.*;

public class GardenManager {
	private ArrayList<Garden> m_gardens = new ArrayList<Garden>();

	public void addGarden(Garden garden) {
		m_gardens.add(garden);
	}
	
	public void removeGarden(Garden garden) {
		m_gardens.remove(garden);
	}
	
	public Garden getGarden(int at) {
		return m_gardens.get(at);
	}
	
	public Garden getGarden(Garden garden) {
		int index = m_gardens.indexOf(garden);
		return m_gardens.get(index);
	}
	
	public List<Garden> getAllGardens() {
		return m_gardens;
	}

}           
