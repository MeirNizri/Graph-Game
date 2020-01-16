package dataStructure;

import java.io.Serializable;

import utils.Point3D;

/**
 * This class represents the set of operations applicable on a 
 * node (vertex) in a (directional) positive weighted graph.
 * @author Meir Nizri, Avihai Bernholtz
 */
public class node implements node_data, Serializable {

	private int key;
	private double weight;  // > 0
	private Point3D location;
	private String info;
	private int tag;
	// Static variable that provide a key for each node built by the default constructor
	public static int id = 0;
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public node() {
		this.key = id++;
		this.weight = 0;
		this.location = null;
		this.info = "";
		this.tag = 0;
	}
	
	/**
	 * Initialize node
	 * @param k - the key of the node
	 */
	public node(int k) {
		this.key = k;
		this.weight = 0;
		this.location = null;
		this.info = "";
		this.tag = 0;
	}

	/**
	 * Initialize node
	 * @param p - the location of the node
	 */
	public node(Point3D p) {
		this.key = id++;
		this.weight = 0;
		this.location = p;
		this.info = "";
		this.tag = 0;
	}
	
	/**
	 * Initialize node
	 * @param p - the location of the node
	 */
	public node(int k, Point3D p) {
		this.key = k;
		this.weight = 0;
		this.location = p;
		this.info = "";
		this.tag = 0;
	}

	/**
	 * @return the key (id) associated with this node.
	 */
	@Override
	public int getKey() {
		return this.key;
	}

	/**
	 * @return the location (of applicable) of this node, if none return null.
	 */
	@Override
	public Point3D getLocation() {
		return this.location;
	}

	/** Allows changing this node's location.
	 * @param p - new location (position) of this node.
	 */
	@Override
	public void setLocation(Point3D p) {
		this.location = p;
	}

	/**
	 * @return the weight associated with this node.
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}

	/**
	 * Allows changing this node's weight.
	 * @param w - the new weight
	 */
	@Override
	public void setWeight(double w) {
		if(w < 0)
			throw new NumberFormatException("ERR: The weight of the node: " + this.toString() + " must be non-negative");
		this.weight = w;
	}

	/**
	 * @return the remark (meta data) associated with this node.
	 */
	@Override
	public String getInfo() {
		return info;
	}

	/**
	 * Allows changing the remark (meta data) associated with this node.
	 * @param s - the information the node will save
	 */
	@Override
	public void setInfo(String s) {
		this.info = s;
	}

	/**
	 * @return @return the temporal data (aka color: e,g, white, gray, black) which can be used by algorithms.
	 */
	@Override
	public int getTag() {
		return this.tag;
	}

	/** 
	 * Allow setting the "tag" value for temporal marking an node - common practice for marking by algorithms.
	 * @param t - the new value of the tag.
	 */
	@Override
	public void setTag(int t) {
		this.tag = t;
	}
	
	/**
	 * Test if this node is logically equals to obj.
	 * @param obj - the object to check if equal.
	 * @return true iff this node represent the same edge as obj.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof node))
			throw new IllegalArgumentException("The argument you entered is not instance of node");
		node n = (node) obj;
		return (this.getKey() == n.getKey());
	}

	/**
	 * print the node. example of the string: [Key: 112, Location: (10,10,10)]
	 * if the location is null will print: "Location: null"
	 * @return string representation of the node
	 */
	@Override
	public String toString() {
		if(this.getLocation() != null)
			return ("[Key: " + key + ", Location: (" + location.toString() + ")]");
		else
			return ("[Key: " + key + ", Location: null]");
	}
	
	public static void resetId() {
		node.id = 1;
	}
}