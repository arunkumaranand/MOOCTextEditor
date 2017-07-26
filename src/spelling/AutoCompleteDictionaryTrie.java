package spelling;

import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author You
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;

    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
	}
	
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should convert the 
	 * string to all lower case before you insert it. 
	 * 
	 * This method adds a word by creating and linking the necessary trie nodes 
	 * into the trie, as described outlined in the videos for this week. It 
	 * should appropriately use existing nodes in the trie, only creating new 
	 * nodes when necessary. E.g. If the word "no" is already in the trie, 
	 * then adding the word "now" would add only one additional node 
	 * (for the 'w').
	 * 
	 * @return true if the word was successfully added or false if it already exists
	 * in the dictionary.
	 */
	public boolean addWord(String word)
	{
		boolean ret = false;
		
	    // convert to lower case
	    String myWord = word.toLowerCase();
	    //System.out.println("");
	    //System.out.println("Word: " + myWord);
	    
	    //if (!isWord(myWord)) {
	    	
	    	TrieNode node = root;
	    	for(int i = 0; i<myWord.length(); i++) {
	    		// get char at position i
	    		char c = myWord.charAt(i);
	    		//System.out.println("" + i + ") Check char. " + c);
	    		
	    		// does the node have a pointer for this character
	    		TrieNode child = node.getChild(c);
	    		
	    		if (child == null) {
	    			// no child there, we need to add it
	    			//System.out.println("" + i + "  we need to add it" );
	    			
	    			/* ret. node if c not in the trie.  If it was, ret. null */
	    			child = node.insert(c);
	    			if (child == null) {
	    				// node is already there
	    				// ops, we have a problem ... guru meditation error
	    				//return false;
	    			}
	    			//System.out.println("" + i + "  we added it ... OK" );
	    			// new node -> it means we are adding
	    			ret = true;
	    		} else {
	    			//System.out.println("" + i + "  we have it ... OK" );
	    		}
	    		
	    		// set the new root to the child
	    		node = child;
	    	}
	    	
	    	// set the ends word flag of the last node to true
	    	node.setEndsWord(true);
	    	
	    //}
	    return ret;
	}
	
	
	
	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size()
	{
		// get all words ... all tries that have endsword flag set to true
		double size = getNumWordsForNode(root);
		
	    return (int)size;
	}
		
 	/** find all words from node down */
 	public double getNumWordsForNode(TrieNode curr)
 	{
 		double numWords = 0;
 		
 		if (curr == null) 
 			return numWords;
 		
 		//System.out.println("getNumWordsForNode: " + curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			// if it's a word increment counter
 			if (next.endsWord()) {
 				numWords++;
 			}
 			// find words for subnodes
 			numWords += getNumWordsForNode(next);
 		}
 		
 		return numWords;
 	}	
	
	
	/** Returns whether the string is a word in the trie, using the algorithm
	 * described in the videos for this week. */
	@Override
	public boolean isWord(String word) 
	{
		boolean found = false;
		String myWord = word.toLowerCase();
		/*System.out.println("");
		System.out.println("isWord: Looking for word: " + myWord );*/
		
		TrieNode node = root;
    	for(int i = 0; i<myWord.length(); i++) {
    		// get char at position i
    		char c = myWord.charAt(i);
    		//System.out.println("isWord: " + i + ") Check char. " + c);
    		
    		// does the node have a pointer for this character
    		TrieNode child = node.getChild(c);
    		
    		if (child == null) {
    			//System.out.println("isWord: " + i + ") Not found!");
    			// exit now
    			return false;
    			
    		} else {
    			// ok, we have a child there ... we can continue
    			
    		}
    		
    		// set the new root to the child
    		node = child;
    	}
    	
    	if (node.endsWord() && node.getText().equals(myWord)) {
    		//System.out.println("isWord: Found word: " + node.getText() );
    		found = true;
    	} else {
    		//System.out.println("isWord: Not found, we are at word: " + node.getText() );
    		/*if (this.size() < 100) {
    			this.printTree();
    		}*/
    	}
		
		return found;
	}
	

	/** 
     * Return a list, in order of increasing (non-decreasing) word length,
     * containing the numCompletions shortest legal completions 
     * of the prefix string. All legal completions must be valid words in the 
     * dictionary. If the prefix itself is a valid word, it is included 
     * in the list of returned words. 
     * 
     * The list of completions must contain 
     * all of the shortest completions, but when there are ties, it may break 
     * them in any order. For example, if there the prefix string is "ste" and 
     * only the words "step", "stem", "stew", "steer" and "steep" are in the 
     * dictionary, when the user asks for 4 completions, the list must include 
     * "step", "stem" and "stew", but may include either the word 
     * "steer" or "steep".
     * 
     * If this string prefix is not in the trie, it returns an empty list.
     * 
     * @param prefix The text to use at the word stem
     * @param numCompletions The maximum number of predictions desired.
     * @return A list containing the up to numCompletions best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
		 // TODO: Implement this method
		 // This method should implement the following algorithm:
		 // 1. Find the stem in the trie.  If the stem does not appear in the trie, return an
		 //    empty list
		 // 2. Once the stem is found, perform a breadth first search to generate completions
		 //    using the following algorithm:
		 //    Create a queue (LinkedList) and add the node that completes the stem to the back
		 //       of the list.
		 //    Create a list of completions to return (initially empty)
		 //    While the queue is not empty and you don't have enough completions:
		 //       remove the first Node from the queue
		 //       If it is a word, add it to the completions list
		 //       Add all of its child nodes to the back of the queue
		 // Return the list of completions
    	 
    	List<String> completions = new LinkedList<String>();
    	if (numCompletions == 0) {
    		// we don't need completitions
    		return completions;
    	}
        
		String myWord = prefix.toLowerCase();
		System.out.println("predictCompletions: Looking for word: " + myWord );
		
		TrieNode node = root;
		for(int i = 0; i<myWord.length(); i++) {
			// get char at position i
			char c = myWord.charAt(i);
			//System.out.println("isWord: " + i + ") Check char. " + c);
			
			// does the node have a pointer for this character
			TrieNode child = node.getChild(c);
			
			if (child == null) {
				System.out.println("predictCompletions: " + i + ") Not found!");
				// exit now
				return completions;
				
			} else {
				// ok, we have a child there ... we can continue
				
			}
			
			// set the new root to the child
			node = child;
		}
		
		System.out.println("predictCompletions: found stem?, let's find words!");
		
		if (node != null) {
			//completions = getWordsForNode(node, completions, prefix, numCompletions);
			
			LinkedList<TrieNode> queue = new LinkedList<TrieNode>();
			queue.add(node);
			while(queue.size()>0) {
				//       remove the first Node from the queue
				node = queue.removeFirst();
				
				//       If it is a word, add it to the completions list
				if (node.endsWord()) {
					completions.add(node.getText());
					if (completions.size() >= numCompletions) {
						// ok, we have enough
						return completions;
					}
				}

				//       Add all of its child nodes to the back of the queue				
				TrieNode next = null;
		  		for (Character c : node.getValidNextCharacters()) {
		  			TrieNode child = node.getChild(c);
		  			queue.add(child);		  			
		  		}
			}
			
		}
		 
    	 
    	 
    	 
        return completions;
     }
          
     
     

 	// For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 			return;
 		
 		System.out.println(curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	

	
}