# com.test.zooplus

## Rest API -  PET API TestCases

You can find the automation package in com.test.zooplus folder.

Please find below the steps to setup this project in any IDE and to run the project in the same

### Basic Requirements
- Java 11 or greater
- any ide with maven
- allure reporting installed(For Mac, installation could be done from brew - brew install allure)

### Project Setup

  1. Clone this repository in your local machine
  2. Open ide and import this project into your workspace as below
        1. File Menu > Import > Projects from Folder or Archive
        2. Click on Directory button in Import wizard and select the path where you have cloned this repository
        3. Click on Finish to import the repository as project in eclipse
  3. Either right click on the project and select **Maven > Update project...** or go to command prompt to this project location and type `mvn clean install`
  
### Run Project

1. To run the test, provide the below command in the terminal
mvn clean test

You will be able to see the testclass is running with all the test cases

### View Reporting

1. Run the command allure serve, which will open the reporting where you can find the test results. 
