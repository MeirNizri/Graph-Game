package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import gameObjects.Robot;
import gameObjects.Robot_Comperator;

class Robot_ComperatorTest {

	@Test
	void testCompare() {
		Robot_Comperator comperator = new Robot_Comperator();
		String robot_Json1 = "{\"Robot\":{\"id\":0,\"value\":4.0,\"src\":7,\"dest\":-1,\"speed\":2.0,\"pos\":\"32.3,34.1,0.0\"}}";
		String robot_Json2 = "{\"Robot\":{\"id\":0,\"value\":4.0,\"src\":7,\"dest\":-1,\"speed\":3.0,\"pos\":\"32.3,34.1,0.0\"}}";
		Robot robot1 = new Robot(robot_Json1);
		Robot robot2 = new Robot(robot_Json2);
		int i1 = comperator.compare(robot1, robot2);
		int i2 = comperator.compare(robot2, robot1);
		assertEquals(1, i1);
		assertEquals(-1, i2);
		
		// Check if sorted correctly
		LinkedList<Robot> robots = new LinkedList<Robot>();
		robots.add(robot1);
		robots.add(robot2);
		Collections.sort(robots, new Robot_Comperator());
		assertEquals(robots.getFirst(), robot2);
		assertEquals(robots.getLast(), robot1);
	}
}