package Tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import graphDataStructure.edge;

class edgeTest {

	@Test
	void testEdge() {
		edge e = new edge();
		assertEquals(e.getSrc(), -1);
		assertEquals(e.getDest(), -1);
		assertEquals(e.getWeight(), 0);
		assertEquals(e.getTag(), 0);
	}
	
	@Test
	void testEdgeIntInt() {
		edge e = new edge(11, 19);
		assertEquals(e.getSrc(), 11);
		assertEquals(e.getDest(), 19);
		assertEquals(e.getWeight(), 0);
		assertEquals(e.getTag(), 0);
	}

	@Test
	void testEdgeIntIntDouble() {
		edge e = new edge(11, 19, 10);
		assertEquals(e.getSrc(), 11);
		assertEquals(e.getDest(), 19);
		assertEquals(e.getWeight(), 10);
		assertEquals(e.getTag(), 0);
	}

	@Test
	void testSetInfo() {
		edge e = new edge();
		e.setInfo("Check setInfo method");
		assertEquals(e.getInfo(), "Check setInfo method");
	}

	@Test
	void testSetTag() {
		edge e = new edge();
		e.setTag(2);
		assertEquals(e.getTag(), 2);
	}

	@Test
	void testEqualsObject() {
		edge e1 = new edge(11, 19, 10);
		edge e2 = new edge(11, 19, 10);
		boolean isTrue = e1.equals(e2);
		assertTrue(isTrue);
		edge e3 = new edge(10, 19, 10);
		boolean isFalse = e1.equals(e3);
		assertFalse(isFalse);
		String e4 = "edge";
		try {
			@SuppressWarnings("unused")
			boolean isException = e1.equals(e4);
		    fail("Should return IllegalArgumentException");
		} 
		catch (IllegalArgumentException e) {}
	}

	@Test
	void testToString() {
		edge e = new edge(11, 19, 10);
		assertEquals(e.toString(), "[Src: " + e.getSrc() + ", " +
								    "Dest: " + e.getDest() + ", " +
								    "Weight: " + e.getWeight() + "]");
	}
}