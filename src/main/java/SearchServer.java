import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class SearchServer {
    private final int port;
    private final BooleanSearchEngine engine;

    public SearchServer(int port, BooleanSearchEngine engine) {
        this.port = port;
        this.engine = engine;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер стартовал");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {
                    String word = in.readLine();
                    Gson gson = new Gson();
                    List<PageEntry> pageEntries = engine.search(word);
                    out.println(gson.toJson(pageEntries));
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
