import java.util.LinkedList;
import java.util.ListIterator;
import java.util.*;

public class RULES {
	
	static String output_rules="";
	static int count=0;
	
	public static LinkedList<String> RULENAMES_WITH_IP(List<String> input, boolean inc_disabled, String src_dst) {
		output_rules="";
	    LinkedList<String> output_names = new LinkedList<String>();
	    String section="";
	    
		for (ListIterator<String> input_i = input.listIterator(); input_i.hasNext(); ) {			//iteration through list of searched objects
			
			String object_name = input_i.next();		
		
			for (ListIterator<String> lista_i = MAIN.lista.listIterator(); lista_i.hasNext(); ) {	//iteration through policies  (for every object)				
				
				String line = lista_i.next();
				
				if(line.contains("<entry name=\"vsys")) { output_rules+=line+"\n"; continue; } 
				
				if(line.contains("<source>")) section = "source";								//remember if we read source or destiantion
				if(line.contains("<destination>")) section = "destination";
				
				if(line.contains("<member>"+object_name+"</member>")) {
					
					if (!inc_disabled)
						while (!line.contains("<disabled>")) { 
							if (line.contains("</entry>")) break;	//because some configs do not have 'disabled' line, we get to end of rule and break this search
							line = lista_i.next();					//check if rule is disabled
						}
					if (line.contains("<disabled>yes</disabled>")) continue;
					
					if (!src_dst.equals("source or destination")) if(!src_dst.equals(section)) continue;		//if we do not look for both src/dst, then check if we are in right section( source or destination)

										
					while (!line.contains("<entry name=")) line = lista_i.previous();			//go some config lines back to get rulename 

					String data = line.split("\"")[1];		
			    	output_names.add(data);

			    	while (!line.contains("</entry>")) { line = lista_i.next();output_rules+=line+"\n"; }   //add all rule content to output_rules while moving up to end of rule

				}
				
			}
	
		}
		// output_names=LIST_DUP.REMOVE(output_names);					//remove duplicates.. i changed my mind
		count= output_names.size();
		return output_names;

	}
	
	
	
	
	
	
	
	
}
