import java.io.*;
import java.text.*;
import java.util.*;

public class Log {

	private String logFile;
	
	//Initialization of the log file
	public Log(String input_log){
		
		logFile=input_log;
		
	}

	//write the log and print to the screen
	public void writeLog(String inputData){
		
		try {
			
			//Open the file and write the log with time stamp and close the file
			PrintWriter pw = new PrintWriter(new FileWriter(logFile, true));
			DateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss.SSS z Z");
			Date currentDate = new Date(System.currentTimeMillis());  
			
			pw.println((currentDateFormat.format(currentDate) + " : " + inputData));
			System.out.println(inputData);
			
			pw.close();
			
		} catch ( IOException e) {
			
			//If it fails to write the log on the log file, it throws IOException.
			//Display the exception on the screen
			System.out.println("Unable to write the log on " + logFile + " IOException -" + e.getMessage());
		
		}
		
	}
	
	
	
	
}
