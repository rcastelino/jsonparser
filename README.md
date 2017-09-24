================================================================================================
# Test 1
================================================================================================
Write a working program code that will take the test_results.json file as an input and provide:
1) For each test suite:
	- Test suite name
	- Print out the total number of tests that passed and their details
	- Print out the total number of tests that failed and their details
	- Print out the total number of test that are blocked
	- Print out the total number of test that took more than 10 seconds to execute
2) Proper treatment for common error conditions
3) All the detail lists need to be printed in ascending order
4) Speed is first priority, memory is secondary
5) Java or Python
================================================================================================


[] To Get this project running on an OSX machine, install the following:
- java jdk 8
- maven
- IntelliJ IDE (community edition) (Any other Java IDE like eclipse should also be fine, but I haven't tried it)


[] After installation:
- launch IntelliJ IDE
- select 'File -> Open'
- select the 'pom.xml' file of this project.'


[] Project layout:
- main() method is in class PrintTestResults.java:
- PrintTestResults.java has methods to print output to console
- JsonReader.java parses the test_suite.json file
- test_suite.json file is under /src/main/resources folder
- the console output is available to view in 'sampleConsoleLogtxt'


[] To execute the test:
- From IntelliJ IDE
    - from 'Project' panel, expand the 'com.jsonparser.test' package
    - right click on the class PrintTestResults.java and Run
    OR
    - open class PrintTestResults.java and run from toolbar

