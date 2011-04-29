package net;

import gui.AnimatedLabel;
import gui.Driver;
import gui.NewGameMenu;
import gui.PlayGame;
import gui.PlayNetGame;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

import logic.Game;
import logic.Result;

public class NetworkClient {
	
	public static Socket socket;
	
	public static void closeSocket(){
		try {
			socket.close();
		} catch(Exception e) {
			
		}
	}

	public void join(String host) throws Exception{

		ObjectOutputStream out = null;
		ObjectInputStream in = null;

		while(socket == null){
			try{
				socket = new Socket(host, 27335);
			}catch(Exception e){
				if(NewGameMenu.cancelled)
					return;
			}
		}
		AnimatedLabel.finished = true;
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());


		Object fromServer = null;
		Object fromUser;

		Game g = (Game) in.readObject();
		PlayNetGame png = new PlayNetGame(g, false, true);
		PlayGame.resetTimers();
		Driver.getInstance().setPanel(png);


		//		while ((fromServer = in.readObject()) != null) {
		try{
			while(PlayNetGame.running){
				while(g.isBlackMove()==false){
					fromServer = in.readObject();
					FakeMove toMove = (FakeMove)fromServer;

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
						g.playMove(g.fakeToRealMove((FakeMove)fromServer));
						if(g.getLastMove().getResult()!=null)
							continue;
					}
				}
				while(g.isBlackMove()==true){
					while(PlayNetGame.netMove == null && !png.drawRequested)
						Thread.sleep(0);
					if(png.drawRequested){
						fromServer = in.readObject();
						FakeMove toMove = (FakeMove)fromServer;
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

					fromUser = PlayNetGame.netMove;
					PlayNetGame.netMove = null;

					if(((FakeMove)fromUser).originCol == -1)
						png.drawRequested = true;
					out.writeObject(fromUser);
					out.flush();
					if(g.getLastMove().getResult()!=null)
						break;
				}
			}
		}catch(SocketException e){
			JOptionPane.showMessageDialog(null, "Your opponent closed the game", "Oops!", JOptionPane.ERROR_MESSAGE);
			Driver.getInstance().revertPanel();
			return;
		}catch(EOFException e){
			if(g.getHistory().size() != 0 && g.getHistory().get(g.getHistory().size()-1).getResult()!=null)
				return;
			JOptionPane.showMessageDialog(null, "Your opponent closed the game", "Oops!", JOptionPane.ERROR_MESSAGE);
			g.getBlackTimer().stop();
			g.getWhiteTimer().stop();
			Driver.getInstance().revertPanel();
			return;
		}catch(Exception e){
//			JOptionPane.showMessageDialog(null, "Something went wrong! Returning to main menu...", "Oops!", JOptionPane.ERROR_MESSAGE);
//			Driver.getInstance().revertPanel();
//			return;
		}

		out.close();
		in.close();
		socket.close();
	}

}
