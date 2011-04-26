package net;

import gui.Driver;
import gui.PlayNetGame;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import logic.Game;

public class NetworkClient {

	public void join(String host) throws Exception{

		Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		boolean playing = true;

		while(socket == null){
			try{
				socket = new Socket(host, 27335);
			}catch(Exception e){

			}
		}
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());


		Object fromServer = null;
		Object fromUser;

		Game g = (Game) in.readObject();

		PlayNetGame png = new PlayNetGame(g, false, true);
		Driver.getInstance().setPanel(png);


		//		while ((fromServer = in.readObject()) != null) {
		try{
		while(playing){
			while(g.isBlackMove()==false){
				fromServer = in.readObject();
				NetMove toMove = (NetMove)fromServer;
				//				if(toMove.originCol == -1){ //If the object is an initial request to Draw.
				//					int surrender = JOptionPane.showConfirmDialog(null, "Player has requested a Draw. Do you accept?", "Draw",
				//							JOptionPane.YES_NO_OPTION);
				//					if(surrender == 0){ //If this player also accepts the Draw.
				//						png.createMenu(); //Create menu to show end of game.
				//						out.writeObject(new NetMove(-2,-2,-2,-2,-2,null)); //Write out a new object which shows you accepted the Draw.
				//						break;
				//					}
				//					else{
				//						out.writeObject(new NetMove(-3,-3,-3,-3,-3,null));//Else, write out an object which shows you did NOT accept the Draw.
				//					}
				//				}
				//	
				//				if(toMove.originCol == -2){ //Response of accepted Draw request
				//					png.createMenu();
				//					break;
				//				}
				//				if(!(toMove.originCol == -3)){ //If the response is an unaccepted Draw request, do not perform the Move.
				g.playMove(g.fakeToRealMove((NetMove)fromServer));
				//				}
				if(g.getLastMove().getResult()!=null)
					break;
			}
			while(g.isBlackMove()==true){
				while(png.netMove == null)
					Thread.sleep(0);

				fromUser = png.netMove;
				png.netMove = null;

				out.writeObject(fromUser);
				System.out.println("Sent Move: " + fromUser.toString());
				out.flush();
				if(g.getLastMove().getResult()!=null)
					break;
			}
		}
		}catch (Exception e){
			out.close();
			in.close();
			socket.close();	
		}

		out.close();
		in.close();
		socket.close();
	}

}
