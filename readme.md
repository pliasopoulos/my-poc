# How to run it

## Command line

```java
mvn test -Denv="prod" -DbrowserName="chrome" -DbrowserMode="local" -DsuiteXml="FullRegressionSuite.xml"
```

## IntelliJ
Edit Configurations > Create a new config for a TestNG test, give it a name and select the TestKind dropbox --> Suite. Then point to the suiteXml you wish to run.

###### env vars

* -Denv (dev, qa, prod)
--> Relevant tests from any TestSuite will be automatically picked up according to env.

* -Dbrowser (chrome, firefox, openfin)
--> Opting for **chrome** will run a local version on your machine so you can see first hand what the script is actually doing.

* -DbrowserMode (local, headless, grid)
If you choose **local** then all tests will be run using chromedriver with GUI support; so you are able to see what the script it doing in the FE.

* -DsuiteXml (any testng xml file)

# Reporting

## Test Results
In the build artifacts you will find at root a test_results.html file.

## Screenshots
The reporting/screenshots artifact folder contains all screenshots

## Logs
You will find the regression suite consolidated log in the reporting/logs artifact folder.


