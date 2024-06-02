package libraryAPI;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class LibraryAPI {

    public static ResponseBody<Response> postCall(String resource) {
        ResponseBody postResponse = given().log().all().headers("Content-Type", ContentType.JSON).body(LibPayload.generatePostReq())
                .when().log().all().post(resource).body();
        return postResponse;
    }

    public static ResponseBody<Response> getCallAuthorName(String resource, Map<String, String> queryParam) {
        ResponseBody postResponse = given().log().all().headers("Content-Type", ContentType.JSON).queryParams(queryParam)
                .when().log().all().get(resource).body();
        return postResponse;
    }




}
