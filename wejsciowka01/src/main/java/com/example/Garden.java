package com.example;


public class Garden
{
    private String m_flower;
    private int m_number_of_flowers;
    private boolean m_has_space_left = false;
	
	public Garden(String name, int number) {
		m_flower = name;
		m_number_of_flowers = number;
		m_has_space_left = true;
	}
}
