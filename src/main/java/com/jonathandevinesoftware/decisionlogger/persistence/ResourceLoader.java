package com.jonathandevinesoftware.decisionlogger.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ResourceLoader {

    public static String loadResourceFile(String filename) {

        InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(filename);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(loadResourceFile("CreateDecisionMaker.sql"));
    }
}
