package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ReferenceDataSource;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.PersonDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceData;

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

    @Override
    public void addValue(ReferenceData value) {

    }

    @Override
    public void addValue(Person person) {
        try {
            PersonDAO.getInstance().addPerson(person);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PersonDataSource getInstance() {
        if (instance == null) {
            instance = new PersonDataSource();
        }

        return instance;
    }
}
