package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ValueDataSource;

import java.util.List;

public class PersonDataSource implements ValueDataSource {

    private static PersonDataSource instance;

    @Override
    public List<String> searchValues(String query) {

        DecisionMakerDAO.getInstance().searchDecisionMaker(query);
    }

    public static PersonDataSource getInstance() {
        if (instance == null) {
            instance = new PersonDataSource();
        }

        return instance;
    }
}
