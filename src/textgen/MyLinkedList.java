package textgen;

import java.util.AbstractList;


/** A class that implements a doubly linked list
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	int size;

	/** Create a new empty LinkedList */
	public MyLinkedList() {
		// TODO: Implement this method
		size = 0;
		
		head = getNewNode(null, null, null);
		tail = getNewNode(null, null, null);
		
		head.next = tail;
		tail.prev = head;
		
	}
	
	private LLNode<E> getNewNode(LLNode<E> prev, LLNode<E> next, E data) {
		LLNode tmp = new LLNode<E>(data);
		tmp.prev = prev;
		tmp.next = next;
		
		// set next and prev. element's links
		if (prev != null ) {
			prev.next = tmp;
		}
		if (next != null) {
			next.prev = tmp;
		}
		return tmp;
	}
	
	private boolean isIndexValid(int index) {
		if ( index < 0 || index >= size) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element ) 
	{
		if (element == null) {
			throw new NullPointerException();
		}
		
		// we need to add at the end - where is the end?
		LLNode currLast = tail.prev;
		
		// create node
		LLNode newNode = getNewNode(currLast, tail, element);
		
		// increase size
		size++;
		
		//System.out.println("Add element - size now " + size);
		
		return true;
	}

	/** Get the element at position index 
	 * @throws IndexOutOfBoundsException if the index is out of bounds. */
	public E get(int index) 
	{
		LLNode tmp = getNode(index);
		return (E) tmp.data;
	}
	
	// get node - throw and error on problems
	private LLNode getNode(int index) 
	{
		// check if index is valid
		if (!isIndexValid(index)){
			throw new IndexOutOfBoundsException();
		} else {
			//System.out.println("- Get Element 0");
			// set first element
			LLNode tmp = head.next;
			// walk the nodes
			for (int i = 0; i < index; i++) {
				// go to the next node
				tmp = tmp.next;
				//System.out.println("- Get Next Element " + i);				
			}
			return tmp;
		}
	}	

	/**
	 * Add an element to the list at the specified index
	 * @param The index where the element should be added
	 * @param element The element to add
	 */
	public void add(int index, E element ) 
	{
		if (element == null) {
			throw new NullPointerException();
		}
		
		if (size == index) {
			// add to the end
			add(element);
		} else if (!isIndexValid(index)){
			throw new IndexOutOfBoundsException();
		} else {
		
			// we need to add the new element
			LLNode currElementThere = getNode(index);
			
			// create node
			LLNode newNode = getNewNode(currElementThere.prev, currElementThere, element);
			
			// increase size
			size++;
		}
	}


	/** Return the size of the list */
	public int size() 
	{
		// TODO: Implement this method
		return size;
	}

	/** Remove a node at the specified index and return its data element.
	 * @param index The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 * 
	 */
	public E remove(int index) 
	{
		LLNode currElementThere = getNode(index);
		
		currElementThere.prev.next = currElementThere.next;
		currElementThere.next.prev = currElementThere.prev;
		
		// decrease size
		size--;
		
		E currentData = (E) currElementThere.data;
		
		currElementThere = null;
		
		return currentData;
	}

	/**
	 * Set an index position in the list to a new element
	 * @param index The index of the element to change
	 * @param element The new element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public E set(int index, E element) 
	{
		if (element == null) {
			throw new NullPointerException();
		}	
		
		// find the element
		LLNode currElementThere = getNode(index);
		
		// read old data
		E currentData = (E) currElementThere.data;
		
		// set new data
		currElementThere.data = element;
		
		return currentData;
	}
}

class LLNode<E> 
{
	LLNode<E> prev;
	LLNode<E> next;
	E data;

	// TODO: Add any other methods you think are useful here
	// E.g. you might want to add another constructor

	public LLNode(E e) 
	{
		this.data = e;
		this.prev = null;
		this.next = null;
	}

}
