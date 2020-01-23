package GameDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class represents database functions.
 * 
 * @author Meir Nizri
 */
public class DataBase {
	// Variables to connect the data base
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";

	/**
	 * Empty default constructor.
	 */
	public DataBase() {}
	
	/**
	 * Returns a PlayerInfo object according to the inserted ID. 
	 * This object contains all the information about the player, such as: the number of games played, 
	 * what the current level is, and what the best result is at each level.
	 * @param id - the user id.
	 * @return PlayerInfo object.
	 */
	public static PlayerInfo getPlayerInfo(int id) {
		PlayerInfo player = new PlayerInfo();
		player.setId(id);
		
		try {
			// Connect to DB
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = null;
			
			// Maximum moves allowed at certain steps.
			int[] movesLimit = {290,580,-1,580,-1,500,-1,-1,-1,580,-1,580,-1,580,-1,-1,290,-1,-1,580,290,-1,-1,1140};
			
			// Gets the cest scores at each step.
			String levelScore;
			for(int i=0; i<24; i++) {
				// Set the query request according to if this level are limtited with number of moves.
				if(movesLimit[i] == -1) levelScore = "SELECT score FROM Logs where (userID="+id+ " AND levelID="+i+");";
				else levelScore = "SELECT score FROM Logs where (userID="+id+ " AND levelID="+i+ " AND moves<="+movesLimit[i]+");";
				resultSet = statement.executeQuery(levelScore);
				int maxScore = 0;
				// Gets the best score at the current stage.
				while(resultSet.next()) {
					if(maxScore < resultSet.getInt("score"))
						maxScore = resultSet.getInt("score");
				}
				player.setBestScore(i, maxScore);
			}
			
			// Gets the current level
			for(int i=23; i>=0; i--) {
				if(player.getBestScore(i) != 0) {
					player.setCurrentLevel(i);
					break;
				}
			}
			
			// Gets number of games played.
			String userGame = "SELECT * FROM Logs where userID="+id;
			resultSet = statement.executeQuery(userGame);
			int count = 0;
			while(resultSet.next())
				count++;
			player.setNumGames(count);
			
			// close DB connection
			resultSet.close();
			statement.close();		
			connection.close();				
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return player;
	}
	
	/**
	 * Gets an ID and level, and returns the player's position with the ID that was inserted, 
	 * relative to the other players at the selected stage. 
	 * @param id - the player id
	 * @param stage - the stage to check position
	 * @return position of the player at the stage selected.
	 */
	public static int compare(int id, int stage) {
		int myPosition = 1;
		int myMaxScore = 0;
		// Maximum moves allowed at certain steps.
		int[] movesLimit = {290,580,-1,580,-1,500,-1,-1,-1,580,-1,580,-1,580,-1,-1,290,-1,-1,580,290,-1,-1,1140};
		
		try {
			// Connect to DB
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);				
			Statement statement = connection.createStatement();
			
			// Calculates te best result of the user.
			String selectScore = "";
			// Set the query request according to if this level are limtited with number of moves.
			if(movesLimit[stage] == -1) selectScore = "SELECT score FROM Logs where (userID="+id+" AND levelID="+stage+");";
			else selectScore = "SELECT score FROM Logs where (userID="+id+ " AND levelID="+stage+" AND moves<="+movesLimit[stage]+");";
			ResultSet resultSet = statement.executeQuery(selectScore);	
			while(resultSet.next()) {
				int myScore = resultSet.getInt("score");
				if(myScore > myMaxScore) 
					myMaxScore = myScore;
			}
			
			// Gets all the user played in stage.
			String selectUsers = "SELECT * FROM Users where levelNum="+stage+";";
			resultSet = statement.executeQuery(selectUsers);	
			
			// Check if there is a player got greater result from the user.
			while(resultSet.next()) {
				int userId = resultSet.getInt("UserID");
				// Set the query request according to if this level are limtited with number of moves.
				if(movesLimit[stage] == -1) selectScore = "SELECT score FROM Logs where (userID="+userId+" AND levelID="+stage+");";
				else selectScore = "SELECT score FROM Logs where (userID="+userId+ " AND levelID="+stage+" AND moves<="+movesLimit[stage]+");";
				ResultSet resultSet2 = statement.executeQuery(selectScore);	
				while(resultSet2.next()) {
					// If found better result push position up.
					if(resultSet2.getInt("score") > myMaxScore) {
						myPosition++;
						break;
					}
				}
			}		
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return myPosition;
	}
}