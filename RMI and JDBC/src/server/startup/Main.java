/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.startup;

/**
 *
 * @author Tania
 */
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.controller.Controller;
import server.integration.DatabaseAccessException;

//this is the main class that starts the server and binds it in the RMIregistry
public class Main {
	
	public static void main(String[] args) throws DatabaseAccessException {
		
        try {
        	
            new Main().startRegistry();
        
            Naming.rebind(Controller.SERVER_REGISTRY_NAME, new Controller());
            System.out.println("Server is started");
            
        } catch (MalformedURLException | RemoteException ex) {
        	
            System.out.println("Could not start server.");
        }
	}
	
	//finds and starts the registry
    private void startRegistry() throws RemoteException {
        try {
        	
        	//returns a reference to a registry (the remote interface)
        	//list all objects that are already bound on the registry
        	LocateRegistry.getRegistry().list();
        } 
        
        catch (RemoteException noRegistryIsRunning) {
            
        	//If there is no registry starts the registry
        	
        	LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
    }
}
