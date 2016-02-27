package com.example.app;

public class LiczbaRzymska {

	private int liczbarzymska;
	
	public LiczbaRzymska(int liczba) {
		liczbarzymska = liczba;
	}
	
	public String toString() {
		final int arabic[] = {1000, 500, 100, 50, 10, 5, 1};
		final char roman[] = {'M', 'D', 'C', 'L', 'X', 'V', 'I'};
		final int ROMAN_N = arabic.length;
		
		if(liczbarzymska < 1 || liczbarzymska > 1000) {
			throw new IllegalArgumentException("Zła liczba!");
		}
		else {
			int i = 0;
			 
			String result = "";
			 
			while ((liczbarzymska > 0) && (i < ROMAN_N)) {
				if(liczbarzymska >= arabic[i]) {
					liczbarzymska -= arabic[i];
					result += roman[i];
				}
				else if ((i%2 == 0) && (i<ROMAN_N-2) &&
						 (liczbarzymska >= arabic[i] - arabic[i+2]) &&
						 (arabic[i+2] != arabic[i] - arabic[i+2])) {
					liczbarzymska -= arabic[i] - arabic[i+2];
					result += roman[i+2];
					result += roman[i];
					i++;
				}
				else if ((i%2 == 1) &&
						 (i<ROMAN_N-1) && 
						 (liczbarzymska >= arabic[i] - arabic[i+1]) &&
						 (arabic[i+1] != arabic[i] - arabic[i+1])) {
					liczbarzymska -= arabic[i] - arabic[i+1];
					result += roman[i+1];
					result += roman[i];
					i++;
				}
				else {
					i++;
				}
			}
			
			return result;
		}
	}
}
