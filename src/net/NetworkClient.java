package net;

import gui.AnimatedLabel;
import gui.Driver;
import gui.NewGameMenu;
import gui.PlayGame;
import gui.PlayNetGame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import logic.Game;
import logic.Result;

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
			while(playing){
				while(g.isBlackMove()==false){
					fromServer = in.readObject();
					FakeMove toMove = (FakeMove)fromServer;

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
						g.playMove(g.fakeToRealMove((FakeMove)fromServer));
						if(g.getLastMove().getResult()!=null)
							continue;
					}
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
