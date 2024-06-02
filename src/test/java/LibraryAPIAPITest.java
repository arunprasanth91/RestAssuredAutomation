import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import libraryAPI.LibraryAPI;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.reporters.Files;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class LibraryAPIAPITest {
    static final String postResource = "libraryAPI/Addbook.php";
    static final String getResourceAuthor = "/libraryAPI/GetBook.php";


    @Test (enabled = false)
    public static void postCall() {
        RestAssured.baseURI = "http://216.10.245.166";
        ResponseBody<Response> postResponse = LibraryAPI.postCall(postResource);
        System.out.println(postResponse);
        try{
            String content = Files.readFile(new FileInputStream("src/test/resources/library/post.json"));
            JsonPath jsContent = new JsonPath(content);
            Assert.assertEquals(jsContent.get("isbn")+jsContent.get("aisle").toString(),postResponse.jsonPath().get("ID"));
            Assert.assertEquals("successfully added",postResponse.jsonPath().get("Msg"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public static void getCallAuthorName(){
        RestAssured.baseURI = "http://216.10.245.166";
        Map<String,String> author = new HashMap<>();
        author.put("AuthorName","author5");
        ResponseBody<Response> getResponse = LibraryAPI.getCallAuthorName(getResourceAuthor,author);
        Gson gson = new Gson();
        String response = getResponse.jsonPath().get().toString();
//        Type listType = new TypeToken<List<Books>>() {}.getType();
//        List<Books> booksList = gson.fromJson(response.replaceAll(":",""), listType);
        // we can parse string to list and evaluate the response.
        System.out.println(response);
    }
}
