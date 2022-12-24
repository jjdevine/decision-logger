package com.jonathandevinesoftware.decisionlogger.model;

import com.jonathandevinesoftware.decisionlogger.persistence.Database;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.PersonDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.TagDAO;

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

        //TODO - implement the above

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
}
