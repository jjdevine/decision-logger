package com.jonathandevinesoftware.decisionlogger.persistence;

import com.jonathandevinesoftware.decisionlogger.core.ApplicationConstants;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Database {

    private static final String dbName = "decisionLogger";

    private static Map<String, String> tableToScript;

    static {
        try {
            tableToScript = new HashMap<>();
            tableToScript.put("PERSON", "CreatePerson");
            tableToScript.put("TAG", "CreateTag");
            tableToScript.put("DECISION", "CreateDecision");
            tableToScript.put("DECISION_DECISIONMAKER", "CreateDecision_DecisionMaker");
            tableToScript.put("DECISION_TAG", "CreateDecision_Tag");
            tableToScript.put("MEETING", "CreateMeeting");
            tableToScript.put("MEETING_ATTENDEE", "CreateMeeting_Attendee");
            tableToScript.put("MEETING_TAG", "CreateMeeting_Tag");

          //  dropAllTables();
            initialiseTables(getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {

        String connectionUrlBase = "jdbc:h2:file:";
        String dbOptions = ";DB_CLOSE_ON_EXIT=TRUE;FILE_LOCK=NO";

        String connectionUrlFull =
                connectionUrlBase
                + getCurrentDirectory() + "\\"
                + dbName
                + dbOptions;

        System.out.println("Connection URL is " + connectionUrlFull);

        return DriverManager.getConnection(connectionUrlFull);
    }

    private static void initialiseTables(Connection conn) {
        List<String> existingTableNames = getExistingTables(conn);

        for(String table: tableToScript.keySet()) {
            boolean tableExists = existingTableNames.contains(table);

            if(!tableExists) {
                if(ApplicationConstants.DEBUG) {
                    System.out.println("Table <" + table + "> does not exist - creating..");
                }
                executeCreateDDL(conn, DatabaseUtils.loadSqlQuery(tableToScript.get(table)));
            } else {
                if (ApplicationConstants.DEBUG) {
                    System.out.println("Table <" + table + "> already exists");
                }
            }


        }
    }

    private static void executeCreateDDL(Connection conn, String sql) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getExistingTables(Connection conn) {

        List<String> result = new ArrayList<>();

        try (
            PreparedStatement stmt = conn.prepareStatement(DatabaseUtils.loadSqlQuery("QueryExistingTables"));
            ResultSet rs = stmt.executeQuery())
        {
            while(rs.next()) {
                result.add(rs.getString(1));
            }
            stmt.close();
            rs.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void debugResultSet(ResultSet rs) throws SQLException {

        StringBuilder result = new StringBuilder();
        List<String> values = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        for(int col = 1; col<=columnCount; col++) {
            values.add(meta.getColumnName(col));
        }

        result.append(values.stream().collect(Collectors.joining(",")));

        while(rs.next()) {
            values = new ArrayList<>();
            for(int col = 1; col<=columnCount; col++) {
                values.add(rs.getString(col));
            }
            result.append("\n" + values.stream().collect(Collectors.joining(",")).replace("\n", ""));
        }

        System.out.println(result);
    }

    public static void main(String[] args) throws SQLException {
        getConnection();
    }

    private static String getCurrentDirectory() {
        return new File("").getAbsolutePath();
    }

    private static void dropAllTables() throws SQLException {
        System.out.println("REMOVING ALL TABLES FROM DB!!");

        Connection conn = getConnection();
        for(String table: tableToScript.keySet()) {

            PreparedStatement stmt = conn.prepareStatement("DROP TABLE " + table);
            stmt.execute();
            stmt.close();
            System.out.println("Dropped table <" + table + ">");
        }
        conn.close();
    }
}
