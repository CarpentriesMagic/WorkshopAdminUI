package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class RoomPanel extends JPanel {
    Logger logger = LoggerFactory.getLogger(RoomPanel.class);
    RoomTable roomTable = new RoomTable();

    public RoomPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(roomTable);
        // Create scroll bars
        scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        //  enable horizontal scrolling
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        //  enable vertical scrolling
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add components to the frame
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JButton btn_save = new JButton("Save");
        JButton btn_add = new JButton("Add");
        JButton btn_del = new JButton("Delete");
        btn_save.addActionListener(e -> logger.info("Save button clicked"));
        btn_add.addActionListener(e -> logger.info("Add button clicked"));
        btn_del.addActionListener(e -> logger.info("Delete button clicked"));
        panel.add(btn_save);
        panel.add(btn_add);
        panel.add(btn_del);

        // Add panel and scroll pane to the frame
        this.add(panel);
        this.add(scrollPane);
    }
}
