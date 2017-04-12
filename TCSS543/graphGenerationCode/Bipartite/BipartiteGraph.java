package graphGenerationCode.Bipartite;

import java.io.*;
import java.util.*;

public class BipartiteGraph
{
  public static void Generate(int n, int m, double maxProbability, int minCapacity, int maxCapacity, String fileName)
  {
		int i, j;
		double value, x;

		try
		{
			PrintWriter outFile = new  PrintWriter(new FileWriter(fileName));

			double[][] edge = new double[n][m];
			for(i=0; i<n; i++)
			{
				for(j=0; j<m; j++)
				{
					value = Math.random();
					if(value <= maxProbability)
						edge[i][j] = value;
					else
						edge[i][j] = 0;
				}
			}
			
			System.out.println("-----------------------------------------");
			System.out.println("\tSource\tSink\tCapacity");
			System.out.println("-----------------------------------------");			

			//computing the edges out of source
			for (i = 0; i < n; i++)
			{
				x=Math.random();
				//Compute a capacity in range of [minCapacity, maxCapacity]
				value = Math.floor(minCapacity + (x * (maxCapacity - minCapacity + 1)));
				System.out.println("\t" + "s" + "\tl" + (i + 1) + "\t" + (int)value);
				outFile.println("\t" + "s" + "\tl" + (i + 1) + "\t" + (int)value);
			}
			for(i=0; i<n; i++)
			{
				for(j=0; j<m; j++)
				{				
					if(edge[i][j] > 0)
					{
						edge[i][j] = Math.floor(minCapacity + (edge[i][j] * (maxCapacity - minCapacity + 1)));
						System.out.println("\tl"+ (i+1) + "\tr" + (j+1) + "\t" + (int)edge[i][j]);
						//computing for the vertices between source and sink and writing them to the output file
						outFile.println("\tl"+ (i+1) + "\tr" + (j+1) + "\t" + (int)edge[i][j]);
					}
				}
			}
			//computing the edges into the sink
			for (j=0; j < m; j++)
			{
				x=Math.random();
				value = Math.floor(minCapacity + (x * (maxCapacity - minCapacity + 1)));
				System.out.println("\tr" + (j+1) + "\t" + "t" + "\t" + (int)value);
				outFile.println("\tr" + (j + 1) + "\t" + "t" + "\t" + (int)value);
			}

			System.out.println("\n\nOutput is created at: \t" + fileName);
			outFile.close();
		}
		catch(Exception ex)
		{
			System.err.println(ex);
		}
  }
}
