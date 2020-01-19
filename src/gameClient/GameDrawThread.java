package gameClient;

import Server.game_service;

/**
 * This class represent a thread that updates the drawing of the game on the GUI every tenth of a second.
 */
public class GameDrawThread implements Runnable{
	// The GUI to draw on.
	private MyGameGUI GUI;
	// The game to draw.
	private game_service game;
	
	/**
	 * Empty default constructor
	 */
	public GameDrawThread() {}

	/**
	 * Instantiates a new game draw.
	 * @param GUI - The GUI to draw on.
	 * @param game - The game to draw.
	 */
	public GameDrawThread(MyGameGUI GUI, game_service game) {
		this.GUI = GUI;
		this.game = game;
	}

	/**
	 * Run the thread.
	 */
	@Override
	public void run() {
		while(game.timeToEnd() > 300) {
			// While game is running, update the drawing on the GUI every tenth of a second.
			GUI.updateGame(game);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}			
		}
		// Draw the result of the game.
		GUI.gameOver(game);
	}
}