package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ReferenceDataSource;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.PersonDAO;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PersonDataSource implements ReferenceDataSource {

    private static PersonDataSource instance;

    @Override
    public List<Person> searchValues(String query) {

        try {
            return PersonDAO.getInstance().searchPerson(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public static PersonDataSource getInstance() {
        if (instance == null) {
            instance = new PersonDataSource();
        }

        return instance;
    }
}
