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
public enum MessageType {
        //to make a connection to the server
	CONNECT, 
        //disconnecting from the server
	DISCONNECT, 
        //when a character is entred
	LETTER, 
        //when a full word is entered
	WORD, 
        //starting a new game
	NEWGAME, 
        //invalid input by the user
	INVALID
}
