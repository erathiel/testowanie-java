package com.example;

import java.util.*;

public class GardenManager {
	private IGardens m_gardens;
	
    public GardenManager(IGardens garden) {
        this.m_gardens = garden;
    }

	public void addGarden(Garden garden) {
		m_gardens.add(garden);
	}
	
	public void removeGarden(Garden garden) {
		m_gardens.remove(garden);
	}
	
	public Garden getGarden(int at) {
		return m_gardens.get(at);
	}
}
