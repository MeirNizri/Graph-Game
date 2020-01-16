package gameObjects;

import org.json.JSONException;
import org.json.JSONObject;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

/**
 * This class represents fruit on the overall game in this project.
 * The purpose of this class is to represent all information in the fruit,
 * so that the robots can gather as much fruits as possible in the fastest
 * and most efficient way.
 * @author Meir Nizri
 */
public class Fruit {
	// Information about the fruit.
	private int type;
	private Point3D location;
	// The edge the fruit is locate on.
	private edge_data edge;
	// src and dest arranged so that to collect the fruit one must go from src to dest.
	private int src;
	private int dest;
	private node_data srcNode;
	private double value;
	// Boolean variable to check if this fruit already targeted by another robot.
	private boolean isTaken;
	private double EPSILON = 0.000000001;
	
	/**
	 * Empty default constructor
	 */
	public Fruit() {}
	
	/**
	 * Initializes a fruit from Json String received from the game server
	 * @param Graph - àhe graph on which the fruit is placed
	 * @param fruit_Json - Json string the game server return with command "getFruits()",
	 * 					   containing information about the fruit.
	 */
	public Fruit(graph Graph, String fruit_Json) {
		try {
			JSONObject fruit = new JSONObject(fruit_Json).getJSONObject("Fruit");
			type = fruit.getInt("type");
			location = new Point3D(fruit.getString("pos"));
			// find the edge the fruit locate on
			edge = findEdge(Graph, location);
			// If type is -1 to collect the fruit must move from the big node to the small node.
			if(type == -1) {
				src = Math.max(edge.getSrc(), edge.getDest());
				dest = Math.min(edge.getSrc(), edge.getDest());
			}
			// If type is 1 to collect the fruit must move from the small node to the big node.
			else {
				src = Math.min(edge.getSrc(), edge.getDest());
				dest = Math.max(edge.getSrc(), edge.getDest());
			}
			srcNode = Graph.getNode(src);
			value = fruit.getDouble("value");
			isTaken = false;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the type of the fruit
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * @return the location of the fruit
	 */
	public Point3D getLocation() {
		return location;
	}
	
	/**
	 * @return the edge the fruit locate on
	 */
	public edge_data getEdge() {
		return edge;
	}
	
	/**
	 * Find the edge the fruit locate on. if not fount return null.
	 * This algorithem is based on "Triangle inequality".
	 * See: https://en.wikipedia.org/wiki/Triangle_inequality
	 * @param Graph - the graph on which the fruit is placed
	 * @param p - the location of the fruit on the graph
	 * @return the edge the fruit locate is on, or null if not found any
	 */
	public edge_data findEdge(graph Graph, Point3D p) {
		// Go through all the edges until you find an edge (a,b),
		// such that the distance from p to a plus the distance from p to b,
		// equals the distance from a to b at  EPSILON difference.
		// This algorithem is based on "Triangle inequality". see: https://en.wikipedia.org/wiki/Triangle_inequality
		for(node_data n : Graph.getV()) {
			Point3D src_p = n.getLocation();
			for(edge_data e : Graph.getE(n.getKey())) {
				Point3D dest_p = Graph.getNode(e.getDest()).getLocation();
				if((p.distance2D(src_p) + p.distance2D(dest_p)) - src_p.distance2D(dest_p) < EPSILON)
					return e;
			}
		}
		return null;
	}
	
	/**
	 * Gets the fruit src node key. this is the node you need to go first to collect the fruit.
	 * @return the fruit src node key.
	 */
	public int getSrc() {
		return src;
	}
	
	/**
	 * Gets the fruit dest node key. this is the node you need to go after you arrived to src to collect the fruit
	 * @return the dest node key.
	 */
	public int getDest() {
		return dest;
	}
	
	/**
	 * @return the fruit src node.
	 */
	public node_data getSrcNode() {
		return srcNode;
	}
	
	/**
	 * @return the value of the fruit
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * @return boolean variable indicating whether this fruit was targeted by another robot
	 */
	public boolean getIsTaken() {
		return isTaken;
	}
	
	/**
	 * Set the bðoolean variable indicating whether this fruit was targeted by another robot
	 */
	public void setIsTaken(boolean flag) {
		isTaken = flag;
	}
	
	/**
	 * Test if this Fruit is logically equals to fruit f.
	 * @param f - the Fruit to check if equal.
	 * @return true iff this fruit represent the same fruitas f.
	 */
	public boolean equals(Fruit f) {
		return (type == f.type &&
				edge == f.edge &&
				value == f.value );
	}
}