package com.jsonparser.test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by RolandC on 2017-09-08.
 * This class is responsible for reading JSON file and parsing it to get necessary data
 */
public class JsonReader {

    private static JSONObject testSuiteJson;
    private static String TEST_SUITE_FILENAME = "/test_suite.json";


    public static JSONObject getTestSuiteJsonObject() {
        return testSuiteJson;
    }

    public static String getTestSuiteFilename() {
        return TEST_SUITE_FILENAME;
    }

    /**
     * Read the test suite json file, and sets the json object representing test suite file
     * @throws Exception
     */
    public static void loadTestSuiteFile() throws Exception {
        JSONParser parser = new JSONParser();
        InputStream stream = JsonReader.class.getResourceAsStream(TEST_SUITE_FILENAME);

        if (stream == null) {
            throw new NullPointerException(String.format("Test suite json file not found: %s", TEST_SUITE_FILENAME));
        }

        //not required but additional check in case we mess up the json file
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder strBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            if (inputStr.trim().startsWith("//")) {
                // Skip lines that were comments within the test config JSON file
                continue;
            }

            strBuilder.append(inputStr);
        }

        testSuiteJson = (JSONObject) parser.parse(strBuilder.toString());
        //System.out.println("Debug: - testSuiteJson: " + testSuiteJson);
    }


    /**
     * Helper method to get the JSONArray under 'test_suites'
     * @return JSONArray of 'suite_name' and 'results'
     * @throws Exception
     */
    public static JSONArray getTestSuites() throws Exception {
        JSONArray testSuites = (JSONArray) getTestSuiteJsonObject().get("test_suites");
        return testSuites;
    }

    /**
     * Helper method to get JSONObject based on test suite index
     * @param suiteIndex - index
     * @return JSONObject for the specified index
     * @throws Exception
     */
    public static JSONObject getTestSuiteByIndex(int suiteIndex) throws Exception {

        JSONArray testSuite = getTestSuites();
        int testSuiteSize = testSuite.size();
        if (suiteIndex >= testSuiteSize) {
            throw new IndexOutOfBoundsException(String.format("There are only %d test suites and we requested for suite index %d, please choose an index from 0 to %d", testSuiteSize, suiteIndex, testSuiteSize-1));
        }

        return (JSONObject) testSuite.get(suiteIndex);
    }


    /**
     * Get the test suite names in alphabetical order
     * @return Alphabetically Sorted String array
     * @throws Exception
     */
    public static String[] getTestSuiteNames() throws Exception {

        JSONArray testSuites = getTestSuites();

        JSONObject json = null;
        String[] testSuiteNames = new String[testSuites.size()];

        //getting suite names from Json
        for (int i = 0; i < testSuites.size(); i++) {
            json = getTestSuiteByIndex(i);
            testSuiteNames[i] = json.get("suite_name").toString();
        }
        //sorting suite names alphabetically
        Arrays.sort(testSuiteNames);
        return testSuiteNames;
    }

    /**
     * To get all tests that passed
     * @return - HashMap of suite name and list of JSONObject's of pass tests
     * @throws Exception
     */
    public static HashMap<String, List<JSONObject>> getAllTests(Result desiredResult) throws Exception {
        return getSortedTestResultsHashMap(desiredResult);
    }

    /**
     * To get all tests greater than specified duration
     * @param duration - Execution time
     * @return - HashMap of suite name and list of JSONObject's greater than duration
     * @throws Exception
     */
    public static HashMap<String, List<JSONObject>> getAllTestsGreaterThanDuration(Double duration) throws Exception {
        return getAllTestsBasedOnExecutionTime(TimeTaken.GREATER, duration);
    }


    /**
     *
     * @param operation enum TimeTaken value (greater, lesser, equal, greater than and equal, less than and equal)
     * @param duration time taken by test case to execute as type double.
     * @return HashMap of suite name and list of JSONObject's based on desired duration operations
     * @throws Exception
     */
    private static HashMap<String, List<JSONObject>> getAllTestsBasedOnExecutionTime(TimeTaken operation, Double duration) throws Exception {
        JSONArray testSuites = getTestSuites();
        JSONObject json ;
        JSONArray jsonResults;

        // HashMap that holds information of testSuite along with test results
        HashMap<String, List<JSONObject>> testListMap = new HashMap<String, List<JSONObject>>();

        // Outer loop to Iterate the results array
        // System.out.println("Debug - testSuites.size(): " + testSuites.size());
        for (int i = 0; i < testSuites.size(); i++) {
            List<JSONObject> testDurationList = new ArrayList<JSONObject>();

            json = getTestSuiteByIndex(i);
            jsonResults = (JSONArray) json.get("results");

            //System.out.println("Debug - jsonResults: " + jsonResults);

            // Inner loop to Iterate individual results to get pass tests
            for (int j = 0; j < jsonResults.size(); j++) {
                JSONObject singleTest = (JSONObject) jsonResults.get(j);

                //System.out.println("Debug - singleTest: " + singleTest);

                //Capture tests that ran more than duration
                String timeValue =  singleTest.get("time").toString();

                //Ignoring tests that do not have time, those will be blocked tests.
                if (timeValue.equals("")) {
                    continue;
                }

                //Capture tests and their time duration based on desired comparison
                if (operation.perform(Double.parseDouble(timeValue), duration)) {
                    testDurationList.add((JSONObject) jsonResults.get(j));
                    testListMap.put(json.get("suite_name").toString(), testDurationList);
                }
            }

            //This call is to sort the pass list based on
            sortedAlphabeticallyList(testDurationList);
        }
        return testListMap;
    }

    /**
     * Helper method to get HashMap of suite name and list of JSONObjects based on test results.
     * @param desiredResult - enum for Result - pass / fail / blocked
     * @return HashMap of suite name and list of JSONObject's
     * @throws Exception
     */
    private static HashMap<String, List<JSONObject>> getSortedTestResultsHashMap(Result desiredResult) throws Exception {

        JSONArray testSuites = getTestSuites();
        JSONObject json ;
        JSONArray jsonResults;

        // HashMap that holds information of testSuite along with test results
        HashMap<String, List<JSONObject>> passListMap = new HashMap<String, List<JSONObject>>();

        // Outer loop to Iterate the results array
        // System.out.println("Debug - testSuites.size(): " + testSuites.size());
        for (int i = 0; i < testSuites.size(); i++) {
            List<JSONObject> passList = new ArrayList<JSONObject>();

            json = getTestSuiteByIndex(i);
            jsonResults = (JSONArray) json.get("results");

            //System.out.println("Debug - jsonResults: " + jsonResults);

            // Inner loop to Iterate individual results to get pass tests
            for (int j = 0; j < jsonResults.size(); j++) {
                JSONObject singleTest = (JSONObject) jsonResults.get(j);

                //System.out.println("Debug - singleTest: " + singleTest);

                //Capture tests based on results
                if (singleTest.get("status").equals(desiredResult.toString())) {
                    passList.add((JSONObject) jsonResults.get(j));
                    passListMap.put(json.get("suite_name").toString(), passList);
                }
            }

            //This call is to sort the pass list based on
            sortedAlphabeticallyList(passList);
        }

        return passListMap;
    }

    /**
     * Helper method to sort a list of JSONObjects alphabetically
     * @param testList - list we would like to sort
     * @return - alphabetically sorted list of JSONObjects
     * @throws Exception
     */
    private static List<JSONObject> sortedAlphabeticallyList(List<JSONObject> testList) throws Exception {

        //Here we sort the passList based on test name
        Collections.sort(testList, new Comparator<JSONObject>() {

            public int compare(JSONObject obj1, JSONObject obj2) {
                String value1 = new String();
                String value2 = new String();
                try {
                    value1 = (String) obj1.get("test_name");
                    value2 = (String) obj2.get("test_name");
                } catch (Exception exp) {
                    Logger.logComment(String.format("Caught an Exception while trying to sort in ascending order : %s", exp.getMessage()));
                }
                return value1.compareTo(value2);
            }
        });

        return testList;
    }


    /**
     * Enum to specify Time Comparison, used to get test suites based on operation.
     */
    public enum TimeTaken {
        GREATER (">") {
            @Override
            public boolean perform (double fromJson, double desiredTime) {
                return fromJson > desiredTime;
            }
        },
        GREATER_AND_EQUAL (">=") {
            @Override
            public boolean perform (double fromJson, double desiredTime) {
                return fromJson >= desiredTime;
            }
        },
        LESSER ("<") {
            @Override
            public boolean perform (double fromJson, double desiredTime) {
                return fromJson < desiredTime;
            }
        },
        LESSER_AND_EQUAL ("<=") {
            @Override
            public boolean perform (double fromJson, double desiredTime) {
                return fromJson <= desiredTime;
            }
        },
        EQUAL ("==") {
            @Override
            public boolean perform (double fromJson, double desiredTime) {
                return fromJson == desiredTime;
            }
        };

        private String operator;

        TimeTaken(String operator) {
            this.operator = operator;
        }

        // declaring the override function
        public abstract boolean perform(double fromJson, double desiredTime);

        @Override
        public String toString() {
            return operator;
        }
    }

    /**
     * Enum for results of test case
     */
    public enum Result {
        PASS ("pass"),
        FAIL ("fail"),
        BLOCKED ("blocked");

        private String result;

        Result(String result) { this.result = result; }

        @Override
        public String toString() { return result; }
    }

}
