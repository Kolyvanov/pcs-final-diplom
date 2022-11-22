import com.google.gson.Gson;

import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        System.out.println(new Gson().toJson(engine.search("skills")));

        SearchServer server = new SearchServer(8989, engine);
        server.start();

    }
}