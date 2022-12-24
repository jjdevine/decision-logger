package com.jonathandevinesoftware.decisionlogger.persistence;

public class DatabaseUtils {

    public static String loadSqlQuery(String queryName) {
        String fileName = queryName + ".sql";
        System.out.println("Loading " + fileName);
        return ResourceLoader.loadResourceFile(fileName);
    }
}
