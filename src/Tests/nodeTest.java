package Tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import graphDataStructure.node;
import utils.Point3D;

class nodeTest {
	
	@Test
	void testNode() {
		node n = new node();
		assertEquals(n.getKey(), 1);
		assertEquals(n.getWeight(), 0);
		assertEquals(n.getLocation(), null);
		assertEquals(n.getTag(), 0);
	}

	@Test
	void testNodePoint3D() {
		node n = new node(new Point3D(1,2,3));
		assertEquals(n.getLocation(), new Point3D(1,2,3));
		assertEquals(n.getWeight(), 0);
		assertEquals(n.getTag(), 0);
	}


	@Test
	void testSetLocation() {
		node n = new node();
		Point3D p = new Point3D(3,2,1);
		n.setLocation(p);
		assertEquals(n.getLocation(), p);
	}


	@Test
	void testSetWeight() {
		node n = new node();
		n.setWeight(15.86);
		assertEquals(n.getWeight(), 15.86);
	}

	@Test
	void testSetInfo() {
		node n = new node();
		n.setInfo("Check setInfo method");
		assertEquals(n.getInfo(), "Check setInfo method");
	}

	@Test
	void testSetTag() {
		node n = new node();
		n.setTag(2);
		assertEquals(n.getTag(), 2);
	}
	
	@Test
	void testEqualsObject() {
		node n1 = new node();
		node n2 = new node();
		boolean isFalse = n1.equals(n2);
		assertFalse(isFalse);
		String n3 = "node";
		try {
			@SuppressWarnings("unused")
			boolean isException = n1.equals(n3);
		    fail("Should return IllegalArgumentException");
		} 
		catch (IllegalArgumentException e) {}
	}

	@Test
	void testToString() {
		node n1 = new node(new Point3D(1,2,3));
		assertEquals(n1.toString(), "[Key: " + n1.getKey() + ", " +
								    "Location: (" + n1.getLocation() + ")]");
		node n2 = new node();
		assertEquals(n2.toString(), "[Key: " + n2.getKey() + ", " +
								    "Location: null]");
	}
}