/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import server.model.HangmanGame;
/**
 *
 * @author Tania
 */
public class ServerController {
    
private HangmanGame handleGuess = new HangmanGame();
	private String responseFromServer;

	/**
	 * Parses the input from the user and calls the appropriate method in "HangmanGame"
	 * @param userInputFromClient The input provided by the user and sent from the client
	 * @return The response to the client
	 */
	public String handleUserInput(String userInputFromClient) {
		if (userInputFromClient.contains("Connect")) {
			responseFromServer = handleGuess.initialMessage();
		}
		else if(userInputFromClient.equals("New Game")){
			responseFromServer = handleGuess.newGame();
		}
		else if(userInputFromClient.length() == 1) {
			responseFromServer = handleGuess.letterGuess(userInputFromClient.charAt(0));
		}
		else {
			responseFromServer = handleGuess.wordGuess(userInputFromClient);
		}
		
		return responseFromServer;
	}
}

