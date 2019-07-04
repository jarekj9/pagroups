//Call method CHECK to check if IP is in subnet for example" IN_SUBNET.CHECK("192.168.10.80", "192.168.10.64/26"));
//this method needs method NOT_FULL_OCTET to work

public class IN_SUBNET {

	//returns not full octetof of mask for number of '1' , for example: N=7 :128, dla 6 : 192, dla 5 : 224; 
	//base should be 128 and P should be 64 at start
	private static int NOT_FULL_OCTET(int N, int base, int P) {          
		if (N == 1)  return base;										
		else {
		    base+=P;
		    P=P/2;
		    return NOT_FULL_OCTET(N-1,base,P);
		   }    		
	}

	//returns true if IP is in SUBNET; it needs function 'NOT_FULL_OCTET' to work
	public static boolean CHECK(String inputIP, String inputSUBNET) {	
		
		String[] IP = inputIP.split("\\.");				//IP octets in Array IP, subnet octets in Array SUBNET, number of mask '1' bits in var MASKD
		String[] SUBNET = inputSUBNET.split("\\.");	
		int MASKD = Integer.parseInt(SUBNET[3].split("/")[1]);   //f.e. for mask /24 we get int 24
		SUBNET[3] = SUBNET[3].split("/")[0];
		int[] MASK255= {0,0,0,0};
		int[] NET_AND= {0,0,0,0};
		int[] IP_AND= {0,0,0,0};
		
	                               
	    for (int i=0; i<4;i++) {                        //loop creates mask array in format x.x.x.x based on mask format /XX
	        if (MASKD >= 8) {
	            MASK255[i]=255;
	            MASKD-=8;
	        }
	        else if( MASKD > 0) {
	             MASK255[i]=NOT_FULL_OCTET(MASKD,128,64);
	             MASKD=0;
	        }
	        else MASK255[i]=0;    
	     }
		
	                             
		for (int i=0; i<4; i++) {				 //AND of SUBNET and MASK
			 NET_AND[i]= Integer.parseInt(SUBNET[i]) & MASK255[i];	
		}
		
		for (int i=0; i<4; i++) {				//AND of IP and MASK
			 IP_AND[i]= Integer.parseInt(IP[i]) & MASK255[i];	
		}	

												//compare 2 results of AND
		if(IP_AND[0]==NET_AND[0] && IP_AND[1]==NET_AND[1]
		 &&IP_AND[2]==NET_AND[2] && IP_AND[3]==NET_AND[3])	
		return true;
		
		else return false;
	}
	
	
}
