package dataStructure;

import java.io.Serializable;

/**
 * This class represents the set of operations applicable on a 
 * directional edge(src,dest) in a (directional) weighted graph.
 * @author Meir Nizri and Avihai Bernholtz
 */
public class edge implements edge_data,Serializable{
	
	private int src;
	private int dest;
	private double weight;
	private String info;
	private int tag;
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor.
	 */
	public edge() {
		this.src = -1;
		this.dest = -1;
		this.weight = 0;
		this.info = "";
		this.tag = 0;
	}
	
	/**
	 * Initialize an edge.
	 * @param s - the source node of the edge.
	 * @param d - the destination node of the edge.
	 */
	public edge(int s, int d) {
		this.src = s;
		this.dest = d;
		this.weight = 0;
		this.info = "";
		this.tag = 0;
	}
	
	/**
	 * Initialize an edge.
	 * @param s - the source node of the edge.
	 * @param d - the destination node of the edge.
	 * @param w - the weight of the node.
	 */
	public edge(int s, int d, double w) {
		this.src = s;
		this.dest = d;
		if(w < 0)
			throw new NumberFormatException("ERR: The weight of the edge must be non-negative");
		this.weight = w;
		this.info = "";
		this.tag = 0;
	}

	/**
	 * @return the id of the source node of the edge.
	 */
	@Override
	public int getSrc() {
		return this.src;
	}

	/**
	 * @return the id of the destination node of this edge.
	 */
	@Override
	public int getDest() {
		return this.dest;
	}

	/**
	 * @return the weight of this edge (positive value).
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}

	/**
	 * @return the remark (meta data) associated with this edge.
	 */
	@Override
	public String getInfo() {
		return this.info;
	}

	/**
	 * Allows changing the remark (meta data) associated with this edge.
	 * @param s - the information the edge will save
	 */
	@Override
	public void setInfo(String s) {
		this.info = s;
	}

	/**
	 * @return the temporal data (aka color: e,g, white, gray, black) which can be used by algorithms.
	 */
	@Override
	public int getTag() {
		return this.tag;
	}

	/** 
	 * Allow setting the "tag" value for temporal marking an edge - common practice for marking by algorithms.
	 * @param t - the new value of the tag.
	 */
	@Override
	public void setTag(int t) {
		this.tag = t;
	}
	
	/**
	 * Test if this edge is logically equals to obj.
	 * @param obj - the object to check if equal.
	 * @return true iff this edge represent the same edge as obj.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof edge))
			throw new IllegalArgumentException("The argument you entered is not instance of edge");
		edge e = (edge) obj;
		return (this.getSrc() == e.getSrc() &&
				this.getDest() == e.getDest() &&
				this.getWeight() == e.getWeight());
	}
	
	/**
	 * print the edge. example of the string: [Src: 112, Dest: 115, Weight: 10]
	 * @return string representation of the edge
	 */
	@Override
	public String toString() {
		return ("[Src: " + src + ", Dest: " + dest + ", Weight: " + weight + "]");
	}
}