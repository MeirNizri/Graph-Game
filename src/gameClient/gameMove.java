package gameClient;

import Server.game_service;

public class gameMove implements Runnable{
	MyGameGUI GUI;
	game_service game;

	public gameMove(MyGameGUI GUI, game_service game) {
		this.GUI = GUI;
		this.game = game;
	}

	@Override
	public void run() {
		// Infinite loop
		while (game.isRunning()) {
			game.move();
			GUI.updateGame();
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {}			
		}
		GUI.gameOver();
	}
}