package server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
		
    public static void main(String[] args) {
        Server server = new Server();
        server.acceptSockets();
    }

	/**
	 * Accepts incoming requests. Opens a listening socket on port 8085 and accepts connection requests on that socket.
	 * Then the socket is passed to startClientThreads, where the client is started. 
	 */
	private void acceptSockets() {
		ServerSocket listeningSocket;
		try {
			listeningSocket = new ServerSocket(8085);
			System.out.println("Server running...");

			while (true) {
				Socket clientSocket = listeningSocket.accept();
				startClientThread(clientSocket);
				System.out.println("Client connected...");

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the client to the list of clients and starts a client thread 
	 * @param socket The socket on which the server and client communicate
	 */
	private void startClientThread(Socket socket) {
		Client client = new Client(socket);
		Thread clientThread = new Thread(client);
		clientThread.setPriority(Thread.MAX_PRIORITY);
		clientThread.start();
	}
}
