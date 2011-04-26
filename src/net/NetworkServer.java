package net;

import gui.Driver;
import gui.NewGameMenu;
import gui.PlayGame;
import gui.PlayNetGame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import logic.Game;

public class NetworkServer {

	public void host(PlayNetGame png) throws Exception{



		ServerSocket serverSocket = null;


		Socket clientSocket = null;
		serverSocket = new ServerSocket(27335);
		serverSocket.setSoTimeout(1000);
		while(clientSocket == null){
			try {
				PlayGame.resetTimers();
				
				clientSocket = serverSocket.accept();
			} catch (Exception e) {
				if(NewGameMenu.cancelled)
					return;
			}
		}

		
		ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
		boolean playing = true;

		Object fromUser;
		Object fromServer;

		Game g = PlayNetGame.getGame();
		fromServer = g;
		if(fromServer!=null)
			out.writeObject(fromServer);
		PlayGame.resetTimers();
		Driver.getInstance().setPanel(png);

		while(g.isBlackMove()==false){
			while(png.netMove == null)
				Thread.sleep(0);
			fromServer = png.netMove;
			png.netMove = null;

			out.writeObject(fromServer);
			System.out.println("Sent Move: " + fromServer.toString());
			out.flush();
		}

		try{
			while(playing) {
				while(g.isBlackMove()==true){
					fromUser = in.readObject();
					NetMove toMove = (NetMove)fromUser;
					//System.out.println("Received Move: " + fromUser.toString());
					//					if(toMove.originCol == -1){ //If the object is an initial request to Draw.
					//						int surrender = JOptionPane.showConfirmDialog(null, "Player has requested a Draw. Do you accept?", "Draw",
					//								JOptionPane.YES_NO_OPTION);
					//						if(surrender == 0){ //If this player also accepts the Draw.
					//							png.createMenu(); //Create menu to show end of game.
					//							out.writeObject(new NetMove(-2,-2,-2,-2,-2,null)); //Write out a new object which shows you accepted the Draw.
					//							break;
					//						}
					//						else{
					//							out.writeObject(new NetMove(-3,-3,-3,-3,-3,null));//Else, write out an object which shows you did NOT accept the Draw.
					//						}
					//					}
					//
					//					if(toMove.originCol == -2){ //Response of accepted Draw request
					//						png.createMenu();
					//						break;
					//					}
					//					if(!(toMove.originCol == -3)){ //If the response is an unaccepted Draw request, do not perform the Move.
					g.playMove(g.fakeToRealMove((NetMove)fromUser));
					//					}

					if(g.getLastMove().getResult()!=null)
						break;
				}

				while(g.isBlackMove()==false){
					while(png.netMove == null)
						Thread.sleep(0);
					
					fromServer = png.netMove;
					png.netMove = null;

					out.writeObject(fromServer);
					//System.out.println("Sent Move: " + fromServer.toString());
					out.flush();
					if(g.getLastMove().getResult()!=null)
						break;
				}
			}
		}catch(Exception e){
			out.close();
			in.close();
			clientSocket.close();
			serverSocket.close();
		}
		out.close();
		in.close();
		clientSocket.close();
		serverSocket.close();
	}

}
