import java.io.*;
import java.net.*;

public class TCPServer {
	
	//helper class for HashMap and Log
	private static KVParser parser = new KVParser();
	
	private static Log serverLog = new Log("TCPServerLog.log");
	

	public static void main(String[] args) {

		ServerSocket listenSocket = null;
		Socket clientSocket = null;
		
		try{
			
			int port;
			
			try {
				
				//try to get the port # from the command line argument
				//if there is no argument or invalid arguments, it throws Exception.
				//display the Exception on the screen and write the log about the exception.
				port = Integer.parseInt(args[0]);
				
			} catch (Exception e) {

				serverLog.writeLog("TCP Server failed due to Invalid Argument(s).");
				System.out.println("Usage : TCP Server Port");
				System.out.println();
				
				return;
				
			}
			
			//create a ServerSocket and listen to the port
			listenSocket = new ServerSocket(port);
			
			//write the log that the socket was created and note the port
			serverLog.writeLog("Created a socket and listening to port " + port);
			
			while(true) {
				
				clientSocket = listenSocket.accept();
				
				//Obtain the client information and write the log of it.
				String client = clientSocket.getInetAddress().getHostAddress() + " Port " + clientSocket.getPort();
				serverLog.writeLog("Client " + client + " has been connected.");
				
				//create a data input/output stream
				DataInputStream input = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

				//infinitely receive from the socket until termination (i.e. Ctrl+C, or terminate)
				while(true){
					
					String receivedData;
					
					try {
						
						receivedData = input.readUTF();
						
					} catch (EOFException e) {
						
						//EOFException is thrown by DataInputStream.readUTF()
						//if there is a sudden interruption on the connection from the client
						//write the log, close the socket, and listen for another client
						serverLog.writeLog("EOFException : " + e.getMessage());
					
						try {
							
							clientSocket.close();
							
						} catch (IOException e2) {
							
							//IOException is thrown if closing the socket was unsuccessful.
							//write the log and force closing and exit
							serverLog.writeLog("Unable to close the socket. IOException : " + e2.getMessage());
							clientSocket = null;
							return;
						
						}
						
						break;
						
					}
					
					//write the log about the received data from the client
					//if the data is longer than 50, only write and display about the first 50
					serverLog.writeLog("Data received from " + client +" : " + 
							(receivedData.length()> 50 ? receivedData.substring(0, 50) + "..." : receivedData));
					
					String dataOutput;
					
					try {
						
						//process the data with the KVParser
						//the KVParse will throw an Exception if the input data is malformed
						//write the log about the received data after process
						dataOutput = parser.KVParse(receivedData);
						serverLog.writeLog("Received Data : " + dataOutput);
						
						
					} catch (Exception e) {
						
						//write the log about the malformed request
						dataOutput = "Malformed request";
						serverLog.writeLog("Received malformed request of length " + receivedData.length() + " from " + client + ". Exception:" + e.getMessage());
					
					}
					
					//send the reply back to the client
					output.writeUTF(dataOutput);
					
				}
				
			}
		
		} catch(IOException e) {

			//IOException is thrown if there is an I/O error when opening the socket.
			//write the log about the IOException
			serverLog.writeLog("IOException: " + e.getMessage());
			
		} catch(Exception e){
			
			//write the log for any other Exceptions
			serverLog.writeLog("Exception : "+ e.getMessage());
			
		} finally{

			//if the socket is not closed, close before exiting the program
			if(clientSocket!=null){
				
				try {
					
					clientSocket.close();
					
				} catch (IOException e) {
					
					//IOException is thrown if closing the socket was unsuccessful.
					//write the log about IOException
					serverLog.writeLog("Unable to close the socket. IOException : " + e.getMessage());	
					
				}
				
			}
			try{
				
				//if the socket is not closed, close before exiting the program
				if(listenSocket!=null)
				listenSocket.close();
				
			} catch (IOException e){
				
				//IOException is thrown if closing the socket was unsuccessful.
				//write the log about IOException
				serverLog.writeLog("Unable to close the socket. IOException : "	+ e.getMessage());
				
			}
			
		}

	}
	
}