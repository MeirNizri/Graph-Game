package gameObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import graphAlgorithms.Graph_Algo;
import graphDataStructure.DGraph;
import graphDataStructure.node_data;

/**
 * This class represents a game management system. 
 * It enables automatic (efficient) management of the robots. 
 * The constructor receives a 0-23 scenario from the server, places the robots optimally, 
 * and automatically moves the robots along the graph to collect as much fruit as possible.
 * 
 * @author Meir Nizri
 */
public class GameManager {	
	// The game server operate the game.
	private game_service game;
	// The fruits and robots on the game.
	private ArrayList<Fruit> Fruits = new ArrayList<Fruit>();
	private ArrayList<Robot> Robots = new ArrayList<Robot>();
	private int numRobots;
	// The graph on which the fruits and robots are placed.
	private DGraph Graph;
	
	/**
	 * Empty default constructor
	 */
	public GameManager() {}

	/**
	 * Instantiates a new game manager.
	 * @param scenario_num - the scenario to play from the server.
	 */
	public GameManager(int scenario_num) {
		game = Game_Server.getServer(scenario_num);
	}

	/**
	 * Instantiates a new game manager.
	 * @param game - game server that already has some scenario in it.
	 */
	public GameManager(game_service game) {
		this.game = game;
	}
	
	/**
	 * This function initialize all the variables needed to start the game,
	 * such as: the graph, the location of the fruits and the robots, and then start the game.
	 */
	public void startGame() {
		// Init the graph from Json string gets from the game server.
		Graph = new DGraph();
		Graph.init(game.getGraph());	
		
		// Gets the number of robots from the game server. 
		// We'll need this variable to know how many robots to add before the game starts.
		try {
			JSONObject gameServer = new JSONObject(game.toString()).getJSONObject("GameServer");
			numRobots = gameServer.getInt("robots");
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		// Adds the robots in an optimal location, and starts the game.
		addRobots();
		game.startGame();
	}
	
	/**
	 * This function is activated before the game starts. 
	 * It adds the robots to the game in an optimal location for the fruit gathering.
	 * It also create ArrayList of all the fruit in the game.
	 */
	public void addRobots() {
		// Create ArrayList of all the fruits in the game.
		List<String> Fruits_Json = game.getFruits();
		for(int i=0; i<Fruits_Json.size(); i++) 
			Fruits.add(new Fruit(Graph, Fruits_Json.get(i)));
		
		for(int i=0; i<numRobots; i++) {
			// Add the robot to the game at the fruit src node.
			game.addRobot(Fruits.get(i).getSrc());
			// Create ArrayList of all the robots in the game.
			List<String> Robots_Json = game.getRobots();
			Robots.add(new Robot(Robots_Json.get(i)));
			// Initializes the next node of the robot to be the dest node of the fruit, 
			// so that already in the first move the robot will collect the fruit.
			Robots.get(i).getPath().add(Graph.getNode(Fruits.get(i).getDest()));
		}
		// Sorts robots by speed from high to low.
		Collections.sort(Robots, new Robot_Comperator());
	}
	
	/**
	 * Check if one of the fruits has changed.
	 * @return true if one of the fruit changed, false otherwise.
	 */
	public boolean fruitChanged() {
		List<String> Fruits_Json = game.getFruits();
		for(int i=0; i<Fruits_Json.size(); i++) {
			// If fruit has changed return true
			if(!Fruits.get(i).equals(new Fruit(Graph, Fruits_Json.get(i)))) 
				return true;
		}
		return false;
	}

	/**
	 * Updating the fruits on the game. 
	 * This function will be activated after one of the fruits is collected.
	 */
	public void updateFruits() {
		List<String> Fruits_Json = game.getFruits();
		for(int i=0; i<Fruits_Json.size(); i++) {
			Fruits.set(i, new Fruit(Graph, Fruits_Json.get(i)));
		}
	}
	
	/**
	 * Update the path of all robots using updateRobotPath function.
	 */
	public void updateAllRobotsPath() {
		List<String> Robots_Json = game.getRobots();
		for(int i=0; i<Robots_Json.size(); i++) {
			updateRobotPath(i);
		}
	}
	
	/**
	 * This function calculate for robot what is the most profitable fruit to collect, 
	 * and builds the shortest path to it.
	 * The most profitable fruit is defined as the fruit to which the time is shortest relative to its value.
	 * This fruit will be the fruit on which the following calculation: 
	 * (fruitValue / (timeToFruit/robotSpeed)) is the largest.
	 * @param i - the index of the robot we updating.
	 */
	public void updateRobotPath(int i) {
		// Update robot information.
		Robot robot = Robots.get(i);
		robot.update(game.getRobots().get(i));
		// Auxiliary variable.
		Fruit fruit, profitableFruit = null;
		double maxProfit = Double.MIN_VALUE, timeToFruit, profit;
		
		// Activate Dijksra's algorithem on the robot destination node
		int robotDest;
		if(robot.getDest() != -1) robotDest = robot.getDest();
		else					  robotDest = robot.getSrc();
		Graph_Algo.Dijkstra(Graph, robotDest);
		
		// Find the most profitable fruit that is not taken
		for(int j=0; j<Fruits.size(); j++) {
			fruit = Fruits.get(j);
			if(!fruit.getIsTaken()) {
				timeToFruit = fruit.getSrcNode().getWeight() + fruit.getEdge().getWeight();
				profit = fruit.getValue() / (timeToFruit/robot.getSpeed());
				if(profit > maxProfit) {
					maxProfit = profit;
					profitableFruit = fruit;
				}
			}
		}
		// Calculate the shortest path to the profitable fruit we found, and insert it to the robot path
		LinkedList<node_data> pathToFruit = new LinkedList<node_data>();
		pathToFruit.addAll(Graph_Algo.findPath(Graph, robotDest, profitableFruit.getSrc()));
		// Delete the current node and insert the dest node of the fruit to collect it
		pathToFruit.removeFirst();
		pathToFruit.add(Graph.getNode(profitableFruit.getDest()));
		robot.setPath(pathToFruit);
		profitableFruit.setIsTaken(true);
	}
	
	/**
	 * Updates the location of the robots, and calculates for 
	 * each robot its next step if it reaches the dest node.
	 */
	public void nextMove() {
		List<String> Robots_Json = game.getRobots();	
		for(int i=0; i<Robots_Json.size(); i++) {
			try { // Checks if the robot has reached its dest node (equale -1). if true set is next move
				JSONObject robot_Jobj = new JSONObject(Robots_Json.get(i)).getJSONObject("Robot");
				if(robot_Jobj.getInt("dest") == -1) {	
					// Updates the location of the robots, and check if the robots path is not empty.
					Robots.get(i).update(Robots_Json.get(i));
					if(Robots.get(i).getPath().isEmpty()) 
						updateRobotPath(i);
					// Set next move for robot.
					game.chooseNextEdge(Robots.get(i).getId(), Robots.get(i).nextMove());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}	
	}

	/**
	 * Activate when the game is over. Returns the final result of the game.
	 * @return the final result of the game.
	 */
	public int gameOver() {
		int result = 0;
		// Gets the game result from server.
		try {
			JSONObject gameServer = new JSONObject(game.toString()).getJSONObject("GameServer");
			result = gameServer.getInt("grade");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Print and return the result.
		System.out.println("the final score is: " + result);
		return result;
	}
}