package com.jonathandevinesoftware.decisionlogger.model;

import com.jonathandevinesoftware.decisionlogger.gui.utils.MultiMap;
import com.jonathandevinesoftware.decisionlogger.persistence.Database;
import com.jonathandevinesoftware.decisionlogger.persistence.DatabaseUtils;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.TagDAO;

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

    public void saveOrUpdateDecision(Decision decision) throws SQLException {
        //delete and re-create
        deleteDecision(decision.getId());
        saveDecision(decision);
    }

    public void deleteDecision(UUID decisionId) throws SQLException {
        System.out.println("Deleting decision with id " + decisionId);
        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Decision_DecisionMaker WHERE DecisionId = ?");
        ps.setString(1, decisionId.toString());
        ps.execute();
        ps.close();

        ps = conn.prepareStatement(
                "DELETE FROM Decision_Tag WHERE DecisionId = ?");
        ps.setString(1, decisionId.toString());
        ps.execute();
        ps.close();

        ps = conn.prepareStatement(
                "DELETE FROM Decision WHERE Id = ?");
        ps.setString(1, decisionId.toString());
        ps.execute();
        ps.close();

        conn.close();
    }

    public void deleteDecisionWithLinkedMeeting(UUID meetingId) throws SQLException {
        System.out.println("Deleting decision with linked meeting id " + meetingId);
        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                DatabaseUtils.loadSqlQuery("DeleteDecisionMakerByMeetingId"));
        ps.setString(1, meetingId.toString());
        ps.execute();
        ps.close();

        ps = conn.prepareStatement(
                DatabaseUtils.loadSqlQuery("DeleteDecisionTagByMeetingId"));
        ps.setString(1, meetingId.toString());
        ps.execute();
        ps.close();

        ps = conn.prepareStatement(
                "DELETE FROM Decision WHERE LinkedMeeting = ?");
        ps.setString(1, meetingId.toString());
        ps.execute();
        ps.close();

        conn.close();
    }

    public void saveDecision(Decision decision) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Decision ("+DECISION_COLUMNS+") VALUES (?, ?, ?, ?)");

        int index = 1;
        ps.setString(index++, decision.getId().toString());
        ps.setString(index++, decision.getDecisionText());
        if(decision.getLinkedMeeting().isPresent()) {
            ps.setString(index++, decision.getLinkedMeeting().get().toString());
        } else {
            ps.setString(index++, null);
        }
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

        String query = buildQueryDecisionsSql(decisionMakerIds, tagIds, "SELECT d.*");

        System.out.println(query);

        PreparedStatement ps = conn.prepareStatement(query);
        applyQueryDecisionsSqlParams(decisionMakerIds, tagIds, ps);

        List<Decision> decisionList = new ArrayList<>();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            decisionList.add(mapRow(rs));
        }

        rs.close();
        ps.close();

        /*
            Execute 2 queries - one for all decision maker ids for any decisions returned,
            the second for all tag ids for any decisions returned. Once we have this information
            we will map in memory all the decision makers/tags to the decisions they belong to.
         */

        applyDecisionMakers(decisionMakerIds, tagIds, decisionList, conn);
        applyTags(decisionMakerIds, tagIds, decisionList, conn);

        conn.close();
        return decisionList;
    }

    public List<Decision> loadDecisionsByLinkedMeetingId(UUID linkedMeetingId) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "SELECT "+DECISION_COLUMNS+" FROM Decision WHERE LinkedMeeting = ?");

        ps.setString(1, linkedMeetingId.toString());

        List<Decision> decisionList = new ArrayList<>();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            decisionList.add(mapRow(rs));
        }

        applyTags(decisionList, conn);
        applyDecisionMakers(decisionList, conn);

        rs.close();
        ps.close();
        conn.close();

        return decisionList;
    }

    private void applyTags(List<Decision> decisions, Connection conn) throws SQLException {
        String sql = "SELECT DecisionId, TagId " +
                "FROM Decision_Tag " +
                "WHERE DecisionId = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        for(Decision decision: decisions) {
            List<UUID> tags = new ArrayList<>();
            pstmt.setString(1, decision.getId().toString());
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                UUID tagId = UUID.fromString(rs.getString("TagId"));
                tags.add(tagId);
            }

            decision.setTags(tags);

            rs.close();
        }
        pstmt.close();
    }

    private void applyDecisionMakers(List<Decision> decisions, Connection conn) throws SQLException {
        String sql = "SELECT DecisionId, DecisionMakerId " +
                "FROM Decision_DecisionMaker " +
                "WHERE DecisionId = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        for(Decision decision: decisions) {

            List<UUID> decisionMakers = new ArrayList<>();
            pstmt.setString(1, decision.getId().toString());
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                UUID decisionMakerId = UUID.fromString(rs.getString("DecisionMakerId"));
                decisionMakers.add(decisionMakerId);
            }

            decision.setDecisionMakers(decisionMakers);
            rs.close();
        }
        pstmt.close();
    }

    private void applyDecisionMakers(
            List<UUID> decisionMakerIds,
            List<UUID> tagIds,
            List<Decision> decisionList,
            Connection conn) throws SQLException {
        //load decision maker ids
        /*
            SELECT dm.DecisionId, dm.DecisionMakerId
            FROM DecisionMaker dm
            WHERE dm.DecisionId
            IN (SUBQUERY)
         */
        String decisionMakerSql =
                "SELECT dm.DecisionId, dm.DecisionMakerId\n " +
                        "FROM Decision_DecisionMaker dm\n " +
                        "WHERE dm.DecisionId \n " +
                        "IN (\nSUBQUERY)".replace(
                                "SUBQUERY",
                                buildQueryDecisionsSql(decisionMakerIds, tagIds, "SELECT d.id"));
        PreparedStatement ps = conn.prepareStatement(decisionMakerSql);
        applyQueryDecisionsSqlParams(decisionMakerIds, tagIds, ps);

        ResultSet rs = ps.executeQuery();

        //A decision can have multiple decision makers so can't use a standard map
        MultiMap<UUID, UUID> decisionToDecisionMaker = new MultiMap<>();
        while(rs.next()) {
            decisionToDecisionMaker.put(
                    UUID.fromString(rs.getString("DecisionId")),
                    UUID.fromString(rs.getString("DecisionMakerId"))
            );
        }

        for(Decision decision: decisionList) {
            decision.setDecisionMakers(
                    decisionToDecisionMaker.getValuesOrNewList(decision.getId()));
        }

        rs.close();
        ps.close();
    }

    private void applyTags(
            List<UUID> decisionMakerIds,
            List<UUID> tagIds,
            List<Decision> decisionList,
            Connection conn) throws SQLException {
        //load tag ids
        String tagSql =
                "SELECT dt.DecisionId, dt.TagId\n " +
                        "FROM Decision_Tag dt\n " +
                        "WHERE dt.DecisionId \n " +
                        "IN (\nSUBQUERY)".replace(
                                "SUBQUERY",
                                buildQueryDecisionsSql(decisionMakerIds, tagIds, "SELECT d.id"));
        PreparedStatement ps = conn.prepareStatement(tagSql);
        applyQueryDecisionsSqlParams(decisionMakerIds, tagIds, ps);

        ResultSet rs = ps.executeQuery();

        //A decision can have multiple tags so can't use a standard map
        MultiMap<UUID, UUID> decisionToTag = new MultiMap<>();
        while(rs.next()) {
            decisionToTag.put(
                    UUID.fromString(rs.getString("DecisionId")),
                    UUID.fromString(rs.getString("TagId"))
            );
        }

        for(Decision decision: decisionList) {
            decision.setTags(
                    decisionToTag.getValuesOrNewList(decision.getId()));
        }

        rs.close();
        ps.close();
    }

    private String buildQueryDecisionsSql(List<UUID> decisionMakerIds, List<UUID> tagIds, String selectStatement) {
        /*
        Ideal SQL is:

            SELECT d.*
            FROM Decision d
            INNER JOIN Decision_DecisionMaker dm on dm.DecisionId = d.Id
            INNER JOIN Decision_Tag t on t.DecisionId = d.Id
            WHERE dm.id IN (?, ?)
            AND t.id IN (?, ?)

        But if either list is empty, the join and associated filter needs to be removed or there will be no results
         */

        boolean decisionMakersEmpty = decisionMakerIds.isEmpty();
        boolean tagsEmpty = tagIds.isEmpty();
        StringBuilder sql = new StringBuilder();
        sql.append(selectStatement + "\n ");
        sql.append("FROM Decision d\n ");
        if(!decisionMakersEmpty) {
            sql.append("INNER JOIN Decision_DecisionMaker dm on dm.DecisionId = d.Id\n ");
        }
        if(!tagsEmpty) {
            sql.append("INNER JOIN Decision_Tag t on t.DecisionId = d.Id\n ");
        }
        if(!decisionMakersEmpty) {
            sql.append("WHERE dm.DecisionMakerId IN (DM_PLACEHOLDERS)\n "
                    .replace("DM_PLACEHOLDERS", placeholders(decisionMakerIds.size())));
        }
        if(!tagsEmpty) {
            String filterClause = decisionMakersEmpty ? "WHERE" : "AND";
            sql.append(filterClause + " t.TagId IN (T_PLACEHOLDERS)\n "
                    .replace("T_PLACEHOLDERS", placeholders(tagIds.size())));
        }
        return sql.toString();
    }

    private void applyQueryDecisionsSqlParams(List<UUID> decisionMakerIds, List<UUID> tagIds, PreparedStatement ps) throws SQLException {

        int index=1;
        for(UUID decisionMakerId: decisionMakerIds) {
            ps.setString(index++, decisionMakerId.toString());
        }

        for(UUID tagId: tagIds) {
            ps.setString(index++, tagId.toString());
        }
    }


    public static void main(String[] args) throws SQLException {
        System.out.println(DecisionDAO.getInstance().loadDecision(UUID.fromString("750161b3-360e-467d-b51d-61c9d1f63472")));
    }

    private Decision mapRow(ResultSet rs) throws SQLException {
        Decision decision = new Decision(UUID.fromString(rs.getString("id")));
        decision.setDecisionText(rs.getString("text"));
        decision.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        String linkedMeeting = rs.getString("linkedMeeting");
        if(linkedMeeting != null) {
            decision.setLinkedMeeting(Optional.of(UUID.fromString(linkedMeeting)));
        }

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
