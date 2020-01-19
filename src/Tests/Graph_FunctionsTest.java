package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import graphAlgorithms.Graph_Functions;
import graphDataStructure.DGraph;
import graphDataStructure.edge;
import graphDataStructure.edge_data;
import graphDataStructure.node_data;
import utils.Point3D;
import utils.Range;

class Graph_FunctionsTest {	
	static game_service game = Game_Server.getServer(0);
	static DGraph Graph = new DGraph();

	@BeforeAll
	public static void initialize() {
		Graph.init(game.getGraph());
	}

	@Test
	void testFind_rx() {
		Range r = Graph_Functions.find_rx(Graph);
		assertEquals(r.get_min(), 35.18753053591606);
		assertEquals(r.get_max(), 35.20792948668281);
	}

	@Test
	void testFind_ry() {
		Range r = Graph_Functions.find_ry(Graph);
		assertEquals(r.get_min(), 32.10154696638656);
		assertEquals(r.get_max(), 32.10785303529412);

	}

	@Test
	void testFindPointOnLine() {
		Point3D src = new Point3D("10,10,0.0");
		Point3D dest = new Point3D("20,10,0.0");
		double resolution = 2;
		Point3D actual = Graph_Functions.findPointOnLine(src, dest, 2, resolution);
		Point3D expected = new Point3D("16,10,0.0");
		assertEquals(expected, actual);
	}

	@Test
	void testGetNodeOnPoint() {
		node_data expected = Graph.getNode(0);
		Point3D p = new Point3D(expected.getLocation());
		double radius = 0.020398950766747248/250;
		node_data actual = Graph_Functions.getNodeOnPoint(p.x(), p.y(), Graph, radius);
		assertEquals(expected, actual);		
	}

	@Test
	void testFindEdge() {
		edge_data expected = new edge(8,9,1.8526880332753517);
		Point3D p = new Point3D("35.197656770719604,32.10191878639921,0.0");
		edge_data actual = Graph_Functions.findEdge(Graph, p, 0.000000000001);
		assertEquals(expected, actual);
	}
}