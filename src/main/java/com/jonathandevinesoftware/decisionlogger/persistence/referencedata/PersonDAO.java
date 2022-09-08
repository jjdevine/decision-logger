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

public class PersonDAO {

    private static PersonDAO instance;

    public static PersonDAO getInstance() {
        if (instance == null) {
            instance = new PersonDAO();
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
    }

    public List<Person> searchPerson(String query) throws SQLException {

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

    public static void main(String[] args) throws SQLException {
        PersonDAO.getInstance().addPerson(new Person(UUID.randomUUID(), "Bob"));
        PersonDAO.getInstance().addPerson(new Person(UUID.randomUUID(), "Fred"));
        PersonDAO.getInstance().addPerson(new Person(UUID.randomUUID(), "Billy"));
        PersonDAO.getInstance().addPerson(new Person(UUID.randomUUID(), "Boris"));
        //PersonDAO.getInstance().searchPerson("bo1");
    }
}

