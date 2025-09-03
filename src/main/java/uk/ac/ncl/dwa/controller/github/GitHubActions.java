package uk.ac.ncl.dwa.controller.github;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.sshd.SshdSessionFactory;
import org.slf4j.Logger;
import uk.ac.ncl.dwa.database.DBHandler;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;
import static uk.ac.ncl.dwa.database.SpecificQueriesHelper.getHelpers;
import static uk.ac.ncl.dwa.database.SpecificQueriesHelper.getInstructors;

public class GitHubActions {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(GitHubActions.class);

    public static String createFromTemplate(String owner, String repo) {
        Scanner sc;
        String token = "";
        try {
            logger.info("Creating new repository from template");
            sc = new Scanner(new File(System.getProperty("user.home") + "/.ssh/gh_token"));
            token = sc.nextLine();
            sc.close();
        } catch ( FileNotFoundException e) {
          logger.info("FileNotFoundException: {}", e.getMessage());
        }

        try {

            // Create JSON body for the request
            String jsonBody = String.format("{\"owner\": \"%s\"," +
                    "\"name\": \"%s\"," +
                    "\"description\": \"Workshop site using Carpentries website template\"," +
                    "\"private\": false}", owner, repo);
//                    "\"homepage\":\"https://%sgithub.io/%s\"}", owner, repo, owner, repo);
            URL url = new URL("https://api.github.com/repos/carpentries/workshop-template/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Accept", "application/vnd.github+json");
            conn.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                logger.info("Something to do with outputstream");
            }
            int status = conn.getResponseCode();
            logger.info("Response code: {}", status);
            if (status == 201 || status == 202) {
                logger.info("Repository created successfully.");
            } else {
                logger.info("Failed to create repository. HTTP status: {}", status);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String line;
                    if ((line = br.readLine()) != null) {
                        logger.info(line);
                        return "1"; // ERROR
                    }
                }
            }
            logger.info("Disconnecting from the server.");
            conn.disconnect();
            return "0"; // SUCCESS
        } catch (IOException e) {
            logger.error("IOException: {}", e.getMessage());
            logger.error("Localised message: {}", e.getLocalizedMessage());
            return "1"; // ERROR
        }

// TODO: This update needs to be replaced by OpenFeign as the HttpURLConnection does not support PATCH
//        try {
//            logger.info("Update homepage URL for the new repository");
//            // Create JSON body for the request
//            String jsonBody2 = String.format("{\"homepage\":\"https://%s.github.io/%s\"}", owner, repo);
//            logger.info("JSON body: {}", jsonBody2);
//            String urlstring = String.format("https://api.github.com/repos/%s/%s", owner, repo);
//            logger.info("URL string: {}", urlstring);
//            URL url2 = new URL(urlstring);
//            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
//            conn2.setRequestMethod("PATCH");
//            conn2.setRequestProperty("Authorization", "Bearer " + token);
//            conn2.setRequestProperty("Accept", "application/vnd.github+json");
//            conn2.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");
//            conn2.setDoOutput(true);
//
//            try (OutputStream os = conn2.getOutputStream()) {
//                byte[] input = jsonBody2.getBytes(StandardCharsets.UTF_8);
//                os.write(input, 0, input.length);
//                logger.info("Something to do with outputstream");
//            }
//            int status2 = conn2.getResponseCode();
//            logger.info("Response code: {}", status2);
//            if (status2 == 201 || status2 == 202) {
//                logger.info("Repository updated successfully.");
//                return 0;
//            } else {
//                logger.info("Failed to update repository. HTTP status: {}", status2);
//                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn2.getErrorStream()))) {
//                    String line;
//                    if ((line = br.readLine()) != null) {
//                        logger.info(line);
//                        return 1;
//                    }
//                }
//            }
//            conn2.disconnect();
//        } catch (IOException e) {
//            logger.error("IOException: {}", e.getMessage());
//            logger.error("Localised message: {}", e.getLocalizedMessage());
//            return 1;
//        }
    }

    public static String getToken() {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(System.getProperty("user.home") + "/.ssh/gh_token"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String token = sc.nextLine();
        sc.close();
        return token;
    }

    public static String deleteRepository(String owner, String repo) {
        try {
            String token = getToken();

            URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Accept", "application/vnd.github+json");
            conn.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            logger.info("Response code: {}", responseCode);
            if (responseCode == 204) {
                logger.info("Repository deleted successfully.");
            } else {
                logger.info("Failed to delete repository. HTTP status: {}", responseCode);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String line;
                    if ((line = br.readLine()) != null) {
                        logger.info(line);
                        return "1";
                    }
                }
            }
            conn.disconnect();
        } catch (IOException e) {
            logger.error("IOException: {}", e.getMessage());
            return "2";
        }
        return "0";
    }

    public static String cloneRepository(String owner, String repo, String username) {
        String remoteUrl = "https://github.com/" + owner + "/" + repo + ".git";
        remoteUrl = "git@github.com:" + owner + "/" + repo + ".git";
        String token = getToken();
        String schedule = "";
        String carpentrycode = "";

        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        try {
            logger.info("Cloning from {} into {}",remoteUrl, repo);
            SshSessionFactory sshSessionFactory = new SshdSessionFactory();

            CloneCommand clone = Git.cloneRepository()
                    .setURI(remoteUrl)
                    .setDirectory(new File(repo))
                    .setTransportConfigCallback(transport -> {
                        if (transport instanceof SshTransport sshTransport) {
                            sshTransport.setSshSessionFactory(sshSessionFactory);
                        }
                    } );
            try (Git git = clone.call()) {
                logger.info("Repository cloned successfully.");
                // READ index.md into text area
                Scanner indexScanner = new Scanner(new File(repo + "/index.md"));
                Scanner configScanner = new Scanner(new File(repo + "/_config.yml"));
                String contact_emails = DBHandler.getInstance().selectString(
                        "settings", "value", "keyValue=\"contact_emails\""
                );
                contact_emails = "[\"" + contact_emails.replace(",", "\",\"") + "\"]";
                String organisation = DBHandler.getInstance().selectString(
                        "settings", "value", "keyValue=\"venue\"");
                String collabdoc = DBHandler.getInstance().selectString(
                        "settings", "value",
                        "keyValue=\"collabdoc\"").replace("<slug>", repo);
                logger.info("Collaborative document: {}", collabdoc);
                String[] workshopColumns = new String[]{"slug", "title", "humandate", "humantime", "startdate",
                        "enddate", "language", "country", "room_id", "eventbrite", "carpentry_code", "curriculum_code",
                "flavour_id", "inc_lesson_site", "pre_survey", "post_survey", "schedule"};
                HashMap<String, Object> columnValues = DBHandler.getInstance().selectStringArray("workshops",
                        workshopColumns,
                        "slug=\"" + repo + "\"");
                HashMap<String, Object> roomValues = DBHandler.getInstance().selectStringArray("room",
                        new String[]{"description", "longitude", "latitude", "what_three_words"},
                        "room_id=\"" + columnValues.get("room_id") + "\"");
                String helperlist = getHelpers(repo);
                String instructorlist = getInstructors(repo);
                boolean skip = false;
                while (indexScanner.hasNext()) {
                    String line = indexScanner.nextLine();
                    if (line.startsWith("8")) skip = !skip;
                    if (!skip) {
                        if (line.startsWith("helper:")) line = "helper: " + helperlist;
                        if (line.startsWith("instructor:")) line = "instructor: " + instructorlist;
                        if (line.startsWith("email:")) line = "email: " + contact_emails;

                        if (line.startsWith("venue")) line = line.replaceFirst("FIXME", organisation);
                        if (line.startsWith("humandate"))
                            line = line.replaceFirst("FIXME", (String)columnValues.get("humandate"));
                        if (line.startsWith("humantime"))
                            line = line.replaceFirst("FIXME", (String)columnValues.get("humantime"));
                        if (line.startsWith("startdate"))
                            line = line.replaceFirst("FIXME", (String)columnValues.get("startdate"));
                        if (line.startsWith("enddate"))
                            line = line.replaceFirst("FIXME", (String)columnValues.get("enddate"));
                        if (line.startsWith("language"))
                            line = line.replaceFirst("FIXME", (String)columnValues.get("language"));
                        if (line.startsWith("country"))
                            line = line.replaceFirst("FIXME", (String)columnValues.get("country"));
                        if (line.startsWith("enddate"))
                            line = line.replaceFirst("FIXME", (String)columnValues.get("enddate"));
                        if (line.startsWith("address"))
                            line = line.replaceFirst("FIXME", (String)roomValues.get("description"));
                        if (line.startsWith("latitude"))
                            line = line.replaceFirst("45", (String)roomValues.get("latitude"));
                        if (line.startsWith("longitude"))
                            line = line.replaceFirst("-1", (String)roomValues.get("longitude"));
                        if (line.startsWith("what3words"))
                            line = line.replaceFirst("what3words: ", "what3words: " +
                                    (String)roomValues.get("what_three_words"));
                        if (line.startsWith("eventbrite")) {
                            String eventbrite = (roomValues.get("eventbrite") == null ?
                                    "" : (String)roomValues.get("eventbrite"));
                            line = line.replaceFirst("eventbrite: ", "eventbrite: " + eventbrite);
                        }
                        if (line.startsWith("collaborative_notes"))
                            line = line.replaceFirst("collaborative_notes: ",
                                    "collaborative_notes: " + collabdoc);
                        if (!line.startsWith("8")) sb.append(line).append("\n");
                    }
                }
                schedule = (String)columnValues.get("schedule") + ".html";
                logger.info("Set schedule file to {}", schedule);
                // Write back to file
                PrintWriter pw = new PrintWriter(repo + "/index.md");
                pw.write(sb.toString());
                pw.close();

                logger.info("Reading _config.yml");
                boolean incubator = false;
                while (configScanner.hasNext()) {
                    String line = configScanner.nextLine();
                    if (line.startsWith("carpentry: ")) {
                        line = "carpentry: " + columnValues.get("carpentry_code") + "\n";
                        carpentrycode = (String) columnValues.get("carpentry_code");
                    }
                    if (line.startsWith("curriculum: "))
                        line = "curriculum: " + columnValues.get("curriculum_code") + "\n";
                    if (line.startsWith("flavor")) {
                        line = "flavor: " + columnValues.get("flavour_id") + "\n";
                    }
                    if (columnValues.get("carpentry_code").equals("incubator")) incubator = true;
                    if (line.startsWith("title"))
                        line = "title: " + columnValues.get("title") + "\n";
                    if (incubator) {
                        if (line.startsWith("# incubator_lesson_site")) {
                            line = "incubator_lesson_site: " + (String)columnValues.get("inc_lesson_site") + "\n";
                        }
                        if (line.startsWith("# incubator_pre_survey")) {
                            line = "incubator_pre_survey: " + columnValues.get("pre_survey") + "\n";
                        }
                        if (line.startsWith("# incubator_post_survey")) {
                            line = "incubator_post_survey: " + columnValues.get("post_survey") + "\n";
                        }
                    }
                    sb2.append(line).append("\n");
                }
                PrintWriter pw_config = new PrintWriter(repo + "/_config.yml");
                pw_config.write(sb2.toString());
                pw_config.close();

                // Copy schedule into repository
                try {
                    File copied = new File(repo + "/_includes/" + carpentrycode + "/schedule.html");
                    File originalfile = new File("schedules/" + schedule);
                    com.google.common.io.Files.copy(originalfile, copied);
                } catch (IOException e) {
                    logger.error("IOException: {}", e.getMessage());
                    //return "3";
                    logger.error("Localised message: {}", e.getLocalizedMessage());
                    logger.error("Problem with schedule file");
                }

                // Add, commit and push changes to GitHub
                git.add().addFilepattern(".").call();
                logger.info("File added to index.");
                git.commit()
                        .setMessage("Updated by CarpentriesMagic").call();
                logger.info("Committed successfully.");
                Iterable<PushResult> pushResults = git.push()
                        .setTransportConfigCallback(transport -> {
                            if (transport instanceof SshTransport sshTransport) {
                                sshTransport.setSshSessionFactory(sshSessionFactory);
                            }
                        })
                        .call();
                for (PushResult result : pushResults) {
                    logger.info("Push result: " + result.getMessages());
                }
            } catch (JGitInternalException | GitAPIException e) {
                logger.error("GitAPI Exception " + e.getMessage());
                return "1";
            }
        } catch (FileNotFoundException e) {
            logger.info("GitAPI Exception " + e.getMessage());
            return "2";
        }
        return "0";//sb2.toString();
    }
}
