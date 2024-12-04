package com.tecksupport.database;

import com.tecksupport.glfw.pathfinder.node.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NodeQuery {
    private final Map<String, Node> entryMap; // FacultyID, Node
    private final Map<Integer, Node> nodeMap; // NodeId, Node
    private final Connection connection;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public NodeQuery(Connection connection) {
        this.connection = connection;
        this.nodeMap = getNodeMapFromDB();
        this.entryMap = getEntryNodesFromDB();
        getNeighborNodesFromDB();
    }

    private Map<Integer, Node> getNodeMapFromDB() {
        Map<Integer, Node> nodes = new HashMap<>();
        try {
            String query = "SELECT PositionID, X, Y, Z FROM Positions;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                int id = resultSet.getInt("PositionID");
                float x = resultSet.getFloat("X");
                float y = resultSet.getFloat("Y");
                float z = resultSet.getFloat("Z");

                Node node = new Node(id, x, y, z);
                nodes.put(id, node);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Node Query Error!");
        }

        return nodes;
    }

    private void getNeighborNodesFromDB() {
        try {
            String query = "SELECT FromPosition, ToPosition FROM PositionConnects;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                int fromPositionID = resultSet.getInt("FromPosition");
                int toPositionID = resultSet.getInt("ToPosition");
                Node fromNode = nodeMap.get(fromPositionID);
                Node toNode = nodeMap.get(toPositionID);
                fromNode.addNeighborNode(toNode);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Node Query Error!");
        }
    }

    private Map<String, Node> getEntryNodesFromDB() {
        Map<String, Node> entryMap = new HashMap<>();
        try {
            String query = "SELECT FacultyID, PositionID FROM Entries;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                String facultyID = resultSet.getString("FacultyID");
                int positionID = resultSet.getInt("PositionID");

                Node node = nodeMap.get(positionID);
                entryMap.put(facultyID, node);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Entries Query Error!");
        }

        return entryMap;
    }

    public List<Node> getNodes() {
        return nodeMap.values().stream().toList();
    }

    public Map<String, Node> getEntryMap() {
        return entryMap;
    }
}
