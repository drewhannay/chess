package net;

import gui.AnimatedLabel;
import gui.Driver;
import gui.NewGameMenu;
import gui.PlayGame;
import gui.PlayNetGame;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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

		Object fromUser;
		Object fromServer;

		Game g = PlayNetGame.getGame();
		fromServer = g;
		if(fromServer!=null)
			out.writeObject(fromServer);
		PlayGame.resetTimers();
		Driver.getInstance().setPanel(png);

		while(g.isBlackMove()==false){
			while(PlayNetGame.netMove == null)
				Thread.sleep(0);
			fromServer = PlayNetGame.netMove;
			PlayNetGame.netMove = null;

			out.writeObject(fromServer);
			out.flush();
		}

		try{
			while(PlayNetGame.running) {
				while(g.isBlackMove()==true){
					fromUser = in.readObject();
					FakeMove toMove = (FakeMove)fromUser;
					if(toMove.originCol == -1){
						int surrender = JOptionPane.showConfirmDialog(null, "The other player has requested a Draw. Do you accept?", "Draw",
								JOptionPane.YES_NO_OPTION);
						if(surrender == 0){ //If this player also accepts the Draw.
							out.writeObject(new FakeMove(-2,-2,-2,-2,-2,null)); //Write out a new object which shows you accepted the Draw.
							Result r = new Result(Result.DRAW);
							r.setText("The game has ended in a Draw!");
							g.getLastMove().setResult(r);
							PlayGame.endOfGame(r);
							throw new Exception();
						}
						else{
							out.writeObject(new FakeMove(-3,-3,-3,-3,-3,null));//Else, write out an object which shows you did NOT accept the Draw.
							continue;
						}
					}
					else{
						g.playMove(g.fakeToRealMove((FakeMove)fromUser));
						if(g.getLastMove().getResult()!=null)
							continue;
					}
				}

				while(g.isBlackMove()==false){
					while(PlayNetGame.netMove == null && !png.drawRequested)
						Thread.sleep(0);
					if(png.drawRequested){
						fromUser = in.readObject();
						FakeMove toMove = (FakeMove)fromUser;
						if(toMove.originCol == -2){
							Result r = new Result(Result.DRAW);
							r.setText("The game has ended in a Draw!");
							g.getLastMove().setResult(r);
							PlayGame.endOfGame(r);
							png.drawRequested = false;
							throw new Exception();
						}
						else if(toMove.originCol == -3){ //If the response is an unaccepted Draw request, do not perform the Move.
							JOptionPane.showMessageDialog(null, "Your request for a draw has been denied. Continue play as normal.","Denied",JOptionPane.PLAIN_MESSAGE);
							png.drawRequested = false;
							continue;
						}
					}

					fromServer = PlayNetGame.netMove;
					PlayNetGame.netMove = null;
					
					if(((FakeMove)fromServer).originCol == -1)
						png.drawRequested = true;

					out.writeObject(fromServer);
					out.flush();
					if(g.getLastMove().getResult()!=null)
						break;
				}
			}
		}catch(SocketException e){
			JOptionPane.showMessageDialog(null, "Your opponent closed the game", "Oops!", JOptionPane.ERROR_MESSAGE);
			Driver.getInstance().revertPanel();
			serverSocket.close();
			return;
		}catch(EOFException e){
			if(g.getHistory().get(g.getHistory().size()-1).getResult()!=null)
				return;
			JOptionPane.showMessageDialog(null, "Your opponent closed the game", "Oops!", JOptionPane.ERROR_MESSAGE);
			Driver.getInstance().revertPanel();
			serverSocket.close();
			return;
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Something went wrong! Returning to main menu...", "Oops!", JOptionPane.ERROR_MESSAGE);
			Driver.getInstance().revertPanel();
			serverSocket.close();
			return;
		}
		
		out.close();
		in.close();
		clientSocket.close();
		serverSocket.close();
	}

}
