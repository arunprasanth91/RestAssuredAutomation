import org.json.JSONObject;
import org.testng.reporters.Files;

import java.io.*;
import java.util.List;
import java.util.Random;

public class Payload {

    public static String getPostPayload(){
        try {
            return Files.readFile(new FileInputStream("src/test/resources/post.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUpdatePayload(){
        try {
            return Files.readFile(new FileInputStream("src/test/resources/update.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
