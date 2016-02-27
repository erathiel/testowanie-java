package com.example.app;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.app.Calculator;

public class CalculatorTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none(); 
	
	private Calculator calc = new Calculator();
	
	@Test
	public void addCheck() {
		assertEquals(5, calc.add(3,2));
	}
	
	@Test
	public void subCheck() {
		assertEquals(1, calc.sub(3,2));
	}
	
	@Test
	public void multiCheck() {
		assertEquals(6, calc.multi(3,2));
	}
	
	@Test
	public void divCheck() {
		assertEquals(2, calc.div(6, 3));
	}
	
	@Test
	public void greaterCheck() {
		assertTrue(calc.greater(3, 2));
		assertFalse(calc.greater(2, 3));
	}
	
	@Test
	public void divZeroCheck() {
		thrown.expect(ArithmeticException.class);
		calc.div(5, 0);
	}

    @Test
    public void divTestFailOnException() {
        calc.div(5, 0);
    }
}
