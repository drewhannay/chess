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
import ai.FakeMove;

/**
 * 
 * Class to create the network host
 * 
 * @author Drew Hannay & Andrew Wolfe & John McCormick
 * 
 */
public class NetworkServer
{
	/**
	 * Method to create the host to wait for a client and to set up the new game
	 * 
	 * @param png the game that is going to be played
	 * @throws Exception socket or end of file exception
	 */
	public void host(PlayNetGame png) throws Exception
	{
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		serverSocket = new ServerSocket(27335);
		serverSocket.setSoTimeout(1000);
		while (clientSocket == null)
		{
			try
			{
				PlayGame.resetTimers();

				clientSocket = serverSocket.accept();
			}
			catch (Exception e)
			{
				if (NewGameMenu.m_isCancelled)
					return;
			}
		}

		AnimatedLabel.m_isFinished = true;
		ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

		Object fromUser;
		Object fromServer;

		Game g = PlayGame.getGame();
		fromServer = g;
		if (fromServer != null)
			out.writeObject(fromServer);
		PlayGame.resetTimers();
		Driver.getInstance().setPanel(png);
		try
		{
			while (g.isBlackMove() == false)
			{
				while (PlayNetGame.netMove == null)
					Thread.sleep(0);
				fromServer = PlayNetGame.netMove;
				PlayNetGame.netMove = null;

				out.writeObject(fromServer);
				out.flush();
			}

			while (PlayNetGame.running)
			{
				while (g.isBlackMove() == true && PlayNetGame.running)
				{
					fromUser = in.readObject();
					FakeMove toMove = (FakeMove) fromUser;
					if (toMove.m_originColumn == -1)
					{
						int surrender = JOptionPane.showConfirmDialog(null, "The other player has requested a Draw. Do you accept?",
								"Draw", JOptionPane.YES_NO_OPTION);
						if (surrender == 0)
						{ // If this player also accepts the Draw.
							out.writeObject(new FakeMove(-2, -2, -2, -2, -2, null)); // Write
																						// out
																						// a
																						// new
																						// object
																						// which
																						// shows
																						// you
																						// accepted
																						// the
																						// Draw.
							Result r = new Result(Result.DRAW);
							r.setText("The game has ended in a Draw!");
							g.getLastMove().setResult(r);
							PlayGame.endOfGame(r);
							throw new Exception();
						}
						else
						{
							out.writeObject(new FakeMove(-3, -3, -3, -3, -3, null));// Else,
																					// write
																					// out
																					// an
																					// object
																					// which
																					// shows
																					// you
																					// did
																					// NOT
																					// accept
																					// the
																					// Draw.
							continue;
						}
					}
					else
					{
						g.playMove(g.fakeToRealMove((FakeMove) fromUser));
						if (g.getLastMove().getResult() != null)
							continue;
					}
				}

				while (g.isBlackMove() == false && PlayNetGame.running)
				{
					while (PlayNetGame.netMove == null && !png.drawRequested && PlayNetGame.running)
						Thread.sleep(0);
					if (png.drawRequested)
					{
						fromUser = in.readObject();
						FakeMove toMove = (FakeMove) fromUser;
						if (toMove.m_originColumn == -2)
						{
							Result r = new Result(Result.DRAW);
							r.setText("The game has ended in a Draw!");
							g.getLastMove().setResult(r);
							PlayGame.endOfGame(r);
							png.drawRequested = false;
							throw new Exception();
						}
						else if (toMove.m_originColumn == -3)
						{ // If the response is an unaccepted Draw request, do
							// not perform the Move.
							JOptionPane.showMessageDialog(null, "Your request for a draw has been denied. Continue play as normal.",
									"Denied", JOptionPane.PLAIN_MESSAGE);
							png.drawRequested = false;
							continue;
						}
					}

					fromServer = PlayNetGame.netMove;
					PlayNetGame.netMove = null;

					if (((FakeMove) fromServer).m_originColumn == -1)
						png.drawRequested = true;

					out.writeObject(fromServer);
					out.flush();
					if (g.getLastMove().getResult() != null)
						break;
				}
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Your opponent closed the game", "Oops!", JOptionPane.ERROR_MESSAGE);
			Driver.getInstance().m_fileMenu.setVisible(true);
			Driver.getInstance();
			Driver.m_gameOptionsMenu.setVisible(false);
			Driver.getInstance().revertToMainPanel();
			serverSocket.close();
			return;
		}
		catch (EOFException e)
		{
			e.printStackTrace();
			if (g.getHistory().size() != 0 && g.getHistory().get(g.getHistory().size() - 1).getResult() != null)
				return;
			JOptionPane.showMessageDialog(null, "Your opponent closed the game", "Oops!", JOptionPane.ERROR_MESSAGE);
			g.getBlackTimer().stopTimer();
			g.getWhiteTimer().stopTimer();
			Driver.getInstance().m_fileMenu.setVisible(true);
			Driver.getInstance();
			Driver.m_gameOptionsMenu.setVisible(false);
			Driver.getInstance().revertToMainPanel();
			serverSocket.close();
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		out.close();
		in.close();
		clientSocket.close();
		serverSocket.close();
	}

}
