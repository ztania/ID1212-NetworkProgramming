/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import common.MessageType;

/**
 *
 * @author Tania
 */

public class UserInput {
	
	private String userInput;
	private String[] userArguments;
	
	public UserInput(String userInput) {
		this.userInput = userInput;
		splitUserInput(userInput);
	}
	
	public String getMessage() {
		return userInput;
	}
	
	public MessageType getInputType() {
		if(userInput.contains("Connect")) {
			return MessageType.CONNECT;
		}
		else if(userInput.equals("Quit")) {
			return MessageType.DISCONNECT;
		}
		else if(userInput.length() == 1 && !userInput.equals(" ")) {
			return MessageType.LETTER;
		}
		else if(userInput.equals("New Game")) {
			return MessageType.NEWGAME;
		}
		else if(userInput.length() > 1 && !userInput.contains(" ")){
			return MessageType.WORD;
		}
		else return MessageType.INVALID;
	}

	public String getHost() {
		String host;
		try {
			//the host is set in the 1st position of the array
			host = userArguments[1];
		}
		catch(Exception e){
			host = "127.0.0.1";
		}
		return host;
	}

	public int getPort() {
		int port;
		try {
			//the port is set in the 2nd position of the array
			port = Integer.parseInt(userArguments[2]);
		}
		catch(Exception e){
			port = 8089;
		}
		return port;
	}

	public String getLetter() {
		return userInput;
	}

	public String getWord() {
		return userInput;
	}
	/**
	 * Splitting the user input string. The first position of the string should contain the host 
         * and the second position should contain the port number in the format: "connect <host> <port>". 
	 */
	public void splitUserInput(String userInput) {
		userArguments = userInput.split(" ");
	}
}

