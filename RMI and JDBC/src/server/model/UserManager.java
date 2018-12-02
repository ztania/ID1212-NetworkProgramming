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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import common.Client;
import common.Credentials;

// keeps track of all users of the file system
public class UserManager {
	
	//holds all the users identified by their username
	private List<User> users = new ArrayList<>();
	
	//to add a user in the list
    public String addUser(Client remoteNode, Credentials credentials) {
    	
    	String clientName = credentials.getUsername();
    	
    	User user = new User(clientName, remoteNode, this);
    	users.add(user);
    	
    	return clientName;
    }
    
    //to remove a user from the list
    public void removeUser(String clientName) {
    	
    	Iterator<User> iter = users.iterator();
    	
    	while(iter.hasNext())
    		 
    		 if (iter.next().equals(clientName)) {
    			 iter.remove();
    		 }
    }
    
    //notifies the owner
    public void sendNotification(String clientName, String client, String action) {
    	
    	for (User user : users) {
    		
    		System.out.println(user.getUsername());
    		
    		if (user.getUsername().equals(clientName)) {
    			
    			user.send( client + " accessed your file, action taken: " + action);
    		}
    	}
    }
}

