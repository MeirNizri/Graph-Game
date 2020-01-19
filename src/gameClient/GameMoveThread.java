package gameClient;

import Server.game_service;
import gameObjects.GameManager;

/**
 * This class represent a thread that operate "move" on the the game every tenth of a second.
 * If the graph is activated automatically, a gameManager object is built. 
 * After every operation of move, the gameManager update the fruit location and the robots path.
 * In addition, all information about the game is stored in a String variable in the form of KML. 
 * At the end of the game this variable is sent back to the GUI.
 */
public class GameMoveThread implements Runnable{
	// The game to play.
	private MyGameGUI GUI;
	private game_service game;
	// Automatic game manager.
	private GameManager manager;
	// Boolean variable to check if the game is activated automatically.
	private boolean automaticActivated;

	/**
	 * Empty default constructor
	 */
	public GameMoveThread() {}
	
	/**
	 * Instantiates a new game move.
	 * @param GUI - the gui to draw to.
	 * @param game - the game server.
	 */
	public GameMoveThread(MyGameGUI GUI, game_service game) {
		this.GUI = GUI;
		this.game = game;
	}
	
	/**
	 * Instantiates a new game move.
	 * @param GUI - the gui to draw to.
	 * @param game - the game server.
	 * @param manager - the game manager
	 */
	public GameMoveThread(MyGameGUI GUI, game_service game, GameManager manager) {
		this.GUI = GUI;
		this.game = game;
		this.manager = manager;
		this.automaticActivated = true;
	}

	/**
	 * Run the thread.
	 */
	@Override
	public void run() {
		// Write the head of the kml file.
		String kmlStr = KML_Logger.makeHead();
		while(game.isRunning()) {
			// While game running, "move" the game every tenth of a second.
			game.move();
			if(automaticActivated) {
				// If the one of the fruit changed update fruit location and robots path.
				if(manager.fruitChanged()) {
					manager.updateFruits();
					manager.updateAllRobotsPath();
				}
				// Sets the robots next move.
				manager.nextMove();
			}
			// Write the current position of the robots and fruits to kml.
			kmlStr += KML_Logger.gameToKML(game);
			// Wait 80 milliseconds.
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}			
		}
		// Write the graph and the closing to kml file, and send it back to the GUI.
		kmlStr += KML_Logger.graphToKML(game.getGraph());
		kmlStr += KML_Logger.makeTail();
		GUI.updateKML(kmlStr);
	}
}