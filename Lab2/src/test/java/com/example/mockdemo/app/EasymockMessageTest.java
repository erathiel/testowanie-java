package com.example.mockdemo.app;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.mockdemo.messenger.ConnectionStatus;
import com.example.mockdemo.messenger.MalformedRecipientException;
import com.example.mockdemo.messenger.MessageService;
import com.example.mockdemo.messenger.SendingStatus;

public class EasymockMessageTest {
	
	//SUT
	private Messenger messenger;
	private MessageService mock;
	
	private static final String VALID_SERVER = "inf.ug.edu.pl";
	private static final String INVALID_SERVER = "inf.ug.edu.eu";

	private static final String VALID_MESSAGE = "some message";
	private static final String INVALID_MESSAGE = "ab";
	
	private static final String MESSAGE = "";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void Setup() {
		mock = createMock(MessageService.class);
		messenger = new Messenger(mock);
	}
	
	@Test
	public void checkSendingMessage() throws MalformedRecipientException {
		expect(mock.send(VALID_SERVER, INVALID_MESSAGE)).andReturn(SendingStatus.SENDING_ERROR).once();
		expect(mock.send(VALID_SERVER, VALID_MESSAGE)).andReturn(SendingStatus.SENT).once();
		expect(mock.send(INVALID_SERVER, VALID_MESSAGE)).andReturn(SendingStatus.SENDING_ERROR).once();
		expect(mock.send(INVALID_SERVER, INVALID_MESSAGE)).andThrow(new MalformedRecipientException()).once();
		replay(mock);
		assertEquals(1, messenger.sendMessage(VALID_SERVER, INVALID_MESSAGE));
		assertEquals(2, messenger.sendMessage(INVALID_SERVER, INVALID_MESSAGE));
		assertEquals(0, messenger.sendMessage(VALID_SERVER, VALID_MESSAGE));
		assertEquals(1, messenger.sendMessage(INVALID_SERVER, VALID_MESSAGE));
		verify(mock);
	}
	
	@Test
	public void checkCheckingConnection() {
		expect(mock.checkConnection(VALID_SERVER)).andReturn(ConnectionStatus.SUCCESS).once();
		expect(mock.checkConnection(INVALID_SERVER)).andReturn(ConnectionStatus.FAILURE).once();
		replay(mock);
		assertEquals(0, messenger.testConnection(VALID_SERVER));
		assertEquals(1, messenger.testConnection(INVALID_SERVER));
		verify(mock);
	}
	
}
