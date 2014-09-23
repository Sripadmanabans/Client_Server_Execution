package message;

import java.io.Serializable;

public class Message implements Serializable
{
	/**
	 * Needed by Serializable
	 */
	private static final long serialVersionUID = 1L;
	
	//Stores the User Name
	private String userName = null;
	
	//Store the Password
	private String password = null;
	
	//Stores the list of Programs to execute
	private String programs[] = null;
	
	//Stores the message that the Server whats to convey to the Client
	private String message = null;
	
	//Used to set the userName field
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	//Used to set the password field
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	//Used to the the list of programs field
	public void setPrograms(String program[])
	{
		programs = new String[program.length];
		System.arraycopy(program, 0, programs, 0, program.length);
	}
	
	//Used to set the message field by the server
	public void setMessage(String message)
	{
		this.message = message;
	}

	//Used to retrieve the value of userName
	public String getUserName()
	{
		return userName;
	}
	
	//Used to retrieve the value of password
	public String getPassword()
	{
		return password;
	}
	
	//Used to retrieve the value of the list of programs
	public String[] getPrograms()
	{
		return programs;
	}
	
	//Used to retrieve the value of message in client
	public String toPrint()
	{
		return message;
	}
}
