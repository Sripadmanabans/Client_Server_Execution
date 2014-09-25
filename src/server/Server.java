package server;

//Used for the sockets and its connections
import java.net.*;

//Used for the HashMap to store the list of programs of the user
import java.util.HashMap;

//Used to create the Object Streams to receive and send data from sockets
import java.io.*;

//The class used to encapsulate the data
import message.Message;

public class Server extends Thread
{
	//Used to create the ServerSocket
	private ServerSocket serverSocket;
	
	//Used in the reading of the config file
	private static BufferedReader br;
	
	//Path to the config file
	private static String path;
	
	//The server port number
	private static final int port = 9000;
	
	//Constructor of the class Server needed to set the Server port and the Timeout
	public Server(int port) throws IOException
	{
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
	}
	
	//Used for executing the .exe files
	private static void executeProgam(String exePath) 
	{
		try 
		{
			//Running the .exe file in the give path
			Runtime.getRuntime().exec(exePath);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//Used to validate the user and to return the list of programs that the valid user is eligible to execute
	private static String validateUser(String configPath, Message m)
	{
		try 
		{
			//Used to read the configuration file
			FileReader fr = new FileReader(configPath);
			br = new BufferedReader(fr);
			String read;
			while((read = br.readLine()) != null)
			{
				String temp[] = read.split("\\$:");
				String userName = temp[0];
				String password = temp[1];
				String programs = temp[2];
				
				//Checking if the userName is valid 
				if(userName.equals(m.getUserName()))
				{
					//If the user name is valid checking for the password 
					if(password.equals(m.getPassword()))
					{
						return programs;
					}
				}
			}
			br.close();
			fr.close();
			
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("The Config file you mentioned is not in here.");
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
		
		//Returning null if the user is not valid
		return null;
	}
	
	//Used to send the appropriate message the Client based on the situation
	public static void sendMessage(Socket server, Message m, String value)
	{
		//Creating the Object Stream to send the info to Client
		ObjectOutputStream out;
		try 
		{
			out = new ObjectOutputStream(server.getOutputStream());
			
			//Setting the message to be shown to the Client
			m.setMessage(value);
			
			//Writing into the stream to be received by the client
			out.writeObject(m);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//Used to Start the server
	public void run()
	{
		while(true)
		{
			try
			{
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				
				//Listening for a connection to the server
				Socket server = serverSocket.accept();
				
				//Connected to the Client
				System.out.println("Just connected to " + server.getRemoteSocketAddress());
				
				//Object stream to receive the data that the Client sends
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());
				
				//ip is the object that contains the excapsulated data
				Message ip = (Message) in.readObject();
				
				/*Hold the response to if the user is valid or not. And if Valid hold the exe files that the user
				can execute*/
				String hold = validateUser(path, ip);
				
				//If loops checks if the user is valid
				if(hold == null)
				{
					//Sending a message back to the Client saying the user is not a Valid user
					System.out.println("The user is not validated");
					sendMessage(server, ip, "Invalid User");
				}
				else
				{
					//This is the map used to hold all the files that can be executed
					HashMap<String, Integer> program = new HashMap<String, Integer>();
					
					//Holds all the programs that can be executed
					String allowedPrograms[] = hold.split(",");
					
					//Holds all the programs that the user wants executed
					String userPrograms[] = ip.getPrograms();
					
					//Populating the HashMap
					for(int i = 0; i < allowedPrograms.length; i++)
					{
						program.put(allowedPrograms[i], 1);
					}
					
					//Checking to see what executables can be run
					for(int i = 0; i < userPrograms.length; i++)
					{
						//If the file is present in the list of programs that the user is valid to run
						if(program.containsKey(userPrograms[i]))
						{
							//Calls the function to execute the file in the path
							executeProgam(userPrograms[i]);
							String message = "Program " + userPrograms[i] + " is executing";
							
							//Sends a message to the Client saying the program is being executed
							sendMessage(server, ip, message);
						}
						else
						{
							String message = "The program " + userPrograms[i] + " is not accessable by you";
							
							//Sending a message saying the user is not allowed to run the program
							sendMessage(server, ip, message);
						}
					}
				}
				
				//Closing the Server
				server.close();
			}
			catch(SocketTimeoutException s)
			{
				System.out.println("Socket timed out!");
				break;
			}
			catch(IOException e)
			{
				e.printStackTrace();
				break;
			} 
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
				break;
			}
		}
	}
	
	public static void main(String [] args)
	{
		try
		{
			//Creating an instance of the Server to be run
			Thread t = new Server(port);
			path = args[0];
			//Starting the Server
			t.start();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}