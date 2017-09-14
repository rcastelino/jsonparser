package com.jsonparser.test;

/**
 * Created by RolandC on 2017-09-08.
 * This class holds methods that aid in printing to console
 */
public class Logger {

    private static String suffix = "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ";
    private static String banner = "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *";
    private static String warning = "#######################";


    public static void logAction(final String msg) {
        System.out.println("  " + msg);
        System.out.println("  " + suffix);
    }

    public static void logComment(final String msg) {
        System.out.println("        -> " + msg);
        System.out.println("  " + suffix);
    }

    public static void logWarning(final String msg) {
        System.out.println(warning);
        System.out.println("### WARNING: " + msg);
        System.out.println(warning);
    }

    public static void logOutput(final String msg) {
        System.out.println("\n" + banner);
        System.out.println("*** Result: " + msg);
        System.out.println(banner);
    }

}
