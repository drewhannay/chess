package net;

import gui.PlayNetGame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import logic.Game;

public class NetworkClient {

	public void join(String host) throws Exception{

		Socket socket = null;
		ObjectOutputStream out = null;
        ObjectInputStream in = null;
        
        while(socket == null){
	        try{
	        	socket = new Socket(host, 27335);
	        }catch(Exception e){
	        	
	        }
        }
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());


		Object fromServer;
		Object fromUser = "";
		
		Game g = (Game) in.readObject();
		
		PlayNetGame png = new PlayNetGame(g, false, true);
		
		while ((fromServer = in.readObject()) != null) {
			g.playMove(g.fakeToRealMove((NetMove)fromServer));

			fromUser = null;
			if (fromUser != null) {
				out.writeObject(fromUser);
				out.flush();
			}
		}

		out.close();
		in.close();
		socket.close();
	}

}
