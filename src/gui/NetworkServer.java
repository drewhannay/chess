package gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkServer {

	public static void main(String[] args) throws Exception {
		 
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(27335);
        } catch (Exception e) {
        	e.printStackTrace();
        }
 
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (Exception e) {
        	e.printStackTrace();
        }
 
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                clientSocket.getInputStream()));
        String inputLine, outputLine;
        NetworkProtocol np = new NetworkProtocol();
 
        outputLine = np.processInput(null);
        out.println(outputLine);
 
        while ((inputLine = in.readLine()) != null) {
             outputLine = np.processInput(inputLine);
             out.println(outputLine);
             if (outputLine.equals("Bye."))
                break;
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
	
}
