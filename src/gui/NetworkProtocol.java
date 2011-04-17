package gui;

import java.util.Scanner;

public class NetworkProtocol {

    private static final int WAITING = 0;
    private static final int SENTMOVE = 1;
 
    private int state = WAITING;
 
    Scanner stdIn = new Scanner(System.in);
    
    public String processInput(Object theInput) {
        String theOutput = null;
        
        System.out.print("=> ");
        theOutput = stdIn.nextLine();
        if(theInput == null)return theOutput;
        
        
        System.out.println(theInput.toString());
        
        
        if(theInput.toString().equalsIgnoreCase("Bye"))
        	theOutput = "Bye";
 
        switch(state){
        case WAITING:
        	break;
        case SENTMOVE:
        	break;
        default:
        	break;
        	
        }
        
        
//        if (state == WAITING) {
//            theOutput = "Knock! Knock!";
//            state = SENTKNOCKKNOCK;
//        } else if (state == SENTKNOCKKNOCK) {
//            if (theInput.equalsIgnoreCase("Who's there?")) {
//                theOutput = clues[currentJoke];
//                state = SENTCLUE;
//            } else {
//                theOutput = "You're supposed to say \"Who's there?\"! " +
//                "Try again. Knock! Knock!";
//            }
//        } else if (state == SENTCLUE) {
//            if (theInput.equalsIgnoreCase(clues[currentJoke] + " who?")) {
//                theOutput = answers[currentJoke] + " Want another? (y/n)";
//                state = ANOTHER;
//            } else {
//                theOutput = "You're supposed to say \"" + 
//                clues[currentJoke] + 
//                " who?\"" + 
//                "! Try again. Knock! Knock!";
//                state = SENTKNOCKKNOCK;
//            }
//        } else if (state == ANOTHER) {
//            if (theInput.equalsIgnoreCase("y")) {
//                theOutput = "Knock! Knock!";
//                if (currentJoke == (NUMJOKES - 1))
//                    currentJoke = 0;
//                else
//                    currentJoke++;
//                state = SENTKNOCKKNOCK;
//            } else {
//                theOutput = "Bye.";
//                state = WAITING;
//            }
//        }
        return theOutput;
    }
}
