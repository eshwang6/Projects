import java.util.*;

public class PreflowPush {

	private SimpleGraph graph;
	private SimpleGraph residual;
	
	public Double maxFlow;
	public long elapsedTime;
	
	//<VertexName, height>
	private HashMap<String, Integer> height;
	
	//<EdgeName, FlowValue>
	private HashMap<String, Double> flowTable;
	
	private Hashtable graphNodeTable;
	private Hashtable residualNodeTable;

	public PreflowPush(String filePath) { 
		
		maxFlow = 0.0;
		//load graph from a file
		graph = new SimpleGraph();
		graphNodeTable = GraphLoader.LoadSimpleGraph(graph, filePath);
		
		initializeFlowTable();
		initializeHeight();
		
		createResidualGraph();
	}
	private void initializeFlowTable() { 
		System.out.println("Initializing Flow...");
		
		flowTable = new HashMap<String, Double>();
		for (Iterator<Edge> edges = graph.edges(); edges.hasNext();) {
			Edge e = edges.next();
			Double flowValue = 0.0;
			
			//initially saturate every edge out of source 
			if(e.getFirstEndpoint().getName().equals("s"))
				flowValue = (Double)e.getData();
			
			flowTable.put((String)e.getName(), flowValue);
		}
		
		System.out.println("Initializing flow done successfully");
	}
	
	private void initializeHeight() {
		System.out.println("Initializing Labeles...");
		height = new HashMap<String, Integer>();

		for (Iterator<Vertex> verteces = graph.vertices(); verteces.hasNext();) {
			Vertex v = verteces.next();
			
			if(v.getName().equals("s")) { 
				height.put((String)v.getName(), graph.numVertices());
			}
			else { 
				height.put((String)v.getName(), 0);
			}
		}
		
		System.out.println("Initializing labels done successfully");
	}

  private void removeEdges(SimpleGraph g, Vertex v, Vertex w)
  {
    Vector<Edge> edgesToDelete = new Vector<Edge>();
		Iterator<Edge> edges = g.incidentEdges(v);
		while(edges.hasNext()) {
			Edge e = edges.next();
      if (e.getFirstEndpoint().getName().equals(v.getName()) &&
          e.getSecondEndpoint().getName().equals(w.getName()))
      {
        edgesToDelete.add(e);
      }
    }
    for (Edge e : edgesToDelete)
    {
      g.edgeList.remove(e);
      v.incidentEdgeList.remove(e);
      w.incidentEdgeList.remove(e);
    }
  }

	private void updateResidualGraph(Vertex v1, Vertex v2, Double flow) { 
		
		Edge secondEdge = findEdge(v2, v1, true, true);
		Vertex v = (Vertex)residualNodeTable.get(v1.getName());
		Vertex w = (Vertex)residualNodeTable.get(v2.getName());
    removeEdges(residual, v, w);
    removeEdges(residual, w, v);
		
//		//add the edge on opposite direction if exists
		if(secondEdge != null) {
			residual.insertEdge(w, v, secondEdge.getData(), "true" + w.getName() + v.getName());
		}
		
		Double capacity = (Double)findEdge(v1, v2, true, true).getData();
		if(flow == 0) { 
			residual.insertEdge(v, w, capacity, "true" + v.getName() + w.getName());
		}
		else {
			Double remaining = capacity - flow;
			if(remaining == 0) {
				residual.insertEdge(w, v, capacity, "false" +w.getName() + v.getName());
			}
			else {
				//add one forward edge
				residual.insertEdge(v, w, remaining, "true" + v.getName() + w.getName());
				//add one backward edge
				residual.insertEdge(w, v, flow, "false" + w.getName() + v.getName());
			}
		}
//		printResidual();
//		System.out.println("----------------");
	}

	private void createResidualGraph() { 
		System.out.println("start creating residual");
		residual = new SimpleGraph();
		residualNodeTable = new Hashtable();

		for (Iterator<Edge> edges = graph.edges(); edges.hasNext();) {
			Edge e = edges.next();

			Vertex v1, v2;
			String v1Name = (String)e.getFirstEndpoint().getName();
			String v2Name = (String)e.getSecondEndpoint().getName();

			v1 = (Vertex) residualNodeTable.get(v1Name);
			if (v1 == null) {
				v1 = residual.insertVertex(null, v1Name);
				residualNodeTable.put(v1Name, v1);
			}
			v2 = (Vertex) residualNodeTable.get(v2Name);
			if (v2 == null) {
				v2 = residual.insertVertex(null, v2Name);
				residualNodeTable.put(v2Name, v2);
			}

			Double flowVal = flowTable.get(e.getName());
			if(flowVal == null || flowVal == 0) { 
				residual.insertEdge(v1, v2, e.getData(), "true" + v1.getName() + v2.getName());
			}
			else {
				Double remaining = (Double)e.getData() - flowVal;
				if(remaining == 0) {
					residual.insertEdge(v2, v1, e.getData(), "false" + v2.getName() + v1.getName());
				}
				else {
					//add one forward edge
					residual.insertEdge(v1, v2, flowVal, "true" + v1.getName() + v2.getName());
					//add one backward edge
					residual.insertEdge(v2, v1, remaining, "false" + v2.getName() + v1.getName());
				}
			}
		}
//		printResidual();
		System.out.println("residual created");
	}

	public void runPreflowPush() { 
		
		//v from graph
		System.out.println("push-relable started at: " + System.currentTimeMillis());
		long start = System.currentTimeMillis();
		Vertex v = findVertexWithPositiveExcess();
		
 		while ( v != null) { 
			
			Vertex w = findVertexWithLowerHeight(v);
			if(w != null ) 
				pushFlow(v,w);
			else
				relable(v);
			
			v = findVertexWithPositiveExcess();
		}
 		long end = System.currentTimeMillis();
 		System.out.println("push-relable started at: " + end);
 		elapsedTime = end - start;
 		maxFlow = calculateMaxFlow();
	}
	
	private Vertex findVertexWithPositiveExcess() { 
		
		Vertex v;
		for (Iterator<Vertex> verteces = graph.vertices(); verteces.hasNext();) {
			
			v = verteces.next();	
			if(!v.getName().equals("t") && excess(v) > 0)
				return v;
		}
		
		return null;
	}
	
	private Double excess(Vertex v) { 

		Double excess = 0.0;
		
		for (Iterator<Edge> edges = graph.incidentEdges(v); edges.hasNext();) {
			Edge e = edges.next();
			Double flowVal = flowTable.get(e.getName());
			if(flowVal == null || flowVal == 0)
				continue;
			//incoming edge
			if(e.getSecondEndpoint().getName().equals(v.getName())) 
				excess += flowVal;
			//outgoing edge
			else 
				excess -= flowVal;
		}
		
		return excess;
	}
	
	private Vertex findVertexWithLowerHeight(Vertex vertex) { 
		
		Vertex v = (Vertex)residualNodeTable.get(vertex.getName());
		Iterator<Edge> edges = residual.incidentEdges(v);
		while(edges.hasNext()) {
			Edge e = edges.next();
			
			// only find edges that start from v
			if(e.getFirstEndpoint().getName().equals(v.getName())) {
				Vertex w = e.getSecondEndpoint();
				if(height.get(v.getName()) > height.get(w.getName()))
					return w;
			}
		}
		
		return null;
	}
	
	private void pushFlow(Vertex v, Vertex w) { 
		
		String edgeName = (String)v.getName() + (String)w.getName();
		
		Edge vw = findEdge(v, w, false, false);
		if(vw == null)
			vw = findEdge(v, w, true, false);
		
		Double finalFlow = 0.0;
		Double currentFlow = 0.0;
		Double addedFlow = 0.0;
		
		if(((String)vw.getName()).contains("true")) {
			currentFlow = flowTable.get(edgeName);
			Double capacity = 0.0;
			capacity = (double)findEdge(v,w,true, true).getData();

			Double delta = (capacity-(double)currentFlow);
			addedFlow = Math.min(excess(v), delta);
			finalFlow = currentFlow + addedFlow;
			flowTable.put(edgeName, finalFlow);
			updateResidualGraph(v, w, finalFlow);
		}
		else {
			edgeName = (String)w.getName()+(String)v.getName();
			currentFlow = flowTable.get(edgeName);
			addedFlow = Math.min(excess(v), (double)vw.getData());
			finalFlow = currentFlow - addedFlow;
			flowTable.put(edgeName, finalFlow);
			updateResidualGraph(w, v, finalFlow);
		}
		
		//createResidualGraph();
	}
	
	private void relable(Vertex vertex) {
		
		if(excess(vertex) <= 0)
			return;
		
		Vertex v = (Vertex)residualNodeTable.get(vertex.getName());
		
		//only runs on time. but I wanted to implement everything in the pseudo-code
		for (Iterator<Edge> edges = residual.incidentEdges(v); edges.hasNext();) {
			Edge e = edges.next();
			
			Vertex w = e.getSecondEndpoint();
			if(!w.getName().equals(v.getName())) {
				int wHeight = height.get(w.getName());
				int vHeight = height.get(v.getName());
				if(wHeight >= vHeight) {
					height.put((String)v.getName(), vHeight + 1);
					return;
				}
			}
				
		}
	}
	
	private Double calculateMaxFlow() {
		Double maxFlow = 0.0;
		Vertex v = (Vertex)graphNodeTable.get("s");
		for (Iterator<Edge> e = graph.incidentEdges(v); e.hasNext();) {
			Edge edge = e.next();
			maxFlow += flowTable.get((String)edge.getFirstEndpoint().getName() + (String)edge.getSecondEndpoint().getName());
		}
		
		return maxFlow;
	}
	
	private Edge findEdge(Vertex v1, Vertex v2, boolean isForward, boolean inMainGraph) { 
		String edgeName = "";
		
		if(inMainGraph) {
			Vertex node = (Vertex)graphNodeTable.get(v1.getName());
			edgeName = (String)v1.getName() + (String)v2.getName();
			for (Iterator<Edge> iterator = graph.incidentEdges(node); iterator.hasNext();) {
				Edge edge = (Edge) iterator.next();
				if(edge.getName().equals(edgeName))
					return edge;
			}
		}
		else { 
			Vertex node = (Vertex)residualNodeTable.get(v1.getName());
			edgeName = isForward + (String)v1.getName() + v2.getName();
			for (Iterator<Edge> iterator = residual.incidentEdges(node); iterator.hasNext();) {
				Edge edge = (Edge) iterator.next();
				if(edge.getName().equals(edgeName))
					return edge;
			}
		}
		return null;
	}
	
	public void printGraph() { 
		for (Iterator<Edge> edges = graph.edges(); edges.hasNext();) {
			Edge e = edges.next();
			System.out.println(
					e.getFirstEndpoint().getName() 
					+ " -> " 
					+ e.getSecondEndpoint().getName() 
					+ ": " + e.getData() + ";" + e.getName());
		}
	}
	
	public void printResidual() { 
		for (Iterator<Edge> edges = residual.edges(); edges.hasNext();) {
			Edge e = edges.next();
			if(((String)e.getName()).contains("false")) {
				System.out.println(
						e.getFirstEndpoint().getName() 
						+ " -> " 
						+ e.getSecondEndpoint().getName() 
						+ ": " + e.getData() );
			}
			else {
				System.out.println(
						e.getFirstEndpoint().getName() 
						+ " => " 
						+ e.getSecondEndpoint().getName() 
						+ ": " + e.getData() );
			}
		}
	}
	
	public static void main(String[] args) {
		PreflowPush pfp = new PreflowPush("/Users/mehdijazayeri3000/Documents/workspace/NetworkFlow/bipartiteDemo1.txt");
		pfp.runPreflowPush();
		
	}
}
