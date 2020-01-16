package gameObjects;

import java.util.Comparator;

/**
 * This class task is to sort a collection of robots according to their speed,
 * from high to low, so that the fastest robot will be first.
 * The algorithm we built in the gameManager goes over the robots and defibe each robot
 * what is the most profitable fruit to collect. Using the Robot_Comparator, 
 * we will first specify to the fastest robot what fruit to collect.
 */
public class Robot_Comperator implements Comparator<Robot> {

	/**
	 * Empty default constructor
	 */
	public Robot_Comperator() {;}
	
	/**
	 * Compare robots according to their speed.
	 * @param o1 - first robot
	 * @param o2 - second robot
	 * @return positive integer if the first is greater, or negative if the scenod is greater.
	 */
	public int compare(Robot o1, Robot o2) {
		double dp = o2.getSpeed() - o1.getSpeed();
		return (int)dp;
	}
}
