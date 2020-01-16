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

/**
 * This class represents a graphical interface of a game. In this game there is a server,
 * to which number of scenario are inserted. The scenarios are listed fron 0 to 23.
 * Each scenario has a graph, time, fruits, and robots.
 * The purpose of the robots is to obtain as much points as possible. points are obtained by collecting fruits.
 * 
 * Each fruit has a location and value. After the fruit is collected the number of points is obtained,
 * the fruit appears elsewhere on the graph. There are two types of fruit:
 * Banana - must be collected at the crossing on the edge from the high node to the low node.
 * Apple - must be collected at the crossing on the edge from the low node to the high node.
 * 
 * There are two ways to run the game:
 * Manual - where the user places the fruits on the graph, and After that the game begins. 
 * During the game the user chooses each robot where to go.
 * Automatic - where the user only runs the game and the robots collect the fruit relatively optimally,
 * according to the algorithm in the GameManager class.
 * 
 * @author Meir Nizri
 */
public class MyGameGUI implements ActionListener, MouseListener {
	// The game server operate the game.
	private game_service game;
	// Variables to track game information.
	private int scenario_num, numRobots, robotsOnGraph;
	// The robots on the game.
	private Robot[] Robots;
	// The graph on which the fruits and robots are placed.
	private DGraph Graph;
	// Range for x and y scale on the graph.
	private Range rx, ry;
	// Radius of circle to draw node. We will use this variable to draw more details in the GUI.
	private double radius;
	// Flags indicating in which method the game is running: Manual or automatic.
	private boolean manualActivated = false, automaticActivated = false;

	/**
	 * Instantiates a new game GUI.
	 */
	public MyGameGUI() {	
		initGUI();
	}
	
	/**
	 * Inits the GUI. Opens a window that allows the user to choose how they want to play: manual or automatic.
	 */
	public void initGUI() {
		// Preparation of the frame
		StdDraw.setCanvasSize(1200,600);
		StdDraw.setMenuBar(createMenuBar());
		StdDraw.addMouseListener(this);
		StdDraw.enableDoubleBuffering();
	}

	/**
	 * Initialize the game. It asks the user for a proper scenario number.
	 * Then, gets from the game server all information about this scenario.
	 * And finally, draws the graph and the fruits on it.
	 */
	public void initGame() {
		// Ask the user number of scenario.
		String scenario_str = JOptionPane.showInputDialog(StdDraw.frame,"Enter scenario 0-23 to play");
		// If can't parse the string entered to int, or the nuber is not between 0-23, inform the user.
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
		
		// Gets all information on the game scenario the user chosed.
		game = Game_Server.getServer(scenario_num);
		if(game.isRunning()) game.stopGame();
		try {
			JSONObject gameServer = new JSONObject(game.toString()).getJSONObject("GameServer");
			numRobots = gameServer.getInt("robots");
			Robots = new Robot[numRobots];
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// Draw the graph and the fruit on it.
		initGraph(game.getGraph());
		drawFruits(game.getFruits());	
		drawGraph();	
		StdDraw.show();
	}	
	
	/**
	 * Adds robot in the selected node and draw it on GUI.
	 * @param pressed - the node to locate the robot on.
	 */
	public void addRobot(node_data pressed) {
		// Add robot and draw it on GUI.
		game.addRobot(pressed.getKey());
		drawRobots(game.getRobots());
		StdDraw.show();
		robotsOnGraph++;
		// If after adding the robot, the number of robots in the graph is equal 
		// to the number of robots defined in the game - start the game.
		if(robotsOnGraph == numRobots) {
			game.startGame();
			GameMove move = new GameMove(game);
			Thread t1 = new Thread(move);
			GameDraw draw = new GameDraw(game, this);
			Thread t2 = new Thread(draw);
			t1.start();
			t2.start();
		}	
	}

	/**
	 * Updates the game GUI while the game is running. 
	 * Draws the graph, fruits, robots and time left for the game.
	 * @param game - the game.
	 */
	public void updateGame(game_service game) {
		StdDraw.clear();
		drawFruits(game.getFruits());
		drawGraph();
		drawRobots(game.getRobots());
		drawInfo(game.timeToEnd());
		StdDraw.show();
	}
	
	/**
	 * When the game is over, notifies the user of his final result.
	 * @param game the game
	 */
	public void gameOver(game_service game) {
		try {
			// Extract result from the game information.
			JSONObject gameServer = new JSONObject(game.toString()).getJSONObject("GameServer");
			int result = gameServer.getInt("grade");
			// Notifies the user of his final result.
			JOptionPane.showMessageDialog(StdDraw.frame, "Your total score is: " + result,
					"Game Over", JOptionPane.PLAIN_MESSAGE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This function identifies when an action performed.
	 * We will use this function to identify when the user clicked an option from the menu bar.
	 * @param e - The object that is sent when an action performed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Cancel any play options and reset the GUI.
		manualActivated = false;
		automaticActivated = false;
		robotsOnGraph = 0;
		StdDraw.clear();
		StdDraw.show();
		String ActivatedItem = e.getActionCommand();
		
		if(ActivatedItem.equals("Manual")) {
			// Initialize game, and notify the user that he has to place robots on the graph.
			initGame();
			JOptionPane.showMessageDialog(StdDraw.frame, "Select " + numRobots + " nodes to place the robots.\n"
						+ "After that the game will start immediatly", "Manual Game", JOptionPane.PLAIN_MESSAGE);
			manualActivated = true;	
		}
		
		if(ActivatedItem.equals("Automatic")) {
			// Initialize game.
			initGame();
			automaticActivated = true;
			GameManager manager = new GameManager(game);;
			// Create thread that operate "move" on the game, and manage the game. 
			GameMove move = new GameMove(game, manager, automaticActivated);
			Thread t1 = new Thread(move);
			// Create thread that draw the game
			GameDraw draw = new GameDraw(game, this);
			Thread t2 = new Thread(draw);
			// Start game and threads
			manager.startGame();
			t1.start();
			t2.start();
		}
	}

	/**
	 * This function identifies when the mouse is clicked.
	 * We will use this function To find location of the point the user clicked on the GUI
	 * @param e - The object that is sent when the mouse is clicked.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// Find location of the point the user clicked on gui
		double x_location = StdDraw.userX(e.getX()); 
		double y_location = StdDraw.userY(e.getY()); 	

		if(manualActivated) {
			// Get the node the user clicked on
			node_data pressed = Graph_Functions.getNodeOnPoint(x_location, y_location, Graph, radius);
			if(pressed != null) {
				// If the number of robots on the graph is less than the number of robots
				// defined in the game, add robot in the selected node and draw it on GUI.
				if(robotsOnGraph < numRobots)
					addRobot(pressed);

				else { // The game has already started
					// If the robot has reached a node and this node is also a neighbor of the pressed node,
					// advance that robot toward the pressed node.
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
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}	

	/**
	 * This function creates the menu bar in the top of the window. Using the menus the user decide 
	 * how to play the game. The options are:
	 * Manual - where the user places the fruits on the graph, and After that the game begins. 
	 * During the game the user chooses each robot where to go.
	 * Automatic - where the user only runs the game and the robots collect the fruit relatively optimally,
	 * according to the algorithm in the GameManager class.
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
	
	/**
	 * Initializes all the details needed to draw the graph.
	 * @param graph_Json - Json string representing the graph.
	 */
	public void initGraph(String graph_Json) {
		// Init the graph from Json string
		Graph = new DGraph();
		Graph.init(graph_Json);
		// Calculate boundaries and fringes of the frame
		rx = Graph_Functions.find_rx(Graph);
		ry = Graph_Functions.find_ry(Graph);
		double Xfringe = rx.get_length()/20;
		double Yfringe = ry.get_length()/15;
		// Set scale of x and y according to the range
		StdDraw.setXscale(rx.get_min()-Xfringe, rx.get_max()+Xfringe);
		StdDraw.setYscale(ry.get_min()-Yfringe, ry.get_max()+Yfringe);
		// Radius of circle to draw node. We will use this variable to draw more details in the GUI.
		radius = rx.get_length()/250;		
	}
	
	/**
	 * Draw the graph.
	 */
	public void drawGraph() {
		// Draw all the nodes
		for(node_data n : Graph.getV()) {
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
				Point3D p = Graph_Functions.findPointOnLine(src, dest, 5, radius);
				StdDraw.text(p.x(), p.y(), ""+ new DecimalFormat("#.#").format(e.getWeight()));	
			}
		}		
	}
	
	/**
	 * Draw all the fruits on the graph.
	 * @param Fruits_Json - List of all Json string representing the fruits.
	 */
	public void drawFruits(List<String> Fruits_Json) {
		int type;
		Point3D location;
		for(int i=0; i<Fruits_Json.size(); i++) {
			try {
				// Get type and location of the fruit from Fruits_Json.
				JSONObject fruit = new JSONObject(Fruits_Json.get(i)).getJSONObject("Fruit");
				type = fruit.getInt("type");
				location = new Point3D(fruit.getString("pos"));

				// If type is -1 draw banana, else draw apple
				if(type == -1)
					StdDraw.picture(location.x(), location.y(), "data/banana.png", 0.0005, 0.0005);
				else
					StdDraw.picture(location.x(), location.y(), "data/apple.png", 0.0005, 0.0005);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Draw all the robots on the graph.
	 * @param Robots_Json - List of all Json string representing the robots.
	 */
	public void drawRobots(List<String> Robots_Json) {
		for(int i=0; i<Robots_Json.size(); i++) {
			Robots[i] = new Robot(Robots_Json.get(i));
			StdDraw.picture(Robots[i].getLocation().x(), Robots[i].getLocation().y(), "data/robot.png", 0.0007, 0.0007);
		}
	}
	
	/**
	 * Draw the time left for the game and, current result, current number of moves, and the scenario number.
	 * @param t - the time left for the game in miliSeconds. if the game not started t=-1.
	 */
	public void drawInfo(long t) {
		String time;
		long miliSecond = 00, second= 00, minute = 00;
		// Calculate all time variable from t.
		if(t != -1) {
			miliSecond = t % 60;
			second = t / 1000;
			minute = (t / 60000);
		}
		// Draw the time.
		time = String.format("%2d:%02d:%02d", minute, second, miliSecond);
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.text(rx.get_max() - rx.get_length()/7, ry.get_max() - ry.get_length()/9, "Time:" + time);
        
        try {
        	// Extract current result from the game information.
        	JSONObject gameServer = new JSONObject(game.toString()).getJSONObject("GameServer");
        	int numOfMove = gameServer.getInt("moves");
        	int result = gameServer.getInt("grade");
        	
        	// Draw the current result, number of current moves, and the scenario number.
        	StdDraw.text(rx.get_max() - rx.get_length()/7, ry.get_max() - ry.get_length()/12, "Score: " + result);
        	StdDraw.text(rx.get_max() - rx.get_length()/7, ry.get_max() - ry.get_length()/19, "Moves: " + numOfMove);
        	StdDraw.text(rx.get_max() - rx.get_length()/7, ry.get_max() - ry.get_length()/43, "Scenario: " + scenario_num);
        } catch (JSONException e) {
        	e.printStackTrace();
        }
	}
}