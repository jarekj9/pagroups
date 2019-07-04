//Version 0.92 Jaroslaw Jankun
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;


public class MAIN{

static List<String> lista = new ArrayList<String>();				//all processed lines of configuration
static String output="";											//output to file

//START of process after button is clicked

public static void START(String input) {
	
	String src_dst="all";														//this is for func RULES.RULENAMES_WITH_IP	
	if(GUI2.srcbox.isSelected()) src_dst="source";								//check if we need rules with specific source or destination
	if(GUI2.dstbox.isSelected()) src_dst="destination";
	if( GUI2.dstbox.isSelected() && GUI2.srcbox.isSelected() ) src_dst="source or destination";
	
	lista.clear();
	
	GUI2.status+="<br/>Loading objects...";
	FWOBJECTS.LOAD("<address>", "<address-group>", "</address>", "</address-group>",GUI2.vsys.getText());		//load part of config, where objects are
	output+="Analysis for IP: "+input+" as "+src_dst+" in vsys: "+GUI2.vsys.getText()+"\r\n\r\n";	
	
	GUI2.status+="<br/>Finding object names with IP you asked for...";
	output+="IP belongs to objects:\r\n"+FWOBJECTS.OBJECT_NAME(input)+"\r\n(count: "+FWOBJECTS.count+")\r\n\r\n";			//display objects
	
	GUI2.status+="<br/>Finding object groups with that objects...";
	FWOBJECTS.ALL_GROUPS(FWOBJECTS.OBJECT_NAME(input));											//display groups (adds to output String inside method)
	
	output+="Finally, all objects, that contain given IP: \r\n"+FWOBJECTS.ALL_OBJ_WITH_IP+"\r\n";						//display all objects combined
	output+="(count: "+FWOBJECTS.ALL_OBJ_WITH_IP.size()+")\r\n\r\n";
	
	lista.clear();		//clear lista to load policies later
	
	GUI2.status+="<br/>Loading policies...";
	FWOBJECTS.LOAD("<rules>", "<rules>", "</rules>", "</rules>",GUI2.vsys.getText());							//load part of config, where rules are	
	
	GUI2.status+="<br/>Finding policies with previously found objects...";
	output+="Rulenames, that include any of these objects:\r\n"+RULES.RULENAMES_WITH_IP(FWOBJECTS.ALL_OBJ_WITH_IP,GUI2.disabledbox.isSelected(), src_dst)+"\r\n";	//display rulenames, argument is object name and checkbox state (to include/exclude disabled rules
	output+="(count: "+RULES.count+")\r\n\r\n";
	
	output+="Rules:\r\n\r\n";																//display rules
	output+=RULES.output_rules;	
	
	GUI2.status+="<br/>Writing output to file...";
	Charset charset = Charset.forName("UTF-8");								//print and write to file
	Path file = Paths.get("./output.txt");
	try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
	    writer.write(output, 0, output.length());
	    writer.close();
	} catch (IOException x) {
	    System.err.format("IOException: %s%n", x);
	}	
	GUI2.status+="<br/>Written to file: output.txt";
	
	
	output="";																	//cleaning
	lista.clear();
	FWOBJECTS.ALL_OBJ_WITH_IP.clear();
	


}



	public static void main (String[] args) {	
			GUI2.RAMKA();	
		}
	

}
