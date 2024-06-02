package libraryAPI;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class LibPayload {

    public static String generatePostReq(){
        String[] bookTitles = new String[] {"Do Androids Dream of Electric Sheep?","The Long Way to a Small, Angry Planet","Cloud Cuckoo Land","One Hundred Years of Solitude",
                "Perfume: The Story of a Murderer"};
        String[] authors = new String[] {"author1","author2","author3","author4","author5","author6"};
        var letters = List.of("a","b","c","d","e","f","g","h","i");
        String isbn = letters.get(new Random().nextInt(9)) + letters.get(new Random().nextInt(9)) + letters.get(new Random().nextInt(9)) + letters.get(new Random().nextInt(9));
        int aisle = new Random().nextInt(100000);
        JSONObject postReq = new JSONObject();
        postReq.put("name",bookTitles[new Random().nextInt(5)]);
        postReq.put("isbn",isbn);
        postReq.put("aisle",aisle);
        postReq.put("author",authors[new Random().nextInt(5)]);
        try {
            var postPayload = new FileWriter("src/test/resources/library/post.json");
            postPayload.write(postReq.toString());
            postPayload.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return postReq.toString();
    }
}
