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
import java.util.stream.Collectors;

public class PersonDAO {

    private List<Person> cache = new ArrayList<>();

    private static PersonDAO instance;

    public static PersonDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new PersonDAO();
            instance.initialiseCache();
        }
        return instance;
    }

    public void addPerson(Person person) throws SQLException {

        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO person (id, name) VALUES (?, ?)");

        ps.setString(1, person.getId().toString());
        ps.setString(2, person.getValue());

        ps.execute();
        ps.close();
        conn.close();

        cache.add(person);
    }

    private void initialiseCache() throws SQLException {
        System.out.println("Initialising cache");

        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT id, name FROM person");

        ResultSet rs = ps.executeQuery();

        List<Person> result = new ArrayList<>();

        while(rs.next()) {
            cache.add(new Person(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("name")
            ));
        }

        rs.close();
        ps.close();
        conn.close();
    }

    public List<Person> searchPerson(String query) throws SQLException {

        return cache.stream()
                .filter(p -> p.getValue().contains(query))
                .collect(Collectors.toList());
    }

    private List<Person> searchPersonDB(String query) throws SQLException {
        if(ApplicationConstants.DEBUG) {
            System.out.println("Looking for person with query <" + query + ">");
        }

        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "SELECT id, name FROM person WHERE UPPER(name) LIKE ?");

        ps.setString(1, "%" + query.toUpperCase() + "%");
        ResultSet rs = ps.executeQuery();

        List<Person> result = new ArrayList<>();

        while(rs.next()) {
            result.add(new Person(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("name")
            ));
        }

        rs.close();
        ps.close();
        conn.close();

        result.forEach(System.out::println);

        return result;
    }

    public List<Person> getPeopleWithIds(List<UUID> ids) {
        return cache.stream()
                .filter(p -> ids.contains(p.getId()))
                .collect(Collectors.toList());
    }


    public Person getPersonWithName(String name) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "SELECT id, name FROM person WHERE UPPER(name) LIKE ?");

        ps.setString(1, name.toUpperCase());
        ResultSet rs = ps.executeQuery();
        Person result = null;

        if(rs.next()) {
            result= new Person(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("name"));
        }

        rs.close();
        ps.close();
        conn.close();

        return result;
    }
}

