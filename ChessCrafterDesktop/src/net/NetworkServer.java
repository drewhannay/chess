package net;

import gui.Driver;
import gui.PlayGamePanel;
import gui.PlayNetGamePanel;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

import utility.GuiUtility;

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
	public void host(PlayNetGamePanel png) throws Exception
	{
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		serverSocket = new ServerSocket(27335);
		serverSocket.setSoTimeout(1000);
		while (clientSocket == null)
		{
			try
			{
				GuiUtility.getChessCrafter().getPlayGameScreen().resetTimers();

				clientSocket = serverSocket.accept();
			}
			catch (Exception e)
			{
//				if (NewGameMenu.mIsCancelled)
//					return;
			}
		}

		AnimatedLabel.m_isFinished = true;
		ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

		Object fromUser;
		Object fromServer;

		Game g = PlayGamePanel.getGame();
		fromServer = g;
		if (fromServer != null)
			out.writeObject(fromServer);
		GuiUtility.getChessCrafter().getPlayGameScreen().resetTimers();
		Driver.getInstance().setPanel(png);
		try
		{
			while (g.isBlackMove() == false)
			{
				while (PlayNetGamePanel.mNetMove == null)
					Thread.sleep(0);
				fromServer = PlayNetGamePanel.mNetMove;
				PlayNetGamePanel.mNetMove = null;

				out.writeObject(fromServer);
				out.flush();
			}

			while (PlayNetGamePanel.mIsRunning)
			{
				while (g.isBlackMove() == true && PlayNetGamePanel.mIsRunning)
				{
					fromUser = in.readObject();
					FakeMove toMove = (FakeMove) fromUser;
					if (toMove.mOriginColumn == -1)
					{
						int surrender = JOptionPane.showConfirmDialog(null, Messages.getString("NetworkServer.otherRequestedDraw"), //$NON-NLS-1$
								Messages.getString("NetworkServer.draw"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
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
							result.setGuiText(Messages.getString("NetworkServer.endedInDraw")); //$NON-NLS-1$
							g.getLastMove().setResult(result);
							GuiUtility.getChessCrafter().getPlayGameScreen().endOfGame(result);
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

				while (g.isBlackMove() == false && PlayNetGamePanel.mIsRunning)
				{
					while (PlayNetGamePanel.mNetMove == null && !png.mDrawRequested && PlayNetGamePanel.mIsRunning)
						Thread.sleep(0);
					if (png.mDrawRequested)
					{
						fromUser = in.readObject();
						FakeMove toMove = (FakeMove) fromUser;
						if (toMove.mOriginColumn == -2)
						{
							Result result = Result.DRAW;
							result.setGuiText(Messages.getString("NetworkServer.endedInDraw")); //$NON-NLS-1$
							g.getLastMove().setResult(result);
							GuiUtility.getChessCrafter().getPlayGameScreen().endOfGame(result);
							png.mDrawRequested = false;
							throw new Exception();
						}
						else if (toMove.mOriginColumn == -3)
						{ // If the response is an unaccepted Draw request, do
							// not perform the Move.
							JOptionPane.showMessageDialog(null, Messages.getString("NetworkServer.requestDenied"), //$NON-NLS-1$
									Messages.getString("NetworkServer.denied"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
							png.mDrawRequested = false;
							continue;
						}
					}

					fromServer = PlayNetGamePanel.mNetMove;
					PlayNetGamePanel.mNetMove = null;

					if (((FakeMove) fromServer).mOriginColumn == -1)
						png.mDrawRequested = true;

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
			JOptionPane.showMessageDialog(null, Messages.getString("NetworkServer.opponentClosed"), Messages.getString("NetworkServer.oops"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			Driver.getInstance().setFileMenuVisibility(true);
			Driver.getInstance().setOptionsMenuVisibility(false);
			Driver.getInstance().revertToMainPanel();
			serverSocket.close();
			return;
		}
		catch (EOFException e)
		{
			e.printStackTrace();
			if (g.getHistory().size() != 0 && g.getHistory().get(g.getHistory().size() - 1).getResult() != null)
				return;
			JOptionPane.showMessageDialog(null, Messages.getString("NetworkServer.opponentClosed"), Messages.getString("NetworkServer.oops"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			g.getBlackTimer().stopTimer();
			g.getWhiteTimer().stopTimer();
			Driver.getInstance().setFileMenuVisibility(true);
			Driver.getInstance().setOptionsMenuVisibility(false);
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
