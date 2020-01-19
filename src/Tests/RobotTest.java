package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import gameObjects.Robot;
import graphDataStructure.node;
import graphDataStructure.node_data;
import utils.Point3D;

class RobotTest {
	static game_service game;
	static String robot_Json;
	static Robot robot;

	@BeforeAll
	public static void initialize() {
		game = Game_Server.getServer(0);
		game.addRobot(9);
		List<String> Robots_Json = game.getRobots();
		robot_Json = Robots_Json.get(0);
	}
	
	@BeforeEach
	public void initializeRobot() {
		robot = new Robot(robot_Json);
	}

	@Test
	void testRobotString() {
		Point3D p = new Point3D("35.19597880064568,32.10154696638656,0.0");
		assertEquals(0, robot.getId());
		assertEquals(p, robot.getLocation());
		assertEquals(9, robot.getSrc());
		assertEquals(-1, robot.getDest());
		assertEquals(1.0, robot.getSpeed());
		assertEquals(0.0, robot.getValue());
		assertTrue(robot.getPath().isEmpty());
	}

	@Test
	void testUpdate() {
		String robot_Json2 = "{\"Robot\":{\"id\":0,\"value\":4.0,\"src\":7,\"dest\":-1,\"speed\":2.0,"
							+ "\"pos\":\"32.19534560064568,34.124252438656,0.0\"}}";
		robot.update(robot_Json2);
		Point3D p = new Point3D("32.19534560064568,34.124252438656,0.0");
		assertEquals(0, robot.getId());
		assertEquals(p, robot.getLocation());
		assertEquals(7, robot.getSrc());
		assertEquals(-1, robot.getDest());
		assertEquals(2.0, robot.getSpeed());
		assertEquals(0.0, robot.getValue());
		assertTrue(robot.getPath().isEmpty());
	}

	@Test
	void testGetId() {
		assertEquals(0, robot.getId());
	}

	@Test
	void testGetSpeed() {
		assertEquals(1.0, robot.getSpeed());
	}

	@Test
	void testGetSrc() {
		assertEquals(9, robot.getSrc());
	}

	@Test
	void testGetDest() {
		assertEquals(-1, robot.getDest());
	}

	@Test
	void testGetLocation() {
		Point3D p = new Point3D("35.19597880064568,32.10154696638656,0.0");
		assertEquals(p, robot.getLocation());
	}

	@Test
	void testGetPath() {
		assertTrue(robot.getPath().isEmpty());
		LinkedList<node_data> pathToAdd = new LinkedList<node_data>();
		pathToAdd.add(new node());
		pathToAdd.add(new node());
		pathToAdd.add(new node());
		robot.setPath(pathToAdd);
		assertEquals(robot.getPath(), pathToAdd);
	}

	@Test
	void testSetPath() {
		LinkedList<node_data> pathToAdd = new LinkedList<node_data>();
		pathToAdd.add(new node());
		pathToAdd.add(new node());
		pathToAdd.add(new node());
		robot.setPath(pathToAdd);
		assertEquals(robot.getPath(), pathToAdd);
	}

	@Test
	void testNextMove() {
		assertEquals(9, robot.nextMove());
		LinkedList<node_data> pathToAdd = new LinkedList<node_data>();
		node.id = 0;
		pathToAdd.add(new node());
		pathToAdd.add(new node());
		pathToAdd.add(new node());
		robot.setPath(pathToAdd);
		assertEquals(0, robot.nextMove());
		assertEquals(1, robot.nextMove());
		assertEquals(2, robot.nextMove());
		assertTrue(robot.getPath().isEmpty());
	}

	@Test
	void testGetNextMove() {
		assertEquals(9, robot.getNextMove());
		LinkedList<node_data> pathToAdd = new LinkedList<node_data>();
		node.id = 0;
		pathToAdd.add(new node());
		pathToAdd.add(new node());
		pathToAdd.add(new node());
		robot.setPath(pathToAdd);
		assertEquals(0, robot.getNextMove());
	}

	@Test
	void testGetValue() {
		assertEquals(0.0, robot.getValue());
	}
}