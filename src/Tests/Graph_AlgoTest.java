package Tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import algorithms.Graph_Algo;
import dataStructure.*;
import utils.Point3D;

class Graph_AlgoTest {

	Collection<node_data> nodes = new LinkedList<node_data>();
	Collection<edge_data> edges = new LinkedList<edge_data>();

	@BeforeEach public void initialize() {
		node.id = 1;
		nodes.add(new node(new Point3D(-100, 400)));
		nodes.add(new node(new Point3D(800, 500)));
		nodes.add(new node(new Point3D(500, 400)));
		nodes.add(new node(new Point3D(900, -200)));
		nodes.add(new node(new Point3D(405, 350)));
		nodes.add(new node(new Point3D(200, -100)));
		nodes.add(new node(new Point3D(300, 600)));
		nodes.add(new node(new Point3D(-700, 300)));
		nodes.add(new node(new Point3D(110, 450)));
		nodes.add(new node(new Point3D(950, -600)));
		nodes.add(new node(new Point3D(800, -300)));
		nodes.add(new node(new Point3D(400, 200)));
		nodes.add(new node(new Point3D(-750, 300)));
		nodes.add(new node(new Point3D(200, 400)));
		nodes.add(new node(new Point3D(100, -200)));
		edges.add(new edge(1,2,7));
		edges.add(new edge(2,3,2));
		edges.add(new edge(3,4,1));
		edges.add(new edge(4,5,3));
		edges.add(new edge(5,6,1));
		edges.add(new edge(6,7,5));
		edges.add(new edge(7,8,7));
		edges.add(new edge(8,9,2));
		edges.add(new edge(9,10,1));
		edges.add(new edge(10,11,3));
		edges.add(new edge(11,12,1));
		edges.add(new edge(12,13,5));
		edges.add(new edge(13,14,3));
		edges.add(new edge(14,15,1));
		edges.add(new edge(15,1,5));
	}

	@Test
	void testGraph_Algo() {
		Graph_Algo g=new Graph_Algo();
		assertNotNull(g);
	}

	@Test
	void testInitGraph() {
		graph DG = new DGraph(nodes, edges);
		Graph_Algo GA = new Graph_Algo();
		GA.init(DG);
		Iterator<node_data> itr1=DG.getV().iterator(),itr2=GA.AlgoG.getV().iterator();
		while(itr1.hasNext()&&itr2.hasNext()) {
			node_data n1=itr1.next();
			node_data n2=itr2.next();
			assertEquals(n1,n2);
			Iterator<edge_data> itr3=DG.getE(n1.getKey()).iterator();
			Iterator<edge_data> itr4=GA.AlgoG.getE(n2.getKey()).iterator();
			while(itr3.hasNext()&&itr4.hasNext()) {
				edge_data e1=itr3.next();
				edge_data e2=itr4.next();
				assertEquals(e1,e2);
			}
		}

	}

	@Test
	void testSaveAndInitFromFile() {

		graph DG = new DGraph(nodes, edges);
		Graph_Algo GA = new Graph_Algo();
		GA.init(DG);
		GA.save("graph.txt");
		Graph_Algo AG=new Graph_Algo();
		AG.init("graph.txt");
		Iterator<node_data> itr1=AG.AlgoG.getV().iterator(),itr2=GA.AlgoG.getV().iterator();
		while(itr1.hasNext()&&itr2.hasNext()) {
			node_data n1=itr1.next();
			node_data n2=itr2.next();
			assertEquals(n1,n2);
			Iterator<edge_data> itr3=AG.AlgoG.getE(n1.getKey()).iterator();
			Iterator<edge_data> itr4=GA.AlgoG.getE(n2.getKey()).iterator();
			while(itr3.hasNext()&&itr4.hasNext()) {
				edge_data e1=itr3.next();
				edge_data e2=itr4.next();
				assertEquals(e1,e2);
			}
		}


	}
	@Test
	void testIsConnected() {
		graph dg = new DGraph(nodes, edges);
		Graph_Algo ga = new Graph_Algo();
		ga.init(dg);
		boolean isTrue = ga.isConnected();
		assertTrue(isTrue);
		ga.AlgoG.removeNode(1);
		boolean isFalse = ga.isConnected();
		assertFalse(isFalse);
	}

	@Test
	void testShortestPathDist() {
		graph DG = new DGraph(nodes, edges);
		Graph_Algo GA = new Graph_Algo();
		GA.init(DG);
		double distance1 = GA.shortestPathDist(2, 15);
		assertEquals(distance1, 35);
		double distance2 =  GA.shortestPathDist(5, 4);
		assertEquals(distance2, 44);
	}

	@Test
	void testShortestPath() {
		graph DG = new DGraph(nodes, edges);
		Graph_Algo GA = new Graph_Algo();
		GA.init(DG);
		List<node_data> list1 = GA.shortestPath(2, 15);
		int[] arr1= {2,3,4,5,6,7,8,9,10,11,12,13,14,15};
		for(int i=0;i<arr1.length;i++) {
			assertEquals(arr1[i],list1.get(i).getKey());

		}
		list1 = GA.shortestPath(5, 4);
		int[] arr2= {5,6,7,8,9,10,11,12,13,14,15,1,2,3,4};
		for(int i=0;i<arr2.length;i++) {
			assertEquals(arr2[i],list1.get(i).getKey());

		}

	}

	@Test
	void testTSP() {
		graph g=new DGraph(nodes,edges);
		Graph_Algo ga=new Graph_Algo();
		ga.init(g);
		List<Integer> list=new LinkedList<>();
		list.add(7);
		list.add(9);
		list.add(4);
		list.add(6);
		List<node_data> listcheck=ga.TSP(list);
		list.clear();
		for(int i=7;i<16;i++) {
			list.add(i);
		}
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		Iterator<Integer> itr1=list.iterator();
		Iterator<node_data> itr2=listcheck.iterator();
		while(itr1.hasNext()&&itr2.hasNext()) {
			int a=itr1.next();
			node_data n=itr2.next();
			int b=n.getKey();
			System.out.println(a +"  "+n.toString());
			boolean bool=(a==b);
			assertTrue(bool);
		}
		
		
	}

	@Test
	void testCopy() {
		graph g=new DGraph(nodes,edges);
		Graph_Algo ga=new Graph_Algo();
		ga.init(g);
		graph d=ga.copy();
		assertEquals(g.nodeSize(),d.nodeSize());
		assertEquals(d.edgeSize(),g.edgeSize());
		Iterator<node_data> itr1=d.getV().iterator(),itr2=g.getV().iterator();
		while(itr1.hasNext()&&itr2.hasNext()) {
			node_data n1=itr1.next();
			node_data n2=itr2.next();
			assertEquals(n1,n2);
			Iterator<edge_data> itr3=d.getE(n1.getKey()).iterator();
			Iterator<edge_data> itr4=g.getE(n2.getKey()).iterator();
			while(itr3.hasNext()&&itr4.hasNext()) {
				edge_data e1=itr3.next();
				edge_data e2=itr4.next();
				assertEquals(e1,e2);
			}
		}
		d.connect(7,9,10);
		assertNotEquals(d.edgeSize(),g.edgeSize());
		node_data n= new node(100);
		g.addNode(n);
		assertNotEquals(d.nodeSize(),g.nodeSize());




	}

}
