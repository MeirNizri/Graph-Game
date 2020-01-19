package Tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.util.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import graphDataStructure.DGraph;
import graphDataStructure.edge;
import graphDataStructure.edge_data;
import graphDataStructure.node;
import graphDataStructure.node_data;

class DGraphTest {
	Collection<node_data> nodes = new LinkedList<node_data>();
	Collection<edge_data> edges = new LinkedList<edge_data>();
	
    @BeforeEach public void initialize() {
    	node.id = 1;
    	nodes.add(new node());
    	nodes.add(new node());
    	nodes.add(new node());
    	nodes.add(new node());
    	nodes.add(new node());
    	edges.add(new edge(3,1,1));
    	edges.add(new edge(2,3,2));
    	edges.add(new edge(2,5,4));
    	edges.add(new edge(4,5,1));
    	edges.add(new edge(3,2,1));
     }

	@Test
	void testDGraph() {
		DGraph g = new DGraph();
		boolean isTrue = g.getV().isEmpty();
		assertTrue(isTrue);
	}

	@Test
	void testDGraphCollectionOfnode_data() {
		DGraph g = new DGraph(nodes);
		assertNotNull(g.getNode(1));
		assertNotNull(g.getNode(2));
		assertNotNull(g.getNode(3));
		assertNotNull(g.getNode(4));
		assertNotNull(g.getNode(5));
		assertNull(g.getNode(10));
	}

	@Test
	void testDGraphCollectionOfnode_dataCollectionOfedge_data() {
		DGraph g = new DGraph(nodes, edges);
		assertNotNull(g.getNode(1));
		assertNotNull(g.getNode(2));
		assertNotNull(g.getNode(3));
		assertNotNull(g.getNode(4));
		assertNotNull(g.getNode(5));
		assertNull(g.getNode(10));
		assertEquals(g.getEdge(3, 1), new edge(3,1,1));
		assertEquals(g.getEdge(2, 3), new edge(2,3,2));
		assertEquals(g.getEdge(2, 5), new edge(2,5,4));
		assertEquals(g.getEdge(4, 5), new edge(4,5,1));
		assertEquals(g.getEdge(3, 2), new edge(3,2,1));
		assertNull(g.getEdge(12, 3));
	}

	@Test
	void testGetNode() {
		DGraph g = new DGraph(nodes);
		node_data n = new node();
		g.addNode(n);
		assertEquals(g.getNode(6), n);
		assertNull(g.getNode(10));
	}

	@Test
	void testGetEdge() {
		DGraph g = new DGraph(nodes, edges);
		assertEquals(g.getEdge(4, 5), new edge(4,5,1));
		assertNull(g.getEdge(2, 4));
		assertNull(g.getEdge(5, 2));
	}

	@Test
	void testAddNode() {
		node_data n = new node();
		DGraph g = new DGraph(nodes);
		g.addNode(n);
		assertEquals(g.getNode(6), n);
		try {
			g.addNode(n);
		    fail("Should return RuntimeException");
		} 
		catch (RuntimeException e) {}
	}

	@Test
	void testConnect() {
		edge_data e = new edge(4, 1);
		DGraph g = new DGraph(nodes, edges);
		g.connect(4, 1, 0);
		assertEquals(g.getEdge(4, 1), e);
		try {
			g.connect(4, 1, 0);
		    fail("Should return RuntimeException");
		} 
		catch (RuntimeException f) {}
	}

	@Test
	void testGetV() {
		Collection<node_data> n1 = nodes;
		DGraph g = new DGraph(nodes, edges);
		Collection<node_data> n2 = new LinkedList<node_data>(g.getV());
		assertEquals(n1, n2);
	}

	@Test
	void testGetE() {
		Collection<edge_data> e1 = new LinkedList<edge_data>();
		e1.add(new edge(2,3,2));
		e1.add(new edge(2,5,4));
		DGraph g = new DGraph(nodes, edges);
		Collection<edge_data> e2 = new LinkedList<edge_data>(g.getE(2));
		assertEquals(e1, e2);
	}

	@Test
	void testRemoveNode() {
		DGraph g = new DGraph(nodes, edges);
		node_data n1 = g.removeNode(2);
		node_data n2 = g.removeNode(2);
		assertNull(g.getNode(2));
		assertNull(g.getEdge(2, 3));
		assertNull(g.getEdge(2, 5));
		assertEquals(n1.getKey(), 2);
		assertNull(n2);
	}

	@Test
	void testRemoveEdge() {
		DGraph g = new DGraph(nodes, edges);
		edge_data e1 = g.removeEdge(4, 5);
		edge_data e2 = g.removeEdge(3, 2);
		edge_data e3 = g.removeEdge(3, 2);
		edge_data e4 = g.removeEdge(1, 1);
		assertNull(g.getEdge(4, 5));
		assertNull(g.getEdge(3, 2));
		assertEquals(e1, new edge(4, 5, 1));
		assertEquals(e2, new edge(3, 2, 1));
		assertNull(e3);
		assertNull(e4);
	}

	@Test
	void testNodeSize() {
		DGraph g = new DGraph(nodes, edges);
		assertEquals(g.nodeSize(), 5);
		g.addNode(new node());
		assertEquals(g.nodeSize(), 6);
	}

	@Test
	void testEdgeSize() {
		DGraph g = new DGraph(nodes, edges);
		assertEquals(g.edgeSize(), 5);
		g.connect(4, 1, 2);
		assertEquals(g.edgeSize(), 6);
	}

	@Test
	void testGetMC() {
		DGraph g = new DGraph(nodes, edges);
		assertEquals(g.getMC(), 0);
		g.connect(4, 1, 2);
		g.removeNode(1);
		g.removeEdge(1, 1);
		g.removeEdge(4, 10);
		assertEquals(g.getMC(), 2);
		g.removeEdge(4, 5);
		assertEquals(g.getMC(), 3);
	}
}