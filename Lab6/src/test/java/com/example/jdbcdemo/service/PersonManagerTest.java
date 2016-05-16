package com.example.jdbcdemo.service;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.example.jdbcdemo.domain.Person;

public class PersonManagerTest {
	
	
	PersonManager personManager = new PersonManager();
	
	private final static String NAME_1 = "Ryszard";
	private final static int YOB_1 = 2005;
	
	private final static String NAME_2 = "Wladzio";
	private final static int YOB_2 = 45;
	
	@Test
	public void checkConnection() {
		assertNotNull(personManager.getConnection());
	}
	
	@Test
	public void checkAdd() {	
		//Setup
		Person person = new Person(NAME_1, YOB_1);
		
		personManager.clearPersons();
		assertEquals(1, personManager.addPerson(person));
		
		List<Person> persons = personManager.getAllPersons();
		Person personRetrieved = persons.get(0);
		
		assertEquals(NAME_1, personRetrieved.getName());
		assertEquals(YOB_1, personRetrieved.getYob());	
	}
	
	@Test
	public void checkDelete() {
		//Setup
		int count = 0;
		Person person = new Person(NAME_1, YOB_1);
		Person person2 = new Person(NAME_2, YOB_2);
		personManager.clearPersons();
		
		//Initializing persons List
		personManager.addPerson(person2);
		personManager.addPerson(person);
		personManager.addPerson(person2);
		List<Person> persons = personManager.getAllPersons();
		count = persons.size();
		
		personManager.deletePerson(NAME_1);
		persons = personManager.getAllPersons();
		
		//Assertions
		assertThat(persons.size(), not(equalTo(count)));
		assertEquals(count - 1, persons.size());
		
		for(int i = 0; i < persons.size(); i++)
		{
			Person personRetrieved = persons.get(i); 
			assertThat(NAME_1, not(equalTo(personRetrieved.getName())));
		}
	}
	
	@Test
	public void checkClear() {
		//Setup
		Person person = new Person(NAME_1, YOB_1);
		
		//Add to db
		personManager.addPerson(person);
		personManager.addPerson(person);
		
		//Assertion
		personManager.clearPersons();
		assertEquals(0, personManager.getAllPersons().size());	
	}
	
	@Test
	public void checkUpdate() {
		//Setup
		Person person = new Person(NAME_1, YOB_1);
		personManager.clearPersons();
		
		//Init db
		personManager.addPerson(person);
		
		personManager.updatePerson(NAME_1, NAME_2);
		
		List<Person> persons = personManager.getAllPersons();
		
		//Assertions
		assertEquals(NAME_2, persons.get(0).getName());
	}
	
	@Test
	public void checkGetPersons() {
		//Setup
		Person person = new Person(NAME_1, YOB_2);
		Person person2 = new Person(NAME_2, YOB_1);
		
		//Prepare DB
		personManager.clearPersons();
		personManager.addPerson(person2);
		personManager.addPerson(person);
		
		//Prepare expected
		List<Person> expected = new ArrayList<Person>();
		expected.add(person2);
		expected.add(person);
		
		List<Person> persons = personManager.getAllPersons();
		
		//Assertion
		for(int i = 0; i < persons.size(); i++) {
			assertEquals(expected.get(i).getName(), persons.get(i).getName());
		}
	}
}
