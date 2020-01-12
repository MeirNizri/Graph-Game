package gameObjects;

import org.json.JSONException;
import org.json.JSONObject;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

public class Fruit {

	private int type;
	private Point3D location;
	private edge_data edge;
	private int edgeSrc;
	private int edgeDest;
	private double value;
	private double EPSILON = 0.001;
	
	public Fruit(graph g, String fruit_Json) {
		try {
			JSONObject fruit = new JSONObject(fruit_Json).getJSONObject("Fruit");
			type = fruit.getInt("type");
			location = new Point3D(fruit.getString("pos"));
			edge = findEdge(g, location);
			edgeSrc = edge.getSrc();
			edgeDest = edge.getDest();
			value = fruit.getDouble("value");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void update(graph g, String fruit_Json) {
		try {
			JSONObject fruit = new JSONObject(fruit_Json).getJSONObject("Fruit");
			type = fruit.getInt("type");
			location = new Point3D(fruit.getString("pos"));
			edge = findEdge(g, location);
			edgeSrc = edge.getSrc();
			edgeDest = edge.getDest();
			value = fruit.getDouble("value");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public int getType() {
		return type;
	}
	
	public Point3D getLocation() {
		return location;
	}
	
	public edge_data getEdge() {
		return edge;
	}
	
	public int getEdgeSrc() {
		return edgeSrc;
	}
	
	public int getEdgeDest() {
		return edgeDest;
	}
	
	private edge_data findEdge(graph g, Point3D p) {
		for(node_data n : g.getV()) {
			Point3D src_p = n.getLocation();
			for(edge_data e : g.getE(n.getKey())) {
				Point3D dest_p = g.getNode(e.getDest()).getLocation();
				if((p.distance2D(src_p) + p.distance2D(dest_p)) - src_p.distance2D(dest_p) < EPSILON)
					return e;
			}
		}
		return null;
	}
	
	public double getValue() {
		return value;
	}
}