/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

/**
 *
 * @author Tania
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;


import javax.security.auth.login.AccountException;

import common.Client;
import common.Credentials;
import common.Server;
import server.integration.DatabaseAccess;
import server.integration.DatabaseAccessException;
import server.model.FileObject;
import server.model.User;
import server.model.UserManager;




public class Controller extends UnicastRemoteObject implements Server{

	private DatabaseAccess databaseAccess;
	private UserManager userManager;
	
	//UnicastRemoteObject constructor will be executed, it calls the superclass constructor
	public Controller() throws RemoteException, DatabaseAccessException {
        super();
        this.databaseAccess = new DatabaseAccess();
        this.userManager = new UserManager();
	}
	
	@Override
	public Boolean credentialCheck(Credentials credentials) {
		
		Boolean credentialCheck = databaseAccess.checkLogin(credentials);
		
		if (!credentialCheck) {
			return false;
		}
		else {
		return true;
		}
	}

	@Override
	public String login(Client remoteNode, Credentials credentials) throws RemoteException {
		
		String clientName = userManager.addUser(remoteNode, credentials);		
		return clientName;	
	}

	@Override
	public void logout(String clientName) throws RemoteException {
		
		userManager.removeUser(clientName);		
	}

	@Override 
	public void register(String holderName, String password) throws RemoteException, AccountException {
	        String acctExistsMsg = "Account for: " + holderName + " already exists";
	        String failureMsg = "Could not create account for: " + holderName;
	        try {
	            if (databaseAccess.findAccountByName(holderName) != null) {
	                throw new AccountException(acctExistsMsg);
	            }
	            databaseAccess.register(new User(holderName, password));
	        } catch (DatabaseAccessException ex) {
	            throw new AccountException(failureMsg);
	        }

	    }


	@Override
	public void unregister(Credentials credentials) throws RemoteException {
		
		databaseAccess.unregister(credentials);;
	}

	@Override
	public void upload(FileObject file) throws RemoteException {
		
		databaseAccess.uploadFile(file);
	}

	@Override
	public FileObject download(String fileName, String client) throws RemoteException {
		
		FileObject file = databaseAccess.downloadFile(fileName);
		
		if (file.getNotification().equals("yes")) {
			
			userManager.sendNotification(file.getOwner(), client, "view");
		}
		
		return file;
	}
	
	@Override
	public void update(FileObject file, String client) throws RemoteException {
		
		
		if (file.getNotification().equals("yes")) {
			
			userManager.sendNotification(file.getOwner(), client, "update");
		}
		
		databaseAccess.updateFile(file);
	}

	@Override
	public void delete(String fileName) throws RemoteException {
		
		FileObject file = databaseAccess.downloadFile(fileName);
		
		databaseAccess.deleteFile(fileName);
	}
	
	@Override
	public List<FileObject> list() throws RemoteException {
		
		List<FileObject> files = databaseAccess.listFiles();
		
		return files;
	}

	@Override
	public void sendMessage(int id, String message) throws RemoteException {
		// TODO Auto-generated method stub
		
	}


}

