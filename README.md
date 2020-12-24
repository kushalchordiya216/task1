# Introduction
This is a spring boot application for a simple image upload API that can accept a JPG/PNG image under 500KB and store it to AWS S3 bucket.
It also stores metadata regarding the image onto an RDS database.

# Requirements
To run this code you must have the following requirements satisfied
- Java (oracleJDK11 or open-jdk-11)
- Gradle (version 6.6.1 or higher)
- AWS IAM credentials set up on your local machine (inside ~/.aws folder on linux for eg.)
- SonarQube server and SonarServer

# Running the Code
Complete the following steps to run the code
- cd into the directory and execute 

 ```./gradlew build```

this will download all the dependencies and build the application, including tests

- in the application.properties file change the value of variables aws.bucketName and spring.datasource.url to your respective storage bucket name and database endpoint
- Set the following environment variable in your respective shells 
  
Bash (On linux/mac)
```
    export SPRING_DATASOURCE_USERNAME=${YOUR DB USERNAME}
    export SPRING_DATASOURCE_PASSWORD=${YOUR DB PASSWORD}
```
CMD/Powershell (On windows)
```
    set SPRING_DATASOURCE_USERNAME=${YOUR DB USERNAME}
    set SPRING_DATASOURCE_PASSWORD=${YOUR DB PASSWORD}
```
- If you do not have AWS credentials set up by default, you need to export those as environment variables as well

Bash (On linux/mac) 
```
    export AWS_ACCESS_KEY_ID=${your access key}
    export AWS_SECRET_ACCESS_KEY=${your secret access key}
```
CMD/Powershell (On windows)
```
    set AWS_ACCESS_KEY_ID=${your access key}
    set AWS_SECRET_ACCESS_KEY=${your secret access key}
```

- if the tests have completed, run

```./gradlew sonarqube```

to get an analysis of the code on http://locahost:9000/. (This assumes you have the sonarqube server running)
- To run the application, execute 

```./gradlew bootRun```

The application by default runs on port 8080. 
If that port is busy,set the server.port property in application.properties to what ever port is available.

You can now test the application on http://localost:8080/api/images