import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create HTTP server listening on localhost:8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Context for handling requests
        server.createContext("/getTimeStories", exchange -> {
            // Set response headers
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);

            // Get response body
            OutputStream responseBody = exchange.getResponseBody();
            String jsonResponse = getTimeStoriesJSON(); // Call your API method here to get JSON response

            // Write response body
            responseBody.write(jsonResponse.getBytes());
            responseBody.close();
        });

        // Start the server
        server.start();

        System.out.println("Server is running on http://localhost:8000");
    }

    // Method to get the JSON response from TimeStoriesAPI
    private static String getTimeStoriesJSON() {
        TimeStoriesAPI api = new TimeStoriesAPI();
        List<Story> stories = api.getTimeStories();

        StringBuilder json = new StringBuilder("[");
        for (Story story : stories) {
            json.append("{");
            json.append("\"title\": \"" + story.getTitle() + "\",");
            json.append("\"link\": \"" + story.getLink() + "\"");
            json.append("},");
        }
        if (!stories.isEmpty()) {
            json.deleteCharAt(json.lastIndexOf(","));
        }
        json.append("]");

        return json.toString();
    }
}