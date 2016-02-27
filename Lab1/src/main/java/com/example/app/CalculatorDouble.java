package com.example.app;

public class CalculatorDouble {

	public double add(double n1, double n2) {
		return n1 + n2;
	}
	
	public double sub(double n1, double n2) {
		return n1 - n2;
	}
	
	public double multi(double n1, double n2) {
		return n1 * n2;
	}
	
	public double div(double n1, double n2) {
        if( n2 == 0 )
            throw new ArithmeticException("Division by 0!");
		return n1 / n2;
	}
	
	public boolean greater(double n1, double n2) {
		return n1 > n2;
	}
}
