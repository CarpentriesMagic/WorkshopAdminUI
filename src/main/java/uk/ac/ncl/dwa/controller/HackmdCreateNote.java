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
import uk.ac.ncl.dwa.database.DBHandler;

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

    /**
     * Use the templateName specified as a filename and read the contents of the file
     * to be used as a template for creating a HackMD document
     * @param templateName
     * @param slug
     * @return
     */
    public static String getTemplate(String templateName, String slug) {
        templateName = "HackMD_Templates/" + templateName + ".md";
        StringBuilder sb = new StringBuilder();
        try {
            Scanner sc = new Scanner(new File(templateName));
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        return sb.toString().replace("[slug]", slug);
    }

    public static int deleteDoc(String slug) {
        final String API_TOKEN = getToken();
        String organisation = DBHandler.getInstance().selectString("settings",
                "value", "keyValue=\"HackMD_team\"");
        String folder_id = DBHandler.getInstance().selectString("settings",
                "value", "keyValue=\"HackMD_folder_id\"");
        final String API_URL = "https://api.hackmd.io/v1/teams/" + organisation + "/notes/" + slug;
        logger.info("API_url: {}", API_URL);
        if (API_TOKEN == null || API_TOKEN.isBlank()) {
            System.err.println("❌ Please create a file containing your HackMD API token in ~/.ssh/hmd_token.");
            return 1;
        }

        Gson gson = new Gson();
        String requestBody = "";
        System.out.println(requestBody);        // Build HTTP client and request
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofSeconds(20))
                .header("Authorization", "Bearer " + API_TOKEN)
                .DELETE()
                .build();


        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();

            if (status == 201 || status == 200) {
                logger.info("✅ Note deleted {} successfully!", slug);
            } else {
                logger.error("❌ Failed to delete note. HTTP status: " + status);
                logger.error("Response body: " + response.body());
                return 2;
            }
        } catch (IOException | InterruptedException e) {

        }
        return 1;
    }

    /**
     * Create a HackMD document using the file specified as a templage
     * @param templateName
     * @param slug
     * @return
     */
    public static int createDoc(String templateName, String slug) {
        String template = getTemplate(templateName, slug);
        if (template == null) {
            return 4;
        } else {
            final String API_TOKEN = getToken();
            String organisation = DBHandler.getInstance().selectString("settings",
                    "value", "keyValue=\"HackMD_team\"");
            String folder_id = DBHandler.getInstance().selectString("settings",
                    "value", "keyValue=\"HackMD_folder_id\"");
            final String API_URL = "https://api.hackmd.io/v1/teams/" + organisation + "/notes";
            logger.info("API_url: {}", API_URL);
            if (API_TOKEN == null || API_TOKEN.isBlank()) {
                System.err.println("❌ Please create a file containing your HackMD API token in ~/.ssh/hmd_token.");
                return 1;
            }
            // Prepare note data as a Map
            Map<String, Object> noteData = Map.of(

                    "title", slug,
                    "content", template,
                    "readPermission", "guest",     // options: owner, signed_in, guest
                    "writePermission", "guest",    // same or stricter than readPermission
                    "commentPermission", "everyone", // options: everyone, signed_in, owner
                    "permalink", slug, //
                    "parentFolderId", folder_id
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
                logger.info("Request: {}", request.toString());
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
        }
        return 0;
    }
}
