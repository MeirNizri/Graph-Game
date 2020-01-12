package algorithms;

import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;
import utils.Range;

public class graphFunctions {

	public graphFunctions() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Find the min and max x location in this graph
	 * @param g - the graph 
	 * @return The range of x in the graph
	 */
	public static Range find_rx(graph g) {
		double maxX = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE;
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
		for(node_data n : g.getV()) {
			if(maxY < n.getLocation().y())
				maxY = n.getLocation().y();
			if(minY > n.getLocation().y())
				minY = n.getLocation().y();
		}
		return (new Range(minY, maxY));
	}

	public static Point3D findPointOnLine(Point3D src, Point3D dest, int numOfCircles, double radius) {
		double dist = dest.distance2D(src);
		double percent = (radius*numOfCircles)/dist;				
		Point3D start, end;
		double z;
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
		double m = (end.y()-start.y()) / (end.x()-start.x());
		double nn = (m * (-start.x())) + start.y();
		double y = (m * (dest.x()+z)) + nn;
		double x = dest.x() + z;
		return (new Point3D(x, y, 0));
	}
	

	/**
	 * Find the node the user pressed on in the gui
	 * @param x - the x location of the mouse
	 * @param y - the y location of the mouse
	 * @return the node the user pressed on
	 */
	public static node_data getNodeOnPoint(double x, double y, graph Graph, double radius) {
		Point3D p = new Point3D(x, y);
		for(node_data n : Graph.getV()) {
			if(p.distance2D(n.getLocation()) < radius*2) {
				return n;
			}
		}
		return null;
	}

}
