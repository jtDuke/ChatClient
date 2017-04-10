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
    	ArrayList<Connection> clientList = new ArrayList();
    	
    	try
    	{
	        int port = 7279;
	        ServerSocket ss = new ServerSocket(port);
	        ss.setSoTimeout(500);
	        String data = "";

	        while (true)
	        {
	        	ArrayList<Connection> discList = new ArrayList();
	        	try
	        	{
	        		Socket clientSocket = ss.accept();
	        		if (clientSocket != null)
	        		{
	        			Connection conn = new Connection(clientSocket);
	        			clientList.add(conn);   		// add new connection to list	        			
	        			data = conn.readMessage();		// read user's name
	        			System.out.println(conn.getUsername() + " is now connected.");
	        		}
	        		
	        	}catch(SocketTimeoutException e){}

	        	// loop through connections checking for messages
	        	for(Connection sender : clientList)
	        	{
	        		if (sender.in.available() > 0)  // is there a message to be read?
	        		{
	        			data = sender.readMessage();
	        			sender.writeMessage(data, clientList);
	        			System.out.println(sender.getUsername() + ": " + data )	;
	        		}
	        		if (sender.errorFlag == true)
	        			discList.add(sender);
	        	}

	        	for (Connection c : discList)
				{
					System.out.println(c.getUsername() + " has disconnected.");
					clientList.remove(c);
					c.clientSocket.close();
				}
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
	boolean errorFlag = false;

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

	public String readMessage()
	{
		String data = "";

		try 
		{	
			if (reads == 0)
			{
				data = in.readUTF();
				this.setUsername(data);
				reads++;
				return data;
			}
			else 
				return in.readUTF();	
		}
		catch(IOException e)
		{ 
			this.errorFlag = true;
			return data;
		}
	}

	public void writeMessage(String data, ArrayList<Connection> clientList)
	{
		for (Connection recvr : clientList)
		{	
		    if (recvr == this)
				continue; // skip sender

			try
			{
			 	recvr.out.writeUTF(this.username + " says: " + data);		
			}
			catch (IOException e) 
			{
				recvr.errorFlag = true;
			}
		}	    
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
