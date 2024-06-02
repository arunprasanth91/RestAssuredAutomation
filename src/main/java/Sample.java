import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.json.JSONObject;

import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class Sample {



    static ResponseBody<Response> postCall(String resource, Map<String,String> queryParams) {
        ResponseBody postResponse = given().log().all().headers("Content-Type", ContentType.JSON).queryParams(queryParams).body(Payload.getPostPayload())
                .when().log().all().post(resource).body();
        return postResponse;
    }

    static ResponseBody<Response> getCall(String resource, Map<String,String> queryParams) {
        ResponseBody getResponse = given().log().all().queryParams(queryParams).when().log().all().get(resource).body();
        return getResponse;
    }

    static ResponseBody<Response> putCall(String resource, Map<String,String> queryParams, String place_id){
        JSONObject updateResponse = new JSONObject(Payload.getUpdatePayload());
        updateResponse.put("place_id",place_id);
        ResponseBody postResponse = given().log().all().headers("Content-Type", ContentType.JSON).queryParams(queryParams).body(updateResponse.toString())
                .when().log().all().put(resource).body();
        return postResponse;
    }
}
