package net;

import gui.Driver;
import gui.PlayNetGame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JMenu;
import javax.swing.JOptionPane;

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
		Object fromUser;

		Game g = (Game) in.readObject();

		PlayNetGame png = new PlayNetGame(g, false, true);
		Driver.getInstance().setPanel(png);
		

		while ((fromServer = in.readObject()) != null) {
			NetMove toMove = (NetMove)fromServer;
			if(toMove.originCol == -1){
				int surrender = JOptionPane.showConfirmDialog(null, "Player has requested a Draw. Do you accept?", "Draw",
						JOptionPane.YES_NO_OPTION);
				if(surrender == 0){
					JMenu menu = png.createMenu();
					menu.setVisible(true);
					out.writeObject(new NetMove(-1,-1,-1,-1,-1,null));
					break;
				}
			}
				//TODO HERE
			g.playMove(g.fakeToRealMove((NetMove)fromServer));
			
			while(png.netMove == null)
				Thread.sleep(0);

			fromUser = png.netMove;
			png.netMove = null;

			out.writeObject(fromUser);
			out.flush();
			if(g.getLastMove().getResult()!=null)
				break;
		}

		out.close();
		in.close();
		socket.close();
	}

}
