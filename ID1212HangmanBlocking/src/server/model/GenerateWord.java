package server.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Handles the choosing of a word for the hangman game 
 */
public class GenerateWord {
	
	
	/**
	 * Read the text file "words.txt" to get all the words and save them in an array
	 * @return An ArrayList with all words in the textfile
	 * @throws FileNotFoundException 
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
	 * Get a random word from the list of words
	 * @param words An ArrayList containing all possible words for the game 
	 * @return The chosen word that will be used in the game
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
