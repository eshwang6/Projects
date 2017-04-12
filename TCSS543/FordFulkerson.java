import java.io.*;
import java.util.*;
import java.lang.*;

public class FordFulkerson
{
  private static PrintStream OUT = System.out;
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
          Run(input, false);
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
        Run(filename, false);
      }
    }
  }

  public static void MuteOut()
  {
    PrintStream silence = new PrintStream(new OutputStream(){
      public void write(int b) {}});
    System.setOut(silence);
  }

  public static void UnmuteOut()
  {
    System.setOut(OUT);
  }

  public static void DebugMessage(String msg)
  {
    System.out.println(msg);
  }

  public static void Run(String filename, boolean scaling)
  {
    // mute debug messages
    MuteOut();

    SimpleGraph g = new SimpleGraph();
    Hashtable tab = GraphLoader.LoadSimpleGraph(g, filename);
    long start = System.currentTimeMillis();
    double maxFlow = Run(tab, g, scaling);
    long timespan = System.currentTimeMillis() - start;

    // done. unmute
    UnmuteOut();
    System.out.println(filename + "," + (scaling ? "Scaling" : "")  + "FordFulkerson," + "Max flow:" + Double.toString(maxFlow) + ",Run time(ms):" + Long.toString(timespan));
  }

  public static void Run(Vector<String> inputs, boolean scaling)
  {
    // mute debug messages
    MuteOut();

    SimpleGraph g = new SimpleGraph();
    Hashtable tab = GraphLoader.LoadSimpleGraph(g, inputs);
    long start = System.currentTimeMillis();
    double maxFlow = Run(tab, g, scaling);
    long timespan = System.currentTimeMillis() - start;

    // done. unmute
    UnmuteOut();
    System.out.println((scaling ? "Scaling" : "")  + "FordFulkerson," + "Max flow:" + Double.toString(maxFlow) + ",Run time(ms):" + Long.toString(timespan));
  }

  public static double Run(Hashtable tab, SimpleGraph g, boolean scaling)
  {
    HashMap<String, HashMap<String, Double>> currentFlow = new HashMap<String, HashMap<String, Double>>();

    int delta = 1;
    if (scaling)
    {
      double maxOutOfS = 0;
      Vertex s = (Vertex)tab.get("s");
      Iterator itere = g.incidentEdges(s);
      while (itere.hasNext())
      {
        Edge e = (Edge)itere.next();
        if ((double)e.getData() > maxOutOfS)
        {
          maxOutOfS = (double)e.getData();
        }
      }
      delta = (int)Math.pow(2, (int)(Math.log(maxOutOfS) / Math.log(2)));
    }

    // Ford-Fulkerson implementation
    while (delta >= 1)
    {
      while (true)
      {
        Hashtable tabr = new Hashtable();
        SimpleGraph gr = new SimpleGraph();
        GetResidualGraph(g, tabr, gr, currentFlow, delta);
        Vector<Edge> augPath = GetAugmentingPath(tabr, gr);
        if (augPath.size() == 0)
        {
          break;
        }
        UpdateFlow(currentFlow, augPath);
      }
      delta /= 2;
    }

    VerifyFlow(tab, g, currentFlow);
    return GetFlowValue(currentFlow);
  }

  private static void VerifyFlow(Hashtable tab, SimpleGraph g, HashMap<String, HashMap<String, Double>> flow)
  {
    // check the following:
    // 1. every edge in the flow has an edge in the graph in correct direction
    // 2. every edge in the flow has a value >=0 and <= capacity
    // 3. every vertex incoming flow equals to outgoing flow
    for (String v1 : flow.keySet())
    {
      for (String v2 : flow.get(v1).keySet())
      {
        Vertex v = (Vertex)tab.get(v1);
        Iterator itere = g.incidentEdges(v);
        boolean found = false;
        while (itere.hasNext())
        {
          Edge e = (Edge)itere.next();
          if (e.getSecondEndpoint().getName() == v2)
          {
            found = true;
          }
        }
        if (!found)
        {
          System.err.println("VerfyFlowErr flow has no edge " + v1 + "," + v2 + " flow " + flow.get(v1).get(v2));
        }
      }
    }

    Iterator iterv = g.vertices();
    while (iterv.hasNext())
    {
      double in = 0;
      double out = 0;
      Vertex v = (Vertex)iterv.next();
      Iterator itere = g.incidentEdges(v);
      while (itere.hasNext())
      {
        Edge e = (Edge)itere.next();
        String src = e.getFirstEndpoint().getName().toString();
        String tgt = e.getSecondEndpoint().getName().toString();
        // there is a flow in this edge
        if (flow.containsKey(src) && flow.get(src).containsKey(tgt))
        {
          double value = flow.get(src).get(tgt);
          if (value > (double)e.getData())
          {
            System.err.println("VerfyFlowErr overflow " + src + "," + tgt + " flow " + value + " cap " + e.getData());
          }
          else if (value < 0)
          {
            System.err.println("VerfyFlowErr negative flow " + src + "," + tgt + " flow " + value + " cap " + e.getData());
          }

          // this is an incoming flow
          if (tgt.equals(v.getName().toString()))
          {
            in += value;
          }
          // this is an outgoing flow
          else
          {
            out += value;
          }
        }
      }
      if (in != out && !v.getName().toString().equals("s") && !v.getName().toString().equals("t"))
      {
        System.err.println("VerfyFlowErr unbalance " + v.getName() + " in " + in + " out " + out);
      }
    }
  }

  private static double GetFlowValue(HashMap<String, HashMap<String, Double>> flow)
  {
    double value = 0;
    HashMap<String, Double> sFlow = flow.get("s");
    if (sFlow != null)
    {
      for (Double flowValue : sFlow.values())
      {
        value += flowValue;
      }
    }

    // print every edge of the flow
    for (String v1 : flow.keySet())
    {
      for (String v2 : flow.get(v1).keySet())
      {
        DebugMessage(v1 + " " + v2 + " " + flow.get(v1).get(v2));
      }
    }

    return value;
  }

  private static void GetResidualGraph(SimpleGraph g, Hashtable tabr, SimpleGraph gr, HashMap<String, HashMap<String, Double>> currentFlow, int minCap)
  {
    // add each vertex in g to gr
    Iterator iterv = g.vertices();
    while (iterv.hasNext())
    {
      // v1o vertex in the original graph
      Vertex v1o = (Vertex)iterv.next();
      DebugMessage("Gr iterating " + v1o.getName().toString() + " edges");

      // create new vertex and add it to residual graph if not already in there
      Vertex v1r = (Vertex)tabr.get(v1o.getName().toString());
      if (v1r == null)
      {
        v1r = gr.insertVertex(null, v1o.getName());
        tabr.put(v1r.getName().toString(), v1r);
        DebugMessage("Add1 " + v1r.getName().toString() + " to gr");
      }

      HashMap<String, Double> v1Flow = currentFlow.get(v1o.getName().toString());
      // for each edge in the original graph, add forward and backward edges to residual graph
      Iterator itere = g.incidentEdges(v1o);
      while (itere.hasNext())
      {
        Edge e = (Edge)itere.next();
        // it is an incoming edge, skip it, because outgoing edges alread included it
        // if capacity < minCap, skip it
        if (e.getSecondEndpoint().getName().toString().equals(v1o.getName().toString()) ||
            (double)e.getData() < minCap)
        {
          continue;
        }

        DebugMessage("Gr edge found " + e.getFirstEndpoint().getName().toString() + "," + e.getSecondEndpoint().getName().toString() + " data " + e.getData().toString());

        // insert second vertex into residual graph if not already in there
        Vertex v2r = (Vertex)tabr.get(e.getSecondEndpoint().getName().toString());
        if (v2r == null)
        {
          v2r = gr.insertVertex(null, e.getSecondEndpoint().getName());
          tabr.put(v2r.getName().toString(), v2r);
          DebugMessage("Add2 " + v2r.getName().toString() + " to gr");
        }

        // there is no flow in this edge, add forward edge with full capacity
        if (v1Flow == null || !v1Flow.containsKey(v2r.getName().toString()))
        {
          // before adding, check if there exists edge in this direction
          Edge er = GetEdge(gr, v1r, v2r);
          if (er == null)
          {
            // it doesn't exist, go ahead and add it
            gr.insertEdge(v1r, v2r, e.getData(), "");
            DebugMessage("Add3 " + v1r.getName().toString() + "," + v2r.getName().toString() + " to gr");
          }
          else
          {
            // there is an edge from v1r to v2r due to the backward edge of some flow from v2r to v1r. add the capacity
            double value = (double)er.getData() + (double)e.getData();
            er.setData(value);
            DebugMessage("Add3.1 " + v1r.getName().toString() + "," + v2r.getName().toString() + " to gr " + value);
          }
        }
        // there is some flow in this edge
        else
        {
          double value = v1Flow.get(v2r.getName().toString());
          double cap = (double)e.getData();
          if (value > cap)
          {
            // something wrong. the flow exceeds capacity
            System.err.println("ResidualGraphErr " + v1r.getName().toString() + "," + v2r.getName().toString() + " flow " + value + " cap " + cap);
          }

          // add a backward edge
          // before adding, check if there exists edge in this direction
          Edge er = GetEdge(gr, v2r, v1r);
          if (er == null)
          {
            // it doesn't exist, go ahead and add it
            gr.insertEdge(v2r, v1r, value, "");
            DebugMessage("Add4 " + v2r.getName().toString() + "," + v1r.getName().toString() + " to gr");
          }
          else
          {
            // there is an existing edge because some capacity of v2r to v1r is unused. add the value
            double backwardValue = (double)er.getData() + value;
            er.setData(backwardValue);
            DebugMessage("Add4.1 " + v1r.getName().toString() + "," + v2r.getName().toString() + " to gr " + backwardValue);
          }
          // if there is remaining capacity, add forward edge
          if (value < cap)
          {
            // there won't be existing edge in residual graph from v1r to v2r because:
            // there is some flow from v1r to v2r, so there is no flow from v2r to v1r, and there won't be backward edges of the flow of v2r to v1r
            if ((er = GetEdge(gr, v1r, v2r)) != null)
            {
              // something wrong. this edge should not exist
              System.err.println("ResidualGraphErr " + v1r.getName().toString() + "," + v2r.getName().toString() + " flow and edge exist together");
            }
            gr.insertEdge(v1r, v2r, cap - value, "");
            DebugMessage("Add5 " + v1r.getName().toString() + "," + v2r.getName().toString() + " to gr");
          }
        }
      }
    }
  }

  private static Edge GetEdge(SimpleGraph g, Vertex v1, Vertex v2)
  {
      Iterator itere = g.incidentEdges(v1);
      while (itere.hasNext())
      {
        Edge e = (Edge)itere.next();
        if (e.getSecondEndpoint() == v2)
        {
          return e;
        }
      }
      return null;
  }

  private static Vector<Edge> GetAugmentingPath(Hashtable tabr, SimpleGraph gr)
  {
    HashMap<String, Edge> paths = new HashMap<String, Edge>();

    // find a s-t path by breadth-first search, starting from source
    Vertex v = (Vertex)tabr.get("s");
    Queue<Vertex> q = new LinkedList<Vertex>();
    q.add(v);
    while ((v = q.poll()) != null)
    {
      boolean broken = false;
      // search each connecting vertices
      Iterator itere = gr.incidentEdges(v);
      while (itere.hasNext())
      {
        Edge e = (Edge)itere.next();
        DebugMessage("BFS found edge " + e.getFirstEndpoint().getName().toString() + "," + e.getSecondEndpoint().getName().toString());
        String target = e.getSecondEndpoint().getName().toString();
        // if this is a valid and unvisited edge, add it to paths
        if ((double)e.getData() > 0 && !paths.containsKey(target) && !target.equals("s"))
        {
          paths.put(target, e);
          q.add(e.getSecondEndpoint());
          DebugMessage("BFS add " + v.getName().toString() + "," + target + " to visited vertices");
          // path found, break all loops
          if (target.equals("t"))
          {
            broken = true;
            break;
          }
        }
      }
      if (broken)
      {
        break;
      }
    }

    // from the result of BFS, trace back the s-t path
    Vector<Edge> path = new Vector<Edge>();
    String target = "t";
    while (paths.containsKey(target))
    {
      Edge e = paths.get(target);
      path.add(e);
      target = e.getFirstEndpoint().getName().toString();
      DebugMessage("BFS add " + e.getFirstEndpoint().getName().toString() + "," + e.getSecondEndpoint().getName().toString() + " to augmented path");
    }
    return path;
  }

  private static void UpdateFlow(HashMap<String, HashMap<String, Double>> flow, Vector<Edge> path)
  {
    double bottleneck = Double.MAX_VALUE;
    for (Edge e : path)
    {
      bottleneck = (double)e.getData() < bottleneck ? (double)e.getData() : bottleneck;
    }
    DebugMessage("Update flow bottleneck " + bottleneck);

    for (Edge e : path)
    {
      String v1 = e.getFirstEndpoint().getName().toString();
      String v2 = e.getSecondEndpoint().getName().toString();
      HashMap<String, Double> v1Flow = flow.get(v1);
      HashMap<String, Double> v2Flow = flow.get(v2);
      // check if it is a backward flow
      if (v2Flow != null && v2Flow.containsKey(v1))
      {
        // it is a backward edge, there are 3 cases
        if (bottleneck == v2Flow.get(v1))
        {
          // if the bottheneck is the same as current flow, cancel it
          v2Flow.remove(v1);
          DebugMessage("Update flow backward cancel " + v1 + "," + v2);
        }
        else if (bottleneck < v2Flow.get(v1))
        {
          // if the bottlenect is smaller than current flow, deduct it
          v2Flow.put(v1, v2Flow.get(v1) - bottleneck);
          DebugMessage("Update flow backward " + v1 + "," + v2);
        }
        else
        {
          // if the bottleneck is greater than current flow, cancel the flow, add a reverse flow
          v1Flow.put(v2, bottleneck - v2Flow.get(v1));
          v2Flow.remove(v1);
          DebugMessage("Update flow backward reverse " + v1 + "," + v2);
        }
      }
      else if (v1Flow == null)
      {
        // there is no flow from v1, this is a new edge
        v1Flow = new HashMap<String, Double>();
        v1Flow.put(v2, bottleneck);
        flow.put(v1, v1Flow);
        DebugMessage("Update flow new " + v1 + "," + v2);
      }
      else
      {
        // there are some existing flows from v1, let's check if it flows to v2
        if (v1Flow.containsKey(v2))
        {
          // there is an existing flow from v1 to v2, add it
          v1Flow.put(v2, v1Flow.get(v2) + bottleneck);
          DebugMessage("Update flow existing " + v1 + "," + v2);
        }
        else
        {
          // there are existing flows from v1 but none of them towards v2
          // this is a new flow
          v1Flow.put(v2, bottleneck);
          DebugMessage("Update flow new " + v1 + "," + v2);
        }
      }
    }
  }
}
