package gameObjects;

import java.util.LinkedList;
import org.json.JSONException;
import org.json.JSONObject;
import dataStructure.node_data;
import utils.Point3D;

public class Robot {
	
	private int id;
	private double speed;
	private int src;
	private int dest;
	private Point3D location;
	private LinkedList<node_data> path;
	private node_data nextNode;
	private int timeToEndPath;
	private double value;
	
	public Robot() {
		
	}
	
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
	
	public void update(String robot_Json) {
		try {
			JSONObject robot = new JSONObject(robot_Json).getJSONObject("Robot");
			src = robot.getInt("src");
			dest = robot.getInt("dest");
			location = new Point3D(robot.getString("pos"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public int getId() {
		return id;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public int getSrc() {
		return src;
	}
	
	public int getDest() {
		return dest;
	}
	
	public Point3D getLocation() {
		return location;
	}

	public LinkedList<node_data> getPath() {
		return path;
	}
	
	public void addPath(LinkedList<node_data> pathToAdd, double timeToAdd) {
		path.addAll(pathToAdd);
		timeToEndPath += timeToAdd;
	}
	
	public node_data getNextNode() {
		return nextNode;
	}
	
	public void updateNextNode() {
		if(path != null)
			nextNode = path.removeFirst();
	}
	
	public int getTimeToEndPath() {
		return timeToEndPath;
	}
	
	public double getValue() {
		return value;
	}
}