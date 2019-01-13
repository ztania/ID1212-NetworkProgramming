package client.view;

public class UserInput {
	
	private String userInput;
	private String[] userArgs;
	
	public UserInput(String userInput) {
		this.userInput = userInput;
		splitUserInput(userInput);
	}
	
	public String getMessage() {
		return userInput;
	}
	
	InputType getInputType() {
		if(userInput.contains("Connect")) {
			return InputType.CONNECT;
		}
		else if(userInput.equals("Quit")) {
			return InputType.DISCONNECT;
		}
		else if(userInput.length() == 1 && !userInput.equals(" ")) {
			return InputType.LETTER;
		}
		else if(userInput.equals("New Game")) {
			return InputType.NEWGAME;
		}
		else if(userInput.length() > 1 && !userInput.contains(" ")){
			return InputType.WORD;
		}
		else return InputType.INVALID;
	}

	public String getHost() {
		String host;
		try {
			//Position 1 in the array contains the host
			host = userArgs[1];
		}
		catch(Exception e){
			host = "127.0.0.1";
		}
		return host;
	}

	public int getPort() {
		int port;
		try {
			//Position 2 in the array contains the port
			port = Integer.parseInt(userArgs[2]);
		}
		catch(Exception e){
			port = 8085;
		}
		return port;
	}

	public char getLetter() {
		return userInput.charAt(0);
	}

	public String getWord() {
		return userInput;
	}
	/**
	 * Splits the user input string. The string should have the format: "connect <host> <port>". 
	 */
	public void splitUserInput(String userInput) {
		userArgs = userInput.split(" ");
	}
}
