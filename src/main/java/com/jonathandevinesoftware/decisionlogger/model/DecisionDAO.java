package com.jonathandevinesoftware.decisionlogger.model;

import com.jonathandevinesoftware.decisionlogger.persistence.Database;
import com.jonathandevinesoftware.decisionlogger.persistence.DatabaseUtils;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class DecisionDAO {

    private static String DECISION_COLUMNS = "id, text, linkedmeeting, timestamp";

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
                "INSERT INTO Decision ("+DECISION_COLUMNS+") VALUES (?, ?, ?, ?)");

        int index = 1;
        ps.setString(index++, decision.getId().toString());
        ps.setString(index++, decision.getDecisionText());
        ps.setString(index++, decision.getLinkedMeeting().toString());
        ps.setTimestamp(index++, Timestamp.valueOf(decision.getTimestamp()));

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
                "SELECT "+DECISION_COLUMNS+" FROM Decision WHERE Id = ?");

        ps.setString(1, decisionId.toString());
        ResultSet rs = ps.executeQuery();
        Decision result = null;

        if(rs.next()) {
            result = mapRow(rs);
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

    public List<Decision> queryDecisions(List<UUID> decisionMakerIds, List<UUID> tagIds) throws SQLException {

        Connection conn = Database.getConnection();

        String query = DatabaseUtils.loadSqlQuery("QueryDecisions")
                .replace("DM_PLACEHOLDERS", placeholders(decisionMakerIds.size()))
                .replace("T_PLACEHOLDERS", placeholders(tagIds.size()));

        System.out.println(query);

        PreparedStatement ps = conn.prepareStatement(query);

        int index=1;
        for(UUID decisionMakerId: decisionMakerIds) {
            ps.setString(index++, decisionMakerId.toString());
        }

        for(UUID tagId: tagIds) {
            ps.setString(index++, tagId.toString());
        }

        List<Decision> decisionList = new ArrayList<>();

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            decisionList.add(mapRow(rs));
        }

        rs.close();
        ps.close();
        conn.close();

        //TODO: load Tag and Decision maker Ids
        for(Decision decision: decisionList) {

        }

        return decisionList;
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(DecisionDAO.getInstance().loadDecision(UUID.fromString("750161b3-360e-467d-b51d-61c9d1f63472")));
    }

    private Decision mapRow(ResultSet rs) throws SQLException {
        Decision decision = new Decision(UUID.fromString(rs.getString("id")));
        decision.setDecisionText(rs.getString("text"));
        decision.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        decision.setLinkedMeeting(Optional.ofNullable(UUID.fromString(rs.getString("linkedMeeting"))));
        return decision;
    }

    private String placeholders(int count) {
        List<String> placeholders = new ArrayList<>();
        for(int x=0; x<count; x++) {
            placeholders.add("?");
        }

        return placeholders.stream().collect(Collectors.joining(","));
    }
}
