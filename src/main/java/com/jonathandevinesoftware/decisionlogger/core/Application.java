package com.jonathandevinesoftware.decisionlogger.core;

public class Application {

    public static void debug(Object o) {
        if(ApplicationConstants.DEBUG) {
            System.out.println(o);
        }
    }

    public static void log(Exception o) {
        System.out.println(o);
        o.printStackTrace();
    }

    public static void log(Object o) {
        System.out.println(o);
    }
}
