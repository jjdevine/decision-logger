package com.jonathandevinesoftware.decisionlogger.gui.valueselector;

import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceData;

import java.util.List;
import java.util.UUID;

public interface ReferenceDataSource {

    List<? extends ReferenceData> searchValues(String query);

    void addValue(ReferenceData value);

    ReferenceData getExactValue(String value);

    ReferenceData constructInstance(UUID id, String value);
}
