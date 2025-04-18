package uk.ac.ncl.dwa.view;

import com.sun.tools.javac.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ncl.dwa.model.WorkshopTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainFrame extends JFrame implements ActionListener {
    Logger logger = LoggerFactory.getLogger(MainFrame.class);

    public MainFrame(String connectionString) {
        this.setTitle("Desperado Workshop Admin");
        this.setLayout(new BorderLayout());

        // Create WorkshopTableModel
        MainTable mainTable = new MainTable(connectionString);

        JScrollPane scrollPane = new JScrollPane(mainTable);
        // Create scroll bars
        scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        //  enable horizontal scrolling
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        //  enable vertical scrolling
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Set preferred size for the table
        //table.setPreferredScrollableViewportSize(new Dimension(800, 600));

        // Add components to the frame
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Workshops");
        JButton button = new JButton();
        button.setText("Save");
        button.addActionListener(this);
        panel.add(label);
        panel.add(button);

        // Add panel and scroll pane to the frame
        this.add(panel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        // Frame settings
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logger.trace("MainFrame constructor");
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());
        if (e.getActionCommand().equals("Save")) {
            // Save action
            logger.debug("Save button clicked");
            // Implement save functionality here
        } else {
            logger.debug("Unknown action command: " + e.getActionCommand());
        }
    }
}
