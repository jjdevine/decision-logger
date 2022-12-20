package com.jonathandevinesoftware.decisionlogger.gui.decision;

import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ReferenceDataSource;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.PersonDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceData;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public void addValue(ReferenceData person) {
        try {
            if(person instanceof Person) {
                PersonDAO.getInstance().addPerson((Person)person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReferenceData getExactValue(String value) {
        try {
            Optional<Person> person = PersonDAO.getInstance().getPersonWithName(value);
            if(person.isEmpty()) {
                return null;
            } else {
                return person.get();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ReferenceData constructInstance(UUID id, String value) {
        return new Person(id, value);
    }

    @Override
    public Optional<? extends ReferenceData> getById(UUID id) {
        try {
            return PersonDAO.getInstance().getPersonWithId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static PersonDataSource getInstance() {
        if (instance == null) {
            instance = new PersonDataSource();
        }

        return instance;
    }
}
