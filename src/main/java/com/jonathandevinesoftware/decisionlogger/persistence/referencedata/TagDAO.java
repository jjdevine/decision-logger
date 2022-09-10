package com.jonathandevinesoftware.decisionlogger.persistence.referencedata;

import com.jonathandevinesoftware.decisionlogger.core.ApplicationConstants;
import com.jonathandevinesoftware.decisionlogger.persistence.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TagDAO {

    private static TagDAO instance;

    public static TagDAO getInstance() {
        if (instance == null) {
            instance = new TagDAO();
        }
        return instance;
    }

    public void addTag(Tag tag) throws SQLException {

        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO tag (id, value) VALUES (?, ?)");

        ps.setString(1, tag.getId().toString());
        ps.setString(2, tag.getValue());

        ps.execute();
        ps.close();
        conn.close();
    }

    public List<Tag> searchTag(String query) throws SQLException {

        if(ApplicationConstants.DEBUG) {
            System.out.println("Looking for tag with query <" + query + ">");
        }

        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "SELECT id, value FROM tag WHERE UPPER(value) LIKE ?");

        ps.setString(1, "%" + query.toUpperCase() + "%");
        ResultSet rs = ps.executeQuery();

        List<Tag> result = new ArrayList<>();

        while(rs.next()) {
            result.add(new Tag(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("value")
            ));
        }

        rs.close();
        ps.close();
        conn.close();

        return result;
    }

    public Tag getTagWithValue(String value) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "SELECT id, value FROM tag WHERE UPPER(value) LIKE ?");

        ps.setString(1, value.toUpperCase());
        ResultSet rs = ps.executeQuery();
        Tag result = null;

        if(rs.next()) {
            result= new Tag(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("value"));
        }

        rs.close();
        ps.close();
        conn.close();

        return result;
    }
}
