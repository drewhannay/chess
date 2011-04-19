package net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkClient {

    public static void main(String[] args) throws Exception {

        Socket socket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        
        ArrayList<Socket> sockets = new ArrayList<Socket>();

        
        for(int hostIndex = 1; hostIndex < 26; hostIndex++){
            try {
                String num = ((hostIndex+"").length()==1)?("0"+hostIndex):hostIndex+"";
                socket = new Socket("cslab"+num, 27336);

                sockets.add(socket); // won't happen if previous line throws exception

            } catch(Exception e) {
                /*
                System.out.println(e.getClass() + " / " + e.getMessage());
                hostIndex = (hostIndex+1)%26;
                
                if(hostIndex == 0)
                    hostIndex++;
                //e.printStackTrace();
                */
            }
        }

        System.out.println("The following hosts will accept this connection:");
        for (int i = 0; i < sockets.size(); i++)
            System.out.println(i + ". " + sockets.get(i).getInetAddress().getHostName());
        System.out.print("Pick one--> ");

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        int choice = Integer.parseInt(stdIn.readLine());
        
        for (int i = 0; i < sockets.size(); i++)
            if (i == choice)
                socket = sockets.get(i);

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
     
        Object fromServer;
        //Object fromUser = "";
        String fromUser = "";
        
        while ((fromServer = in.readObject()).toString() != null) {
            if(!fromServer.toString().equals(""))
                System.out.println("Server: " + fromServer.toString());
            if (fromUser.toString().equalsIgnoreCase("Bye"))
                break;
            
            System.out.print("=> ");
            fromUser = /*new TestPacket(*/stdIn.readLine()/*)*/;
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
