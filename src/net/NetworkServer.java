package net;

import gui.Driver;
import gui.PlayNetGame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import logic.Builder;

public class NetworkServer {

	public void host(PlayNetGame png) throws Exception{
		 
		
		
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(27335);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	//e.printStackTrace();
        }

        	
 
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (Exception e) {
        	e.printStackTrace();
        }
 
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        
        Object input;
        Object output;
        NetworkProtocol np = new NetworkProtocol();
 
//        output = np.processInput(null);
//        out.writeObject(output);
        
        
        output = png.getGame();
        if(output!=null)
        	out.writeObject(output);
        
        Driver.getInstance().setPanel(png);
        
        try{
	        while ((input = in.readObject()) != null) {
//	             output = np.processInput(input);
//	             out.writeObject(output);
//	             out.flush();
//	             if (input.toString().equalsIgnoreCase("Bye"))
//	                break;
	        }
        }catch(Exception e){
        	//The try catch is here because when the client closes the connection,
        	//(input = in.readObject()) throws an End Of File Exception
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
	
}
