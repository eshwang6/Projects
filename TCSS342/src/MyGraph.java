import java.util.*;

/**
 * A representation of a graph. Assumes that we do not have negative cost edges
 * in the graph.
 */
public class MyGraph implements Graph {

	// you will need some private fields to represent the graph
	// you are also likely to want some private helper methods

	private Collection<Vertex> vertex;
	private Collection<Edge> edge;

	private Map<Vertex, ArrayList<Vertex>> adjVertices;


	/**
	 * Creates a MyGraph object with the given collection of vertices and the
	 * given collection of edges.
	 * 
	 * @param v
	 *            a collection of the vertices in this graph
	 * @param e
	 *            a collection of the edges in this graph
	 */
	public MyGraph(Collection<Vertex> v, Collection<Edge> e) {

		for(Edge currentEdge : e){

			if(currentEdge.getWeight() < 0){

				throw new NegativeWeightException();

			}else{

				for(Edge edge : e){

					if(edge.getSource().equals(currentEdge.getSource()) && edge.getDestination().equals(currentEdge.getDestination()) && edge.getWeight() != currentEdge.getWeight()){

						throw new DifferentEdgeWeightException();

					}

				}

			}

		}

		vertex = new ArrayList<Vertex>();
		edge = new ArrayList<Edge>();
		adjVertices = new HashMap<Vertex, ArrayList<Vertex>>();

		for (Vertex currentVertex : v) {

			vertex.add(new Vertex(currentVertex.getLabel(), currentVertex.getPath(), currentVertex.getDist()));

		}

		for (Edge currentEdge : e) {

			Vertex source = null;
			Vertex destination = null;

			for (Vertex vert : vertex) {

				if (vert.equals(currentEdge.getSource())) {

					source = vert;

				} else if (vert.equals(currentEdge.getDestination())) {

					destination = vert;

				}

			}

			edge.add(new Edge(source, destination, currentEdge.getWeight()));

		}

		for (Vertex currentVertex : vertex) {

			adjVertices.put(currentVertex, new ArrayList<Vertex>());

		}

		for (Edge currentEdge : edge) {

			adjVertices.get(currentEdge.getSource()).add(currentEdge.getDestination());
			
		}

	}

	/**
	 * Return the collection of vertices of this graph
	 * 
	 * @return the vertices as a collection (which is anything iterable)
	 */
	@Override
	public Collection<Vertex> vertices() {

		Collection<Vertex> verticesList = new ArrayList<Vertex>();

		for(Vertex currentVertex : vertex){

			verticesList.add(new Vertex(currentVertex.getLabel(), currentVertex.getPath(), currentVertex.getDist()));

		}

		return verticesList;

	}

	/**
	 * Return the collection of edges of this graph
	 * 
	 * @return the edges as a collection (which is anything iterable)
	 */
	@Override
	public Collection<Edge> edges() {

		Collection<Edge> edgesList = new ArrayList<Edge>();

		for(Edge currentEdge : edge){

			edgesList.add(new Edge(currentEdge.getSource(), currentEdge.getDestination(), currentEdge.getWeight()));

		}

		return edgesList;

	}

	/**
	 * Return a collection of vertices adjacent to a given vertex v. i.e., the
	 * set of all vertices w where edges v -> w exist in the graph. Return an
	 * empty collection if there are no adjacent vertices.
	 * 
	 * @param v
	 *            one of the vertices in the graph
	 * @return an iterable collection of vertices adjacent to v in the graph
	 * @throws IllegalArgumentException
	 *             if v does not exist.
	 */
	@Override
	public Collection<Vertex> adjacentVertices(Vertex v) {

		if(!adjVertices.containsKey(v)){

			throw new IllegalArgumentException();

		}

		return adjVertices.get(v);

	}

	/**
	 * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed
	 * graph. Assumes that we do not have negative cost edges in the graph.
	 * 
	 * @param a
	 *            one vertex
	 * @param b
	 *            another vertex
	 * @return cost of edge if there is a directed edge from a to b in the
	 *         graph, return -1 otherwise.
	 * @throws IllegalArgumentException
	 *             if a or b do not exist.
	 */
	@Override
	public int edgeCost(Vertex a, Vertex b) {

		if(!vertex.contains(a) || !vertex.contains(b))
			throw new IllegalArgumentException();

		int costOfEdge = -1;

		if(!adjVertices.get(a).contains(b)){

			return costOfEdge;

		}else{

			for(Edge currentEdge : edge){

				if(currentEdge.getSource().equals(a) && currentEdge.getDestination().equals(b)){

					costOfEdge = currentEdge.getWeight();

				}

			}

			return costOfEdge;

		}

	}

	/**
	 * Returns the shortest path from a to b in the graph, or null if there is
	 * no such path. Assumes all edge weights are nonnegative. Uses Dijkstra's
	 * algorithm.
	 * 
	 * @param a
	 *            the starting vertex
	 * @param b
	 *            the destination vertex
	 * @return a Path where the vertices indicate the path from a to b in order
	 *         and contains a (first) and b (last) and the cost is the cost of
	 *         the path. Returns null if b is not reachable from a.
	 * @throws IllegalArgumentException
	 *             if a or b does not exist.
	 */
	public Path shortestPath(Vertex a, Vertex b) {

		if(!vertex.contains(a) || !vertex.contains(b))
			throw new IllegalArgumentException();

		for(Vertex currentVertex : vertex){

			if(currentVertex.equals(a)){

				a = currentVertex;

			}else if(currentVertex.equals(b)){

				b=currentVertex;

			}

		}

		if(a.equals(b)){

			List<Vertex> shortestPathList = new ArrayList<Vertex>();
			
			shortestPathList.add(a);

			return new Path(shortestPathList, 0);

		}

		runDijkstra(a);
		
		List<Vertex> shortestPathList = new ArrayList<Vertex>();

		Vertex tempVertex = b;

		while(!tempVertex.equals(a)) {

			if(tempVertex.getPath() == null) {

				initializeVertex();

				return null;

			}

			shortestPathList.add(tempVertex);

			tempVertex = tempVertex.getPath();

		}

		int totalDist = b.getDist();

		initializeVertex();

		return new Path(shortestPathList, totalDist);

	}

	private void runDijkstra(Vertex startingVertex){

		Collection<Vertex> vertexList = new ArrayList<Vertex>();

		for(Vertex currentVertex : vertex){

			currentVertex.setPath(null);

			if(currentVertex.getLabel().equals(startingVertex.getLabel())){

				currentVertex.setDist(0);

			}

			vertexList.add(currentVertex);

		}

		boolean hasPath = true;

		while(!vertexList.isEmpty() && hasPath){

			Vertex v = minimumDist(vertexList);

			if (v == null) {

				hasPath = false;

			} else {

				v.setKnown(true);

				vertexList.remove(v);

				for(Vertex adjVertex : adjVertices.get(v)) {

					if(!adjVertex.getKnown()) {

						int temp = edgeCost(v,adjVertex);

						if((v.getDist() + temp) < adjVertex.getDist()) {

							adjVertex.setDist(v.getDist() + temp);

							adjVertex.setPath(v);

						}

					}

				}

			}

		}
		
	}

	private Vertex minimumDist(Collection<Vertex> vertexList) {

		int minDist = Integer.MAX_VALUE;

		Vertex v = null;

		for(Vertex currentVertex: vertexList) {

			if (currentVertex.getDist() < minDist) {

				minDist = currentVertex.getDist();

				v = currentVertex;

			}

		}

		return v;
		
	}
	
	private void initializeVertex() {

		for (Vertex currentVertex : vertex) {

			currentVertex.setKnown(false);

			currentVertex.setPath(null);

			currentVertex.setCost(Integer.MAX_VALUE);

			currentVertex.setDist(Integer.MAX_VALUE);

		}

	}

	@SuppressWarnings("serial")
	private class NegativeWeightException extends RuntimeException{

		public NegativeWeightException(){
			System.out.println("The collection of edges contains negative edge weight(s).");
		}

	}
	@SuppressWarnings("serial")
	private class DifferentEdgeWeightException extends RuntimeException{

		public DifferentEdgeWeightException(){
			System.out.println("Exception : The collection of edges contains the same directed edge(s) with a different weight");
		}

	}

}
