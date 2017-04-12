/**
 * Representation of a graph vertex
 */
public class Vertex {
	// label attached to this vertex
	private String label;
	
	private boolean known;
	
	@SuppressWarnings("unused")
	private int cost;
	
	private int dist;
	
	private Vertex path;

	/**
	 * Construct a new vertex
	 * 
	 * @param label
	 *            the label attached to this vertex
	 */
	public Vertex(String label) {
		
		if (label == null)
			throw new IllegalArgumentException("null");
		this.label = label;

		this.path = null;
		
		this.dist = Integer.MAX_VALUE;
		
	}

	public Vertex(String label, Vertex path, int dist){
		
		if (label == null)
			throw new IllegalArgumentException("null");
		
		this.label = label;

		this.known = false;

		this.path = path;
		
		this.dist = dist;

		this.cost = Integer.MAX_VALUE;
		
	}
	
	/**
	 * Get a vertex label
	 * 
	 * @return the label attached to this vertex
	 */
	public String getLabel() {
		
		return label;
	
	}

	public int getDist(){
		
		return dist;
		
	}
	
	public Vertex getPath(){
		
		return path;
		
	}
	
	public boolean getKnown(){
		
		return known;
		
	}
	
	public void setKnown(boolean updateKnown){
		
		known = updateKnown;
		
	}
	
	public void setPath(Vertex updatedPath){
		
		path = updatedPath;
		
	}
	
	public void setCost(int updatedCost){
		
		cost = updatedCost;
		
	}
	
	public void setDist(int updatedDist){
		
		dist = updatedDist;
		
	}
	/**
	 * A string representation of this object
	 * 
	 * @return the label attached to this vertex
	 */
	public String toString() {
		return label;
	}

	// auto-generated: hashes on label
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	// auto-generated: compares labels
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Vertex other = (Vertex) obj;
		if (label == null) {
			return other.label == null;
		} else {
			return label.equals(other.label);
		}
	}

}
