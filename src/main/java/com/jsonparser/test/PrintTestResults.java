package com.jsonparser.test;

import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by RolandC on 2017-09-08.
 * This class holds the main method, and defines all methods related to printing output to console
 */

/**
 * Test 1
 Write a working program code that will take the test_results.json file as an input and provide:
 1) For each test suite:
 Test suite name
 Print out the total number of tests that passed and their details
 Print out the total number of tests that failed and their details
 Print out the total number of test that are blocked
 Print out the total number of test that took more than 10 seconds to execute
 2) Proper treatment for common error conditions
 3) All the detail lists need to be printed in ascending order
 4) Speed is first priority, memory is secondary
 5) Java or Python
 */
public class PrintTestResults {

    //main method
    public static void main(String[]args) throws Exception {
        JsonReader.loadTestSuiteFile();

        //Test suite name
        printAllTestSuiteNames();

        //Print out the total number of tests that passed and their details
        printAllPassTestDetails();

        //Print out the total number of tests that failed and their details
        printAllFailTestDetails();

        //Print out the total number of tests that are blocked and their details
        printAllBlockedTestDetails();

        //Print out the total number of test that took more than 10 seconds to execute
        printAllTestsGreaterThanDuration(10.0);
    }


    /**
     * Prints test suite name
     * @throws Exception
     */
    private static void printAllTestSuiteNames() throws Exception {
        String[] suiteNames = JsonReader.getTestSuiteNames();

        //Checking if test suites are available
        if (suiteNames.length != 0) {
            Logger.logOutput("Test suite names are");
            for (String suite : suiteNames) {
                Logger.logComment(suite);
            }
        } else {
            Logger.logWarning(String.format("There are no test suite's in json file %s", JsonReader.getTestSuiteFilename()));
        }
    }

    /**
     * Prints Pass Test details (test name and duration)
     * @throws Exception
     */
    private static void printAllPassTestDetails() throws Exception {
        HashMap<String, List<JSONObject>> passTests = JsonReader.getAllTestsPassed();

        String testName;
        String testExecutionTime;

        //Check if any suite with pass tests available
        if (!passTests.keySet().isEmpty()) {
            Logger.logOutput("PASS List --> Total number of tests that passed and their details");

            //for each suite
            for (String suite : passTests.keySet()) {
                Logger.logAction("Test Suite --> " + suite);

                //get pass results and time duration
                for (int i = 0; i < passTests.get(suite).size(); i++) {
                    testName = passTests.get(suite).get(i).get("test_name").toString();
                    testExecutionTime = passTests.get(suite).get(i).get("time").toString();
                    Logger.logAction(String.format("%d) %s      -        %s", i+1, testName, testExecutionTime));
                }
            }
        } else {
            Logger.logWarning(String.format("There are no PASS tests in json file %s", JsonReader.getTestSuiteFilename()));
        }
    }


    /**
     * Prints Fail Test details (test name and duration)
     * @throws Exception
     */
    private static void printAllFailTestDetails() throws Exception {
        HashMap<String, List<JSONObject>> failTests = JsonReader.getAllTestsFailed();

        String testName;
        String testExecutionTime;

        //Check if any suite with failed tests available
        if (!failTests.keySet().isEmpty()) {
            Logger.logOutput("FAIL List --> Total number of tests that failed and their details");

            //for each suite
            for (String suite : failTests.keySet()) {
                Logger.logAction("Test Suite --> " + suite);

                //get fail tests and time duration
                for (int i = 0; i < failTests.get(suite).size(); i++) {
                    testName = failTests.get(suite).get(i).get("test_name").toString();
                    testExecutionTime = failTests.get(suite).get(i).get("time").toString();
                    Logger.logAction(String.format("%d) %s      -        %s", i+1, testName, testExecutionTime));
                }
            }
        } else {
            Logger.logWarning(String.format("There are no FAIL tests in json file %s", JsonReader.getTestSuiteFilename()));
        }
    }

    /**
     * Prints Blocked Test details (test name and duration)
     * @throws Exception
     */
    private static void printAllBlockedTestDetails() throws Exception {
        HashMap<String, List<JSONObject>> failTests = JsonReader.getAllTestsBlocked();

        String testName;
        String testExecutionTime;

        //Check if any suite with blocked tests available
        if (!failTests.keySet().isEmpty()) {
            Logger.logOutput("BLOCKED list --> Total number of tests that blocked and their details");

            //for each suite
            for (String suite : failTests.keySet()) {
                Logger.logAction("Test Suite --> " + suite);

                //get fail tests and time duration
                for (int i = 0; i < failTests.get(suite).size(); i++) {
                    testName = failTests.get(suite).get(i).get("test_name").toString();
                    testExecutionTime = failTests.get(suite).get(i).get("time").toString();
                    Logger.logAction(String.format("%d) %s      -        %s", i+1, testName, testExecutionTime));
                }
            }
            Logger.logComment(" Note - Blocked tests did not execute and hence should not have any duration");
        } else {
            Logger.logWarning(String.format("There are no FAIL tests in json file %s", JsonReader.getTestSuiteFilename()));
        }
    }

    /**
     * Prints Blocked Test details (test name and duration)
     * @throws Exception
     */
    private static void printAllTestsGreaterThanDuration(Double executionTime) throws Exception {
        HashMap<String, List<JSONObject>> timedTests = JsonReader.getAllTestsGreaterThanDuration(executionTime);

        String testName;
        String testExecutionTime;

        //Check if any suite available with duration greater than expected
        if (!timedTests.keySet().isEmpty()) {
            Logger.logOutput(String.format("Execution Time GREATER than %1$,.2f seconds",executionTime));

            //for each suite
            for (String suite : timedTests.keySet()) {
                Logger.logAction("Test Suite --> " + suite);

                ////get fail tests and time duration
                for (int i = 0; i < timedTests.get(suite).size(); i++) {
                    testName = timedTests.get(suite).get(i).get("test_name").toString();
                    testExecutionTime = timedTests.get(suite).get(i).get("time").toString();
                    Logger.logAction(String.format("%d) %s      -        %s", i+1, testName, testExecutionTime));
                }
            }
        } else {
            Logger.logWarning(String.format("There are no tests that ran greater than %1$,.2f in json file %s", executionTime, JsonReader.getTestSuiteFilename()));
        }
    }
}
