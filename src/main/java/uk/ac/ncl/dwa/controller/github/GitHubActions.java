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

import javax.swing.*;
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

    public static int createFromTemplate(String owner, String repo) {
        Scanner sc;
        try {
            logger.info("Creating new repository from template");
            sc = new Scanner(new File(System.getProperty("user.home") + "/.ssh/gh_token"));
            String token = sc.nextLine();
            sc.close();

            // Create JSON body for the request
            String jsonBody = String.format("{\"owner\": \"%s\",\"name\": \"%s\",\"description\": \"Workshop site using Carpentries website template\",\"private\": false}", owner, repo);
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
                return 0;
            } else {
                logger.info("Failed to create repository. HTTP status: {}", status);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String line;
                    if ((line = br.readLine()) != null) {
                        logger.info(line);
                        return 1;
                    }
                }
            }

            conn.disconnect();
        } catch (IOException e) {
            logger.error("IOException: {}", e.getMessage());
            logger.error("Localised message: {}", e.getLocalizedMessage());
            return 1;
        }
        return 2;
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

    public static int deleteRepository(String owner, String repo) {
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
                        return 1;
                    }
                }
            }
            conn.disconnect();
        } catch (IOException e) {
            logger.error("IOException: {}", e.getMessage());
            return 2;
        }
        return 0;
    }

    public static String cloneRepository(String owner, String repo, String username) {
        String remoteUrl = "https://github.com/" + owner + "/" + repo + ".git";
        remoteUrl = "git@github.com:" + owner + "/" + repo + ".git";
        String token = getToken();

        StringBuilder sb = new StringBuilder();
        try {
            System.out.println("Cloning from " + remoteUrl + " into " + repo);
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
                Scanner sc = new Scanner(new File(repo + "/index.md"));
                String contact_emails = DBHandler.getInstance().selectString(
                        "settings", "value", "keyValue=\"contact_emails\""
                );
                contact_emails = "[\"" + contact_emails.replace(",", "\",\"") + "\"]";
                String organisation = DBHandler.getInstance().selectString(
                        "settings", "value", "keyValue=\"venue\"");
                String collabdoc = DBHandler.getInstance().selectString(
                        "settings", "value", "keyValue=\"collabdoc\"").replace("<slug>", repo);
                String[] workshopColumns = new String[]{"slug", "title", "humandate", "humantime", "startdate",
                        "enddate", "language", "country", "room_id", "eventbrite"};
                HashMap<String, String> columnValues = DBHandler.getInstance().selectStringArray("workshops",
                        workshopColumns,
                        "slug=\"" + repo + "\"");
                HashMap<String, String> roomValues = DBHandler.getInstance().selectStringArray("room",
                        new String[]{"description", "longitude", "latitude", "what_three_words"},
                        "room_id=\"" + columnValues.get("room_id") + "\"");
                String helperlist = getHelpers(repo);
                String instructorlist = getInstructors(repo);
                boolean skip = false;
                while (sc.hasNext()) {
                    String line = sc.nextLine();
                    if (line.startsWith("8")) skip = !skip;
                    if (!skip) {
                        if (line.startsWith("helper")) line = "helper: " + helperlist;
                        if (line.startsWith("instructor")) line = "instructor: " + instructorlist;
                        if (line.startsWith("email")) line = "email: " + contact_emails;

                        if (line.startsWith("venue")) line = line.replaceFirst("FIXME", organisation);
                        if (line.startsWith("humandate"))
                            line = line.replaceFirst("FIXME", columnValues.get("humandate"));
                        if (line.startsWith("humantime"))
                            line = line.replaceFirst("FIXME", columnValues.get("humantime"));
                        if (line.startsWith("startdate"))
                            line = line.replaceFirst("FIXME", columnValues.get("startdate"));
                        if (line.startsWith("enddate")) line = line.replaceFirst("FIXME", columnValues.get("enddate"));
                        if (line.startsWith("language"))
                            line = line.replaceFirst("FIXME", columnValues.get("language"));
                        if (line.startsWith("country")) line = line.replaceFirst("FIXME", columnValues.get("country"));
                        if (line.startsWith("enddate")) line = line.replaceFirst("FIXME", columnValues.get("enddate"));
                        if (line.startsWith("address"))
                            line = line.replaceFirst("FIXME", roomValues.get("description"));
                        if (line.startsWith("latitude")) line = line.replaceFirst("45", roomValues.get("latitude"));
                        if (line.startsWith("longitude")) line = line.replaceFirst("-1", roomValues.get("longitude"));
                        if (line.startsWith("what3words"))
                            line = line.replaceFirst("what3words: ", "what3words: " + roomValues.get("what_three_words"));
                        String eventbrite = (roomValues.get("eventbrite") == null ? "" : roomValues.replace("null", ""));
                        if (line.startsWith("eventbrite"))
                            line = line.replaceFirst("eventbrite: ", "eventbrite: " + eventbrite);
                        if (line.startsWith("collaborative_notes"))
                            line = line.replaceFirst("collaborative_notes: ", "collaborative_notes: " + collabdoc);
                        if (!line.startsWith("8")) sb.append(line).append("\n");
                    }
                }



                PrintWriter pw = new PrintWriter(repo + "/index.md");
                pw.write(sb.toString());
                pw.close();

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
                return "0";
            }

        } catch (FileNotFoundException e) {
            logger.info("GitAPI Exception " + e.getMessage());
            return "1";
        }
        return sb.toString();
    }


}
