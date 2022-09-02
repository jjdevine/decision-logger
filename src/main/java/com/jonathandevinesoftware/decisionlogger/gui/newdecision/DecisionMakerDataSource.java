package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ValueDataSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DecisionMakerDataSource implements ValueDataSource {

    private String[] values = {"Bob", "Barry", "Jim"};

    private static DecisionMakerDataSource instance;

    @Override
    public List<String> searchValues(String query) {

        List<String> valueList = Arrays.asList(values);

        return valueList.stream().filter(val -> val.contains(query)).collect(Collectors.toList());
    }

    public static DecisionMakerDataSource getInstance() {
        if (instance == null) {
            instance = new DecisionMakerDataSource();
        }

        return instance;
    }
}
