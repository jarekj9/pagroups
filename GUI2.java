//defines GUI, when button is clicked we go to START method in MAIN
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class GUI2 extends Thread{
	
	
  static JTextField filename = new JTextField(20);
  static JTextField vsys = new JTextField(20);
  static JLabel statuslabel = new JLabel("<html>Status progres:<br/><br/><br/><br/><br/><br/><br/><br/></html>",JLabel.LEFT);		//status label
  static volatile String status;
 
	
	
  public static void RAMKA() {
    JFrame frame = new JFrame("PA Object Finder");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Box box = Box.createVerticalBox(); 	
    JTextField ip = new JTextField(20);	
    ip.setText("192.168.1.10");
    vsys.setText("vsys1");
	filename.setText("candidate-config.xml");
	
	JLabel versionlabel = new JLabel("Version 0.8, Jaroslaw Jankun");
	JButton helpbutton = new JButton("Click here for help");
	JLabel infolabel = new JLabel("");
	JLabel iplabel = new JLabel("Enter IP address(no subnet, just single address):");
	JLabel configlabel = new JLabel("Enter xml config filename (in the same folder):");
	JLabel vsyslabel = new JLabel("Enter vsys number, for example 'vsys1' or 'all':");	
	JButton button = new JButton("Process and save to file");
	
	box.add(versionlabel);
	box.add(Box.createVerticalStrut(10));
	box.add(helpbutton);   
	box.add(Box.createVerticalStrut(10));
	box.add(infolabel);
	box.add(iplabel);
    box.add(ip);
    box.add(Box.createVerticalStrut(10));
    box.add(configlabel);
    box.add(filename);
    box.add(Box.createVerticalStrut(10));
    box.add(vsyslabel);
    box.add(vsys);
    box.add(Box.createVerticalStrut(10));
    box.add(button);
    box.add(Box.createVerticalStrut(30));
    box.add(statuslabel);
    box.add(Box.createVerticalStrut(30));

    
    frame.add(box, BorderLayout.CENTER);
    frame.setSize(300, 450);
    frame.setVisible(true);
  
    
    
    helpbutton.addActionListener( new ActionListener()		//help button shows help message
    	    {
    	        public void actionPerformed(ActionEvent e)
    	        {
    	         JOptionPane.showMessageDialog(null, "<html>You need to have Palo Alto xml config in the same folder.<br/><br/>Program will provide names of all objects, ranges, all groups<br/>(and all groups with these groups, all levels deep)<br/>and all rulenames and full rules, which contain the requested IP.<br/>It only ignores 'any' objects.<br/>If you select vsys 'all' and have duplicate rulenames,<br/>then first occurance is taken into consideration. <br><br></html>", "Help", JOptionPane.INFORMATION_MESSAGE);
    	        }
    	    });
    
    
    button.addActionListener( new ActionListener()		//if button clicked, start analysis
    	    {
    	        public void actionPerformed(ActionEvent e)
    	        {
    	        	status="<br/>Started for IP: "+ip.getText();
    	        	(new GUI2()).start();
    	        	
    	        	try {
    	        		MAIN.START(ip.getText());
    	        }
    	        	catch(Exception ee) {
    	    	         JOptionPane.showMessageDialog(null, "<html>Please make sure you enter everything correctly and have good file in the folder.</html>", "Error", JOptionPane.INFORMATION_MESSAGE);

    	        	}
    	        	
    	  
    	        }
    	    });
  
    }

  //just prints real-time status as new thread
  public void run() {
	  
	  for(;;){
		  statuslabel.setText("<html>Status progress: "+status+"</html>");
		  statuslabel.paintImmediately(0,0,500,500);   		  
		  if(status.contains("Written to file")) break;
		  try{Thread.sleep(200);} catch(InterruptedException ex) {Thread.currentThread().interrupt();}	//delay
	  }
	  status="";
	 
	  
  }
  
  
  
}
