package gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkClient {

	public static void main(String[] args) throws Exception {

		Socket socket = null;
//		PrintWriter out = null;
//		BufferedReader in = null;
		ObjectOutputStream out = null;
        ObjectInputStream in = null;

        
        int hostIndex = 1;
        
        while(true){
			try {
				String num = ((hostIndex+"").length()==1)?("0"+hostIndex):hostIndex+"";
				socket = new Socket("cslab"+num, 27335);
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
				break;
			} catch (Exception e) {
				hostIndex = (hostIndex+1)%25;
				
	        	if(hostIndex == 0)
	        		hostIndex++;
				//e.printStackTrace();
			}
        }

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		Object fromServer;
		Object fromUser;

		while ((fromServer = in.readObject()).toString() != null) {
			System.out.println("Server: " + fromServer.toString());
			if (fromServer.toString().equals("Bye"))
				break;

			fromUser = new TestPacket(stdIn.readLine());
			if (fromUser.toString() != null) {
				System.out.println("Client: " + fromUser.toString());
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
