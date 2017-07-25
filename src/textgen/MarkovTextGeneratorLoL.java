package textgen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * An implementation of the MTG interface that uses a list of lists.
 * @author UC San Diego Intermediate Programming MOOC team 
 */
public class MarkovTextGeneratorLoL implements MarkovTextGenerator {

	// The list of words with their next words
	private List<ListNode> wordList; 
	
	// The starting "word"
	private String starter;
	
	// The random number generator
	private Random rnGenerator;
	
	public MarkovTextGeneratorLoL(Random generator)
	{
		wordList = new LinkedList<ListNode>();
		starter = "";
		rnGenerator = generator;
	}
	
	
	/** Train the generator by adding the sourceText */
	@Override
	public void train(String sourceText)
	{
		// get all words
		List<String> words = getWords(sourceText);
		
		System.out.println("List size: " + words.size());
		
		String starterWord; //hmm... why would this be global?
		String prevWord;
		String currWord;
		
		if (words.size()>0) {
			
			starterWord = words.get(0);
			prevWord = starterWord;
			
			for(int i = 1; i < words.size(); i++) {
				// get word into var., so it's easier to read code
				currWord = words.get(i);
				//System.out.println("Check: " + prevWord + " " + currWord);
				// check if we have the prev. word
				/*if (!wordList.contains(prevWord)) {
					// add node for prev. word
					//System.out.println("- we add: " + prevWord);
					ListNode node = new ListNode(prevWord);
					wordList.add(node);
				}*/
				// add next word to the list
				//System.out.println("- find prev word: " + prevWord);
				//System.out.println("- find position: " + indexOfWord(prevWord));
				
				addNextWordToWord(prevWord, currWord);
				
				// set prevWord 
				prevWord = currWord;
			}
			
			// add starter to be the next word for the last word
			/*if (!wordList.contains(prevWord)) {
				// add node for prev. word
				//System.out.println("- we add: " + prevWord);
				ListNode node = new ListNode(prevWord);
				wordList.add(node);
			}*/
			//wordList.get(indexOfWord(prevWord)).addNextWord(starterWord);
			
			addNextWordToWord(prevWord, starterWord);

			
			// almost forgot about this one
			this.starter = starterWord;
			System.out.println("Trained.");
			
		} else {
			// throw warning?
			System.out.println("List is empty!");
			System.out.println(sourceText);
		}
		
		
	}
	
	private void addNextWordToWord(String prevWord, String currWord) {
		if (indexOfWord(prevWord) == -1) {
			// add node for prev. word
			//System.out.println("- we add: " + prevWord);
			ListNode node = new ListNode(prevWord);
			wordList.add(node);
		}
		// re-check
		if (indexOfWord(prevWord)>=0) {
			wordList.get(indexOfWord(prevWord)).addNextWord(currWord);
		} else {
			// ops
			System.out.println("ERROR! Details:");
			for (ListNode word : wordList) {
				System.out.print(word.toString() );
			}
			System.out.println("Will exit.");
			throw new IndexOutOfBoundsException();
		}		
	}
	
	/** 
	 * Generate the number of words requested.
	 */
	@Override
	public String generateText(int numWords) {
	    /* 
set "currWord" to be the starter word
set "output" to be ""
add "currWord" to output
while 
you need more words
find the "node" corresponding to "currWord" in the list
select a random word "w" from the "wordList" for "node"
add "w" to the "output"
set "currWord" to be "w" 
increment number of words added to the lis	     
	     */
		
		String output = "";
		
		if (wordList.size()>0 && numWords>0) {
			// set curr to starter
			String currWord = starter;
				
			output += currWord + " ";
			// repeat for the num of words needed
			for (int i = 1; i < numWords; i++) {
				String newWord = getRandomWord(currWord);
				output += newWord + " ";
				currWord = newWord;
			}
		}
		
		return output;
	}
	
	// find the "node" corresponding to "currWord" in the list
	// select a random word "w" from the "wordList" for "node"
	private String getRandomWord(String currWord) {
		String newWord = "";
		// find node
		ListNode node = wordList.get(indexOfWord(currWord));
		// call the generator method from the node
		newWord = node.getRandomNextWord(rnGenerator);
		return newWord;
	}
	
	
	// Can be helpful for debugging
	@Override
	public String toString()
	{
		String toReturn = "";
		for (ListNode n : wordList)
		{
			toReturn += n.toString();
		}
		return toReturn;
	}
	
	/** Retrain the generator from scratch on the source text */
	@Override
	public void retrain(String sourceText)
	{
		wordList = new ArrayList<ListNode>();;
		train(sourceText);
	}
	
	// TODO: Add any private helper methods you need here.
	private int indexOfWord(String word) {
		
		for(int i = 0; i < wordList.size(); i++) {
			if (wordList.get(i).getWord().equals(word)) {
				return i;
			}
		}
		
		return -1;
	}

	
	/** Returns the tokens that match the regex pattern from the document 
	 * text string.
	 * @param pattern A regular expression string specifying the 
	 *   token pattern desired
	 * @return A List of tokens from the document text that match the regex 
	 *   pattern
	 */
	private List<String> getWords(String text)
	{
		String pattern = "[a-zA-Z']+[!?.,]*";
		ArrayList<String> tokens = new ArrayList<String>();
		Pattern tokSplitter = Pattern.compile(pattern);
		Matcher m = tokSplitter.matcher(text);
		
		while (m.find()) {
			tokens.add(m.group());
		}
		
		return tokens;
	}
	
	
	/**
	 * This is a minimal set of tests.  Note that it can be difficult
	 * to test methods/classes with randomized behavior.   
	 * @param args
	 */
	public static void main(String[] args)
	{
		// feed the generator a fixed random value for repeatable behavior
		MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));
		String textString = "Hello.  Hello there.  This is a test.  Hello there.  Hello Bob.  Test again.";
		System.out.println(textString);
		gen.train(textString);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
		String textString2 = "You say yes, I say no, "+
				"You say stop, and I say go, go, go, "+
				"Oh no. You say goodbye and I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"I say high, you say low, "+
				"You say why, and I say I don't know. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"Why, why, why, why, why, why, "+
				"Do you say goodbye. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"You say yes, I say no, "+
				"You say stop and I say go, go, go. "+
				"Oh, oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello,";
		System.out.println(textString2);
		gen.retrain(textString2);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
	}

}

/** Links a word to the next words in the list 
 * You should use this class in your implementation. */
class ListNode
{
    // The word that is linking to the next words
	private String word;
	
	// The next words that could follow it
	private List<String> nextWords;
	
	ListNode(String word)
	{
		this.word = word;
		nextWords = new LinkedList<String>();
	}
	
	public String getWord()
	{
		return word;
	}

	public void addNextWord(String nextWord)
	{
		nextWords.add(nextWord);
	}
	
	public String getRandomNextWord(Random generator)
	{
		// TODO: Implement this method
	    // The random number generator should be passed from 
	    // the MarkovTextGeneratorLoL class
		int randomInt = generator.nextInt(nextWords.size());
	    return nextWords.get(randomInt);
	}

	public String toString()
	{
		String toReturn = word + ": ";
		for (String s : nextWords) {
			toReturn += s + "->";
		}
		toReturn += "\n";
		return toReturn;
	}
	
}


