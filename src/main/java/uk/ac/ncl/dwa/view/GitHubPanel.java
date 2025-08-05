package uk.ac.ncl.dwa.view;

import net.miginfocom.swing.MigLayout;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.sshd.SshdSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.model.Helper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class GitHubPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(getClass().getName());
    JTextArea textArea;
    JComboBox<String> workshopComboBox;
    String[] workshopList;

    public GitHubPanel() {
        super();
        workshopList = DBHandler.getInstance().selectStringArray("workshops", "slug", "");
        workshopComboBox = new JComboBox<>(workshopList);
        setLayout(new MigLayout("", "[50%][50%]", "[fill][fill]"));
        textArea = new JTextArea("");
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        // Create scroll bars
        scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        //  enable horizontal scrolling
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        //  enable vertical scrolling
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add components to the frame
        JPanel buttonPanel = new JPanel();
        JButton btn_gen = new JButton("Generate");
        JButton btn_del = new JButton("Delete");
        JButton btn_clone = new JButton("Clone");
        btn_gen.addActionListener(this);
        btn_del.addActionListener(this);
        btn_clone.addActionListener(this);
        buttonPanel.add(workshopComboBox);
        buttonPanel.add(btn_gen);
        buttonPanel.add(btn_del);
        buttonPanel.add(btn_clone);
        this.add(buttonPanel, "wrap");
        this.add(scrollPane, "span, grow, push, wrap");
    }

    public void createFromTemplate(String owner, String repo) {
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
                JOptionPane.showMessageDialog(this, "Repository created successfully.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            } else {
                logger.info("Failed to create repository. HTTP status: {}", status);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        JOptionPane.showMessageDialog(this, line, "FAIL", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            conn.disconnect();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Creating failed: ", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    public String getToken() {
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

    public void deleteRepository(String owner, String repo) {
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
                    while ((line = br.readLine()) != null) {
                        JOptionPane.showMessageDialog(this,line,"FAIL",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            conn.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void cloneRepository(String owner, String repo, String username) {
        String remoteUrl = "https://github.com/" + owner + "/" + repo + ".git";
        remoteUrl = "git@github.com:" + owner + "/" + repo + ".git";
        String token = getToken();

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
                StringBuilder sb = new StringBuilder();
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
                        if (!line.startsWith("8")) sb.append(line + "\n");
                    }
                }

                textArea.setText(sb.toString());


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

            } catch (JGitInternalException|GitAPIException e) {
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Clone exists", JOptionPane.ERROR_MESSAGE);

            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Cloning failed: ", JOptionPane.ERROR_MESSAGE);
            logger.info("GitAPI Exception " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String organisation = DBHandler.getInstance().selectString("settings", "value", "keyValue=\"organisation\"");
        if (workshopComboBox.getSelectedItem() != null) {
            switch (e.getActionCommand()) {
                case "Generate" -> {
                    createFromTemplate(organisation, workshopComboBox.getSelectedItem().toString());
                }
                case "Delete" -> {
                    deleteRepository(organisation, workshopComboBox.getSelectedItem().toString());
                }
                case "Clone" -> {
                    cloneRepository(organisation, workshopComboBox.getSelectedItem().toString(), organisation);
                }
            }
        } else {
            JOptionPane.showConfirmDialog(this, "Please select a workshop first\n");

        }
    }

    private String getHelpers(String repo) {
        String[] columnNames = new String[]{"title", "firstname", "lastname"};
        String sql = "SELECT " +
                "p.title as title, p.firstname as firstname, " +
                "p.lastname as lastname " +
                "FROM helpers as h " +
                "JOIN people as p on p.person_id=h.person_id " +
                "WHERE h.slug=\"" + repo + "\" ";
        logger.info(sql);
        List<Object> helpers = DBHandler.getInstance().query(sql, columnNames);
        StringBuilder sb_helper = new StringBuilder();
        sb_helper.append("[");
        for (Object object : helpers) {
            HashMap<String, Object> helperMap = (HashMap<String, Object>) object;
            String helper =
                    ((String) helperMap.get(columnNames[0]) + " " +
                            (String) helperMap.get(columnNames[1]) + " " +
                            (String) helperMap.get(columnNames[2])).trim();
            sb_helper.append('"').append(helper).append('"').append(",");
        }
        String helperlist = sb_helper.toString();
        helperlist = helperlist.substring(0,helperlist.length()-1) + "]";
        logger.info(helperlist);
        return helperlist;
    }

    private String getInstructors(String repo) {
        String[] columnNames = new String[]{"title", "firstname", "lastname"};
        String sql = "SELECT " +
                "p.title as title, p.firstname as firstname, " +
                "p.lastname as lastname " +
                "FROM instructors as h " +
                "JOIN people as p on p.person_id=h.person_id " +
                "WHERE h.slug=\"" + repo + "\" ";
        logger.info(sql);
        List<Object> instructors = DBHandler.getInstance().query(sql, columnNames);
        StringBuilder sb_instructor = new StringBuilder();
        sb_instructor.append("[");
        for (Object object : instructors) {
            HashMap<String, Object> instructorMap = (HashMap<String, Object>) object;
            String helper =
                    ((String) instructorMap.get(columnNames[0]) + " " +
                            (String) instructorMap.get(columnNames[1]) + " " +
                            (String) instructorMap.get(columnNames[2])).trim();
            sb_instructor.append('"').append(helper).append('"').append(",");
        }
        String instructorlist = sb_instructor.toString();
        instructorlist = instructorlist.substring(0,instructorlist.length()-1) + "]";
        logger.info(instructorlist);
        return instructorlist;
    }
}
