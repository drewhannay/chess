package net;

import java.util.Scanner;

public class NetworkProtocol {

    private static final int WAITING = 0;
    private static final int SENTMOVE = 1;
 
    private int state = WAITING;
 
    Scanner stdIn = new Scanner(System.in);
    
    public String processInput(Object theInput) {
        String theOutput = "";
        
        if(theInput == null)return theOutput;
        
        
        System.out.println("Client: " + theInput.toString());
        
        
        System.out.print("=> ");
        theOutput = stdIn.nextLine();
	    
//	    switch(state){
//	    case WAITING:
//	    	break;
//	    case SENTMOVE:
//	    	break;
//	    default:
//	    	break;
//	    	
//	    }
        return theOutput;
    }
}
