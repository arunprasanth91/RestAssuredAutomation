package jiraAPI;

import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import pojo.course.Trainer;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

public class JiraAPI {

    public static ResponseBody<Response> getSession(String resource) {
        ResponseBody postResponse = given().log().all().headers("Content-Type", ContentType.JSON).body(JiraPayload.getSessionPayload())
                .when().log().all().post(resource).body();
        return postResponse;
    }

    public static ResponseBody<Response> getSessionUsingSessionFilter(String resource, SessionFilter sessionFilter) {
        ResponseBody postResponse = given().log().all().filter(sessionFilter).headers("Content-Type", ContentType.JSON).body(JiraPayload.getSessionPayload())
                .when().log().all().post(resource).body();
        return postResponse;
    }



    public static ResponseBody<Response> createIssue(String resource,String session) {
        ResponseBody postResponse = given().log().all().headers("Content-Type", ContentType.JSON).headers("cookie",session).body(JiraPayload.getIssueBug())
                .when().log().all().post(resource).body();
        return postResponse;
    }


    public static ResponseBody<Response> createComment(String createIssueResource, String session, String payload) {
        ResponseBody postResponse = given().log().all().headers("Content-Type", ContentType.JSON).headers("cookie",session).body(payload)
                .when().log().all().post(createIssueResource).body();
        return postResponse;
    }

    public static ResponseBody<Response> updateComment(String createIssueResource, String session, String payload) {
        ResponseBody postResponse = given().log().all().headers("Content-Type", ContentType.JSON).headers("cookie",session).body(payload)
                .when().log().all().put(createIssueResource).body();
        return postResponse;
    }

    public static ResponseBody<Response> getComment(String getCommentResource, String session, Map<String,String> issueID) {
        ResponseBody postResponse = given().pathParams(issueID).log().all().headers("Content-Type", ContentType.JSON).headers("cookie",session).
        when().log().all().get(getCommentResource).body();
        return postResponse;
    }

    public static ResponseBody<Response> addAttachment(String attachementResource, SessionFilter session, Map<String,String> issueID) {
        ResponseBody postResponse = given().filter(session).pathParams(issueID).header("X-Atlassian-Token","no-check").header("Content-Type", "multipart/form-data")
                .multiPart("file",new File("src/test/resources/jira/file.txt")).when().log().all().post(attachementResource).body();
        return postResponse;
    }

    public static ResponseBody<Response> getAccessToken(String URI) {
        ResponseBody postResponse = given().formParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParam("client_secret","erZOWM9g3UtwNRj340YYaK_W").formParam("grant_type","client_credentials")
                .formParam("scope","trust").when().log().all().post(URI).getBody();
        return postResponse;
    }


    public static ResponseBody<Response> getCourseDetails(String URI, Map<String, String> auth) {
        ResponseBody postResponse = given().queryParams(auth).log().all().header("Content-Type",ContentType.JSON).when().log().all().get(URI).getBody();
        return postResponse;
    }

    public static Trainer getCourseDetailsAsDeseralizedObj(String URI, Map<String, String> auth) {
        return given().queryParams(auth).log().all().header("Content-Type",ContentType.JSON).when().log().all().get(URI).as(Trainer.class);
    }
}
