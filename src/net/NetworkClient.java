package net;

import gui.Driver;
import gui.PlayNetGame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import logic.Game;

public class NetworkClient {
	
	public static boolean playing;

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
		Object fromUser;

		Game g = (Game) in.readObject();

		PlayNetGame png = new PlayNetGame(g, false, true);
		Driver.getInstance().setPanel(png);
		
		playing = true;
		
		while (playing) {
			while((fromServer = in.readObject())==null);
			g.playMove(g.fakeToRealMove((NetMove)fromServer));
			
			while(png.netMove == null)
				Thread.sleep(0);

			fromUser = png.netMove;
			png.netMove = null;

			out.writeObject(fromUser);
			out.flush();
		}

		out.close();
		in.close();
		socket.close();
	}

}
