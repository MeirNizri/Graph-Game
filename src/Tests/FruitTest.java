package Tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import gameObjects.Fruit;
import graphDataStructure.DGraph;
import graphDataStructure.edge;
import graphDataStructure.node;
import utils.Point3D;

class FruitTest {
	static game_service game;
	static DGraph Graph = new DGraph();
	static String fruit_Json;
	static Fruit fruit;

	@BeforeAll
	public static void initialize() {
		game = Game_Server.getServer(0);
		Graph.init(game.getGraph());
		List<String> Fruits_Json = game.getFruits();
		fruit_Json = Fruits_Json.get(0);
	}
	
	@BeforeEach
	public void initializeFruit() {
		fruit = new Fruit(Graph, fruit_Json);
	}

	@Test
	void testFruitGraphString() {
		Point3D p = new Point3D("35.197656770719604,32.10191878639921,0.0");
		edge e = new edge(8,9,1.8526880332753517);
		node n = new node(9, new Point3D(35.19597880064568,32.10154696638656,0.0));
		assertEquals(-1, fruit.getType());
		assertEquals(p, fruit.getLocation());
		assertEquals(e, fruit.getEdge());
		assertEquals(9, fruit.getSrc());
		assertEquals(8, fruit.getDest());
		assertEquals(n, fruit.getSrcNode());
		assertEquals(-1, fruit.getType());
		assertEquals(5.0, fruit.getValue());
		assertFalse(fruit.getIsTaken());
	}

	@Test
	void testGetType() {
		assertEquals(-1, fruit.getType());
	}

	@Test
	void testGetLocation() {
		Point3D p = new Point3D("35.197656770719604,32.10191878639921,0.0");
		assertEquals(p, fruit.getLocation());
	}

	@Test
	void testGetEdge() {
		edge e = new edge(8,9,1.8526880332753517);
		assertEquals(e, fruit.getEdge());
	}

	@Test
	void testGetSrc() {
		assertEquals(9, fruit.getSrc());
	}

	@Test
	void testGetDest() {
		assertEquals(8, fruit.getDest());
	}

	@Test
	void testGetSrcNode() {
		node n = new node(9, new Point3D(35.19597880064568,32.10154696638656,0.0));
		assertEquals(n, fruit.getSrcNode());
	}

	@Test
	void testGetValue() {
		assertEquals(5.0, fruit.getValue());
	}

	@Test
	void testGetIsTaken() {
		assertFalse(fruit.getIsTaken());
	}

	@Test
	void testSetIsTaken() {
		fruit.setIsTaken(true);
		assertTrue(fruit.getIsTaken());
	}

	@Test
	void testEqualsFruit() {
		Fruit f = new Fruit(Graph,"{\"Fruit\":{\"value\":5.0,\"type\":-1,\"pos\":\"35.197656770719604,32.10191878639921,0.0\"}}");
		Fruit f1=new Fruit(Graph,"{\"Fruit\":{\"value\":8.0,\"type\":-1,\"pos\":\"35.199963710098416,32.105723673136964,0.0\"}}");
		assertTrue(fruit.equals(f));
		assertFalse(fruit.equals(f1));
	}
}