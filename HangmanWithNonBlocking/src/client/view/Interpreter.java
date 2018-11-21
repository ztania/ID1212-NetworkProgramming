/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

/**
 *
 * @author Tania
 */
import common.UserInput;
import java.io.IOException;
import java.util.Scanner;
import client.net.ServerConnect;
import client.net.HandleServerResponse;


public class Interpreter implements Runnable{
    
	
	private ServerConnect serverConnector;
	private boolean isReceivingInput = false;
	Scanner scanner = new Scanner(System.in);

	public void start() {
		if (isReceivingInput) {
			return;
		}
		isReceivingInput = true;
		serverConnector = new ServerConnect();
		new Thread(this).start();
		
	}
        
@Override
	public void run() {
		while (isReceivingInput) {
		UserInput input = new UserInput(scanner.nextLine());	
			try {
				switch(input.getInputType()) {
					case CONNECT:
						serverConnector.connect(input.getHost(), input.getPort(), input.getMessage(), new Printer());
						break;
					case DISCONNECT:
						isReceivingInput = false;
						scanner.close();
						serverConnector.disconnect(input.getMessage());
						break;
					case LETTER:
						serverConnector.sendLetter(input.getLetter());
						break;
					case WORD:
						serverConnector.sendWord(input.getWord());
						break;
					case NEWGAME:
						serverConnector.newGame(input.getMessage());
						break;
					case INVALID:
						System.out.println("ERROR: Invalid input!\nTo start new game, select 'New Game'\nTo quit, select 'Quit'"
								+ "\nOtherwise, write a letter or word to make a guess");
						break;
				}
			} catch (IOException e) {
				
				e.printStackTrace();
                                
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

