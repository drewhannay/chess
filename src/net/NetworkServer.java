package net;

import gui.Driver;
import gui.PlayNetGame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

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
        
        Object fromUser;
        Object fromServer;
        
        Game g = PlayNetGame.getGame();
        
        fromServer = g;
        if(fromServer!=null)
        	out.writeObject(fromServer);
        
        Driver.getInstance().setPanel(png);
        
        while(png.netMove == null)
        	Thread.sleep(0);
		fromServer = png.netMove;
		png.netMove = null;

		out.writeObject(fromServer);
		out.flush();
        
        try{
	        while ((fromUser = in.readObject()) != null) {
				NetMove toMove = (NetMove)fromServer;
				if(toMove.originCol == -1){
					int surrender = JOptionPane.showConfirmDialog(null, "Player has requested a Draw. Do you accept?", "Draw",
							JOptionPane.YES_NO_OPTION);
					if(surrender == 0){
						JMenu menu = png.createMenu();
						menu.setVisible(true);
						out.writeObject(new NetMove(-1,-1,-1,-1,-1,null));
						break;
					}
				}
				g.playMove(g.fakeToRealMove((NetMove)fromUser));

				while(png.netMove == null)
					Thread.sleep(0);

				fromServer = png.netMove;
				png.netMove = null;

				out.writeObject(fromServer);
				out.flush();
				if(g.getLastMove().getResult()!=null)
					break;
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
