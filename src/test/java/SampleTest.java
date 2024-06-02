import io.restassured.RestAssured;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class SampleTest {
    private static final String baseURI = "https://rahulshettyacademy.com/";
    private static final String postResource = "maps/api/place/add/json";
    private static final String getResource = "maps/api/place/get/json";
    private static final String putResource = "maps/api/place/update/json";

    private static String place_id;

    @Test (priority = 1)
    private void postCall(){
        RestAssured.baseURI = baseURI;
        Map<String, String> postQueryParams = new HashMap<>();
        postQueryParams.put("key","qaclick123");
        ResponseBody<Response> postResponse = Sample.postCall(postResource,postQueryParams);
        System.out.println(postResponse.asString());
        System.out.println(((RestAssuredResponseImpl) postResponse).then().statusCode(200));
        Assert.assertEquals("application/json;charset=UTF-8",((RestAssuredResponseImpl) postResponse).header("Content-Type"));
        Assert.assertEquals("Apache/2.4.52 (Ubuntu)",((RestAssuredResponseImpl) postResponse).getHeader("Server"));
        JsonPath responseJson = postResponse.jsonPath();
        Assert.assertEquals("OK",responseJson.get("status"));
        place_id = responseJson.get("place_id");
        System.out.println("Place id returned: "+place_id);
    }

    @Test (priority = 2)
    private void getCall(){
        System.out.println("--------------------------------- GET REQUEST ---------------------------------------------");
        Map<String,String> getQueryParams = new HashMap<>();
        getQueryParams.put("key","qaclick123");
        getQueryParams.put("place_id",place_id);
        ResponseBody<Response> getResponse = Sample.getCall(getResource,getQueryParams);
        getResponse.peek().then().statusCode(200);
        JsonPath responseJson = getResponse.jsonPath();
        Assert.assertEquals("http://mikkis.com",responseJson.get("website"));
        Assert.assertEquals("English-IN",responseJson.get("language"));
        Assert.assertEquals("Mikkies house",responseJson.get("name"));
    }

    @Test (priority = 3)
    private void putCall() {
        System.out.println("-------------------------------- PUT REQUEST ----------------------------------");
        Map<String, String> postQueryParams = new HashMap<>();
        postQueryParams.put("key","qaclick123");
        Map<String,String> getQueryParams = new HashMap<>();
        getQueryParams.put("key","qaclick123");
        getQueryParams.put("place_id",place_id);
        ResponseBody<Response> putResponse = Sample.putCall(putResource,postQueryParams,place_id);
        System.out.println(putResponse);
        Assert.assertEquals("Address successfully updated",putResponse.jsonPath().get("msg"));
        System.out.println("--------------------------------- GET REQUEST ---------------------------------------------");
        ResponseBody<Response> getResponse = Sample.getCall(getResource,getQueryParams);
        getResponse.peek().then().statusCode(200);
        JsonPath responseJson = getResponse.jsonPath();
        Assert.assertEquals("70 Summer walk, USA",responseJson.get("address"));
    }


}
