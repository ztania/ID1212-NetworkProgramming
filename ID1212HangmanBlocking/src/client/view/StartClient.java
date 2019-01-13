package client.view;

import java.util.Scanner;

import client.controller.ClientController;
import client.net.HandleServerResponse;

public class StartClient implements Runnable{
	
	private ClientController controller;
	private boolean isReceivingInput = false;
	Scanner scanner = new Scanner(System.in);

	public void start() {
		if (isReceivingInput) {
			return;
		}
		isReceivingInput = true;
		controller = new ClientController();
		new Thread(this).start();
		
		System.out.println("Type 'Connect' to connect with the server");
	}
	
	
	@Override
	public void run() {
		while (isReceivingInput) {
		UserInput input = new UserInput(scanner.nextLine());	
			switch(input.getInputType()) {
				case CONNECT:
					controller.connect(input.getHost(), input.getPort(), input.getMessage(), new Printer());
					break;
				case DISCONNECT:
					isReceivingInput = false;
					controller.disconnect(input.getMessage());
					break;
				case LETTER:
					controller.sendLetter(input.getLetter());
					break;
				case WORD:
					controller.sendWord(input.getWord());
					break;
				case NEWGAME:
					controller.newGame(input.getMessage());
					break;
				case INVALID:
					System.out.println("ERROR: Invalid input!\nTo start new game, select 'New Game'\nTo quit, select 'Quit'"
							+ "\nOtherwise, write a letter or word to make a guess");
					break;
			}
		}
	}
	
    private class Printer implements HandleServerResponse {

		@Override
		public void handleResponse(String response) {
			System.out.println(response);
		}
    }
}
