/**
 * 
 */
package textgen;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

/**
 * @author UC San Diego MOOC team
 *
 */
public class MyLinkedListTester {

	private static final int LONG_LIST_LENGTH =10; 

	MyLinkedList<String> shortList;
	MyLinkedList<Integer> emptyList;
	MyLinkedList<Integer> longerList;
	MyLinkedList<String> longerStringList;
	MyLinkedList<Integer> list1;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Feel free to use these lists, or add your own
	    shortList = new MyLinkedList<String>();
		shortList.add("A");
		shortList.add("B");
		//System.out.println("Shortlist: A, B");
		emptyList = new MyLinkedList<Integer>();
		longerList = new MyLinkedList<Integer>();

		for (int i = 0; i < LONG_LIST_LENGTH; i++)
		{
			longerList.add(i);
		}
		
		list1 = new MyLinkedList<Integer>();
		list1.add(65);
		list1.add(21);
		list1.add(42);
		
	
		
	}

	
	/** Test if the get method is working correctly.
	 */
	/*You should not need to add much to this method.
	 * We provide it as an example of a thorough test. */
	@Test
	public void testGet()
	{
		//test empty list, get should throw an exception
		try {
			emptyList.get(0);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
			//System.out.println("IndexOutOfBoundsException OK");
		}
		
		// test short list, first contents, then out of bounds
		//System.out.println("Check if 1st element = A");
		assertEquals("Check first", "A", shortList.get(0));
		//System.out.println("Check if 2nd element = B");
		assertEquals("Check second", "B", shortList.get(1));
		
		try {
			shortList.get(-1);
			fail("Check out of bounds low");
		}
		catch (IndexOutOfBoundsException e) {
			//System.out.println("IndexOutOfBoundsException OK 2");
		}
		try {
			shortList.get(2);
			fail("Check out of bounds high");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		// test longer list contents
		for(int i = 0; i<LONG_LIST_LENGTH; i++ ) {
			//System.out.println("Check long list element " + i);
			assertEquals("Check "+i+ " element", (Integer)i, longerList.get(i));
		}
		
		// test off the end of the longer array
		try {
			longerList.get(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			longerList.get(LONG_LIST_LENGTH);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		}
		
	}
	
	
	/** Test removing an element from the list.
	 * We've included the example from the concept challenge.
	 * You will want to add more tests.  */
	@Test
	public void testRemove()
	{
		int a = list1.remove(0);
		assertEquals("Remove: check a is correct ", 65, a);
		assertEquals("Remove: check element 0 is correct ", (Integer)21, list1.get(0));
		assertEquals("Remove: check size is correct ", 2, list1.size());
		
		// TODO: Add more tests here
		int b = longerList.remove(5);
		// we should have 0, 1, 2, 3, 4, (x 5), 6, 7, 8, 9
		assertEquals("Remove: check prev. element is correct ", (Integer)4, longerList.get(4) );
		assertEquals("Remove: check new element is correct ", (Integer)6, longerList.get(5) );
		assertEquals("Remove: check next element is correct ", (Integer)7, longerList.get(6) );
		assertEquals("Remove: check last element is correct ", (Integer)9, longerList.get(LONG_LIST_LENGTH-2) );
		assertEquals("Remove: check new size is correct ", LONG_LIST_LENGTH-1, longerList.size() );
		
		// out of bounds check on the last element
		try {
			longerList.remove(LONG_LIST_LENGTH-1);
			fail("Remove: Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
			//System.out.println("IndexOutOfBoundsException OK 3");
		}
		
		// out of bounds check on the last element
		try {
			longerList.remove(LONG_LIST_LENGTH);
			fail("Remove: Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
			//System.out.println("IndexOutOfBoundsException OK 3");
		}		
		// out of bounds check on the last element
		try {
			longerList.remove(-1);
			fail("Remove: Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
			//System.out.println("IndexOutOfBoundsException OK 3");
		}		
		
		// test remove all
		for (int i=longerList.size()-1; i>=0; i--) {
			longerList.remove(i);
		}
		
		// out of bounds check on the last element
		try {
			emptyList.remove(0);
			fail("Remove: Check out of bounds for emptyList");
		}
		catch (IndexOutOfBoundsException e) {
			//System.out.println("IndexOutOfBoundsException OK 3");
		}		
		
		// maybe i did too many tests, but for practice it's ok
	}
	
	/** Test adding an element into the end of the list, specifically
	 *  public boolean add(E element)
	 * */
	@Test
	public void testAddEnd()
	{
        int curSize = longerList.size();
		longerList.add(100);
		assertEquals("testAddEnd: check new element is correct ", (Integer)100, longerList.get(curSize) );
		assertEquals("testAddEnd: check size was updated ", curSize+1, longerList.size() );
		
		// out of bounds check on the last element
		try {
			longerList.get(longerList.size()+1);
			fail("testAddEnd: Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
			//System.out.println("IndexOutOfBoundsException OK");
		}
		
		// add null check on the last element
		try {
			longerList.add(null);
			fail("testAddEnd: Check add null");
		}
		catch (IndexOutOfBoundsException e) {
			//System.out.println("IndexOutOfBoundsException OK");
		}		
	}

	
	/** Test the size of the list */
	@Test
	public void testSize()
	{
		assertEquals("testSize: check longerList size is correct ", LONG_LIST_LENGTH, longerList.size() );
		assertEquals("testSize: check shortList size is correct ", 2, shortList.size() );
		assertEquals("testSize: check emptyList size is correct ", 0, emptyList.size() );
	}

	
	/** Test adding an element into the list at a specified index,
	 * specifically:
	 * public void add(int index, E element)
	 * */
	@Test
	public void testAddAtIndex()
	{
        
        int curSize = longerList.size();
        int curEl = longerList.get(0);
        
		longerList.add(0, 100);
		assertEquals("testAddAtIndex: check new element is correct ", (Integer)100, longerList.get(0) );
		assertEquals("testAddAtIndex: check size was updated ", curSize+1, longerList.size() );	
		assertEquals("testAddAtIndex: check old element is correct ", (Integer)curEl, longerList.get(1) );
		
		// out of bounds check on the last element
		try {
			longerList.add(-1, 100);
			fail("testAddAtIndex: Check out of bounds low");
		}
		catch (IndexOutOfBoundsException e) {
			//System.out.println("IndexOutOfBoundsException OK");
		}
		
		// out of bounds check on the last element
		try {
			longerList.add(longerList.size()+1, 100);
			fail("testAddAtIndex: Check out of bounds high");
		}
		catch (IndexOutOfBoundsException e) {
			//System.out.println("IndexOutOfBoundsException OK");
		}		
		
	}
	
	/** Test setting an element in the list */
	@Test
	public void testSet()
	{
		int curSize = longerList.size();
		
        int element3 = longerList.get(3);
        int element4 = longerList.get(4);
        int element5 = longerList.get(5);
        
		longerList.set(4, 100);
		assertEquals("testSet: check new element is correct ", (Integer)100, longerList.get(4) );
		assertEquals("testSet: check size is ok ", curSize, longerList.size() );	
		assertEquals("testSet: check prev. element is correct ", (Integer)element3, longerList.get(3) );
		assertEquals("testSet: check next. element is correct ", (Integer)element5, longerList.get(5) );
				
		
		// out of bounds check on the last element
		try {
			longerList.add(-1, 100);
			fail("testSet: Check out of bounds low");
		}
		catch (IndexOutOfBoundsException e) {
			//System.out.println("IndexOutOfBoundsException OK");
		}
		
		// out of bounds check on the last element
		try {
			longerList.set(longerList.size(), 100);
			longerList.set(longerList.size()+1, 100);
			fail("testSet: Check out of bounds high");
		}
		catch (IndexOutOfBoundsException e) {
			//System.out.println("IndexOutOfBoundsException OK");
		}			
		
	}
	
}
