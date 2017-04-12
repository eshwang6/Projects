import java.io.*;
import java.util.*;
import java.lang.*;

public class ScalingFordFulkerson
{
  public static void main(String[] argv)
  {
    // read input from command line arguments or pipe
    if (argv.length == 0)
    {
      BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
      String input;
      try
      {
        while((input=bufReader.readLine()) != null)
        { 
          FordFulkerson.Run(input, true);
        }
      }
      catch (Exception e)
      {
      }
    }
    else
    {
      for (String filename : argv)
      {
        FordFulkerson.Run(filename, true);
      }
    }
  }
}
