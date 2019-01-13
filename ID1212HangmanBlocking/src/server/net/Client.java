package server.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import server.controller.ServerController;

/**
 * This class is what the client threads will perform when they are started by the server. 
 */
public class Client implements Runnable{

	private Socket socket;
	private boolean connected;
	private BufferedReader fromClient;
	private PrintWriter toClient;
	private ServerController controller = new ServerController();
	private String responseFromServer;

	/**
	 * Constructor for the Client
	 * @param socket The socket on which the client communicates with the server 
	 */
	Client(Socket socket){
		this.socket = socket;
		connected = true;
	}

	@Override
	public void run() {
		try {
			boolean autoFlush = true;
			fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toClient = new PrintWriter(socket.getOutputStream(), autoFlush);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while(connected){
			try {
				String userInputFromClient = fromClient.readLine();
				responseFromServer = controller.handleUserInput(userInputFromClient);
				if (userInputFromClient.contains("Quit")) finalize();
				else toClient.println(responseFromServer);
			} catch (IOException e) {
				System.out.println("client disconnected");
				connected = false;
			}
		}
	}
	
	/**
	 * Closes the socket 
	 */
	protected void finalize() {
        try {
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        connected = false;
    }
	

}
