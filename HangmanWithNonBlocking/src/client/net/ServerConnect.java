/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.net;

/**
 *
 * @author Tania
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;


public class ServerConnect implements Runnable{

	private final ByteBuffer fromServerBuffer = ByteBuffer.allocateDirect(8192);
	
	private final Queue<ByteBuffer> messagesToSend = new ArrayDeque<>();
	private SocketChannel socketChannel;
	private Selector selector;
	private HandleServerResponse handleServerResponse;
	private String connectMessage;
	private InetSocketAddress serverAddress;
	private boolean connected;
	private volatile boolean isTimeToSend = false;


	public void connect(String host, int port, String connectMessage, HandleServerResponse handleServerResponse) {

		this.handleServerResponse = handleServerResponse;
		this.connectMessage = connectMessage;

		serverAddress = new InetSocketAddress(host, port);

		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			
			//Opening a socket channel
			socketChannel = SocketChannel.open();

			//By setting configureBlocking to false we are making the channel to be non-blocking
			socketChannel.configureBlocking(false);

			//Connecting to the specified host and port in the server
			socketChannel.connect(serverAddress);
			
			connected = true;

			//the selector  will be open  
			selector = Selector.open();
                        //the selector will be registered with the operation connect
			socketChannel.register(selector, SelectionKey.OP_CONNECT);

			while (connected || !messagesToSend.isEmpty()) {
				
                if (isTimeToSend) {
                    //allowing the selector to write
                    socketChannel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                    isTimeToSend = false;
                }

				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

				selector.select();
				while(keyIterator.hasNext()) {

					SelectionKey key = keyIterator.next();

					if (!key.isValid()) {
						continue;
					}
					if (key.isConnectable()) {
						socketChannel.finishConnect();
						addToMessageQueue(connectMessage);
					} 
					else if (key.isReadable()) {
						receiveFromServer(key);
					} 
					else if (key.isWritable()) {
						sendToServer(key);
					}
					keyIterator.remove();
				}
			}
		} catch (IOException e) {
			System.out.println("Server disconnected");
			try {
				finalize();
			} catch (IOException e1) {
				System.out.println("Could not Finalize");
			}
		}
	}

	public void sendLetter(String letter) {
		addToMessageQueue(letter);
	}

	public void sendWord(String word) {
		addToMessageQueue(word);
	}

	public void newGame(String message) {
		addToMessageQueue(message);
	}


	private void sendToServer(SelectionKey key) throws IOException{
		ByteBuffer msg;

		while ((msg = messagesToSend.peek()) != null) {
			socketChannel.write(msg);
			if (msg.hasRemaining()) {
				return;
			}
			messagesToSend.remove();
		}
		key.interestOps(SelectionKey.OP_READ);

	}

	private void receiveFromServer(SelectionKey key) throws IOException{
		fromServerBuffer.clear();
		int numOfReadBytes = socketChannel.read(fromServerBuffer);
		if (numOfReadBytes == -1) {
			throw new IOException("Lost Connection");
		}
		fromServerBuffer.flip();
		byte[] bytes = new byte[fromServerBuffer.remaining()];
		fromServerBuffer.get(bytes);
		String fromServer = new String(bytes);
		handleServerResponse.handleResponse(fromServer);
		key.interestOps(SelectionKey.OP_WRITE);
	}

	public void disconnect(String quitMessage) throws IOException{
		addToMessageQueue(quitMessage);
		finalize();
	}
	
	public void finalize() throws IOException {
		socketChannel.close();
		socketChannel.keyFor(selector).cancel();
		connected = false;
	}

	private void addToMessageQueue(String messageToSend) {

		messagesToSend.add(ByteBuffer.wrap(messageToSend.getBytes()));
		isTimeToSend = true;
		selector.wakeup();
	}
}

