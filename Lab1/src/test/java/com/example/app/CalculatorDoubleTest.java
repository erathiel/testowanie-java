package com.example.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.app.CalculatorDouble;

public class CalculatorDoubleTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
	private CalculatorDouble calc = new CalculatorDouble();
	
	@Test
	public void addCheck() {
		assertEquals(5.44, calc.add(3.22,2.22), 0.1);
	}
	
	@Test
	public void subCheck() {
		assertEquals(1.5, calc.sub(3,1.5), 0.1);
	}
	
	@Test
	public void multiCheck() {
		assertEquals(6.6, calc.multi(3.0,2.2), 0.1);
	}
	
	@Test
	public void divCheck() {
		assertEquals(2, calc.div(7, 3.5), 0.1);
	}
	
	@Test
	public void greaterCheck() {
		assertTrue(calc.greater(6.43, 2.4));
		assertFalse(calc.greater(2.4, 6.43));
	}
	
	@Test
	public void divZeroCheck() {
        thrown.expect(ArithmeticException.class);
        thrown.expectMessage("Division by 0!");
        assertEquals(0, calc.div(10.4522, 0), 0.1);
    }
}
