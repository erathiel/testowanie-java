package com.example.mockdemo.app;

import static org.junit.Assert.assertEquals;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.example.mockdemo.messenger.MessageService;
import com.example.mockdemo.messenger.MessageServiceSimpleImpl;

public class MessengerSteps {
	private Messenger messenger;
	private MessageService ms = new MessageServiceSimpleImpl();
	
	private String server;
	private String message;
	
	@Given("a messenger")
	public void messengerSetup() {
		messenger = new Messenger(ms);
	}
	
	@When("set argument to $server")
	public void setServer(String server) {
		this.server = server;
	}
	
	@When("set arguments to $server and $message")
	public void setServerMessage(String server, String message) {
		this.server = server;
		this.message = message;
	}
	
	@Then("test connection should return $result")
	public void shouldTestConnection(int result) {
		assertEquals(result, messenger.testConnection(server));
	}
	
	@Then("send message should return $result")
	public void shouldSendMessage(int result) {
		assertEquals(result, messenger.sendMessage(server, message));
	}
	
}
