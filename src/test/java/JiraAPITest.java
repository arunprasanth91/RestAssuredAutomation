import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import jiraAPI.JiraAPI;
import jiraAPI.JiraPayload;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojo.CreateComment;
import pojo.CreateIssue;
import pojo.course.Courses;
import pojo.course.Trainer;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class JiraAPITest {
    static final String sessionResource="rest/auth/1/session";
    static final String createIssueResource="rest/api/2/issue";
    static final String createCommentResource = "/rest/api/2/issue/{}/comment";
    static String updateCommentResource = "rest/api/2/issue/{}/comment/{id}";
    static final String getCommentResource = "/rest/api/2/issue/{issueIdOrKey}/comment";
    static final String attachementResource = "/rest/api/2/issue/{issueIdOrKey}/attachments";
    static String session;
    static CreateIssue createIssue;
    static CreateComment createComment;
    static SessionFilter sessionFilter;
    static String access_token;



    @Test (priority = 1, enabled = false)
    public static void testSessionID(){
        RestAssured.baseURI = "http://localhost:8080/";
        ResponseBody<Response> postResponse = JiraAPI.getSession(sessionResource);
        JsonPath jsonResponse = postResponse.jsonPath();
        session = jsonResponse.get("session.name")+"="+jsonResponse.get("session.value");
        // Instead of above line - we can use SessionFilter and same session obj can be used in subsequent calls.
//        SessionFilter session = new SessionFilter();
//        given().log().all().headers("Content-Type", ContentType.JSON).body(JiraPayload.getSessionPayload())
//                .when().log().all().filter(session).post(resource).body();

        System.out.println(session);
    }

    @Test (priority = 2, enabled = false)
    public static void testCreateIssue(){
        RestAssured.baseURI = "http://localhost:8080/";
        ResponseBody<Response> postResponse = JiraAPI.createIssue(createIssueResource,session);
        String id = postResponse.jsonPath().get("id").toString();
        String key = postResponse.jsonPath().get("key").toString();
        String self = postResponse.jsonPath().get("self").toString();
        System.out.println("Issue created : "+"\n"+
        "ID --> "+id +"\n" +
        "KEY --> "+key +"\n"+
        "URL -->"+self);
        createIssue = new CreateIssue(id,key,self);
    }

    @Test (priority = 3, dataProvider = "commentsData", enabled = false)
    public static void testAddComment(JSONObject jsonObject){
        RestAssured.baseURI = "http://localhost:8080/";
//        JSONObject commentPayload = new JSONObject();
//        commentPayload.put("body","Commented out from the API CODE");
//        Map<String,String> visiblity = Map.of("type","role","value","Administrators");
//        commentPayload.put("Visibility", visiblity);
        ResponseBody<Response> postResponse = JiraAPI.createComment(createCommentResource.replace("{}",createIssue.getKey()),session,jsonObject.toString());
        System.out.println(postResponse);
    }

    @DataProvider(name = "commentsData")
    public static Object[][] commentsData() {
        JSONObject commentPayload = new JSONObject();
        commentPayload.put("body","Commented out from the API CODE");
        Map<String,String> visiblity = Map.of("type","role","value","Administrators");
        commentPayload.put("Visibility", visiblity);
        JSONObject commentPayload1 = new JSONObject();
        commentPayload.put("body","Commented out from the API CODE");
        commentPayload.put("Visibility", visiblity);
        JSONObject commentPayload2 = new JSONObject();
        commentPayload.put("body","Commented out from the API CODE");
        commentPayload.put("Visibility", visiblity);
        return new Object[][]{new JSONObject[]{commentPayload}, new JSONObject[]{commentPayload1}, new JSONObject[]{commentPayload2}};
    }

    @Test (priority = 4, enabled = false)
    public static void testGetComment(){
        RestAssured.baseURI = "http://localhost:8080/";
        Map<String,String> issueID = new HashMap<>();
        issueID.put("issueIdOrKey",createIssue.getKey());
        ResponseBody<Response> getResponse = JiraAPI.getComment(getCommentResource,session,issueID);
        createComment = new CreateComment(getResponse.jsonPath().get("comments.id[0]"));
        System.out.println("Comment ID --> "+createComment.getCommentId());
    }

    @Test(priority = 5, enabled = false)
    public static void testUpdateComment(){
        RestAssured.baseURI = "http://localhost:8080/";
        JSONObject commentPayload = new JSONObject();
        commentPayload.put("body"," Updated comment from the API CODE");
        Map<String,String> visiblity = Map.of("type","role","value","Administrators");
        commentPayload.put("Visibility", visiblity);
        updateCommentResource=updateCommentResource.replace("{}",createIssue.getKey());
        updateCommentResource = updateCommentResource.replace("{id}",createComment.getCommentId());
        ResponseBody<Response> postResponse = JiraAPI.updateComment(updateCommentResource,session,commentPayload.toString());
        System.out.println("Updated comment: "+postResponse.jsonPath().get("body"));
        System.out.println("Updated comment: "+postResponse.jsonPath().get("updated"));
    }

    @Test (enabled = false)
    public static void getSessionUsingSessionFilter(){
        RestAssured.baseURI = "http://localhost:8080/";
        sessionFilter = new SessionFilter();
        ResponseBody<Response> postResponse = JiraAPI.getSessionUsingSessionFilter(sessionResource,sessionFilter);
        JsonPath jsonResponse = postResponse.jsonPath();
        System.out.println(jsonResponse);
    }

    @Test(dependsOnMethods = "getSessionUsingSessionFilter", enabled = false)
    public static void testAddAttachement(){
        RestAssured.baseURI = "http://localhost:8080/";
        Map<String,String> params = new HashMap<>();
        params.put("issueIdOrKey","APD-6");
        ResponseBody<Response> postResponse = JiraAPI.addAttachment(attachementResource,sessionFilter,params);
        System.out.println(postResponse);


    }

    @Test
    public static void testgetAccessToken(){
        RestAssured.baseURI = "https://rahulshettyacademy.com/";
        String resource = "oauthapi/oauth2/resourceOwner/token";
        ResponseBody<Response> postResponse = JiraAPI.getAccessToken(RestAssured.baseURI+resource);
        access_token = postResponse.jsonPath().get("access_token").toString();
        System.out.println(access_token);
        System.out.println(postResponse.jsonPath().get("token_type").toString());

    }

    @Test (dependsOnMethods = "testgetAccessToken")
    public static void testCourseDetailsResponse(){
        RestAssured.baseURI = "https://rahulshettyacademy.com/";
        String resource = "oauthapi/getCourseDetails";
        Map<String,String> auth = new HashMap<>();
        auth.put("access_token",access_token);
        Trainer trainer = JiraAPI.getCourseDetailsAsDeseralizedObj(RestAssured.baseURI+resource,auth);
        // print all webautomations
        trainer.getCourses().getWebAutomation().stream().forEach(web -> System.out.println("Course --> "+web.getCourseTitle()));
        // get price of SoapUI
        System.out.println("SOAPUI PRICE -->"+ trainer.getCourses().getApi().stream().filter(api -> api.getCourseTitle().contains("SoapUI")).findFirst().get().getPrice());

    }

}
