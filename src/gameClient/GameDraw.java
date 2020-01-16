package gameClient;

import Server.game_service;

/**
 * This class represent a thread that updates the drawing of the game on the GUI every tenth of a second.
 */
public class GameDraw implements Runnable{
	// The GUI to draw on.
	private MyGameGUI GUI;
	// The game to draw.
	private game_service game;
	
	/**
	 * Empty default constructor
	 */
	public GameDraw() {}

	/**
	 * Instantiates a new game draw.
	 * @param GUI - The GUI to draw on.
	 * @param game - The game to draw.
	 */
	public GameDraw(game_service game, MyGameGUI GUI) {
		this.GUI = GUI;
		this.game = game;
	}

	/**
	 * Run the thread.
	 */
	@Override
	public void run() {
		while(game.isRunning()) {
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