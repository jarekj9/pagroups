import java.util.LinkedList;
import java.util.ListIterator;
import java.util.*;

public class RULES {
	
	static String output_rules="";
	static int count=0;
	
	public static LinkedList<String> RULENAMES_WITH_IP(List<String> input) {
		output_rules="";
	    LinkedList<String> output_names = new LinkedList<String>();
	    
		for (ListIterator<String> input_i = input.listIterator(); input_i.hasNext(); ) {			//iteracja przez liste szukanych obiektow
			
			String object_name = input_i.next();			
		
			for (ListIterator<String> lista_i = MAIN.lista.listIterator(); lista_i.hasNext(); ) {	//iteracja przez polityki  (dla kazdego obiektu)				
				
				String line = lista_i.next();
				
				if(line.contains(object_name)) {
					
					while (!line.contains("<entry name=")) line = lista_i.previous();			//go back and remember how many times (L)

					String data = line.split("\"")[1];		
			    	output_names.add(data);

			    	while (!line.contains("</entry>")) { line = lista_i.next();output_rules+=line+"\n"; }   //add all rule content to output_rules

				}
				
			}
	
		}
		output_names=LIST_DUP.REMOVE(output_names);					//remove duplicates
		count= output_names.size();
		return output_names;

	}
	
	
}
