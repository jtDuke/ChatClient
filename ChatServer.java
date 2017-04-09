/**
 * Created by jtryg on 3/31/2017.
 */

import java.net.*;  // socket
import java.io.*;
import java.util.*;
import java.util.Date;


public class ChatServer {

    public static void main(String args[]) throws java.io.IOException
    {
    	ArrayList<Connection> clientList = new ArrayList<Connection>();
    	ArrayList<String> names = new ArrayList<String>();
    	try
    	{
	        int port = 7279;
	        ServerSocket ss = new ServerSocket(port);
	        ss.setSoTimeout(500);

	        while (true)
	        {
	        	try
	        	{
	        		Socket clientSocket = ss.accept();
	        		if (clientSocket != null)
	        		{
	        			Connection conn = new Connection(clientSocket);
	        			clientList.add(conn);	        			
	        			//System.out.println(clientList);
	        			conn.readMessage();
	        			System.out.println(conn.getUsername() + " is now connected.");
	        			
	        			//conn.writeMessage();
	        		}
	        		
	        	}catch(SocketTimeoutException e){}
			}
    	}catch(IOException e) {System.out.println("Listen:" + e.getMessage());}
	}
}


class Connection 
{
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	String username = "";
	int reads = 0;

	public Connection (Socket aClientSocket) 
	{
		try 
		{
			clientSocket = aClientSocket;
			in = new DataInputStream( clientSocket.getInputStream());
			out = new DataOutputStream( clientSocket.getOutputStream());
		}
		catch(IOException e) {System.out.println("Connection:" + e.getMessage());}
	}

	public void readMessage()
	{
		try
		{	
			if (reads == 0)
			{
				String data = in.readUTF();
				this.setUsername(data);
				this.writeMessage("You are now connected to the ChatServer.");
				reads++;
			}
			else
			{
				String data = in.readUTF();	
				System.out.println("Incoming message..:" + data);
			}
			
		}
		catch(IOException e) { System.out.println("Connection:" + e.getMessage()); }
	}

	public void writeMessage(String message)
	{
		try 
		{ 
			out.writeUTF(message); 
		}
		catch(IOException e) { System.out.println("Write Failed."); }
		
	}

	public void setUsername(String name)
	{
		this.username = name;
	}
	public String getUsername()
	{
		return this.username;
	}

}
