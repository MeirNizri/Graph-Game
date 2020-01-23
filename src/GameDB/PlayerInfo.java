package GameDB;

import java.util.HashMap;

/**
 * The Class PlayerInfo represt playet in our game. it contains information about the player.
 * 
 * @author Meir Nizri
 */
public class PlayerInfo {	
	/** The id. */
	int id;
	/** The num games. */
	int numGames;
	/** The current level. */
	int currentLevel;
	/** The best score. */
	HashMap<Integer, Integer> bestScore = new HashMap<Integer, Integer>();

	/**
	 * Instantiates a new player info.
	 */
	public PlayerInfo() {}
	
	/**
	 * Gets the id.
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * @param id - the new id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the num games.
	 * @return the num games
	 */
	public int getNumGames() {
		return numGames;
	}
	
	/**
	 * Sets the num games.
	 * @param numGames - the new num games
	 */
	public void setNumGames(int numGames) {
		this.numGames = numGames;
	}
	
	/**
	 * Gets the current level.
	 * @return the current level
	 */
	public int getCurrentLevel() {
		return currentLevel;
	}
	
	/**
	 * Sets the current level.
	 * @param currentLevel the new current level
	 */
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	
	/**
	 * Gets the best score.
	 * @param level the level
	 * @return the best score
	 */
	public int getBestScore(int level) {
		return bestScore.get(level);
	}
	
	/**
	 * Sets the best score.
	 * @param level the level to update
	 * @param bestScore the best score to insert.
	 */
	public void setBestScore(int level, int bestScore) {
		this.bestScore.put(level, bestScore);
	}
}