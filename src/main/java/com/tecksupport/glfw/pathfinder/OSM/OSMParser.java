package com.tecksupport.glfw.pathfinder.OSM;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OSMParser {
    private static Map<Long, OSMNode> osmNodes = new HashMap<>();

    // Static block for initialization
    static {
        try {
            initialize("src/main/resources/OSMMap/map.osm"); // Path to your map.osm file
        } catch (Exception e) {
            System.err.println("Failed to initialize OSMParser: " + e.getMessage());
        }
    }

    /**
     * Initializes and parses the map.osm file.
     *
     * @param filePath The path to the map.osm file.
     */
    private static void initialize(String filePath) {
        parseOSMFile(filePath);
    }

    /**
     * Retrieves the parsed OSM nodes.
     *
     * @return A map of OSM node IDs to OSMNode objects.
     */
    public static Map<Long, OSMNode> getOSMNodes() {
        return osmNodes;
    }

    /**
     * Parses the map.osm file and stores the data in the `osmNodes` map.
     */
    private static void parseOSMFile(String filePath) {
        try {
            File osmFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(osmFile);
            doc.getDocumentElement().normalize();

            NodeList nodeElements = doc.getElementsByTagName("node");
            for (int i = 0; i < nodeElements.getLength(); i++) {
                Element element = (Element) nodeElements.item(i);

                long id = Long.parseLong(element.getAttribute("id"));
                double lat = Double.parseDouble(element.getAttribute("lat"));
                double lon = Double.parseDouble(element.getAttribute("lon"));

                OSMNode osmNode = new OSMNode(id, lat, lon);
                osmNodes.put(id, osmNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
