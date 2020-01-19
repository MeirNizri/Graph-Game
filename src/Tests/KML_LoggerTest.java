package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import gameClient.KML_Logger;

class KML_LoggerTest {

	@Test
	void testMakeHead() {
		String kmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
				"  <Document>\r\n" + 
				"    <name>Point with TimeStamps</name>\r\n" +		
				"    <Style id=\"robot-icon\">\r\n" +
				"      <IconStyle>\r\n" +
				"        <Icon>\r\n" +
				"          <href>https://cdn2.iconfinder.com/data/icons/miscellaneous-7/100/Robot-512.png</href>\r\n" +
				"        </Icon>\r\n" +
				"      </IconStyle>\r\n" +
				"    </Style>\r\n" +
				"    <Style id=\"fruit-icon\">\r\n" +
				"      <IconStyle>\r\n" +
				"        <Icon>\r\n" +
				"          <href>https://www.pngrepo.com/png/202560/170/banana.png</href>\r\n" +
				"        </Icon>\r\n" +
				"      </IconStyle>\r\n" +
				"    </Style>\r\n" +
				"    <Style id=\"node-icon\">\r\n" +
				"      <IconStyle>\r\n" +
				"        <scale>0.3</scale>\r\n" +
				"        <Icon>\r\n" +
				"          <href>https://www.iconsdb.com/icons/preview/royal-blue/circle-xxl.png</href>\r\n" +
				"        </Icon>\r\n" +
				"      </IconStyle>\r\n" +
				"    </Style>\r\n";
		assertEquals(KML_Logger.makeHead(), kmlHead);
	}

	@Test
	void testGraphToKML() {
		game_service game = Game_Server.getServer(0);
		String kmlGraph = "    <Placemark>\r\n" + 
				"      <styleUrl>#node-icon</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>35.18753053591606,32.10378225882353</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>\r\n" + 
				"    <Placemark>\r\n" + 
				"      <styleUrl>#node-icon</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>35.18958953510896,32.10785303529412</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>\r\n" + 
				"    <Placemark>\r\n" + 
				"      <styleUrl>#node-icon</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>35.19341035835351,32.10610841680672</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>\r\n" + 
				"    <Placemark>\r\n" + 
				"      <styleUrl>#node-icon</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>35.197528356739305,32.1053088</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>\r\n" + 
				"    <Placemark>\r\n" + 
				"      <styleUrl>#node-icon</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>35.2016888087167,32.10601755126051</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>\r\n" + 
				"    <Placemark>\r\n" + 
				"      <styleUrl>#node-icon</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>35.20582803389831,32.10625380168067</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>\r\n" + 
				"    <Placemark>\r\n" + 
				"      <styleUrl>#node-icon</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>35.20792948668281,32.10470908739496</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>\r\n" + 
				"    <Placemark>\r\n" + 
				"      <styleUrl>#node-icon</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>35.20746249717514,32.10254648739496</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>\r\n" + 
				"    <Placemark>\r\n" + 
				"      <styleUrl>#node-icon</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>35.20319591121872,32.1031462</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>\r\n" + 
				"    <Placemark>\r\n" + 
				"      <styleUrl>#node-icon</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>35.19597880064568,32.10154696638656</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>\r\n" + 
				"    <Placemark>\r\n" + 
				"      <styleUrl>#node-icon</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>35.18910131880549,32.103618700840336</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>\r\n" + 
				"    <Placemark>\r\n" + 
				"      <LineString>\r\n" + 
				"        <coordinates>35.18753053591606,32.10378225882353 35.18958953510896,32.10785303529412 35.19341035835351,32.10610841680672 35.197528356739305,32.1053088 35.2016888087167,32.10601755126051 35.20582803389831,32.10625380168067 35.20792948668281,32.10470908739496 35.20746249717514,32.10254648739496 35.20319591121872,32.1031462 35.19597880064568,32.10154696638656 35.18910131880549,32.103618700840336 </coordinates>\r\n" + 
				"      </LineString>\r\n" + 
				"    </Placemark>\r\n";
		assertEquals(KML_Logger.graphToKML(game.getGraph()), kmlGraph);
	}

	@Test
	void testMakeTail() {
		String kmlTail = "  </Document>\r\n" + 
						 "</kml>";
		assertEquals(KML_Logger.makeTail(), kmlTail);
	}

	@Test
	void testCreateKMLFile() {
		String test  = "this is kml test";
		String path = "testKML.kml";
		try {
			KML_Logger.createKMLFile(test, path);
			String str = new String(Files.readAllBytes(Paths.get(path)));
			assertEquals(str, test);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}