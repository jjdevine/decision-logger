package com.jonathandevinesoftware.decisionlogger.model;

import com.jonathandevinesoftware.decisionlogger.persistence.Database;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DecisionDAO {

    private static DecisionDAO instance;

    public static DecisionDAO getInstance() {
        if(instance == null) {
            instance = new DecisionDAO();
        }
        return instance;
    }

    public void saveDecision(Decision decision) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Decision (id, text, timestamp) VALUES (?, ?, ?)");

        ps.setString(1, decision.getId().toString());
        ps.setString(2, decision.getDecisionText());
        ps.setTimestamp(3, Timestamp.valueOf(decision.getTimestamp()));

        ps.execute();
        ps.close();

        for (UUID decisionMakerId: decision.getDecisionMakers()) {
            ps = conn.prepareStatement(
                    "INSERT INTO Decision_DecisionMaker (DecisionId, DecisionMakerId) VALUES (?, ?)");

            ps.setString(1, decision.getId().toString());
            ps.setString(2, decisionMakerId.toString());
            ps.execute();
            ps.close();
        }

        for (UUID tagId: decision.getTags()) {
            ps = conn.prepareStatement(
                    "INSERT INTO Decision_Tag (DecisionId, TagId) VALUES (?, ?)");

            ps.setString(1, decision.getId().toString());
            ps.setString(2, tagId.toString());
            ps.execute();
            ps.close();
        }

        conn.close();
    }

    public Decision loadDecision(UUID decisionId) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "SELECT id, text, timestamp FROM Decision WHERE Id = ?");

        ps.setString(1, decisionId.toString());
        ResultSet rs = ps.executeQuery();
        Decision result = null;

        if(rs.next()) {
            result = new Decision(UUID.fromString(rs.getString("id")));
            result.setDecisionText(rs.getString("text"));
            result.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        }

        rs.close();
        ps.close();

        //load tags
        List<UUID> tags = new ArrayList<>();
        result.setTags(tags);

        ps = conn.prepareStatement(
                "SELECT TagId FROM Decision_Tag WHERE DecisionId = ?");
        ps.setString(1, decisionId.toString());
        rs = ps.executeQuery();

        while(rs.next()) {
            tags.add(UUID.fromString(rs.getString("TagId")));
        }

        rs.close();
        ps.close();

        //load decision makers
        List<UUID> decisionMakers = new ArrayList<>();
        result.setDecisionMakers(decisionMakers);

        ps = conn.prepareStatement(
                "SELECT DecisionMakerId FROM Decision_DecisionMaker WHERE DecisionId = ?");
        ps.setString(1, decisionId.toString());
        rs = ps.executeQuery();

        while(rs.next()) {
            decisionMakers.add(UUID.fromString(rs.getString("DecisionMakerId")));
        }

        rs.close();
        ps.close();
        conn.close();

        return result;
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(DecisionDAO.getInstance().loadDecision(UUID.fromString("750161b3-360e-467d-b51d-61c9d1f63472")));
    }
}
