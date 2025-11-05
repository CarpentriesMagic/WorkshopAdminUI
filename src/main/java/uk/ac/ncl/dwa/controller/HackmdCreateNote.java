package uk.ac.ncl.dwa.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import uk.ac.ncl.dwa.controller.github.GitHubActions;

import static java.lang.System.exit;

public class HackmdCreateNote {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(HackmdCreateNote.class.getName());

    public static String getToken() {
        Scanner sc;
        String token = "";
        try {
            sc = new Scanner(new File(System.getProperty("user.home") + "/.ssh/hmd_token"));
            token = sc.nextLine();
            sc.close();
        } catch ( FileNotFoundException e) {
            logger.info("FileNotFoundException: {}", e.getMessage());
        }
        return token;
    }

    public static String getTemplate(String templateName, String slug) {
        templateName = "HackMD_Templates/" + templateName + ".md";
        StringBuilder sb = new StringBuilder();
        try {
            Scanner sc = new Scanner(new File(templateName));
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return sb.toString().replace("[slug]", slug);
    }

    public static void main(String[] args) throws Exception {
    }

    public static int createDoc(String templateName, String slug) {
        final String API_TOKEN = getToken();
        final String API_URL = "https://api.hackmd.io/v1/notes";
        if (API_TOKEN == null || API_TOKEN.isBlank()) {
            System.err.println("❌ Please create a file containing your HackMD API token in ~/.ssh/hmd_token.");
            return 1;
        }
        // Prepare note data as a Map
        Map<String, Object> noteData = Map.of(

                "title", slug,
                "content", getTemplate(templateName, slug),
                "readPermission", "guest",     // options: owner, signed_in, guest
                "writePermission", "guest",    // same or stricter than readPermission
                "commentPermission", "everyone", // options: everyone, signed_in, owner
                "permalink", slug
        );

        Gson gson = new Gson();
        String requestBody = gson.toJson(noteData);
        System.out.println(requestBody);
        // Build HTTP client and request
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofSeconds(20))
                .header("Authorization", "Bearer " + API_TOKEN)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send the request
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            String responseBody = response.body();

            if (status == 201 || status == 200) {
                logger.info("✅ Note created successfully!");

                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

                String id = jsonResponse.has("id") ? jsonResponse.get("id").getAsString() : "(none)";
                String shortId = jsonResponse.has("shortId") ? jsonResponse.get("shortId").getAsString() : "(none)";
                String title = jsonResponse.has("title") ? jsonResponse.get("title").getAsString() : "(none)";
                String publishLink = jsonResponse.has("publishLink") ? jsonResponse.get("publishLink").getAsString() : "(none)";

                System.out.printf("ID: %s%nShort ID: %s%nTitle: %s%nPublish link: %s%n", id, shortId, title, publishLink);
            } else {
                logger.error("❌ Failed to create note. HTTP status: " + status);
                logger.error("Response body: " + responseBody);
                return 2;
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
            return 3;
        }
        return 0;
    }
}
