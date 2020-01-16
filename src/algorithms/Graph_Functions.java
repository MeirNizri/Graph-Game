package algorithms;

import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;
import utils.Range;

/**
 * This class represents set of static functions for graph GUI
 * @author Meir Nizri
 */
public class Graph_Functions {

	public Graph_Functions() {}

	/**
	 * Find the min and max x location in this graph
	 * @param g - the graph 
	 * @return range of x in the graph
	 */
	public static Range find_rx(graph g) {
		double maxX = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE;
		// Move all node locations and find the minimum and maximum x
		for(node_data n : g.getV()) {
			if(maxX < n.getLocation().x())
				maxX = n.getLocation().x();
			if(minX > n.getLocation().x())
				minX = n.getLocation().x();
		}
		return (new Range(minX, maxX));
	}

	/**
	 * Find the min and max y location in this graph
	 * @param g - the graph 
	 * @return The range of y in the graph
	 */
	public static Range find_ry(graph g) {
		double maxY = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		// Move all node locations and find the minimum and maximum y
		for(node_data n : g.getV()) {
			if(maxY < n.getLocation().y())
				maxY = n.getLocation().y();
			if(minY > n.getLocation().y())
				minY = n.getLocation().y();
		}
		return (new Range(minY, maxY));
	}

	/**
	 * Calculates point on the edge from the src to the dest points, in distance of number of resolutions from the dest point.
	 * @param src - src node location
	 * @param dest - dest node location
	 * @param resolution - resolution on the graph
	 * @param numOfRes - number of resolutions
	 * @return point on the edge from the src to the dest.
	 */
	public static Point3D findPointOnLine(Point3D src, Point3D dest, int numOfRes, double resolution) {
		double dist = dest.distance2D(src);
		double percent = (resolution*numOfRes)/dist;				
		Point3D start, end;
		double z;
		// Find the left node and insert it to start
		if(src.x() < dest.x()) {
			start = src;
			end = dest;
			z = -(dest.x()-src.x())*percent;
		}
		else {
			start = dest;
			end = src;
			z = (src.x()-dest.x())*percent;;
		}
		// Calculating the slope, cutting with the y axis.
		double m = (end.y()-start.y()) / (end.x()-start.x());
		double nn = (m * (-start.x())) + start.y();
		// Building the equation and calculate y and x.
		double y = (m * (dest.x()+z)) + nn;
		double x = dest.x() + z;
		return (new Point3D(x, y, 0));
	}
	
	/**
	 * Find the node the user pressed on in the gui. if not found return null.
	 * @param x - the x location of the mouse
	 * @param y - the y location of the mouse
	 * @param Graph - the Graph containing the nodes
	 * @param resolution - on the graph to check if close enough
	 * @return the node the user pressed on, or null if node not found.
	 */
	public static node_data getNodeOnPoint(double x, double y, graph Graph, double resolution) {
		Point3D p = new Point3D(x, y);
		// Traverse all nodes until you find node that close enough to the point the user pressed
		for(node_data n : Graph.getV()) {
			if(p.distance2D(n.getLocation()) < resolution*2) {
				return n;
			}
		}
		return null;
	}
}