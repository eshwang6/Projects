import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MainNetworkFlow {

	public static void main(String[] args) {
		
		File graphsFolder = new File("./graphs");
		File[] allGraphs = graphsFolder.listFiles();

		for (int i = 0; i < allGraphs.length; i++) {
			if(!allGraphs[i].isHidden()) {
				System.out.println("running flow of <" + allGraphs[i] + ">");
				PreflowPush preflowPush = new PreflowPush(allGraphs[i].getPath());
				preflowPush.runPreflowPush();
				saveResult(preflowPush.maxFlow, preflowPush.elapsedTime, allGraphs[i].getName());
			}
		}
	}
	
	private static void saveResult(Double maxFlow, long elapsed, String fileName) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt", true));
			writer.write("graph: " + fileName + ", maxflow: " + maxFlow + ", elapsedTime: " + elapsed );
			writer.newLine();
			writer.flush();
			System.out.println("result of <" + fileName + "> saved successfully");
		}
		catch (Exception e) {
			System.out.println("exception in saving result of <" + fileName + "> error: " + e.getMessage());
		}
	}
}
