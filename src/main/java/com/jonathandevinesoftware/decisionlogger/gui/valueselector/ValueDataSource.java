package com.jonathandevinesoftware.decisionlogger.gui.valueselector;

import java.util.List;

public interface ValueDataSource {

    List<String> searchValues(String query);
}
