package client;

//Used for the sockets and its connections
import java.net.*;

//Used for the Object Stream to send and receive data from sockets 
import java.io.*;

//Class used to encapsulate the data
import message.Message;

public class Client
{
	//Port to access the Server
	private static final int port = 9000;

	public static void main(String [] args)
	{
		//Holds the server ipaddress
		String serverIp = null;
		
		//Holds the user name
		String userName = null;
		
		//Holds the password
		String password = null;
		
		//Holds the string value
		String value = null;
		
		int x = 0;
		while( x < args.length)
		{
			if(args[x].equals("--server"))
			{
				serverIp = args[++x];
				++x;
			}
			else if(args[x].equals("--user"))
			{
				userName = args[++x];
				++x;
			}
			else if(args[x].equals("--password"))
			{
				password = args[++x];
				++x;
			}
			else if(args[x].equals("--exec"))
			{
				value = args[++x];
				++x;
			}
			else
			{
				++x;
			}
		}
		
		try
		{
			System.out.println("Connecting to " + serverIp + " on port " + port);
			
			//Creating a socket connection with the Server
			Socket client = new Socket(serverIp, port);
         
			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			
			//Creating the stream to send info to the server
			OutputStream outToServer = client.getOutputStream();         
         	ObjectOutputStream out = new ObjectOutputStream(outToServer);
         	
         	//Separating the list of .exe files
         	String programs[] = value.split(",");
         	
         	//Creating an object to encapsulate the data to send to the Server 
         	Message ip = new Message();
         	ip.setUserName(userName);
         	ip.setPassword(password);
         	ip.setPrograms(programs);
         	
         	//Writing into the stream
         	out.writeObject(ip);
         
         	//Receiving the response from the Server
         	InputStream inFromServer = client.getInputStream();
         	for(int i = 0; i < programs.length; i++)
         	{
         		ObjectInputStream in = new ObjectInputStream(inFromServer);
         		Message op = (Message) in.readObject();
         		System.out.println(op.toPrint());
         		
         		//Exiting if the user is invalid
         		if(op.toPrint().equals("Invalid User"))
         		{
         			break;
         		}
         	}
         	client.close();
		}
		catch(IOException e)
      	{
    	  e.printStackTrace();
      	} 
		catch (ClassNotFoundException e) 
      	{
			e.printStackTrace();
		}
	}
}	