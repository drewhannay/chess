package net;

import java.util.Scanner;

public class NetworkProtocol {
 
    Scanner stdIn = new Scanner(System.in);
    
    public String processInput(Object theInput) {
        String theOutput = "";
        
        if(theInput == null)return theOutput;
        
        
        System.out.println("Client: " + theInput.toString());
        
        
        System.out.print("=> ");
        theOutput = stdIn.nextLine();
	    
        return theOutput;
    }
}
