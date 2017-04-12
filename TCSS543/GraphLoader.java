import java.io.*;
import java.util.*;
import java.text.*;

public class GraphLoader
{
    public static Hashtable LoadSimpleGraph(SimpleGraph newgraph, String pathandfilename){
        BufferedReader  inbuf = InputLib.fopen(pathandfilename);
        System.out.println("Opened " + pathandfilename + " for input.");
        String  line = InputLib.getLine(inbuf); // get first line
        StringTokenizer sTok;
        int n, linenum = 0;
        Hashtable table = new Hashtable();
        SimpleGraph sg = newgraph;

        while (line != null) {
            linenum++;
            sTok = new StringTokenizer(line);
            n = sTok.countTokens();
            if (n==3) {
                Double edgedata;
                Vertex v1, v2;
                String v1name, v2name;

                v1name = sTok.nextToken();
                v2name = sTok.nextToken();
                edgedata = new Double(Double.parseDouble(sTok.nextToken()));
                v1 = (Vertex) table.get(v1name);
                if (v1 == null) {
//                      System.out.println("New vertex " + v1name);
                        v1 = sg.insertVertex(null, v1name);
                        table.put(v1name, v1);
                }
                v2 = (Vertex) table.get(v2name);
                if (v2 == null) {
//                      System.out.println("New vertex " + v2name);
                    v2 = sg.insertVertex(null, v2name);
                    table.put(v2name, v2);
                }
//              System.out.println("Inserting edge (" + v1name + "," + v2name + ")" + edgedata);
                sg.insertEdge(v1,v2,edgedata, (String)v1.getName()+(String)v2.getName());
            }
            else {
                System.err.println("Error:invalid number of tokens found on line " +linenum+ "!");
                return null;
            }
            line = InputLib.getLine(inbuf);
        }

        InputLib.fclose(inbuf);
        System.out.println("Successfully loaded "+ linenum + " lines. ");
        return table;
    }

    public static Hashtable LoadSimpleGraph(SimpleGraph newgraph, Vector<String> inputs){
        StringTokenizer sTok;
        int n, linenum = 0;
        Hashtable table = new Hashtable();
        SimpleGraph sg = newgraph;

        for (String line : inputs)
        {
            linenum++;
            sTok = new StringTokenizer(line);
            n = sTok.countTokens();
            if (n==3) {
                Double edgedata;
                Vertex v1, v2;
                String v1name, v2name;

                v1name = sTok.nextToken();
                v2name = sTok.nextToken();
                edgedata = new Double(Double.parseDouble(sTok.nextToken()));
                v1 = (Vertex) table.get(v1name);
                if (v1 == null) {
//                      System.out.println("New vertex " + v1name);
                        v1 = sg.insertVertex(null, v1name);
                        table.put(v1name, v1);
                }
                v2 = (Vertex) table.get(v2name);
                if (v2 == null) {
//                      System.out.println("New vertex " + v2name);
                    v2 = sg.insertVertex(null, v2name);
                    table.put(v2name, v2);
                }
//              System.out.println("Inserting edge (" + v1name + "," + v2name + ")" + edgedata);
                sg.insertEdge(v1,v2,edgedata, (String)v1.getName()+(String)v2.getName());
            }
            else {
                System.err.println("Error:invalid number of tokens found on line " +linenum+ "!");
                return null;
            }
        }

        System.out.println("Successfully loaded "+ linenum + " lines. ");
        return table;
    }
}

