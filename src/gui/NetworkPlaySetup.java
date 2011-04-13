package gui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkPlaySetup {
	
	
	
	
	
	
	private void host(){
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(27335);
		}catch(Exception e){
			e.printStackTrace();
		}
		Socket clientSocket = null;
		try{
			clientSocket = serverSocket.accept();
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			ObjectInputStream obIn = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
