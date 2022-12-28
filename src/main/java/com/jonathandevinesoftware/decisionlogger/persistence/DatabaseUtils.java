package com.jonathandevinesoftware.decisionlogger.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseUtils {

    public static String loadSqlQuery(String queryName) {
        String fileName = queryName + ".sql";
        System.out.println("Loading " + fileName);
        return ResourceLoader.loadResourceFile(fileName);
    }

    public static String placeholders(int count) {
        List<String> placeholders = new ArrayList<>();
        for(int x=0; x<count; x++) {
            placeholders.add("?");
        }

        return placeholders.stream().collect(Collectors.joining(","));
    }
}
