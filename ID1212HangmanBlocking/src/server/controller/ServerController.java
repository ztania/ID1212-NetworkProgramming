package server.controller;

import server.model.HandleUserGuess;
/**
 * Controller for the server. Handles the interaction between the net and model layers
 */
public class ServerController {
	
	private HandleUserGuess handleGuess = new HandleUserGuess();
	private String responseFromServer;

	/**
	 * Parses the input from the user and calls the appropriate method in "HandleUserInput"
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
