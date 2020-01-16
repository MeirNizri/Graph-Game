package gameObjects;

import java.util.LinkedList;
import org.json.JSONException;
import org.json.JSONObject;
import dataStructure.node_data;
import utils.Point3D;

/**
 * This class represents robot on the overall game in this project.
 * The purpose of this class is to represent all information in the robot,
 * so that the robots can gather as much fruits as possible in the fastest
 * and most efficient way.
 * @author Meir Nizri
 */
public class Robot {
	
	// Information about the robot
	private int id;
	private double speed, value;
	private Point3D location;
	private int src, dest;
	// path of nodes where the robot keeps all the nodes it needs to pass through until he reaches next fruit.
	private LinkedList<node_data> path = new LinkedList<node_data>();
	
	/**
	 * Empty default constructor
	 */
	public Robot() {}
	
	/**
	 * Instantiates a new robot from Json String received from the game server.
	 * @param robot_Json - Json string the game server return with command "getRobots()",
	 * 					   containing information about the robot.
	 */
	public Robot(String robot_Json) {
		try {
			JSONObject robot = new JSONObject(robot_Json).getJSONObject("Robot");
			id = robot.getInt("id");
			speed = robot.getDouble("speed");
			src = robot.getInt("src");
			dest = robot.getInt("dest");
			location = new Point3D(robot.getString("pos"));
			value = robot.getDouble("value");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Because there are fixed values that do not change in the robot after
	 * added to the game, the robot values update is partial.
	 * @param robot_Json - Json string the game server return with command "getRobots()",
	 * 					   containing information about the robot.
	 */
	public void update(String robot_Json) {
		try {
			JSONObject robot = new JSONObject(robot_Json).getJSONObject("Robot");
			speed = robot.getDouble("speed");
			src = robot.getInt("src");
			dest = robot.getInt("dest");
			location = new Point3D(robot.getString("pos"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the id of the robot.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the speed of the robot.
	 */
	public double getSpeed() {
		return speed;
	}
	
	/**
	 * @return the src node key the robot came out from.
	 */
	public int getSrc() {
		return src;
	}
	
	/**
	 * Gets the dest node key the robot going to. If equals -1 it means that it has 
	 * reached its destination and needs to define what the next move will be.
	 * @return the dest node key.
	 */
	public int getDest() {
		return dest;
	}
	
	/**
	 * @return the location of the robot.
	 */
	public Point3D getLocation() {
		return location;
	}

	/**
	 * Gets the path of the robot. This path is LinkedList of nodes where the
	 * robot keeps all the nodes it needs to pass through until he reaches next fruit.
	 * @return the path of the robot.
	 */
	public LinkedList<node_data> getPath() {
		return path;
	}
	
	/**
	 * Sets new path to the robot.
	 * @param pathToAdd - the new path.
	 */
	public void setPath(LinkedList<node_data> pathToAdd) {
		path.clear();
		path.addAll(pathToAdd);
	}

	/**
	 * Calculates the next node the robot goes to.
	 * If robot path is not empty then return the first node in path, and delete this node from path.
	 * But if the path is empty, then the robot will remain in its current location.
	 * @return the key of the next node.
	 */
	public int nextMove() {
		if(!path.isEmpty()) {
			return path.removeFirst().getKey();
		}
		return src;
	}
	
	/**
	 * Calculates the next node the robot goes to.
	 * If robot path is not empty then return the first node in path.
	 * But if the path is empty, then the robot will remain in its current location.
	 * @return the key of the next node.
	 */
	public int getNextMove() {
		if(!path.isEmpty()) {
			return path.getFirst().getKey();
		}
		return src;
	}
	
	/**
	 * @return how much points the robot collected so far.
	 */
	public double getValue() {
		return value;
	}
}