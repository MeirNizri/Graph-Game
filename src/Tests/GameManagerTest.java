package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import gameObjects.GameManager;

class GameManagerTest {
	static game_service game;
	static GameManager gm;

	@BeforeAll
	public static void initialize() {
		game = Game_Server.getServer(0);
		gm = new GameManager(game);
	}
	
	@Test
	void testGameManagerInt() {
		GameManager gm1 = new GameManager(0);
		assertEquals(gm1.getGame().toString(),"{\"GameServer\":{\"fruits\":1,\"moves\":0,\"grade\":0,"
											+ "\"robots\":1,\"graph\":\"data/A0\"}}");
	}

	@Test
	void testGameManagerGame_service() {
		assertEquals(gm.getGame().toString(),"{\"GameServer\":{\"fruits\":1,\"moves\":0,\"grade\":0,"
											+ "\"robots\":1,\"graph\":\"data/A0\"}}");
	}

	@Test
	void testStartGame() {
		gm.startGame();
		assertTrue(game.isRunning());
		game.stopGame();
	}

	@Test
	void testAddRobots() {
		gm.addRobots();
		assertEquals(game.getRobots().toString(), "[{\"Robot\":{\"id\":0,\"value\":0.0,\"src\":9,\"dest\":-1,"
									+ "\"speed\":1.0,\"pos\":\"35.19597880064568,32.10154696638656,0.0\"}}]");
	}

	@Test
	void testFruitChanged() {
		assertFalse(gm.fruitChanged());
		gm.startGame();
		game.move();
		assertTrue(gm.fruitChanged());
		game.stopGame();
	}
}