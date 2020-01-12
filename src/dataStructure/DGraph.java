package dataStructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import org.json.JSONObject;

import utils.Point3D;

import org.json.JSONArray;

/**
 * This interface represents a directional weighted graph.
 * The interface has a road-system or communication network in mind - and should support a large number of nodes (over 100,000).
 * The implementation should be based on an efficient compact representation (should NOT be based on a n*n matrix).
 * @author Meir Nizri, Avihai Bernholtz
 */
public class DGraph implements graph,Serializable{
	
	// HashMap that to every node key in the graph contains its node
	private HashMap<Integer, node_data> nodes; 
	// HashMap that to every node key in the graph contains HashMap to its edges
	private HashMap<Integer, HashMap<Integer, edge_data>> adjacencyMap; 
	// Mode counter to track the changes in the graph
	private static int MC;
	private int numEdges = 0;
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor
	 */
	public DGraph() {
		this.nodes = new HashMap<Integer, node_data>();
		this.adjacencyMap = new HashMap<Integer, HashMap<Integer, edge_data>>();
		MC = 0;
	}
	
	/**
	 * Initializes a graph with nodes but without edges
	 * @param nodes - Collection of all nodes that need to be inserted into the graph 
	 */
	public DGraph(Collection<node_data> nodes) {
		this.nodes = new HashMap<Integer, node_data>();
		this.adjacencyMap = new HashMap<Integer, HashMap<Integer, edge_data>>();
		// Add all the nodes to the DGraph
		for(node_data n : nodes)
			addNode(n);
		MC = 0;
	}
	
	/**
	 * Initializes a graph with nodes and edges
	 * @param nodes - Collection of all nodes that need to be inserted into the graph 
	 * @param edges - Collection of all edges that need to be inserted into the graph
	 */
	public DGraph(Collection<node_data> nodes, Collection<edge_data> edges) {
		this.nodes = new HashMap<Integer, node_data>();
		this.adjacencyMap = new HashMap<Integer, HashMap<Integer, edge_data>>();
		// Add all the nodes to the DGraph
		for(node_data n : nodes)
			addNode(n);
		// Add all the edges to the DGraph
		for(edge_data e : edges)
			connect(e.getSrc(), e.getDest(), e.getWeight());
		MC = 0;
		numEdges = edges.size();
	}
	
	/**
	 * Initialize graph from Json String
	 * @param Json_String - Json string containing array of nodes and array of edges
	 */
    public void init(String Json_String) {
        // Reset nodes, edges, node id, and num of edges
        nodes.clear();
        adjacencyMap.clear();
        node.id = 0;
        numEdges = 0;
        // Read all information from string and enter DGraph
        try {
            JSONObject graph = new JSONObject(Json_String);
            JSONArray nodes_Json = graph.getJSONArray("Nodes");
            JSONArray edges_Json = graph.getJSONArray("Edges");
            // Add each node in the Json String
            for (int i = 0; i < nodes_Json.length(); i++) {
                int key = nodes_Json.getJSONObject(i).getInt("id");
                String location = nodes_Json.getJSONObject(i).getString("pos");
                Point3D p = new Point3D(location);
                this.addNode(new node(key, p));
            }
            // Connect each edge in the Json String
            for (int i = 0; i < edges_Json.length(); i++) {
                int src = edges_Json.getJSONObject(i).getInt("src");
                int dest = edges_Json.getJSONObject(i).getInt("dest");
                double weight = edges_Json.getJSONObject(i).getDouble("w");
                this.connect(src, dest, weight);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * return the node_data by the node_id, null if none.
	 * @param key - the node_id
	 * @return the node_data, null if none.
	 */
	@Override
	public node_data getNode(int key) {
		return nodes.get(key);
	}

	/**
	 * return the data of the edge (src,dest), null if none.
	 * @param src - the source node of the edge
	 * @param dest - the destination node of the edge
	 * @return the edge_data, null if none.
	 */
	@Override
	public edge_data getEdge(int src, int dest) {
		// Check if the source node exists
		if(getNode(src) == null)
			return null;
		// Find the HashMap to all the edges coming out of the source, and than the edge itself.
		else
			return adjacencyMap.get(src).get(dest);
	}

	/**
	 * add a new node to the graph with the given node_data. if already exists throw RunTime Exception
	 * @param n - the node to add
	 */
	@Override
	public void addNode(node_data n) {
		// Check if node with this key already exists in the graph
		if(nodes.containsKey(n.getKey()))
			throw new RuntimeException("Unable to insert " + n.toString() + ", " +
									   "node with this key already exists in the graph");
		// Add the node to the nodes map and to the adjacency Map
		nodes.put(n.getKey(), n);
		adjacencyMap.put(n.getKey(), new HashMap<Integer, edge_data>());
		// Update mode count
		MC++;
	}
	
	/**
	 * Connect an edge with weight w between node src to node dest. if already exists throw RunTime Exception
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
	 */
	@Override
	public void connect(int src, int dest, double w) {
		// Check if src equals to dest, if so throw runtime exception
		if(src == dest)
			throw new RuntimeException("can't connect node to itself");
		edge_data newEdge = new edge(src, dest, w);
		// Check if edge with this src and dest already exists in the graph
		if(adjacencyMap.get(src).containsKey(dest))
			throw new RuntimeException("Unable to insert " + newEdge.toString() + ", " +
									   "edge with this src and dest already exists in the graph");
		// Find the HashMap to all the edges coming out of the source, and than put the new edge.
		adjacencyMap.get(src).put(dest, newEdge);
		// Update mode count and num of edges
		MC++;
		numEdges++;
	}

	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph. 
	 * @return Collection of all the nodes
	 */
	@Override
	public Collection<node_data> getV() {
		return nodes.values();
	}

	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the edges getting out of 
	 * the given node (all the edges starting (source) at the given node). 
	 * @return Collection of all the edges
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		return adjacencyMap.get(node_id).values();
	}

	/**
	 * Delete the node with the given ID from the graph,
	 * and removes all edges which starts or ends at this node.
	 * This method should run in O(n), |V|=n, as all the edges should be removed.
	 * @param key - the id of the node to remove
	 * @return the data of the removed node (null if none). 
	 */
	@Override
	public node_data removeNode(int key) {
		// Delete and the node from the nodes map
		node_data removedNode = nodes.remove(key);
		// Delete all the edges from the node
		adjacencyMap.remove(key);
		// Delete all the edges to the node
		for (int itrKey : nodes.keySet()) {
			if(adjacencyMap.get(itrKey).containsKey(key))
				adjacencyMap.get(itrKey).remove(key);
		}
		// Update mode count
		if(removedNode != null)	MC++;
		// Return the node removed or null if not exists
		return removedNode;
	}

	/**
	 * Delete the edge from the graph. 
	 * Note: this method should run in O(1) time.
	 * @param src - the source of the edge
	 * @param dest - the destination of the edge
	 * @return the data of the removed edge (null if none).
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {
		edge_data removedEdge;
		// Check if the source node exists
		if(getNode(src) == null)
			removedEdge = null;
		// Find the HashMap to all the edges coming out of the source, and than remove the edge
		else
			removedEdge = adjacencyMap.get(src).remove(dest);
		// Update mode count
		if(removedEdge != null)	{
			MC++;
			numEdges--;
		}
		// Return the edge removed or null if not exists
		return removedEdge;
	}

	/**
	 * This method return the number of vertices (nodes) in the graph.
	 * Note: this method should run in O(1) time. 
	 * @return the number of vertices (nodes) in the graph.
	 */
	@Override
	public int nodeSize() {
		return nodes.size();
	}

	/** 
	 * This method return the number of edges in the graph (assume directional graph).
	 * Note: this method should run in O(1) time.
	 * @return the number of edges in the graph
	 */
	@Override
	public int edgeSize() {
		return numEdges;
	}

	/**
	 * This method return the Mode Count - for testing changes in the graph.
	 * @return the Mode Count
	 */
	@Override
	public int getMC() {
		return MC;
	}
}