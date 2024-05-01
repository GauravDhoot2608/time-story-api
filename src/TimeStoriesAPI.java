import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeStoriesAPI {

    // Method to get the latest stories from Time.com
    public List<Story> getTimeStories() {
        List<Story> latestStories = new ArrayList<>();

        try {
            // Create URL object
            URL url = new URL("https://time.com");

            // Create HttpURLConnection object
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Send HTTP GET request
            int responseCode = connection.getResponseCode();

            // Check if the response code is successful
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Open input stream to read data from the connection
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                // Read response line by line
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Extract latest stories from HTML content
                latestStories = extractLatestStories(response.toString());
            } else {
                System.out.println("Failed to fetch data. Response code: " + responseCode);
            }

            // Disconnect the connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return latestStories;
    }

    // Method to extract latest stories from HTML content
    private List<Story> extractLatestStories(String htmlContent) {
        List<Story> stories = new ArrayList<>();

        // Regular expression to find story titles and links
        String regex = "<li class=\"latest-stories__item\">\\s*<a href=\"([^\"]+)\">\\s*<h3 class=\"latest-stories__item-headline\">([^<]+)</h3>\\s*</a>\\s*<div class=\"time-to-read\">([^<]+)</div>\\s*<time class=\"latest-stories__item-timestamp\">([^<]+)</time>\\s*</li>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlContent);


        while (matcher.find()) {
            String link = matcher.group(1);
            String title = matcher.group(2);
            stories.add(new Story(title, link));
        }
        return stories;
    }

}
