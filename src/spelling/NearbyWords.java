/**
 * 
 */
package spelling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


/**
 * @author UC San Diego Intermediate MOOC team
 *
 */
public class NearbyWords implements SpellingSuggest {
	// THRESHOLD to determine how many words to look through when looking
	// for spelling suggestions (stops prohibitively long searching)
	// For use in the Optional Optimization in Part 2.
	private static final int THRESHOLD = 1000; 

	Dictionary dict;

	public NearbyWords (Dictionary dict) 
	{
		this.dict = dict;
	}

	/** Return the list of Strings that are one modification away
	 * from the input string.  
	 * @param s The original String
	 * @param wordsOnly controls whether to return only words or any String
	 * @return list of Strings which are nearby the original string
	 */
	public List<String> distanceOne(String s, boolean wordsOnly )  {
		   List<String> retList = new ArrayList<String>();
		   insertions(s, retList, wordsOnly);
		   substitution(s, retList, wordsOnly);
		   deletions(s, retList, wordsOnly);
		   return retList;
	}

	
	/** Add to the currentList Strings that are one character mutation away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void substitution(String s, List<String> currentList, boolean wordsOnly) {
		// for each letter in the s and for all possible replacement characters
		for(int index = 0; index < s.length(); index++){
			for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
				// use StringBuffer for an easy interface to permuting the 
				// letters in the String
				StringBuffer sb = new StringBuffer(s);
				sb.setCharAt(index, (char)charCode);

				// if the item isn't in the list, isn't the original string, and
				// (if wordsOnly is true) is a real word, add to the list
				if(!currentList.contains(sb.toString()) && 
						(!wordsOnly||dict.isWord(sb.toString())) &&
						!s.equals(sb.toString())) {
					currentList.add(sb.toString());
				}
			}
		}
	}
	
	/** Add to the currentList Strings that are one character insertion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void insertions(String s, List<String> currentList, boolean wordsOnly ) {
		// loop through letters and insert at index or append
		for(int index = 0; index <= s.length(); index++){
			// try to insert from a-z
			for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {

				// use StringBuffer for an easy interface to permuting the 
				// letters in the String
				StringBuffer sb = new StringBuffer(s);
				// if index = s.length() -> append
				sb.insert(index, (char)charCode);

				// if the item isn't in the list, isn't the original string, and
				// (if wordsOnly is true) is a real word, add to the list
				
				/*if(!currentList.contains(sb.toString()) && 
						(!wordsOnly||dict.isWord(sb.toString())) ) {
					currentList.add(sb.toString());
				}*/
				
				addWordToList(sb.toString(), currentList, wordsOnly);
			}
		}
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void deletions(String s, List<String> currentList, boolean wordsOnly ) {
		// for loop through letters
		for(int index = 0; index < s.length(); index++){
			// remove letter at position index
			StringBuffer sb = new StringBuffer(s);
			// if index = s.length() -> append
			sb.deleteCharAt(index);
			addWordToList(sb.toString(), currentList, wordsOnly);
		}
		
	}
	
	/** add string to list if it's not there, but check if it's a word if the 
	 * param wordsOnly is true  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */	
	private void addWordToList(String s, List<String> currentList, boolean wordsOnly) {
		if(!currentList.contains(s)) {
			if	(wordsOnly) {
				if (dict.isWord(s) ) {
					currentList.add(s);
				}
			} else {
				currentList.add(s);
			}
		}		
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param word The misspelled word
	 * @param numSuggestions is the maximum number of suggestions to return 
	 * @return the list of spelling suggestions
	 */
	@Override
	public List<String> suggestions(String word, int numSuggestions) {
		// Input:  word for which to provide number of spelling suggestions
		// Input:  number of maximum suggestions to provide
		// Output: list of spelling suggestions
		
		// initial variables
		// - Create a queue to hold words to explore
		// - Create a visited set to avoid looking at the same String repeatedly
		// - Create list of real words to return when finished		
		
		List<String> queue = new LinkedList<String>();     // String to explore
		HashSet<String> visited = new HashSet<String>();   // to avoid exploring the same  
														   // string multiple times
		List<String> retList = new LinkedList<String>();   // words to return
		 
		// optimization: skip words not in dict
		boolean optimize = false;
		
		// insert first node
		// - Add the initial word to the queue and visited
		queue.add(word);
		visited.add(word);		
		
		while(queue.size()>0 && retList.size() < numSuggestions) {
			// get 1st word from queue
			String curr = queue.remove(0);
			
			// get list of neighbors
			List<String> neighbors = distanceOne(curr, true);
			
			// for each n in the list of neighbors
			for(String n : neighbors) {
				// if n is not visited
				if (!visited.contains(n)){
					// add to list of visited & back of the queue
					visited.add(n);
					if (optimize && dict.isWord(n)) {
						// check if it's a word and add
						queue.add(n);
					} else {
						// just add
						queue.add(n);
					}
					
					// if n is a word in the dictionary
					if (dict.isWord(n)) {
						// add n to the list of words to return
						retList.add(n);
					}
				}
			}
		}
		
		// return the list of real words
		return retList;

	}	

   public static void main(String[] args) {
	   /* basic testing code to get started */
	   String word = "i";
	   // Pass NearbyWords any Dictionary implementation you prefer
	   Dictionary d = new DictionaryHashSet();
	   DictionaryLoader.loadDictionary(d, "data/dict.txt");
	   NearbyWords w = new NearbyWords(d);
	   List<String> l = w.distanceOne(word, true);
	   System.out.println("One away word Strings for for \""+word+"\" are:");
	   System.out.println(l+"\n");

	   word = "tailo";
	   List<String> suggest = w.suggestions(word, 10);
	   System.out.println("Spelling Suggestions for \""+word+"\" are:");
	   System.out.println(suggest);
	   
   }

}
