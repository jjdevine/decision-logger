package com.jonathandevinesoftware.decisionlogger.persistence;

public class DatabaseUtils {

    public static String loadSqlQuery(String queryName) {
        return ResourceLoader.loadResourceFile(queryName + ".sql");
    }
}
