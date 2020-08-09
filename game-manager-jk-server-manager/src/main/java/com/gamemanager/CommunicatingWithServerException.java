package com.gamemanager;

public class CommunicatingWithServerException extends Exception{
	
	
	public CommunicatingWithServerException(String message) {
		super(message);
	}
	
	public CommunicatingWithServerException(String message, Throwable cause) {
		super(message, cause);
	}
}
