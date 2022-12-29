package com.jonathandevinesoftware.decisionlogger.persistence.referencedata;

import com.jonathandevinesoftware.decisionlogger.core.Application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReferenceDataUtils {

    public static List<Person> convertIdsToPersonList(List<UUID> personIds) throws ReferenceDataException {
        List<Person> personList = new ArrayList<>();
        for(UUID personId: personIds) {
            try {
                personList.add(PersonDAO.getInstance().getPersonWithId(personId).get());
            } catch (SQLException e) {
                throw new ReferenceDataException(e);
            }
        }
        return personList;
    }

    public static List<Tag> convertIdsToTagList(List<UUID> tagIds) throws ReferenceDataException {
        List<Tag> tagList = new ArrayList<>();
        for(UUID tagId: tagIds) {
            try {
                tagList.add(TagDAO.getInstance().getTagWithId(tagId).get());
            } catch (SQLException e) {
                throw new ReferenceDataException(e);
            }
        }
        return tagList;
    }
}
