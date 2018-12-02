/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author Tania
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

//remote methods server can call on the client
public interface Client extends Remote{
	
	
	void receiveMessage(String message) throws RemoteException;

}

