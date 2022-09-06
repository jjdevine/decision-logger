package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.persistence.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DecisionMakerDAO {

    private static DecisionMakerDAO instance;

    public static DecisionMakerDAO getInstance() {
        if (instance == null) {
            instance = new DecisionMakerDAO();
        }
        return instance;
    }

    public void addDecisionMaker(DecisionMaker decisionMaker) throws SQLException {

        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO decisionmaker (id, name) VALUES (?, ?)");

        ps.setString(1, decisionMaker.getId().toString());
        ps.setString(2, decisionMaker.getName());

        ps.execute();
        ps.close();
        conn.close();
    }

    public List<DecisionMaker> searchDecisionMaker(String query) throws SQLException {

        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "SELECT id, name FROM decisionmaker WHERE UPPER(name) LIKE ?");

        ps.setString(1, "%" + query.toUpperCase() + "%");
        ResultSet rs = ps.executeQuery();

        List<DecisionMaker> result = new ArrayList<>();

        while(rs.next()) {
            result.add(new DecisionMaker(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("name")
            ));
        }

        rs.close();
        ps.close();
        conn.close();

        return result;
    }

    public static void main(String[] args) throws SQLException {
        //DecisionMakerDAO.getInstance().addDecisionMaker(new DecisionMaker(UUID.randomUUID(), "Bob"));
        DecisionMakerDAO.getInstance().searchDecisionMaker("bo1");
    }
}

