import java.io.*;
import java.util.*;
import java.lang.*;

public class tcss543
{
  public static void main(String[] argv)
  {
    // read input from command line arguments or pipe
    if (argv.length == 0)
    {
      Vector<String> inputs = new Vector<String>();
      BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
      String input;
      try
      {
        while((input=bufReader.readLine()) != null)
        { 
          inputs.add(input);
        }
      }
      catch (Exception e)
      {
      }
      FordFulkerson.Run(inputs, false);
      FordFulkerson.Run(inputs, true);
    }
  }

}
