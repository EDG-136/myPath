package com.tecksupport.PathfinderAlgorithm;

import java.util.HashMap;
import java.util.Map;

public class BuildingCoordinates {
    private static final Map<String, String> buildingCoordinates = new HashMap<>();

    static {
        // Coordinates are now in "longitude,latitude" format
        buildingCoordinates.put("PSB", "-117.154346,33.134106");
        buildingCoordinates.put("CCF", "-117.152908,33.132754");
        buildingCoordinates.put("ELB", "-117.160204,33.134169");
        buildingCoordinates.put("QT", "-117.159170,33.134292");
        buildingCoordinates.put("NC", "-117.160908,33.133816");
        buildingCoordinates.put("UVA", "-117.157265,33.133642");
        buildingCoordinates.put("CFH", "-117.160028,33.131462");
        buildingCoordinates.put("SC", "-117.160061,33.132010");
        buildingCoordinates.put("SF", "-117.161048,33.130995");
        buildingCoordinates.put("BF", "-117.162828,33.130645");
        buildingCoordinates.put("MPF", "-117.163869,33.131111");
        buildingCoordinates.put("SBSB", "-117.157348,33.130438");
        buildingCoordinates.put("SCI2", "-117.157915,33.130707");
        buildingCoordinates.put("USU", "-117.159041,33.130537");
        buildingCoordinates.put("ARTS", "-117.158012,33.130016");
        buildingCoordinates.put("SHCSB", "-117.158098,33.131264");
        buildingCoordinates.put("MCM", "-117.162850,33.128687");
        buildingCoordinates.put("MTF", "-117.161843,33.128919");
        buildingCoordinates.put("LOT_XYZ", "-117.164377,33.128844");
        buildingCoordinates.put("KEL", "-117.159546,33.129081");
        buildingCoordinates.put("UNIV", "-117.158695,33.128674");
        buildingCoordinates.put("PF", "-117.164437,33.126785");
        buildingCoordinates.put("LOT_B", "-117.163077,33.126772");
        buildingCoordinates.put("LOT_C", "-117.161156,33.126521");
        buildingCoordinates.put("CRA", "-117.159585,33.127838");
        buildingCoordinates.put("COM", "-117.159385,33.127392");
        buildingCoordinates.put("LOT_G", "-117.158491,33.127353");
        buildingCoordinates.put("LOT_E", "-117.158859,33.126557");
        buildingCoordinates.put("ACD", "-117.158541,33.128077");
        buildingCoordinates.put("MARK", "-117.157798,33.128120");
        buildingCoordinates.put("SCI", "-117.158698,33.127725");
        buildingCoordinates.put("VET", "-117.158198,33.127521");
        buildingCoordinates.put("LOT_H", "-117.157368,33.127527");
        buildingCoordinates.put("VEP", "-117.157920,33.127198");
        buildingCoordinates.put("LOT_F", "-117.156830,33.126177");
    }

    public static String getCoordinates(String buildingName) {
        String coordinates = buildingCoordinates.getOrDefault(buildingName, "0.0,0.0");
        System.out.println("Retrieving coordinates for " + buildingName + ": " + coordinates); // Debugging output
        return coordinates;
    }
}
