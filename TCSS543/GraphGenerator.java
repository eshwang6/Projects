import graphGenerationCode.Bipartite.*;
import graphGenerationCode.Mesh.*;
import graphGenerationCode.FixedDegree.*;
import graphGenerationCode.Random.*;
import java.io.*;

public class GraphGenerator
{
  public static void main(String argv[])
  {
    // shut up
    PrintStream silence = new PrintStream(new OutputStream(){
      public void write(int b) {}});
    System.setOut(silence);

    // these small graphs are for checking purpose
    BipartiteGraph.Generate(2, 2, 0.5, 10, 100, "bpt2x2.txt");
    MeshGenerator.Generate(2, 2, 100, "mesh2x2.txt", false);
    RandomGraph.Generate(5, 2, 10, 100, "fixed5v2epv.txt");
    BuildGraph.Generate(5, 50, 10, 100, "random5v50d.txt");

    for (int i : new int[] { 110, 120, 130, 140, 150})
    {
      String filename = String.format("bpt%1$dx%1$d.txt", i);
      BipartiteGraph.Generate(i, i, 0.5, 10, 100, filename);
    }

    for (int i: new int[] { 5, 10, 15, 20, 25, 30})
    {
      String filename = String.format("mesh%1$dx%1$d.txt", i);
      MeshGenerator.Generate(i, i, 100, filename, false);
    }

    for (int i : new int[] { 110, 120, 130, 140, 150})
    {
      String filename = String.format("fixed%dv%depv.txt", i, i/3);
      RandomGraph.Generate(i, i/3, 10, 100, filename);
    }

    for (int i : new int[] { 60, 70, 80, 90, 100})
    {
      String filename = String.format("random%dv%dd.txt", i, 50);
      BuildGraph.Generate(i, 50, 10, 100, filename);
    }
  }
}
