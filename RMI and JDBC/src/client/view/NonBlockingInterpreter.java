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
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

import javax.security.auth.login.AccountException;

import common.Client;
import common.Credentials;
import common.Server;
import server.integration.DatabaseAccess;
import server.model.FileObject;
import server.model.User;

public class NonBlockingInterpreter implements Runnable {
	
	private Server server;
	private Boolean receiveCommands = false;
	private Scanner console = new Scanner(System.in);
	private String clientName;		// unique for a user
	private Client remoteObject;
	private String username;
	private String password;
	private String fileName;
	private int fileSize;
	private String fileOwner;
	private String fileAccess;
	private String filePermission;
	private String fileNotification;
	private FileObject file;
	private List<FileObject> files;
	private boolean isLoggedIn = false;
	private Credentials credentials;
	private Boolean credentialCheck;

    public NonBlockingInterpreter() throws RemoteException {
    	
    	remoteObject = new ConsoleOutput();
    }
    
    //start the interpreter that waits for user input
    public void start(Server server) {
    	
    	this.server = server;
    	
        if (receiveCommands) {
            return;
        }
        receiveCommands = true;
        new Thread(this).start();
    }
	
	//interprets and performs user commands
	@Override
	public void run() {
		
		while(true) {
			
			System.out.println("What do you want to do? register/unregister/login/logout/upload/download/update/delete");
			
			String clientInput = console.nextLine();
			
			switch(clientInput) {
			
				case "register":
					
					System.out.println("Register username:");
					username = console.nextLine();
					System.out.println("Register password:");
					password = console.nextLine();
					
				//	credentials = new Credentials(username, password);
					
				try {
					server.register(username, password);
				} 
				catch (RemoteException | AccountException e2) {

					e2.printStackTrace();
				}
				
				System.out.println("User: " + username + " has been registered");
					
				break;
				
				case "unregister":
					
					System.out.println("Enter username to unregister:");
					username = console.nextLine();
					System.out.println("Enter password to unregister:");
					password = console.nextLine();
					
				try {
					server.unregister(new Credentials (username, password));
				} 
				catch (RemoteException e2) {

					e2.printStackTrace();
				}
					
					System.out.println("User " + username + " has been unregistered");
					
					break;
				
				case "login":
					
					System.out.println("Enter username:");
					username = console.nextLine();
					System.out.println("Enter password:");
					password = console.nextLine();
					
					credentials = new Credentials(username, password);
					
				try {
					credentialCheck = server.credentialCheck(credentials);
				} 
				catch (RemoteException e2) {

					e2.printStackTrace();
				}
					
					if (!credentialCheck) {
						
						System.out.println("Wrong username or password");
					}
					else {
						
					try {
						clientName = server.login(remoteObject, credentials);
					} 
					catch (RemoteException e) {
						e.printStackTrace();
					}
					
					isLoggedIn = true;
					System.out.println("User: " + clientName + " is logged in");
					
					}
					
				break;	
				
				case "logout":
				
				try {
					//receiveCommands = false;
					server.logout(clientName);
				} 
				catch (RemoteException e) {
					e.printStackTrace();
				}
				
				isLoggedIn = false;
				System.out.println("User: " + clientName + " is logged out");
				
				break;
				
				case "upload":
					
					if (isLoggedIn == false) {	
						System.out.println("User needs to be logged in to upload");
						break;
					}
					
					System.out.println("Enter the file name:");
					fileName = console.nextLine();
					System.out.println("Enter the file size:");
					fileSize = console.nextInt();
					console.nextLine();
					System.out.println("Enter the file owner:");
					fileOwner = console.nextLine();
					System.out.println("Enter the file access, public/private:");
					fileAccess = console.nextLine();
					System.out.println("Enter the file permission, read/write:");
					filePermission = console.nextLine();
					System.out.println("Do you want to recevie notifications for this file?. yes/no");
					fileNotification = console.nextLine();
					
					file = new FileObject(fileName, fileSize, fileOwner, fileAccess, filePermission, fileNotification);
					
				try {
					server.upload(file);
				} 
				catch (RemoteException e) {
					e.printStackTrace();
				}
				
				System.out.println("File: " + fileName + " has been uploaded");
				
				break;
				
				case "download":
					
					if (isLoggedIn == false) {	
						System.out.println("User needs to be logged in to download");
						break;
					}
					
					System.out.println("Enter file name: ");
					fileName = console.nextLine();
					
				try {
					file = server.download(fileName, clientName);
				} 
				catch (RemoteException e) {
					e.printStackTrace();
				}
				
				if (!file.getOwner().equals(clientName) && file.getAccess().equals("private")) {
					
					System.out.println("You do not have access to this file");
				}
				else {
					System.out.println(file.toString());
				}
				
				break;
				
				case "update":
					
					if (isLoggedIn == false) {	
						System.out.println("User needs to be logged in to update");
						break;
					}
					
					System.out.println("Enter the file name:");
					fileName = console.nextLine();
					
				try {
					file = server.download(fileName, clientName);
				} 
				catch (RemoteException e1) {
					
					e1.printStackTrace();
				}
					
					if (!file.getOwner().equals(clientName) && file.getAccess().equals("private")) {
					
					System.out.println("You do not have access to this file");
					
					}
					
					else if (!file.getOwner().equals(clientName) && file.getAccess().equals("public") && file.getPermission().equals("read")) {
						
						System.out.println("You do not have write permissions to this file");
					}
					
					else {
					
					System.out.println("Enter the file size:");
					fileSize = console.nextInt();
					console.nextLine();
					System.out.println("Enter the file owner:");
					fileOwner = console.nextLine();
					System.out.println("Enter the file access, public/private:");
					fileAccess = console.nextLine();
					System.out.println("Enter the file permission:");
					filePermission = console.nextLine();
					System.out.println("Do you want to recevie notifications for this file?. yes/no");
					fileNotification = console.nextLine();
					
					
					file = new FileObject(fileName, fileSize, fileOwner, fileAccess, filePermission, fileNotification);
					
				try {
					server.update(file, clientName);
				} 
				catch (RemoteException e) {
					e.printStackTrace();
				}
				
				System.out.println("File: " + fileName + " has been updated");
				
					}
				
				break;
				
				case "delete":
					
					if (isLoggedIn == false) {	
						System.out.println("User needs to be logged in to delete");
						break;
					}
					
					System.out.println("Enter filename of the file you wish to delete");
					fileName = console.nextLine();
					
					try {
						file = server.download(fileName, clientName);
					} 
					catch (RemoteException e1) {
						
						e1.printStackTrace();
					}
						
						if (!file.getOwner().equals(clientName)) {
						
						System.out.println("You cannot delete this file");
						
						}
						
						else {
					
						try {
							server.delete(fileName);
						} 
						catch (RemoteException e) {
							e.printStackTrace();
						}
						
						System.out.println("File " + fileName + " has been deleted");
					
						}
						break;
					
					//list all files
					case "list":	
						
						try {
							files = server.list();
						} 
						catch (RemoteException e) {
							e.printStackTrace();
						}
						
						System.out.println("Existing files:");
						
						for(FileObject file : files) {
							
							System.out.println(file.toString());
						}
						
						break;	
			}
		}
	}
	
	// client retrieves the remote reference to the Controller
   private void lookupServer(String host) throws NotBoundException, MalformedURLException, RemoteException {
    	
    	//find the server registry, the caller remote object defined in Main
    	//returns the Controller
    	server = (Server) Naming.lookup("//" + host + "/" + Server.SERVER_REGISTRY_NAME);
	}
    
    //class for printing to the console for the client to see
    private class ConsoleOutput extends UnicastRemoteObject implements Client {

        public ConsoleOutput() throws RemoteException {
        }

		@Override
		public void receiveMessage(String message) throws RemoteException {
			
			System.out.println(message);	
		}
    }
}
