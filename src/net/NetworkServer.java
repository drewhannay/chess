package net;

import gui.Driver;
import gui.PlayNetGame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import logic.Game;

public class NetworkServer {
	
	public static boolean playing;

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
        
        Object fromUser;
        Object fromServer;
        NetworkProtocol np = new NetworkProtocol();
        
        Game g = PlayNetGame.getGame();
        
        fromServer = g;
        if(fromServer!=null)
        	out.writeObject(fromServer);
        
        Driver.getInstance().setPanel(png);
        
        while(png.netMove == null)
        	Thread.sleep(0);
		fromServer = png.netMove;
		png.netMove = null;

		out.writeObject(fromServer);
		out.flush();
        
        try{
	        while (playing) {
	        	fromUser = in.readObject();
	        	g.playMove(g.fakeToRealMove((NetMove)fromUser));

				while(png.netMove == null)
					Thread.sleep(0);

				fromServer = png.netMove;
				png.netMove = null;

				out.writeObject(fromServer);
				out.flush();
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
