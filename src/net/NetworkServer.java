package net;

import java.net.ServerSocket;

public class NetworkServer {

	public static void main(String[] args) throws Exception {

		boolean listening = true;

		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(27336);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}



		while(listening){
			new MultiServerThread(serverSocket.accept()).start();
		}

		serverSocket.close();
	}

}
