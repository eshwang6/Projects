import java.net.*;
import java.io.*;

public class TCPClient {
	
	//helper class for writing the log
	private static Log clientLog = new Log("TCPClientLog.log");

	//set the connection timeout to 5s for waiting responses from the server
	private static final int CONNECTION_TIMEOUT = 5000;
	

	public static void main(String[] args) {

		Socket socket = null;
		
		try{
			
			int port = 0;
			
			try {
				
				//try to get the port # from the command line argument
				//if there is no argument or invalid arguments, it throws Exception.
				//display the Exception on the screen and write the log about the exception.
				port = Integer.parseInt(args[1]);
				
				
			} catch (Exception e) {

				clientLog.writeLog("TCP Client failed due to Invalid Argument(s)");
				System.out.println("Usage: TCPClient server-host-address server-port");
				System.out.println();
				
				return;
			
			}
			
			//create a Socket with timeout 
			socket = new Socket(args[0], port);
			socket.setSoTimeout(CONNECTION_TIMEOUT);
			
			//write the log about the information of the server
			String serverHost = args[0] + " : " + port;
			InetAddress serverAdd = InetAddress.getByName(args[0]);
			clientLog.writeLog("Connected to the server " + serverHost);
			
			//create a data input/output stream
			DataInputStream in = new DataInputStream( socket.getInputStream());
			
			DataOutputStream out = new DataOutputStream( socket.getOutputStream());
			
			//list of 66 commands
			String[] cmds = {

					//10 PUT operation to store 10 Key-value pairs
					"put(A,10)", "put(B,20)", "put(C,30)", "put(D,40)", "put(E,50)",
					
					"put(F,60)", "put(G,70)", "put(H,80)", "put(I,90)", "put(J,100)",

					//10 GET operation to get the value
					"get(A)", "get(B)", "get(C)", "get(D)", "get(E)",
					
					"get(F)", "get(G)", "get(H)", "get(I)", "get(J)",
					
					//10 PUT operation to update the values on existing keys
					"put(A,100)", "put(B,200)", "put(C,300)", "put(D,400)", "put(E,500)",
					
					"put(F,600)", "put(G,700)", "put(H,800)", "put(I,900)", "put(J,1000)",

					//10 GET operations
					"get(A)", "get(B)", "get(C)", "get(D)", "get(E)",

					"get(F)", "get(G)", "get(H)", "get(I)", "get(J)",

					//10 DELETE operations to remove key-value pairs
					"delete(A)", "delete(B)", "delete(C)", "delete(D)", "delete(E)",
					
					"delete(F)", "delete(G)", "delete(H)", "delete(I)", "delete(J)",

					//10 GET operations to get values on keys that are deleted
					"get(A)", "get(B)", "get(C)", "get(D)", "get(E)",
					
					"get(F)", "get(G)", "get(H)", "get(I)", "get(J)",


					//try 6 invalid/incorrect commands to test for failure
					"get(S)", "delete(P)", "put(SUPAMAN)", "get(A,50)", "delete(A,50)", "IAMBATMAN"
			};
			
			//send commands to the server
			for(String cmd : cmds) {
				
				//send each commands to the server
				clientLog.writeLog("Command "+ cmd +" sending to "+ serverHost);

				out.writeUTF(cmd);
				
				
				try {
					
					String receivedData = in.readUTF();
					
					//write the log about received data
					clientLog.writeLog("Received : "+ receivedData) ;
					
				} catch (SocketTimeoutException e) {
					
					//if the server does not respond within 5s, SocketTimeoutException is thrown.
					//write the log about such Exception
					clientLog.writeLog("Connection Timeout. Server not responding within 5s." + e.getMessage());
					
				}
				
			}
			
		} catch (UnknownHostException e){
			
			//if the server host address cannot be determined, UnknownHostException is thrown from Socket
			//write the log about the UnknownHostException
			clientLog.writeLog("Host cannot be determined. UnknownHostException : "+e.getMessage());
		
		} catch (EOFException e){
			
			//EOFException is thrown if the input stream is unexpectedly terminated
			//write the log about the EOFException
			clientLog.writeLog("The stream was unexpectedly shut down. EOFException : "+e.getMessage());
		
		} catch (IOException e){
			
			//IOException is thrown if the stream is closed
			//write the log about the IOException
			clientLog.writeLog("The stream has been closed. IOException : "+e.getMessage());
		
		} finally {
			
			//if the socket is not closed, close before exiting the program
			if(socket!=null){
				
				try {
					
					socket.close();
					
				}catch (IOException e){
					
					//IOException is thrown if closing the socket was unsuccessful.
					//write the log about IOException
					clientLog.writeLog("Unable to close the socket. IOException : " + e.getMessage());	
					
				}
				
			}
			
		}
		
	}

}
