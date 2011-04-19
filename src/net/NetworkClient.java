package net;

import gui.TestPacket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkClient {

	public static void main(String[] args) throws Exception {

		Socket socket = null;
		ObjectOutputStream out = null;
        ObjectInputStream in = null;

        String num = "02";
		try {
			socket = new Socket("cslab"+num, 27335);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch(Exception e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		Object fromServer;
		Object fromUser = "";

		while ((fromServer = in.readObject()).toString() != null) {
			if(!fromServer.toString().equals(""))
				System.out.println("Server: " + fromServer.toString());
			if (fromUser.toString().equalsIgnoreCase("Bye"))
				break;

			System.out.print("=> ");
			fromUser = new TestPacket(stdIn.readLine());
			if (fromUser.toString() != null) {
				out.writeObject(fromUser);
				out.flush();
			}
		}

		out.close();
		in.close();
		stdIn.close();
		socket.close();
	}

}
