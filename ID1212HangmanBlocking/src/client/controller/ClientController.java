package client.controller;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import client.net.Connect;
import client.net.HandleServerResponse;

public class ClientController {

	Connect server = new Connect();

	public void connect(String host, int port, String connectMessage, HandleServerResponse handleServerResponse) {

		CompletableFuture.runAsync(() -> {
			try {
				server.connect(host, port, handleServerResponse);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).thenRun(()->server.sendInitialMessage(connectMessage));
	}

	public void disconnect(String quitMessage) {
		try {
			server.disconnect(quitMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void newGame(String message) {
		 CompletableFuture.runAsync(()->server.sendInitialMessage(message));
	}

	public void sendLetter(char letter) {
		 CompletableFuture.runAsync(()->server.sendLetterGuess(letter));	
	}

	public void sendWord(String word) {
		CompletableFuture.runAsync(()->server.sendWordGuess(word));
	}

}
