package Tests;

import gameClient.MyGameGUI;
import gameObjects.GameManager;

public class MyGameGUI_Exemples {

	public static void main(String[] args) {
		//testGameGUI();
		testGameManager();
	}
	
	public static void testGameGUI() {
		MyGameGUI game = new MyGameGUI();
	}

	public static void testGameManager() {
		GameManager manager = new GameManager(0);
		manager.startGame();
	}
}
