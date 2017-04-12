import java.io.*;
import java.net.*;
import java.nio.charset.Charset;

public class UDPServer {
	
	//helper class in order to maintain the log and HashMap parse
	private static KVParser parser = new KVParser();
	
	private static Log serverLog = new Log("UDPServerLog.log");
	
	
	public static void main(String[] args) {

		DatagramSocket socket = null;
		
		try {
			
			int port = 0;
			try {

				//try to get the port # from the command line argument
				//if there is no argument or invalid arguments, it throws Exception.
				//display the Exception on the screen and write the log about the exception.
				port = Integer.parseInt(args[0]);
				
			} catch (Exception e) {
				
				serverLog.writeLog("UDP Server failed due to Invalid Argument(s).");
				System.out.println("Usage : UDP Server Port");
				System.out.println();
				
				return;
				
			}
			
			try {
				
				//create a DatagramSocket on the port
				//if it fails to create a DatagramSocket, it throws IOException.
				//display the Exception on the screen and write the log about it.
				socket = new DatagramSocket(port);
				serverLog.writeLog("Created UDP Server socket on port " + port);
				
			} catch (IOException e) {
				
				serverLog.writeLog("Creating UDP Server socket failed on port " + port);
				
			}
			
			//create a buffer
			byte[] buffer = new byte[1000];
			
			//infinitely receive from the socket until termination (i.e. Ctrl+C, or terminate)
			while(true){
				
				//store the incoming Datagram in the buffer
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				
				socket.receive(request);
				
				//convert the byte array to String (i.e. in a human-readable fashion)
				String receivedData = new String(request.getData(), 0, request.getLength(), "UTF-8");
				
				//obtain the Client IP address and port.
				String client = request.getAddress().getHostAddress() + " Port " + request.getPort();
				
				//write the log about the received data and the client info
				//if it is longer then 50, write only for the first 50.
				serverLog.writeLog("Data received from " + client +" : " + 
										(receivedData.length()> 50 ? receivedData.substring(0, 50) + "..." : receivedData));
				
				String dataOutput;

				try {
					
					//process the data with KVParser
					//the KVParse will throw an Exception if the input data is malformed
					dataOutput = parser.KVParse(receivedData);
					serverLog.writeLog("Received Data : " + dataOutput);
					
					
				} catch (Exception e) {
					
					//if it is a malformed input data, it writes the log that it is malformed.
					dataOutput = "Malformed request";
					serverLog.writeLog("Received malformed request of length " + receivedData.length() + " from " + client);
				
				}
				
				//to send the reply back to the client.
				byte[] stream = dataOutput.getBytes(Charset.forName("UTF-8"));
				DatagramPacket reply = new DatagramPacket(stream, stream.length, 
						request.getAddress(), request.getPort());
				
				socket.send(reply);
			
			}
			
		} catch (SocketException e){
			
			//if the socket could not be opened, it throws the SocketException
			//write the log with such Exception.
			serverLog.writeLog("SocketException : " + e.getMessage());
			
		} catch (IOException e) {
			
			//for any other IOException, write the log
			serverLog.writeLog("IOException : " + e.getMessage());
		
		} catch (Exception e){
			
			//write the log for any other Exceptions
			serverLog.writeLog("Exception : " + e.getMessage());
			
		}
		
		finally {
			
			//if the socket is not closed, close before exiting the program.
			if (socket != null){
				
				socket.close();
			
			}
			
		}
		
	}

}
