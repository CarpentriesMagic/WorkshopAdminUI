package uk.ac.ncl.dwa.view;

import net.miginfocom.swing.MigLayout;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
        Scanner sc = null;
        try {
            logger.trace("Creating new repository from template");
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
                logger.trace("Somthing to do with outputstream");
            }
            int status = conn.getResponseCode();
            logger.trace("Response code: " + status);
            if (status == 201 || status == 202) {
                logger.trace("Repository created successfully.");
            } else {
                logger.trace("Failed to create repository. HTTP status: {}", status);
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
            logger.trace("Response code: {}", responseCode);
            if (responseCode == 204) {
                logger.trace("Repository deleted successfully.");
            } else {
                logger.trace("Failed to delete repository. HTTP status: {}", responseCode);
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
        String token = getToken();

        try {
            System.out.println("Cloning from " + remoteUrl + " into " + repo);
            CloneCommand clone = Git.cloneRepository()
                    .setURI(remoteUrl)
                    .setDirectory(new File(repo))
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, token));
            try (Git git = clone.call()) {
                logger.trace("Repository cloned successfully.");
                JOptionPane.showMessageDialog(this, "Repository cloned successfully.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                // READ index.md into text area
                Scanner sc = new Scanner(new File(repo + "/index.md"));
                while (sc.hasNext()) {
                    String line = sc.nextLine();
                    textArea.append(line + "\n");
                }

            } catch (JGitInternalException|GitAPIException e) {
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Clone exists", JOptionPane.ERROR_MESSAGE);

            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Cloning failed: ", JOptionPane.ERROR_MESSAGE);
            logger.trace("GitAPI Exception " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (workshopComboBox.getSelectedItem() != null) {
            switch (e.getActionCommand()) {
                case "Generate" -> {
                    createFromTemplate("jsteyn", workshopComboBox.getSelectedItem().toString());
                }
                case "Delete" -> {
                    deleteRepository("jsteyn", workshopComboBox.getSelectedItem().toString());
                }
                case "Clone" -> {
                    cloneRepository("jsteyn", workshopComboBox.getSelectedItem().toString(), "jsteyn");
                }
            }
        } else {
            JOptionPane.showConfirmDialog(this, "Please select a workshop first\n");

        }
    }
}
