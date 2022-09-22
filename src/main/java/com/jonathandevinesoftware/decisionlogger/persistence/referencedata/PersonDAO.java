package com.jonathandevinesoftware.decisionlogger.persistence.referencedata;

import com.jonathandevinesoftware.decisionlogger.core.Application;
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
                .filter(p -> p.getValue().toUpperCase().contains(query.toUpperCase()))
                .collect(Collectors.toList());
    }

    public Optional<Person> getPersonWithId(UUID id) {
        Optional<Person> result = cache.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        Application.debug("searched for " + id + " ... returning " + result);

        return result;
    }


    public Person getPersonWithName(String name) {
        return cache
                .stream()
                .filter(p -> p.getValue().toUpperCase().equals(name.toUpperCase()))
                .findFirst()
                .get();
    }
}

