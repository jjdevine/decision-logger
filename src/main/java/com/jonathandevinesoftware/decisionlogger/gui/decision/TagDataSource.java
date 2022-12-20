package com.jonathandevinesoftware.decisionlogger.gui.decision;

import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ReferenceDataSource;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceData;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.TagDAO;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TagDataSource implements ReferenceDataSource {

    private static TagDataSource instance;
    @Override
    public List<? extends ReferenceData> searchValues(String query) {
        try {
            return TagDAO.getInstance().searchTag(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public void addValue(ReferenceData tag) {
        try {
            if(tag instanceof Tag) {
                TagDAO.getInstance().addTag((Tag)tag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReferenceData getExactValue(String value) {
        try {
            Optional<Tag> tag =  TagDAO.getInstance().getTagWithValue(value);
            if(tag.isEmpty()) {
                return null;
            } else {
                return tag.get();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ReferenceData constructInstance(UUID id, String value) {
        return new Tag(id, value);
    }

    @Override
    public Optional<? extends ReferenceData> getById(UUID id) {
        try {
            return TagDAO.getInstance().getTagWithId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static TagDataSource getInstance() {
        if (instance == null) {
            instance = new TagDataSource();
        }

        return instance;
    }

}
