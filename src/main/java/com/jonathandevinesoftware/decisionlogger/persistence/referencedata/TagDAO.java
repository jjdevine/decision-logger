package com.jonathandevinesoftware.decisionlogger.persistence.referencedata;

import com.jonathandevinesoftware.decisionlogger.persistence.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TagDAO {

    private List<Tag> cache = new ArrayList<>();

    private static TagDAO instance;

    public static TagDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new TagDAO();
            instance.initialiseCache();
        }
        return instance;
    }

    private void initialiseCache() throws SQLException {
        System.out.println("Initialising cache");

        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT Id, Value FROM Tag");

        ResultSet rs = ps.executeQuery();

        List<Person> result = new ArrayList<>();

        while(rs.next()) {
            cache.add(new Tag(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("Value")
            ));
        }

        rs.close();
        ps.close();
        conn.close();
    }

    public void addTag(Tag tag) throws SQLException {

        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO tag (id, value) VALUES (?, ?)");

        ps.setString(1, tag.getId().toString());
        ps.setString(2, tag.getValue());

        ps.execute();
        cache.add(tag);

        ps.close();
        conn.close();
    }

    public List<Tag> searchTag(String query) throws SQLException {
        return cache.stream()
                .filter(t -> t.getValue().toUpperCase().contains(query.toUpperCase()))
                .collect(Collectors.toList());
    }

    public Optional<Tag> getTagWithValue(String value) {
        return cache
                .stream()
                .filter(t -> t.getValue().toUpperCase().equals(value.toUpperCase()))
                .findFirst();
    }

    public Optional<Tag> getTagWithId(UUID id) {
        return cache
                .stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }
}
