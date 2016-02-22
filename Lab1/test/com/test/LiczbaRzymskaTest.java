package com.test;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.LiczbaRzymska;

public class LiczbaRzymskaTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private LiczbaRzymska licz = new LiczbaRzymska(2);
	private LiczbaRzymska liczuj = new LiczbaRzymska(-1);
	private LiczbaRzymska liczduza = new LiczbaRzymska(2000);
	
	@Test
	public void toStringCheck() {
		assertEquals("II", licz.toString());
	}
	
	@Test
	public void exceptionCheck() {
		thrown.expect(IllegalArgumentException.class);
		liczuj.toString();
	}
}
