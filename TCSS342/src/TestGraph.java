import java.io.*;
import java.util.*;

public class TestGraph {

	public static void main(String[] args) {

		List<MyGraph> graphs = new ArrayList<MyGraph>();

		try {
			
			MyGraph g1 = readGraph("test_v1.txt","test_e1.txt");
			graphs.add(g1);
			
		} catch (RuntimeException e) {
			
			System.out.println("RuntimeException occurred testing first case.");
			System.out.println();
			
		} 
		
		try {

			MyGraph g2 = readGraph("test_v2.txt","test_e2.txt");
			graphs.add(g2);
			
		} catch (RuntimeException e) {
			
			System.out.println("RuntimeException occurred testing second case.");
			System.out.println();
			
		} 

		try {
		
			MyGraph g3 = readGraph("test_v3.txt","test_e3.txt");
			graphs.add(g3);
		
		} catch (Exception e) {
			
			System.out.println("RuntimeException occurred testing third case.");
			System.out.println();
			
		}
		
		for(MyGraph g : graphs){

			@SuppressWarnings("resource")
			Scanner console = new Scanner(System.in);
			Collection<Vertex> v = g.vertices();
			Collection<Edge> e = g.edges();
			System.out.println("Vertices are "+v);
			System.out.println("Edges are "+e);

			String cmd = "";

			while(!cmd.equals("next test")) {

				System.out.print("Start vertex? (type 'next test'' to move on) ");

				cmd = console.nextLine();

				if(!cmd.equals("next test")){

					Vertex a = new Vertex(cmd);
					if(!v.contains(a)) {
						System.out.println("no such vertex");
						System.exit(0);
					}

					System.out.print("Destination vertex? ");
					Vertex b = new Vertex(console.nextLine());
					if(!v.contains(b)) {
						System.out.println("no such vertex");
						System.exit(1);
					}

					// YOUR CODE HERE: call shortestPath and print

					Path shortestPath = g.shortestPath(a, b);

					if(shortestPath == null){

						System.out.println("does not exist.");

					}else{

						System.out.println("Shortest Path starting from " + a.getLabel() + " to " + b.getLabel() + " : ");

						System.out.print(a.getLabel());

						for(int i = shortestPath.vertices.size() - 1; i >= 0 ; i--) {

							Vertex vert = shortestPath.vertices.get(i);

							System.out.print(" " + vert.getLabel());

						}

						System.out.println();
						System.out.println("Total Cost : " + shortestPath.cost);

					}

				}

			}

		}

	}

	public static MyGraph readGraph(String f1, String f2) {
		Scanner s = null;
		try {
			s = new Scanner(new File(f1));
		} catch(FileNotFoundException e1) {
			System.err.println("FILE NOT FOUND: "+f1);
			System.exit(2);
		}

		Collection<Vertex> v = new ArrayList<Vertex>();
		while(s.hasNext())
			v.add(new Vertex(s.next()));

		try {
			s = new Scanner(new File(f2));
		} catch(FileNotFoundException e1) {
			System.err.println("FILE NOT FOUND: "+f2);
			System.exit(2);
		}

		Collection<Edge> e = new ArrayList<Edge>();
		while(s.hasNext()) {
			try {
				Vertex a = new Vertex(s.next());
				Vertex b = new Vertex(s.next());
				int w = s.nextInt();
				e.add(new Edge(a,b,w));
			} catch (NoSuchElementException e2) {
				System.err.println("EDGE FILE FORMAT INCORRECT");
				System.exit(3);
			}
		}

		return new MyGraph(v,e);
	}
		
}
