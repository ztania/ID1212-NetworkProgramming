/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

/**
 *
 * @author Tania
 */
import java.rmi.RemoteException;

import common.Client;

//The individual of the file system and holds the data related to client

public class User {
	
	private Client remoteNode;

	private String username;
	private String password;//unique for the user
	private UserManager userManager;
	
	public User(String username, Client remoteNode, UserManager userManager) {
		
		this.username = username;
		this.remoteNode = remoteNode;
		this.userManager = userManager;
	}
	
	public User(String username, Client remoteNode, String password) {
		
		this.username = username;
		this.password = password;
		this.remoteNode = remoteNode;
	
	}
	public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
	public void send(String notification) {
		
		try {
			remoteNode.receiveMessage(notification);
		} 
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

}

