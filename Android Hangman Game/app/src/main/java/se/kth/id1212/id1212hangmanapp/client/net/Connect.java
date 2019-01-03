package se.kth.id1212.id1212hangmanapp.client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Connect the client to the server 
 */
public class Connect {
	
	private static Socket socket;
    private static PrintWriter toServer;
    private static BufferedReader fromServer;
	

	public void connect(String host, int port, HandleServerResponse handleServerResponse) throws UnknownHostException, IOException {
		socket = new Socket(host, 8085);
		boolean autoflush = true;
		
		toServer = new PrintWriter(socket.getOutputStream(), autoflush);		
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        //Creates a new receiver thread that listens for incoming messages from the server. 
        new Thread(new Listener(handleServerResponse)).start();
	}
	
	public void disconnect(String quitMessage) throws IOException{
		toServer.println(quitMessage);
		socket.close();
        socket = null;
	}

	public void sendMessage(String message){

		toServer.println(message);
	}
	
	public class Listener implements Runnable{
		
		private final HandleServerResponse handleServerResponse;
		
		private Listener(HandleServerResponse handleResponse) {
			this.handleServerResponse = handleResponse;
		}

		@Override
		public void run() {
			while(true) {
				try {
					handleServerResponse.handleResponse(fromServer.readLine());
				} catch (Exception e) {
					handleServerResponse.handleResponse("Server disconnected");
					break;
				}
			}		
		}	
	}
}
