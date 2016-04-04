package com.example;

import java.util.List;

public interface IGardens { 
    public boolean add(Garden garden);
    public boolean remove(Garden garden);
    public Garden get(int at);
    public Garden findGardenByFlower(String flower);
    public List<Garden> findGardenByNumber(int number);
}
