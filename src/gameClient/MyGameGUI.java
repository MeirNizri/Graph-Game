package gameClient;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.*;
import gameObjects.*;
import utils.*;
import algorithms.*;

public class MyGameGUI implements ActionListener, MouseListener {

	private DGraph Graph;
	private int numRobots, numFruits, robotsOnGraph, result;
	// Range for x and y scale
	private Range rx, ry;
	// Radius of circle to draw node
	private double radius;
	Point3D p;
	boolean manualActivated = false, automaticActivated = false;
	game_service game;
	Fruit[] Fruits;
	Robot[] Robots;
	String bananaIcon = "data/banana.png", appleIcon = "data/apple.png", robotIcon = "data/robot.png";

	public MyGameGUI() {	
		initGui();
	}
	
	public void initGui() {
		// Preparation of the frame
		StdDraw.setCanvasSize(1200,500);
		StdDraw.setMenuBar(createMenuBar());
		StdDraw.addMouseListener(this);
		StdDraw.enableDoubleBuffering();
	}

	public void initGame() {
		String scenario_str = JOptionPane.showInputDialog(StdDraw.frame,"Enter scenario 0-23 to play");
		int scenario_num;
		try {
			scenario_num = Integer.parseInt(scenario_str);
		} catch (Exception e1){
			JOptionPane.showMessageDialog(StdDraw.frame, "The scenario must be between 0-23", "Error", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		if (scenario_num>23 || scenario_num<0) {
			JOptionPane.showMessageDialog(StdDraw.frame, "The scenario must be between 0-23", "Error", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		
		game = Game_Server.getServer(scenario_num);
		String graph_Json = game.getGraph();
		Graph = new DGraph();
		Graph.init(graph_Json);
		initGraph(Graph);

		try {
			JSONObject gameServer = new JSONObject(game.toString()).getJSONObject("GameServer");
			numFruits = gameServer.getInt("fruits");
			Fruits = new Fruit[numFruits];
			numRobots = gameServer.getInt("robots");
			Robots = new Robot[numRobots];
		} catch (JSONException e) {
			e.printStackTrace();
		}
		drawFruits(game.getFruits());
		drawGraph();		
		StdDraw.show();
	}	

	public void updateGame() {
		StdDraw.clear();
		drawFruits(game.getFruits());
		drawGraph();		
		drawRobots(game.getRobots());
		drawClock(game.timeToEnd());
		StdDraw.show();
	}
	
	public void gameOver() {
		try {
			JSONObject gameServer = new JSONObject(game.toString()).getJSONObject("GameServer");
			result = gameServer.getInt("grade");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(StdDraw.frame, "Your total score is: " + result,
									 "Game Over", JOptionPane.PLAIN_MESSAGE);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		manualActivated = false;
		automaticActivated = false;
		StdDraw.clear();
		StdDraw.show();
	
		String ActivatedItem = e.getActionCommand();
		
		if(ActivatedItem.equals("Manual")) {
			initGame();
			JOptionPane.showMessageDialog(StdDraw.frame, "Select " + numRobots + " nodes to place the robots.\n"
						+ "After that the game will start immediatly", "Manual Game", JOptionPane.PLAIN_MESSAGE);
			manualActivated = true;	
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Find location of the point the user clicked on gui
		double x_location = StdDraw.userX(e.getX()); 
		double y_location = StdDraw.userY(e.getY()); 	

		if(manualActivated) {
			node_data pressed = graphFunctions.getNodeOnPoint(x_location, y_location, Graph, radius);
			if(pressed != null) {
				if(robotsOnGraph < numRobots-1) {
					game.addRobot(pressed.getKey());
					drawRobots(game.getRobots());
					StdDraw.show();
					robotsOnGraph++;
				}
				else if(robotsOnGraph == numRobots-1) {
					game.addRobot(pressed.getKey());
					drawRobots(game.getRobots());
					StdDraw.show();
					robotsOnGraph++;
					game.startGame();
					gameMove move = new gameMove(this, game);
					Thread t = new Thread(move);
					t.start();
				}
				else {
					for(int i=0; i<robotsOnGraph; i++) {
						if(Robots[i].getDest()==-1 && Graph.getEdge(Robots[i].getSrc(), pressed.getKey())!=null) {
							game.chooseNextEdge(Robots[i].getId(), pressed.getKey());
							break;
						}
					}
					
				}
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {	}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
	
	private void initGraph(graph Graph) {
		// Calculate boundaries and fringes of the frame
		rx = graphFunctions.find_rx(Graph);
		ry = graphFunctions.find_ry(Graph);
		double Xfringe = rx.get_length()/15;
		double Yfringe = ry.get_length()/10;
		StdDraw.setXscale(rx.get_min()-Xfringe, rx.get_max()+Xfringe);
		StdDraw.setYscale(ry.get_min()-Yfringe, ry.get_max()+Yfringe);
		radius = rx.get_length()/250;
	}

	/**
	 * This function creates the menu bar in the top of the window. Some of the items has a shortcut keys.
	 * The operations are:
	 * Save to Image - Creates a new file with type png or jpeg, containing the drawing of the graph
	 * @return JMenuBar Object to enter the JFrame
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// Creates all the game options
		JMenu startGame = new JMenu("Start Game");
		menuBar.add(startGame);
		JMenuItem Manual = new JMenuItem("Manual");
		Manual.addActionListener(this);
		startGame.add(Manual);
		JMenuItem Automatic = new JMenuItem("Automatic");
		Automatic.addActionListener(this);
		startGame.add(Automatic);

		return menuBar;
	}
	
	public void drawGraph() {
		
		// Draw all the nodes
		for(node_data n : Graph.getV()) {
			StdDraw.setPenRadius();
			StdDraw.setPenColor(Color.BLUE);
			Point3D src = n.getLocation();
			StdDraw.filledCircle(src.x(), src.y(), radius);
			StdDraw.text(src.x(), src.y()+radius*2, ""+n.getKey());

			// Draw all edges came out of the node
			for(edge_data e : Graph.getE(n.getKey())) {
				// Draw line of edge
				StdDraw.setPenColor(Color.GRAY);
				Point3D dest = Graph.getNode(e.getDest()).getLocation();
				StdDraw.line(src.x(), src.y(), dest.x(), dest.y());
				// Draw weight of edge
				p = graphFunctions.findPointOnLine(src, dest, 7, radius);
				StdDraw.text(p.x(), p.y(), ""+ new DecimalFormat("#.#").format(e.getWeight()));	
				// Draw the yellow circle to indicate the direction of the edge	
				p = graphFunctions.findPointOnLine(src, dest, 3, radius);
				StdDraw.setPenColor(Color.YELLOW);	
				StdDraw.filledCircle(p.x(), p.y(), radius);
			}
		}		
	}
	
	public void drawFruits(List<String> Fruits_Json) {
		for(int i=0; i<Fruits_Json.size(); i++) {
			Fruits[i] = new Fruit(Graph, Fruits_Json.get(i));
			if(Fruits[i].getType() == 1)
				StdDraw.picture(Fruits[i].getLocation().x(), Fruits[i].getLocation().y(), appleIcon, 0.0005, 0.0005);
			else
				StdDraw.picture(Fruits[i].getLocation().x(), Fruits[i].getLocation().y(), bananaIcon, 0.0005, 0.0005);
		}
	}
	
	public void drawRobots(List<String> Robots_Json) {
		for(int i=0; i<Robots_Json.size(); i++) {
			Robots[i] = new Robot(Robots_Json.get(i));
			StdDraw.picture(Robots[i].getLocation().x(), Robots[i].getLocation().y(), robotIcon, 0.0007, 0.0007);
		}
	}
	
	public void drawClock(long t) {
		String time;
		long miliSecond = 00, second= 00, minute = 00;
		if(t != -1) {
			miliSecond = t % 60;
			second = t / 1000;
			minute = (t / 60000);
		}
		time = String.format("%2d:%02d:%02d", minute, second, miliSecond);
        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.text(rx.get_max() - rx.get_length()/6, ry.get_max() - ry.get_length()/5, "Time:" + time);
		try {
			JSONObject gameServer = new JSONObject(game.toString()).getJSONObject("GameServer");
			result = gameServer.getInt("grade");
		} catch (JSONException e) {
			e.printStackTrace();
		}
        StdDraw.text(rx.get_max() - rx.get_length()/6, ry.get_max() - ry.get_length()/6, "Score: " + result);
	}
}