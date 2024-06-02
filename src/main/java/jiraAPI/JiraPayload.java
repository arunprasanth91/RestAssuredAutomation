package jiraAPI;

import org.testng.IConfigurable;
import org.testng.reporters.Files;

import java.io.FileInputStream;
import java.io.IOException;

public class JiraPayload {

    static String getSessionPayload(){
        try{
           return Files.readFile(new FileInputStream("src/test/resources/jira/session.json"));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String getIssueBug(){
        try{
            return Files.readFile(new FileInputStream("src/test/resources/jira/issue-bug.json"));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
