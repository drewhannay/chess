package net;

import gui.Driver;
import gui.PlayGameScreen;
import gui.PlayNetGameScreen;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

import logic.Result;
import models.Game;
import utility.GuiUtility;
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
				// if (NewGameMenu.mIsCancelled)
				// return;
			}
		}
		AnimatedLabel.m_isFinished = true;
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

		Object fromServer = null;
		Object fromUser;

		Game g = (Game) in.readObject();
		PlayNetGameScreen png = GuiUtility.getChessCrafter().getNetGameScreen(g, false, true);
		PlayGameScreen pgs = GuiUtility.getChessCrafter().getPlayGameScreen(g);
		pgs.resetTimers();
		GuiUtility.getChessCrafter().setPanel(png);

		try
		{
			while (GuiUtility.getChessCrafter().getNetGameScreen().isRunning())
			{
				while (g.isBlackMove() == false && GuiUtility.getChessCrafter().getNetGameScreen().isRunning())
				{
					fromServer = in.readObject();
					FakeMove toMove = (FakeMove) fromServer;

					if (toMove.mOriginColumn == -1)
					{
						int surrender = JOptionPane.showConfirmDialog(null, Messages.getString("NetworkClient.otherRequestedDraw"), //$NON-NLS-1$
								Messages.getString("NetworkClient.draw"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
						if (surrender == 0)
						{
							// if this player also accepts the Draw
							// write out a new object which shows you accepted
							// the Draw
							out.writeObject(new FakeMove(-2, -2, -2, -2, -2, null));
							Result result = Result.DRAW;
							result.setGuiText(Messages.getString("NetworkClient.gameEndedInDraw")); //$NON-NLS-1$
							g.getLastMove().setResult(result);
							GuiUtility.getChessCrafter().getPlayGameScreen(g).endOfGame(result);
							throw new Exception();
						}
						else
						{
							// else, write out an object which shows you did NOT
							// accept the Draw
							out.writeObject(new FakeMove(-3, -3, -3, -3, -3, null));
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
				while (g.isBlackMove() && png.isRunning())
				{
					while (png.getNetMove() == null && !png.drawRequested() && png.isRunning())
						Thread.sleep(0);
					if (png.drawRequested())
					{
						fromServer = in.readObject();
						FakeMove toMove = (FakeMove) fromServer;
						if (toMove.mOriginColumn == -2)
						{
							Result result = Result.DRAW;
							result.setGuiText(Messages.getString("NetworkClient.gameEndedInDraw")); //$NON-NLS-1$
							g.getLastMove().setResult(result);
							pgs.endOfGame(result);
							png.setDrawRequested(false);
							throw new Exception();
						}
						else if (toMove.mOriginColumn == -3)
						{ // If the response is an unaccepted Draw request, do
							// not perform the Move.
							JOptionPane.showMessageDialog(null, Messages.getString("NetworkClient.requestDeclined"), //$NON-NLS-1$
									Messages.getString("NetworkClient.denied"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
							png.setDrawRequested(false);
							continue;
						}
					}

					fromUser = png.getNetMove();
					png.setNetMove(null);

					if (fromUser != null && ((FakeMove) fromUser).mOriginColumn == -1)
						png.setDrawRequested(true);
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
			JOptionPane
					.showMessageDialog(
							null,
							Messages.getString("NetworkClient.opponentClosedGame"), Messages.getString("NetworkClient.oops"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			Driver.getInstance().setFileMenuVisibility(true);
			Driver.getInstance().setOptionsMenuVisibility(false);
			Driver.getInstance().revertToMainPanel();
			return;
		}
		catch (EOFException e)
		{
			e.printStackTrace();
			if (g.getHistory().size() != 0 && g.getHistory().get(g.getHistory().size() - 1).getResult() != null)
				return;
			if (!GuiUtility.getChessCrafter().getNetGameScreen(g, false, g.isBlackMove()).isRunning())
				return;
			JOptionPane
					.showMessageDialog(
							null,
							Messages.getString("NetworkClient.opponentClosedGame"), Messages.getString("NetworkClient.oops"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			g.getBlackTimer().stopTimer();
			g.getWhiteTimer().stopTimer();
			Driver.getInstance().setFileMenuVisibility(true);
			Driver.getInstance().setOptionsMenuVisibility(false);
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
