package net;

import gui.Driver;
import gui.PlayNetGame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import logic.Game;

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
		boolean playing = true;

		Object fromUser;
		Object fromServer;

		Game g = PlayNetGame.getGame();

		fromServer = g;
		if(fromServer!=null)
			out.writeObject(fromServer);

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
					System.out.println("Received Move: " + fromUser.toString());
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
					System.out.println("Sent Move: " + fromServer.toString());
					out.flush();
					if(g.getLastMove().getResult()!=null)
						break;
				}
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
