package gameClient;

import Server.game_service;
import gameObjects.GameManager;

/**
 * This class represent a thread that operate "move" on the the game every tenth of a second.
 * If the graph is activated automatically, a gameManager object is built. 
 * After every operation of move, the gameManager update the fruit location and the robots path.
 */
public class GameMove implements Runnable{
	// The game to play.
	private game_service game;
	private GameManager manager;
	// Boolean variable to check if the game is activated automatically.
	private boolean automaticActivated;

	/**
	 * Empty default constructor
	 */
	public GameMove() {}
	
	/**
	 * Instantiates a new game move.
	 * @param game - the game server.
	 */
	public GameMove(game_service game) {
		this.game = game;
	}
	
	/**
	 * Instantiates a new game move.
	 * @param game - the game server.
	 * @param manager - the game manager
	 * @param automaticActivated - boolean variable to check if the game is activated automatically.
	 */
	public GameMove(game_service game, GameManager manager, boolean automaticActivated) {
		this.game = game;
		this.manager = manager;
		this.automaticActivated = automaticActivated;
	}

	/**
	 * Run the thread.
	 */
	@Override
	public void run() {
		while (game.isRunning()) {
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
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}			
		}
		// Print result of the game.
		if(automaticActivated)
			manager.gameOver();
	}
}