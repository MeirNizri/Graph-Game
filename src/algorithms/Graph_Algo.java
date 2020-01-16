package algorithms;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import dataStructure.*;
import utils.Point3D;

/**
 * This class represents the set of graph-theory algorithms
 * @author Meir Nizri, Avihai Bernholtz
 *
 */
public class Graph_Algo implements graph_algorithms, Serializable{

	/** graph-theory algorithms */
	public graph AlgoG;
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new graph algorithms.
	 */
	public Graph_Algo() {
		AlgoG = new DGraph();
	}
	
	/**
	 * Instantiates a new graph algorithms from graph
	 */
	public Graph_Algo(graph g) {
		this.init(g);
	}

	/* 
	 * Init this set of algorithms on the parameter - graph.
	 * @param g - directed graph
	 */
	@Override
	public void init(graph g) {

		if(g instanceof DGraph) {
			AlgoG = new DGraph();
			// Enter all nodes to the graph
			for(node_data itr: g.getV()) {
				node_data n=new node(itr.getKey());
				n.setInfo(itr.getInfo());
				n.setTag(itr.getTag());
				n.setWeight(itr.getWeight());
				n.setLocation(new Point3D(itr.getLocation()));
				AlgoG.addNode(n);
			}
			// Enter all edges to the graph
			for(node_data itr: g.getV()) {
				Collection<edge_data> edges=g.getE(itr.getKey());
				Iterator<edge_data> itr2=edges.iterator();
				while(itr2.hasNext()) {
					edge_data edge=itr2.next();
					AlgoG.connect(edge.getSrc(),edge.getDest(),edge.getWeight());

				}
			}
		}

	}


	/* 
	 * Init a graph from file
	 * @param file_name - name of file
	 */
	@Override
	public void init(String file_name) {

		try {
			FileInputStream file = new FileInputStream(file_name);
			ObjectInputStream in = new ObjectInputStream(file);
			// Read all information from file and enter our graph
			this.AlgoG= (graph) in.readObject();
			// close stream
			in.close();
			file.close();
			node.id = this.AlgoG.nodeSize()+1;
			//System.out.println("Object has been deserialized");
		} catch (IOException e) {
			System.err.println("IOException is caught,object didnt uploaded");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException is caught,object didnt uploaded");
		}
	}

	/* 
	 * Saves the graph to a file. 
	 * @param file_name
	 */
	@Override
	public void save(String file_name) {
		try {
			FileOutputStream file = new FileOutputStream(file_name);
			ObjectOutputStream out = new ObjectOutputStream(file);
			// write all information to file
			out.writeObject(this.AlgoG);
			// close stream
			out.close();
			file.close();
			//System.out.println("Object has been serialized");
		} catch (IOException e) {
			System.err.println("IOException is caught,Object didnt save.");
		}

	}


	/* 
	 * Returns true if and only if (iff) there is a valid path from EVREY node to each
	 * other node. NOTE: assume directional graph - a valid path (a-->b) does NOT imply a valid path (b-->a).
	 * @return true if graph connected.
	 */
	@Override
	public boolean isConnected() {
		// Reset all tags of the nodes to 0
		ReseTags(AlgoG);
		// Take the first node (key) and mark all nodes can be reached from it
		Collection<node_data> C1=AlgoG.getV();
		if(C1.size()==0)
			return true;
		int key=C1.iterator().next().getKey();
		MarkTags(key,AlgoG);
		// If not all nodes marked return false
		for(node_data itr:C1) {
			if(itr.getTag()==0) return false;
		}
		
		// Create new graph  with all the edges reversed
		graph Rev=new DGraph();
		Reversed(Rev,AlgoG);
		// Mark again all nodes can be reached from the node
		ReseTags(Rev);
		MarkTags(key,Rev);
		// If not all nodes marked return false
		Collection<node_data> C2=Rev.getV();
		for(node_data itr:C2) {
			if(itr.getTag()==0) return false;
		}
		return true;
	}

	/* 
	 * returns the length of the shortest path between src to dest
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return The shortest path in double
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		if(AlgoG.getNode(src)==null || AlgoG.getNode(dest)==null) 
			throw new IllegalArgumentException("one of the node you entered doesn't exist in this graph");
		// Activate Dijkstra algorithm to calculate all shortest path from src.
		Dijkstra(AlgoG, src);
		// Check if the distance to dest is infinity, which means there is no shortest path. If so returns -1
		double ans = AlgoG.getNode(dest).getWeight();
		if(ans == Double.MAX_VALUE) 
			return -1;
		else
			return ans;
	}

	/* 
	 * returns the the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * see: https://en.wikipedia.org/wiki/Shortest_path_problem
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return a List of the nodes of the shortest path 
	 */
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		// Check if there is a path from src to dest, if not return null.
		double distance = shortestPathDist(src, dest);
		if(distance == -1) return null;
		
		// After we activate Dijkstre in shortestPathDist the current path is the shortest.
		return findPath(AlgoG, src, dest);
	}

	/* 
	 * computes a relatively short path which visit each node in the targets List.
	 * Note: this is NOT the classical traveling salesman problem, 
	 * as you can visit a node more than once, and there is no need to return to source node - 
	 * just a simple path going over all nodes in the list.
	 * if even one node from targets is not in the same connected component the function returns null
	 * @param targets
	 * @return List of the shortest path of nodes to walk through @targets
	 */
	@Override
	public List<node_data> TSP(List<Integer> targets) {
		List<Integer> targetsCopy = new LinkedList<Integer>(targets);
		List<node_data> TSP = new LinkedList<node_data>();
		// Get and remove the first node from targets
		TSP.add(0,AlgoG.getNode(targetsCopy.remove(0)));
		
		while(!targetsCopy.isEmpty()) {
			// Activate Dijkstra algorithm to calculate distance to all nodes
			int src = TSP.get((TSP.size())-1).getKey();
			Dijkstra(AlgoG, src);
			// Find the node in targets the path to is the shortest
			node_data minWeightNode = AlgoG.getNode(targetsCopy.get(0));
			for(Integer i : targetsCopy) {
				if(AlgoG.getNode(i).getWeight() < minWeightNode.getWeight())
					minWeightNode = AlgoG.getNode(i);
			}
			// If aren't any - return null
			if(minWeightNode.getWeight() == Double.MAX_VALUE)
				return null;
			// Enter the shortest path to minWeight node to TSP
			List<node_data> SP = shortestPath(src, minWeightNode.getKey());
			SP.remove(0);
			TSP.addAll(SP);
			// Remove the minWeight node from targets
			int index = targetsCopy.indexOf(minWeightNode.getKey());
			targetsCopy.remove(index);
		}
		return TSP;
	}

	/* 
	 * Compute a deep copy of this AlgoG.
	 * @return a deep copy of AlgoG
	 */
	@Override
	public graph copy() {
		graph g=new DGraph();
		for(node_data itr: AlgoG.getV()) {
			node_data n=new node(itr.getKey());
			n.setInfo(itr.getInfo());
			n.setTag(itr.getTag());
			n.setWeight(itr.getWeight());
			n.setLocation(new Point3D(itr.getLocation()));
			g.addNode(n);
		}
		for(node_data itr: AlgoG.getV()) {
			Collection<edge_data> edges=AlgoG.getE(itr.getKey());
			Iterator<edge_data> itr2=edges.iterator();
			while(itr2.hasNext()) {
				edge_data edge=itr2.next();
				g.connect(edge.getSrc(),edge.getDest(),edge.getWeight());
			}
		}
		return g;
	}
	
	/* 
	 * calculate the shortest path between src to every other node in the graph
	 * see: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
	 * @param src - start node
	 * @param Graph - the graph
	 */
	public static void Dijkstra(graph Graph, int src) {
		// Check if src exist in the graph, if not throw exception.
		if(Graph.getNode(src)==null) {
			throw new IllegalArgumentException("The node you entered doesn't exist in this graph");
		}
		// Reset all the values we will use in this algorithm.
		for(node_data n : Graph.getV()) {
			n.setWeight(Double.MAX_VALUE);
			n.setInfo("");
			n.setTag(0);
		}
		Graph.getNode(src).setWeight(0);
		
		// Make collection of all nodes not visited
		Collection<node_data> notVisited=new LinkedList<>(Graph.getV());
		node_data minWeight;
		while (!notVisited.isEmpty()) {
			// Find the node with the minimum weight from all nodes in the collection.
			minWeight = findMinNode(notVisited);
			// Go over all the neighbors of minWeight that we didn't removed from the collection.
			for(edge_data e : Graph.getE(minWeight.getKey())) {
				node_data neighbor = Graph.getNode(e.getDest());
				if(neighbor.getInfo() != "finished") {
					double distance = minWeight.getWeight() + e.getWeight();
					// If we find a shorter distance update the weight. And save the node who lead to this node.
					if(distance < neighbor.getWeight()) {
						neighbor.setWeight(distance);
						neighbor.setTag(minWeight.getKey());
					}
				}
			}
			// Mark the node as "finished", and remove it from the collection
			minWeight.setInfo("finished");
			notVisited.remove(minWeight);
		}
	}
	
	/* 
	 * returns the the current path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return a List of the nodes of the path 
	 */
	public static List<node_data> findPath(graph Graph, int src, int dest){
		// In each node the "tag" field saves the key of the node from which we reached the current node.
		// We will start from dest and go through all the nodes until we reach the source node.
		List<node_data> nodePath = new LinkedList<node_data>();
		node_data n = Graph.getNode(dest);
		nodePath.add(n);
		// Each node in the path inserted into the stack
		while(n != Graph.getNode(src)) {		
			n = Graph.getNode(n.getTag());
			nodePath.add(0, n);
		} 
		return nodePath;
	}

	/**
	 * Resets all tags to be 0.
	 * @param g the g
	 */
	private static void ReseTags(graph g) {
		for(node_data itr:g.getV()) {
			itr.setTag(0);
		}
	}

	/**
	 * Marks all the tags that the node with the key it gets has path to.
	 * @param key the key
	 * @param g the g
	 */
	private static void MarkTags(int key,graph g) {
		Stack<Integer> Stack=new Stack<Integer>();
		Stack.push(key);
		while(!Stack.isEmpty()) {
			node_data n=g.getNode(Stack.peek());
			n.setTag(1);
			Stack.pop();
			for(edge_data itr:g.getE(n.getKey())) {
				boolean isExsits=g.getNode(itr.getDest())!=null;
				boolean NotPassYet=g.getNode(itr.getDest()).getTag()==0;
				if(isExsits&&NotPassYet) {
					Stack.push(itr.getDest());
					g.getNode(itr.getDest()).setTag(1);
				}
			}
		}
	}

	/**
	 * Finds a node with minimum weight in collection.
	 * @param nodes the nodes
	 * @return minWeight-node_data
	 */
	private static node_data findMinNode(Collection<node_data> nodes) {
		Iterator<node_data> itr = nodes.iterator();
		node_data minWeight = itr.next();
		for(node_data n : nodes) {
			if(n.getWeight() < minWeight.getWeight())
				minWeight = n;
		}
		return minWeight;
	}

	/**
	 * Reverse a graph.turns over all the edges to be b-->a instead of a-->b 
	 * @param rev the rev
	 * @param curr the curr
	 * @return the same graph reversed
	 */
	private static graph Reversed(graph rev,graph curr){
		for(node_data n:curr.getV()) {
			rev.addNode(new node(n.getKey()));
			rev.getNode(n.getKey()).setInfo(n.getInfo());
			rev.getNode(n.getKey()).setWeight((n.getWeight()));
			rev.getNode(n.getKey()).setLocation((n.getLocation()));
		}
		for(node_data n:curr.getV()) {
			for(edge_data e:curr.getE(n.getKey())){
				rev.connect(e.getDest(),e.getSrc(),e.getWeight());
			}
		}
		return rev;
	}
}