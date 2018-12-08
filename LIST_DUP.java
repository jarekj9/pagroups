//removes duplicates from list
import java.util.LinkedList;
import java.util.ListIterator;



public class LIST_DUP {
	
	
	public static LinkedList<String> REMOVE(LinkedList<String> input){
		
		int iterator_index;		
		
		for (ListIterator<String> input_i = input.listIterator(); input_i.hasNext(); ) {				
			
			String element = input_i.next();
			iterator_index=input_i.nextIndex();
		
			for (ListIterator<String> input_i2 = input_i; input_i2.hasNext(); ) {		//for each element of list check all following elements
				
				String element2 = input_i2.next();
				if(element.equals(element2)) input_i2.remove();
				
			}
			input_i = input.listIterator(iterator_index);				//copy iterator index because we cannot have 2 iterators in different positions
		}	
		
		return input;
	}
}
