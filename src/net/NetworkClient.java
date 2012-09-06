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
import ai.FakeMove;

/**
 * 
 * Class to create the network client
 * 
 * @author Drew Hannay & Andrew Wolfe & John McCormick
 * 
 */
public class NetworkClient
{
	/**
	 * Method to allow the client to join the host
	 * 
	 * @param host the computer being joined to
	 * @throws Exception throws an end of file or socket exception
	 */
	public void join(String host) throws Exception
	{

		Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		while (socket == null)
		{
			try
			{
				socket = new Socket(host, 27335);
			}
			catch (Exception e)
			{
				if (NewGameMenu.m_isCancelled)
					return;
			}
		}
		AnimatedLabel.m_isFinished = true;
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

		Object fromServer = null;
		Object fromUser;

		Game g = (Game) in.readObject();
		PlayNetGame png = new PlayNetGame(g, false, true);
		PlayGame.resetTimers();
		Driver.getInstance().setPanel(png);

		try
		{
			while (PlayNetGame.m_isRunning)
			{
				while (g.isBlackMove() == false && PlayNetGame.m_isRunning)
				{
					fromServer = in.readObject();
					FakeMove toMove = (FakeMove) fromServer;

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
							Result result = Result.DRAW;
							result.setGUIText("The game has ended in a Draw!");
							g.getLastMove().setResult(result);
							PlayGame.endOfGame(result);
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
						g.playMove(g.fakeToRealMove((FakeMove) fromServer));
						if (g.getLastMove().getResult() != null)
							continue;
					}
				}
				while (g.isBlackMove() == true && PlayNetGame.m_isRunning)
				{
					while (PlayNetGame.m_netMove == null && !png.m_drawRequested && PlayNetGame.m_isRunning)
						Thread.sleep(0);
					if (png.m_drawRequested)
					{
						fromServer = in.readObject();
						FakeMove toMove = (FakeMove) fromServer;
						if (toMove.m_originColumn == -2)
						{
							Result result = Result.DRAW;
							result.setGUIText("The game has ended in a Draw!");
							g.getLastMove().setResult(result);
							PlayGame.endOfGame(result);
							png.m_drawRequested = false;
							throw new Exception();
						}
						else if (toMove.m_originColumn == -3)
						{ // If the response is an unaccepted Draw request, do
							// not perform the Move.
							JOptionPane.showMessageDialog(null, "Your request for a draw has been denied. Continue play as normal.",
									"Denied", JOptionPane.PLAIN_MESSAGE);
							png.m_drawRequested = false;
							continue;
						}
					}

					fromUser = PlayNetGame.m_netMove;
					PlayNetGame.m_netMove = null;

					if (fromUser != null && ((FakeMove) fromUser).m_originColumn == -1)
						png.m_drawRequested = true;
					out.writeObject(fromUser);
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
			return;
		}
		catch (EOFException e)
		{
			e.printStackTrace();
			if (g.getHistory().size() != 0 && g.getHistory().get(g.getHistory().size() - 1).getResult() != null)
				return;
			if (!PlayNetGame.m_isRunning)
				return;
			JOptionPane.showMessageDialog(null, "Your opponent closed the game", "Oops!", JOptionPane.ERROR_MESSAGE);
			g.getBlackTimer().stopTimer();
			g.getWhiteTimer().stopTimer();
			Driver.getInstance().m_fileMenu.setVisible(true);
			Driver.getInstance();
			Driver.m_gameOptionsMenu.setVisible(false);
			Driver.getInstance().revertToMainPanel();
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		out.close();
		in.close();
		socket.close();
	}

}
