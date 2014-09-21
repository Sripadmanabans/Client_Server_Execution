This is a client server program.

The user gives who runs the client provides the server ip adress, the username, password and a 
list of programs that the user wants to execute on the server side.

The server has a config file which is used to validate the username and password.
If they are a valid user then the list of programs are compared with the list of programs that
the user is allowed to run (mentioned in the config file) on the server and the matching programs 
are run on the server.