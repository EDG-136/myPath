package com.tecksupport.database;

import com.tecksupport.glfw.model.Entity;
import com.tecksupport.glfw.model.TexturedModel;
import com.tecksupport.glfw.pathfinder.node.Node;
import org.joml.Vector3f;

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
    private final Map<String, Node> entryMap;
    private final List<Node> nodeList;
    private final Connection connection;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final TexturedModel texturedModel;

    public NodeQuery(Connection connection, TexturedModel texturedModel) {
        this.connection = connection;
        this.texturedModel = texturedModel;
        this.nodeList = getNodeListFromDB();
        this.entryMap = getEntryNodesFromDB();
    }

    private List<Node> getNodeListFromDB() {
        List<Node> nodes = new ArrayList<>();
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

                Node node = new Node(texturedModel, id, x, y, z);
                nodes.add(node);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Node Query Error!");
        }

        return nodes;
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

                Node node = null;
                for (Node n : nodeList) {
                    if (n.getId() == positionID) {
                        node = n;
                        break;
                    }
                }
                entryMap.put(facultyID, node);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Entries Query Error!");
        }

        return entryMap;
    }

    public List<Node> getNodes() {
        return nodeList;
    }

    public Map<String, Node> getEntryMap() {
        return entryMap;
    }
}
