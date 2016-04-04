package com.example.mockdemo.app;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import com.example.mockdemo.messenger.SendingStatus;
import com.example.mockdemo.messenger.MalformedRecipientException;
import com.example.mockdemo.messenger.MessageService;
import com.example.mockdemo.messenger.ConnectionStatus;

public class MockitoMessageTest {
	
	private static final String VALID_SERVER = "inf.ug.edu.pl";
	private static final String INVALID_SERVER = "inf.ug.edu.eu";

	private static final String VALID_MESSAGE = "some message";
	private static final String INVALID_MESSAGE = "ab";
	
	private static final String MESSAGE = "";

    //SUT
    @Mock
    MessageService mock;
    private Messenger messenger;

	@Before
	public void Setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		messenger = new Messenger(mock);
	}
	
	@Test
	public void checkSendingMessage() throws MalformedRecipientException {
	    when(mock.send(VALID_SERVER, INVALID_MESSAGE)).thenReturn(SendingStatus.SENDING_ERROR);
	    when(mock.send(VALID_SERVER, VALID_MESSAGE)).thenReturn(SendingStatus.SENT);
	    when(mock.send(INVALID_SERVER, VALID_MESSAGE)).thenReturn(SendingStatus.SENDING_ERROR);
		when(mock.send(INVALID_SERVER, INVALID_MESSAGE)).thenThrow(new MalformedRecipientException());
		assertEquals(1, messenger.sendMessage(VALID_SERVER, INVALID_MESSAGE));
		assertEquals(2, messenger.sendMessage(INVALID_SERVER, INVALID_MESSAGE));
		assertEquals(0, messenger.sendMessage(VALID_SERVER, VALID_MESSAGE));
		assertEquals(1, messenger.sendMessage(INVALID_SERVER, VALID_MESSAGE));
		verify(mock).send(INVALID_SERVER,INVALID_MESSAGE);
        verify(mock).send(INVALID_SERVER,VALID_MESSAGE);
        verify(mock).send(VALID_SERVER,INVALID_MESSAGE);
        verify(mock).send(VALID_SERVER,VALID_MESSAGE);
	}

    @Test
    public void checkCheckingConnection() {
        when(mock.checkConnection(VALID_SERVER)).thenReturn(ConnectionStatus.SUCCESS);
        when(mock.checkConnection(INVALID_SERVER)).thenReturn(ConnectionStatus.FAILURE);
        assertEquals(0, messenger.testConnection(VALID_SERVER));
        assertEquals(1, messenger.testConnection(INVALID_SERVER));
        verify(mock).checkConnection(VALID_SERVER);
        verify(mock).checkConnection(INVALID_SERVER);
    }
}
