package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.example.GardenManager;
import com.example.Garden;

public class ManagerSteps {
	private GardenManager manager;
	private Garden flower;
	
	@Given("a manager")
	public void managerSetUp() {
		manager = new GardenManager();
	}
	
	@When("create flower with name $name and add to manager")
	public void createFlowerWithName(String name) {
		this.flower = new Garden(name, 1);
		manager.addGarden(this.flower);
	}
	
	@Then("size of manager should be $size")
	public void checkSize(int result) {
		assertEquals(result, manager.getAllGardens().size());
	}
	
	@Then("name of flower in manager should be $name")
	public void checkName(String name) {
		assertEquals(name, manager.getGarden(1).getName());
	}
	
	@When("delete flower with name $name")
	public void checkDeleteFlower(String name) {
		manager.removeGarden(flower);
		assertEquals(name, flower.getName());
	}
	
	@Then("only flower with name $name should remain in manager and its size should be $size")
	public void checkRemainingFlower(String name, int size) {
		assertEquals(name, manager.getAllGardens().get(0).getName());
		assertEquals(size, manager.getAllGardens().size());
	}
}
