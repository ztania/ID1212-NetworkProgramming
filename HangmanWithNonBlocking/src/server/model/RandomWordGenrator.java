/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
/**
 *
 * @author Tania
 */
public class RandomWordGenrator {
    

	/**
	 * Read data from a file words.txt and  store them in a array list
	 */
	public ArrayList<String> readFile(){
		
		ArrayList<String> words = new ArrayList<String>();
		
		Scanner wordFile = null;
		try {
			wordFile = new Scanner(new File("words.txt")).useDelimiter("\\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(wordFile.hasNext()) {
			words.add(wordFile.next());
		}
		
		return words;
	}
	
	/**
	 * randomly generates a word from the array list
         *The chosen word will be used in the Hangman Game for the user to guess.
	 */
	public String generateWord(ArrayList<String> words) {
		
		Random random = new Random();
		int upperLimit = words.size()-1;
		int lowerLimit = 0;
		int wordNumber = random.nextInt(upperLimit - lowerLimit) + lowerLimit;
		String chosenWord = words.get(wordNumber);
		
		return chosenWord;
	}
}

