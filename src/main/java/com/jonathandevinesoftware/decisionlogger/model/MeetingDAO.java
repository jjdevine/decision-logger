package com.jonathandevinesoftware.decisionlogger.model;

import com.jonathandevinesoftware.decisionlogger.gui.searchmeetings.SearchParameters;
import com.jonathandevinesoftware.decisionlogger.persistence.Database;
import com.jonathandevinesoftware.decisionlogger.persistence.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MeetingDAO {

    private static MeetingDAO instance;

    public static MeetingDAO getInstance() {
        if(instance == null) {
            instance = new MeetingDAO();
        }
        return instance;
    }

    public void saveOrUpdate(Meeting meeting) throws SQLException {
        deleteMeeting(meeting.getId());
        insertMeeting(meeting);
    }

    public void deleteMeeting(UUID meetingId) throws SQLException {
        System.out.println("Deleting meeting with id " + meetingId);
        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Meeting_Attendee WHERE MeetingId = ?");
        ps.setString(1, meetingId.toString());
        ps.execute();
        ps.close();

        ps = conn.prepareStatement(
                "DELETE FROM Meeting_Tag WHERE MeetingId = ?");
        ps.setString(1, meetingId.toString());
        ps.execute();
        ps.close();

        ps = conn.prepareStatement(
                "DELETE FROM Meeting WHERE Id = ?");
        ps.setString(1, meetingId.toString());
        ps.execute();
        ps.close();

        conn.close();
    }

    public void insertMeeting(Meeting meeting) throws SQLException {
        Connection conn = Database.getConnection();
        insertMeetingAttendees(meeting.getAttendees(), meeting.getId(), conn);
        insertMeetingTags(meeting.getTags(), meeting.getId(), conn);

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Meeting (Id, Title, Timestamp) VALUES (?, ?, ?)");
        ps.setString(1, meeting.getId().toString());
        ps.setString(2, meeting.getTitle());
        ps.setTimestamp(3, Timestamp.valueOf(meeting.getTimestamp()));

        ps.execute();
        ps.close();
        conn.close();
    }

    private void insertMeetingAttendees(List<UUID> attendees, UUID meetingId, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Meeting_Attendee (MeetingId, AttendeeId) VALUES (?, ?)");

        for(UUID attendeeId: attendees) {
            ps.setString(1, meetingId.toString());
            ps.setString(2, attendeeId.toString());
            ps.execute();
        }

        ps.close();
    }

    private void insertMeetingTags(List<UUID> tags, UUID meetingId, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Meeting_Tag (MeetingId, TagId) VALUES (?, ?)");

        for(UUID tagId: tags) {
            ps.setString(1, meetingId.toString());
            ps.setString(2, tagId.toString());
            ps.execute();
        }

        ps.close();
    }

    public Meeting loadMeeting(UUID meetingId) throws SQLException {
        Meeting meeting = new Meeting(meetingId);

        Connection conn = Database.getConnection();
        populateMeetingHeaderData(meeting, conn);
        populateMeetingAttendees(meeting, conn);
        populateMeetingTags(meeting, conn);
        populateMeetingDecisions(meeting, conn);

        return meeting;
    }

    private void populateMeetingDecisions(Meeting meeting, Connection conn) throws SQLException {
        String sql = "SELECT id FROM Decision WHERE LinkedMeeting = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, meeting.getId().toString());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            meeting.getDecisions().add(UUID.fromString(rs.getString("id")));
        }
    }

    private void populateMeetingHeaderData(Meeting meeting, Connection conn) throws SQLException {

        String sql = "SELECT Id, Title, Timestamp " +
                "FROM Meeting " +
                "WHERE Id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, meeting.getId().toString());

        ResultSet rs = stmt.executeQuery();

        rs.next();
        meeting.setTitle(rs.getString("Title"));
        meeting.setTimestamp(rs.getTimestamp("Timestamp").toLocalDateTime());

        rs.close();
        stmt.close();
    }

    private void populateMeetingAttendees(Meeting meeting, Connection conn) throws SQLException {
        List<UUID> attendees = new ArrayList<>();

        String sql = "SELECT MeetingId, AttendeeId " +
                "FROM Meeting_Attendee " +
                "WHERE MeetingId = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, meeting.getId().toString());

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            UUID personId = UUID.fromString(rs.getString("AttendeeId"));
            attendees.add(personId);
        }

        meeting.setAttendees(attendees);
        rs.close();
        stmt.close();
    }

    private void populateMeetingTags(Meeting meeting, Connection conn) throws SQLException {
        List<UUID> tags = new ArrayList<>();

        String sql = "SELECT MeetingId, TagId " +
                "FROM Meeting_Tag " +
                "WHERE MeetingId = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, meeting.getId().toString());

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            UUID tagId = UUID.fromString(rs.getString("TagId"));
            tags.add(tagId);
        }

        meeting.setTags(tags);
        rs.close();
        stmt.close();
    }

    public List<Meeting> queryMeetings(SearchParameters searchParameters) throws SQLException {
        Connection conn = Database.getConnection();

        String query = buildQueryMeetingsSql(searchParameters);
        System.out.println(query);

        PreparedStatement ps = conn.prepareStatement(query);
        applyQueryMeetingsSqlParams(searchParameters, ps);

        List<Meeting> meetingList = new ArrayList<>();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            meetingList.add(mapRow(rs));
        }

        rs.close();
        ps.close();

        /*
            Next need to populate the lists - attendee ids, tag ids and decision ids
         */


        /*
            Execute 2 queries - one for all decision maker ids for any decisions returned,
            the second for all tag ids for any decisions returned. Once we have this information
        we will map in memory all the decision makers/tags to the decisions they belong to.
         *

        applyDecisionMakers(decisionMakerIds, tagIds, decisionList, conn);
        applyTags(decisionMakerIds, tagIds, decisionList, conn);

        conn.close();
        return decisionList;
         */

        return null;
    }

    private void populateMeetingLists(List<Meeting> meetings, SearchParameters searchParameters, Connection conn) throws SQLException {

        StringBuilder sql = new StringBuilder();
        sql.append("WITH MeetingsQueryResults (id, title, timestamp) AS (");
        sql.append(buildQueryMeetingsSql(searchParameters));
        sql.append(") ");

        sql.append("SELECT ma.MeetingId AS MeetingId, ma.Attendee AS Value, 'Attendee' as Type ");
        sql.append("FROM Meeting_Attendee ma ");
        sql.append("INNER JOIN MeetingsQueryResults mqr on mqr.id = ma.MeetingId");
        System.out.println(sql);

        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        applyQueryMeetingsSqlParams(searchParameters, stmt);

        ResultSet rs = stmt.executeQuery();

        Database.debugResultSet(rs);
        /*

        SELECT MeetingId, 'Attendee', AttendeeId
        FROM Meeting_Attendee
        WHERE MeetingId = ?
        UNION ALL
        SELECT MeetingId, 'Tag', TagId
        FROM Meeting_Tag
        WHERE MeetingId = ?
        UNION ALL
        SELECT LinkedMeeting, 'Decision', Id
        FROM Decision
        WHERE LinkedMeeting = ?

         */
    }

    private Meeting mapRow(ResultSet rs) throws SQLException {

        Meeting meeting = new Meeting(UUID.fromString(rs.getString("Id")));
        meeting.setTimestamp(rs.getTimestamp("Timestamp").toLocalDateTime());
        meeting.setTitle(rs.getString("Title"));

        return meeting;
    }


    private void applyQueryMeetingsSqlParams(SearchParameters searchParameters, PreparedStatement ps) throws SQLException {

        int index=1;
        for(UUID attendeeId: searchParameters.getAttendeeIds()) {
            ps.setString(index++, attendeeId.toString());
        }

        for(UUID tagId: searchParameters.getTagIds()) {
            ps.setString(index++, tagId.toString());
        }

        for(UUID decisionMakerId: searchParameters.getDecisionMakerIds()) {
            ps.setString(index++, decisionMakerId.toString());
        }
    }

    public void withTest2(SearchParameters searchParameters) throws SQLException {
        Connection conn = Database.getConnection();


        StringBuilder sql = new StringBuilder();
        sql.append("WITH MeetingsQueryResults (id, title, timestamp) AS (");
        sql.append(buildQueryMeetingsSql(searchParameters));
        sql.append(") ");

        sql.append("SELECT ma.MeetingId AS MeetingId, ma.AttendeeId AS Value, 'Attendee' as Type ");
        sql.append("FROM Meeting_Attendee ma ");
        sql.append("INNER JOIN MeetingsQueryResults mqr on mqr.id = ma.MeetingId ");

        sql.append("UNION ALL  ");
        sql.append("SELECT mt.MeetingId AS MeetingId, mt.TagId AS Value, 'Tag' as Type ");
        sql.append("FROM Meeting_Tag mt ");
        sql.append("INNER JOIN MeetingsQueryResults mqr on mqr.id = mt.MeetingId ");

        sql.append("UNION ALL  ");
        sql.append("SELECT d.LinkedMeeting AS MeetingId, d.Id AS Value, 'Decision' as Type ");
        sql.append("FROM Decision d ");
        sql.append("INNER JOIN MeetingsQueryResults mqr on mqr.id = d.LinkedMeeting ");
        System.out.println(sql);

        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        applyQueryMeetingsSqlParams(searchParameters, stmt);

        ResultSet rs = stmt.executeQuery();

        Database.debugResultSet(rs);
    }

    public void withTest(SearchParameters searchParameters) throws SQLException {
        Connection conn = Database.getConnection();

        StringBuilder sql = new StringBuilder();
        sql.append("WITH MeetingsTable (id, title, timestamp) AS (");
        sql.append(buildQueryMeetingsSql(searchParameters));
        sql.append(") ");
        sql.append("SELECT * FROM MeetingsTable");
        System.out.println(sql);

        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        applyQueryMeetingsSqlParams(searchParameters, stmt);

        ResultSet rs = stmt.executeQuery();

        Database.debugResultSet(rs);
    }

    private String buildQueryMeetingsSql(SearchParameters searchParameters) {
        /*
            SELECT DISTINCT m.*
            FROM Meeting m
            INNER JOIN Meeting_Attendee ma on ma.MeetingId = m.Id
            INNER JOIN Meeting_Tag mt on mt.MeetingId = m.Id
            INNER JOIN Decision d on d.LinkedMeeting = m.Id
            INNER JOIN Decision_DecisionMaker dm on dm.DecisionId = d.Id
            WHERE ma.AttendeeId in (?, ?)
            OR mt.TagId in (?, ?)
            OR dm.DecisionMakerId in (?, ?)
         */

        boolean noAttendees = searchParameters.getAttendeeIds().size() == 0;
        boolean noTags = searchParameters.getTagIds().size() == 0;
        boolean noDecisionMakers = searchParameters.getDecisionMakerIds().size() == 0;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT m.*\n ");
        sql.append("FROM Meeting m\n ");

        if(!noAttendees) {
            sql.append("INNER JOIN Meeting_Attendee ma on ma.MeetingId = m.Id\n ");
        }
        if(!noTags) {
            sql.append("INNER JOIN Meeting_Tag mt on mt.MeetingId = m.Id\n ");
        }
        if(!noDecisionMakers) {
            sql.append("INNER JOIN Decision d on d.LinkedMeeting = m.Id\n ");
            sql.append("INNER JOIN Decision_DecisionMaker dm on dm.DecisionId = d.Id\n ");
        }

        boolean firstClause = true;

        if(!noAttendees) {
            sql.append("WHERE ma.AttendeeId IN (PLACEHOLDERS)\n "
                    .replace("PLACEHOLDERS",
                            DatabaseUtils.placeholders(searchParameters.getAttendeeIds().size())));
            firstClause = false;
        }

        if(!noTags) {
            String filterClause = firstClause ? "WHERE" : "AND";
            sql.append(filterClause + " mt.TagId IN (PLACEHOLDERS)\n "
                    .replace("PLACEHOLDERS",
                            DatabaseUtils.placeholders(searchParameters.getTagIds().size())));
            firstClause = false;
        }

        if(!noDecisionMakers) {
            String filterClause = firstClause ? "WHERE" : "AND";
            sql.append(filterClause + " dm.DecisionMakerId IN (PLACEHOLDERS)\n "
                    .replace("PLACEHOLDERS",
                            DatabaseUtils.placeholders(searchParameters.getDecisionMakerIds().size())));
        }
        return sql.toString();
    }
}
