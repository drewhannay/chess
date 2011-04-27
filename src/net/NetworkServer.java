package net;

import gui.AnimatedLabel;
import gui.Driver;
import gui.NewGameMenu;
import gui.PlayGame;
import gui.PlayNetGame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import logic.Game;
import logic.Result;

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

		AnimatedLabel.finished = true;
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
					FakeMove toMove = (FakeMove)fromUser;

					if(toMove.originCol == -1){
						int surrender = JOptionPane.showConfirmDialog(null, "The other player has requested a Draw. Do you accept?", "Draw",
								JOptionPane.YES_NO_OPTION);
						if(surrender == 0){ //If this player also accepts the Draw.
							out.writeObject(new FakeMove(-2,-2,-2,-2,-2,null)); //Write out a new object which shows you accepted the Draw.
							continue;
						}
						else{
							out.writeObject(new FakeMove(-3,-3,-3,-3,-3,null));//Else, write out an object which shows you did NOT accept the Draw.
						}
					}
					else if(toMove.originCol == -2){
						Result r = new Result(Result.DRAW);
						r.setText("The game has ended in a Draw!");
						g.getLastMove().setResult(r);
						PlayGame.endOfGame(r);
						break;
					}
					else if(toMove.originCol == -3){ //If the response is an unaccepted Draw request, do not perform the Move.
						continue;
					}
					else{
						g.playMove(g.fakeToRealMove((FakeMove)fromUser));
						if(g.getLastMove().getResult()!=null)
							continue;
					}
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
