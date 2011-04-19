package net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MultiServerThread extends Thread {

	private Socket socket = null;

	public MultiServerThread(Socket socket) {
		super("MultiServerThread");
		this.socket = socket;
	}

	public void run() {

		try {

			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			Object input;
			Object output;
			NetworkProtocol np = new NetworkProtocol();

			output = np.processInput(null);
			out.writeObject(output);

			try{
				while ((input = in.readObject()) != null) {
					output = np.processInput(input);
					out.writeObject(output);
					out.flush();
					if (input.toString().equalsIgnoreCase("Bye"))
						break;
				}
			}catch(Exception e){
				//The try catch is here because when the client closes the connection,
				//(input = in.readObject()) throws an End Of File Exception
			}
			out.close();
			in.close();
			socket.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
