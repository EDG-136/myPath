package com.tecksupport.Pathfinder;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OSMParser {
    private Map<Long, OSMNode> nodes;
    private Map<Long, OSMWay> ways;

    public OSMParser(String filePath) {
        nodes = new HashMap<>();
        ways = new HashMap<>();
        parseOSMFile(filePath);
    }

    private void parseOSMFile(String filePath) {
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
                nodes.put(id, new OSMNode(id, lat, lon));
            }

            NodeList wayElements = doc.getElementsByTagName("way");
            for (int i = 0; i < wayElements.getLength(); i++) {
                Element element = (Element) wayElements.item(i);
                long id = Long.parseLong(element.getAttribute("id"));
                OSMWay way = new OSMWay(id);

                NodeList ndList = element.getElementsByTagName("nd");
                for (int j = 0; j < ndList.getLength(); j++) {
                    Element nd = (Element) ndList.item(j);
                    long ref = Long.parseLong(nd.getAttribute("ref"));
                    way.addNodeRef(ref);
                }

                ways.put(id, way);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Long, OSMNode> getNodes() {
        return nodes;
    }

    public Map<Long, OSMWay> getWays() {
        return ways;
    }
}