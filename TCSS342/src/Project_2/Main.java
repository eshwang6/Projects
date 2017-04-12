package Project_2;


import java.io.*;
import java.util.*;

public class Main {

	public static void main(String[] args) {

		long startTime;
		long endTime;
		
		try {
			
//			File inputFile = new File("src/Test.txt");
			File inputFile = new File(args[0]);
			
			FileReader inputStream = new FileReader(inputFile);
			StringBuilder sb = new StringBuilder();
			int index;
			
			while((index = inputStream.read()) != -1){
				sb.append((char) index);
			}
			
			
			startTime = System.currentTimeMillis();
			CodingTree ct = new CodingTree(sb.toString());
			PrintStream ps = new PrintStream(new File("codes.txt"));
			ps.println(ct.codes);	
			output(ct.bits, new File("compressed.txt"));
			endTime = System.currentTimeMillis();
			
			long uncompressedSize = inputFile.length();
			long compressedSize = new File("compressed.txt").length();
			long elapsedTime = endTime - startTime;
			
			System.out.println("Uncompressed file size : "+uncompressedSize+" bytes");
			System.out.println("Compressed file size : "+compressedSize+" bytes");
			System.out.printf("Compression ratio : %d%%\n", compressedSize * 100/ uncompressedSize);
			System.out.println("Running time : "+elapsedTime+" milliseconds");
			
		} catch (Exception e) {
			
			System.out.println("Exception : "+e.getMessage());
			
		}
		
	}
	
	private static void output(List<Byte> encodedMsg, File file) {
		try {
			
			PrintStream ps = new PrintStream(file);
			byte[] result = new byte[encodedMsg.size()];
			
			for (int i = 0; i < result.length; i++) {
				result[i] = encodedMsg.get(i);
			}
			
			ps.write(result, 0, result.length);
			ps.close();
		
		} catch (Exception e) {
			
			System.out.println("Exception : " + e.getMessage());
		
		}
	}

}
