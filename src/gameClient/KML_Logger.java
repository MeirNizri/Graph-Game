package gameClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.game_service;
import utils.Point3D;

/**
 * This class is a static class whose purpose is to convert game to a KML file.
 */
public class KML_Logger {

	/**
	 * Instantiates a new KML_logger.
	 */
	public KML_Logger() {}

	/**
	 * Returns the opening of the KML file. Additionaly declare the robots, fruits and nodes icon image.
	 * @return string contains the opening of the KML file.
	 */
	public static String makeHead() {
				// Opening of the KML file
		return  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
				"  <Document>\r\n" + 
				"    <name>Point with TimeStamps</name>\r\n" +		
				// Robot icon address
			    "    <Style id=\"robot-icon\">\r\n" +
			    "      <IconStyle>\r\n" +
			    "        <Icon>\r\n" +
			    "          <href>https://cdn2.iconfinder.com/data/icons/miscellaneous-7/100/Robot-512.png</href>\r\n" +
			    "        </Icon>\r\n" +
			    "      </IconStyle>\r\n" +
			    "    </Style>\r\n" +
			    // Fruit icon address
				"    <Style id=\"fruit-icon\">\r\n" +
				"      <IconStyle>\r\n" +
				"        <Icon>\r\n" +
				"          <href>https://www.pngrepo.com/png/202560/170/banana.png</href>\r\n" +
				"        </Icon>\r\n" +
				"      </IconStyle>\r\n" +
				"    </Style>\r\n" +
				// node icon address
				"    <Style id=\"node-icon\">\r\n" +
				"      <IconStyle>\r\n" +
				"        <scale>0.3</scale>\r\n" +
				"        <Icon>\r\n" +
				"          <href>https://www.iconsdb.com/icons/preview/royal-blue/circle-xxl.png</href>\r\n" +
				"        </Icon>\r\n" +
				"      </IconStyle>\r\n" +
				"    </Style>\r\n";
	}

	/**
	 * Gets game info and return kml with timestamp of all the robots and fruits in the current game situation.
	 * @param game - the game.
	 * @return string kml of all the robots and fruits.
	 */
	public static String gameToKML(game_service game) {
		String kmlStr = new String();
		Point3D location = null;
		JSONObject Jobj;

		// Traverse all robots and enter tham to the KML string.
		List<String> Robots_Json = game.getRobots();
		for(int i=0; i<Robots_Json.size(); i++) {		
			try {
				// Gets the location of the robot.
				Jobj = new JSONObject(Robots_Json.get(i)).getJSONObject("Robot");
				location = new Point3D(Jobj.getString("pos"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			kmlStr += objectToKML(location.x(), location.y(), "robot-icon");
		}

		// Traverse all fruits and enter tham to the KML string.
		List<String> Fruits_Json = game.getFruits();
		for(int i=0; i<Fruits_Json.size(); i++) {		
			try {
				// Gets the location of the fruit.
				Jobj = new JSONObject(Fruits_Json.get(i)).getJSONObject("Fruit");
				location = new Point3D(Jobj.getString("pos"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			kmlStr += objectToKML(location.x(), location.y(), "fruit-icon");
		}		
		return kmlStr;
	}

	/**
	 * Gets object location and return it as kml with timestamp.
	 * @param x - the x coordinate.
	 * @param y - the y coordinate.
	 * @param icon - the icon name of this object.
	 * @return string contains the object kml.
	 */
	public static String objectToKML(double x, double y, String icon) {
		String kmlStr = "    <Placemark>\r\n" + 
						"	   <TimeStamp>\r\n" + 
						"	     <when>"+getCurrentTimestamp()+"Z</when>\r\n" +
						"      </TimeStamp>\r\n" +
						"      <styleUrl>#"+icon+"</styleUrl>\r\n" +
						"      <Point>\r\n" +
						"        <coordinates>"+x+","+y+"</coordinates>\r\n" +
						"      </Point>\r\n" +
						"    </Placemark>\r\n";
		return kmlStr;
	}

	/**
	 * Gets graph Json and return it as kml with no timestamp.
	 * @param graph_Json - the graph json string.
	 * @return string contains the graph kml.
	 */
	public static String graphToKML(String graph_Json) {
		String kmlStr = new String();
		Point3D location = null;

		try { 
			// Read all the nodes from the Json_string
			JSONObject graph = new JSONObject(graph_Json);
			JSONArray nodes_Json = graph.getJSONArray("Nodes");
			// This array will store all coordinates of the graph nodes.
			// We will use this information to draw the edged between the nodes.
			String[] locations = new String[nodes_Json.length()];

			// Traverse all node and write placemark for each node,
			for (int i = 0; i < nodes_Json.length(); i++) {
				location = new Point3D(nodes_Json.getJSONObject(i).getString("pos"));
				locations[i] = location.x() + "," + location.y();
				kmlStr += "    <Placemark>\r\n" + 
						  "      <styleUrl>#node-icon</styleUrl>\r\n" +
						  "      <Point>\r\n" +
						  "        <coordinates>"+locations[i]+"</coordinates>\r\n" +
						  "      </Point>\r\n" +
						  "    </Placemark>\r\n";
			}
			
			// Write LineString placemark between all the nodes.
			kmlStr += "    <Placemark>\r\n" + 
					  "      <LineString>\r\n" +
					  "        <coordinates>";
			// Traverse all nodes coordinates and write it to the LineString.
			for (int i = 0; i < locations.length; i++) {
				kmlStr += locations[i] + " ";
			}
			// End of the LineString placemark.
			kmlStr += "</coordinates>\r\n" +
				   	  "      </LineString>\r\n" +
				   	  "    </Placemark>\r\n";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kmlStr;
	}

	/**
	 * return the ending of the KML file.
	 * @return string contains the ending of the KML file.
	 */
	public static String makeTail() {
		return "  </Document>\r\n" + 
			   "</kml>";
	}

	/**
	 * This method create KML file in the path inserted from the String str.
	 * @param str - the str contains all kml information
	 * @param path - where to save the file.
	 * @throws FileNotFoundException if file not found.
	 */
	public static void createKMLFile(String str, String path) throws FileNotFoundException {
		if (path == null) throw new IllegalArgumentException();	
		// Check if the suffix of the path ends with ".kml". if not throws exception.
		String suffix = path.substring(path.lastIndexOf('.') + 1);
		if(!"kml".equalsIgnoreCase(suffix)) throw new IllegalArgumentException();
		
		// Print the kml String to the file.
		PrintWriter pw = new PrintWriter(new File(path));
		StringBuilder sb = new StringBuilder();	
		sb.append(str);
		pw.write(sb.toString());
		pw.close();
	}

	/**
	 * Gets the current timestamp.
	 * @return the current timestamp
	 */
	public static String getCurrentTimestamp() {
		// Gets the current Timestamp
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		// Replace the middle space between the date and time to 'T'. 
		// Google earth can read timestamp only with 'T' between the date and time.
		return timestamp.toString().replaceAll("\\s", "T");
	}
}