import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RandomJoke {
    private static final String API_URL = "https://api.chucknorris.io/jokes/random";

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Chuck Norris Joke Menu");
            System.out.println("1. Get Random Joke");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    displayRandomJoke();
                    break;
                case 2:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
        }
    }

    private static int getUserChoice() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return Integer.parseInt(reader.readLine());
        } catch (NumberFormatException | IOException e) {
            return -1; // Invalid choice
        }
    }

    private static void displayRandomJoke() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response
                String joke = parseJokeFromJson(response.toString());
                System.out.println("Chuck Norris Joke:");
                System.out.println(joke);
            } else {
                System.out.println("Failed to retrieve a joke. Response code: " + responseCode);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while retrieving the joke: " + e.getMessage());
        }
    }

    private static String parseJokeFromJson(String json) {
        // Parse the joke value from the JSON response
        int startIndex = json.indexOf("\"value\"") + 9;
        int endIndex = json.lastIndexOf("\"");
        return json.substring(startIndex, endIndex);
    }
}
