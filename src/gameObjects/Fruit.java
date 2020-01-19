package gameObjects;

import org.json.JSONException;
import org.json.JSONObject;

import graphAlgorithms.Graph_Functions;
import graphDataStructure.edge_data;
import graphDataStructure.graph;
import graphDataStructure.node_data;
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
			edge = Graph_Functions.findEdge(Graph, location, 0.000000000001);
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