package com.jonathandevinesoftware.decisionlogger.gui.valueselector;

import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceData;

import java.util.List;

public interface ReferenceDataSource<T extends ReferenceData> {

    List<T> searchValues(String query);
}
