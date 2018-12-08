//loading objects from file, finding which belongs to which groups and adding them to lists
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.StringUtils;

public class FWOBJECTS {
	
	static List<String> ALL_OBJ_WITH_IP = new ArrayList<String>();		//keeps all objects covering given IP and all groups covering it
	static int count=0;
	
	//just loads file to 'lista' (MAIN), but only content between start and stop markers and in specific vsys
	public static void LOAD(String start1, String start2, String stop1, String stop2, String vsys) {							
		
		String section="out";
		String vsyscheck="out";
		if (vsys.equals("all")) vsyscheck="in"; 									
		
		try {															
			File file = new File(GUI2.filename.getText());					
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;	
			
			while ((line = bufferedReader.readLine()) != null) {
				
				if (line.contains("<entry name=\"vsys") && !vsys.equals("all")) {						//'vsys' word in line sets 'out', because we perhaps went to next vsys, but for our specific vsys in argument it sets 'in'					
					vsyscheck="out";															//for vsys=all, ignore these if's
					if (line.contains("<entry name=\""+vsys+"\">")) vsyscheck="in";
				}
				if (line.contains("<shared>")) vsyscheck="in"; 
				
				if (line.contains(start1) || line.contains(start2)) section="in";	//jestesmy we wlasciwej sekcji
				if (line.contains(stop1)|| line.contains(stop2)) section="out";
				if (section.equals("in") && vsyscheck.equals("in")) MAIN.lista.add(line);							//if in the right section, add to list	
				
			}
		
			fileReader.close();
		}
		catch (IOException e) {e.printStackTrace();}
		
		
	}
	
	
	
	//will return all names of objects ip-netmask or ip-range, that cover given input IP	
	public static LinkedList<String> OBJECT_NAME(String input)	{	
		
		ALL_OBJ_WITH_IP.clear();
		LinkedList<String> output = new LinkedList<String>(); 
		
	for (ListIterator<String> lista_i = MAIN.lista.listIterator(); lista_i.hasNext(); ) {
		    
			String line = lista_i.next();

			if (StringUtils.substringBetween(line, "<ip-netmask>", "</ip-netmask>") != null) {		//find subnet in config and check if input belongs to it									
			    	
					String subnet = StringUtils.substringBetween(line, "<ip-netmask>", "</ip-netmask>");
					if (subnet.indexOf('/')==-1) subnet+="/32";		//add /32 if this is host
							
			    	if (IN_SUBNET.CHECK(input, subnet)) {				//if our input IP belongs to subnet we add it to output list
			    		
						line = lista_i.previous();line = lista_i.previous();
				    	
				    	String data = line.split("\"")[1];							//get just object name between ""
				    	output.add(data);
				    	
				    	line = lista_i.next();line = lista_i.next();
			    	}
			    }	
				
			
			if (StringUtils.substringBetween(line, "<ip-range>", "</ip-range>") != null) {		//find iprange in config and check if input belongs to it									
		    	
				String range = StringUtils.substringBetween(line, "<ip-range>", "</ip-range>");
				String ip1 = range.split("-")[0];
				String ip2 = range.split("-")[1];
				
				String[] ip1tab = ip1.split("\\.");
				String[] ip2tab = ip2.split("\\.");
				String[] ip3tab = input.split("\\.");
						
				if (Integer.parseInt(ip1tab[0]) <= Integer.parseInt(ip3tab[0]) &&				//comparing ip and range octect by octet
					Integer.parseInt(ip1tab[1]) <= Integer.parseInt(ip3tab[1]) &&
					Integer.parseInt(ip1tab[2]) <= Integer.parseInt(ip3tab[2]) &&
					Integer.parseInt(ip1tab[3]) <= Integer.parseInt(ip3tab[3]) &&	
					Integer.parseInt(ip2tab[0]) >= Integer.parseInt(ip3tab[0]) &&
					Integer.parseInt(ip2tab[1]) >= Integer.parseInt(ip3tab[1]) &&
					Integer.parseInt(ip2tab[2]) >= Integer.parseInt(ip3tab[2]) &&
					Integer.parseInt(ip2tab[3]) >= Integer.parseInt(ip3tab[3]))	{	
					
					line = lista_i.previous();line = lista_i.previous();
					String data = line.split("\"")[1];	
					output.add(data);
					line = lista_i.next();line = lista_i.next();
				 }
		    }	
			
		}
		output=LIST_DUP.REMOVE(output);		//remove duplicates
		ALL_OBJ_WITH_IP.addAll(output);		//add also to global (sum) list
		count=output.size();
		
		return output;			
	}
	
	
	
	//will return all names of groups containing object-input
	public static LinkedList<String> PARENT_NAME(String input) {					
		
	LinkedList<String> output = new LinkedList<String>(); 
		
		for (ListIterator<String> lista_i = MAIN.lista.listIterator(); lista_i.hasNext(); ) {
		    
			String element = lista_i.next();
	   
			if (element.contains("<member>"+input+"</member>") ) {				//name found so we go back to get object name
			    				    	
					while (!element.contains("<entry name=")) element = lista_i.previous();			//go back to group name

					String data = element.split("\"")[1];		
			    	output.add(data);
			    	
			    	while (!element.contains("<member>"+input+"</member>")) element = lista_i.next();			//go forward to last found member

			    }		    		
		}	
		output=LIST_DUP.REMOVE(output);				//remove duplicates
		return output;
	}

	//will return all names of groups, in which there are at least some of input objects
	//each iteration of recurrent method will display list of all groups (where at least one input object belongs)
	public static LinkedList<String> ALL_GROUPS(LinkedList<String> input) {
		
		    LinkedList<String> parents = new LinkedList<String>();
		    
			for (ListIterator<String> lista_i = input.listIterator(); lista_i.hasNext(); ) {
				String element = lista_i.next();
				parents.addAll(PARENT_NAME(element));			//add to 'per iteration' list
				ALL_OBJ_WITH_IP.addAll(PARENT_NAME(element));	//add also to global (sum) list
			}
			
			parents=LIST_DUP.REMOVE(parents);		//remove duplicates
			if(!parents.isEmpty()) MAIN.output+="At least some of objects in line above belong to groups: \r\n"+parents+"\r\n(count: "+parents.size()+")\r\n\r\n";		//adding to output information because it it useful

		if(!parents.isEmpty()) return ALL_GROUPS(parents);
		
		else return parents;									//just for exit purpose

	}
	
	

}
