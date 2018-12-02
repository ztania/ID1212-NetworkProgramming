/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.integration;

/**
 *
 * @author Tania
 */
public class DatabaseAccessException extends Exception{
	
    public DatabaseAccessException(String reason) {
        super(reason);
    }

   
    public DatabaseAccessException(String reason, Throwable rootCause) {
        super(reason, rootCause);
    }

}
