package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.Room;
import uk.ac.ncl.dwa.model.Rooms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(RoomPanel.class);
    RoomTable roomTable = new RoomTable();

    public RoomPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add components to the frame
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JButton btn_save = new JButton("Save Rooms");
        JButton btn_add = new JButton("Add Room");
        JButton btn_del = new JButton("Delete Room");
        btn_save.addActionListener(this);
        btn_add.addActionListener(this);
        btn_del.addActionListener(this);
        panel.add(btn_save);
        panel.add(btn_add);
        panel.add(btn_del);

        // Add panel and scroll pane to the frame
        this.add(panel);
        this.add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());
        Globals globals = Globals.getInstance();
        Rooms rooms = roomTable.getModel().getRooms();
        switch (e.getActionCommand()) {
            case "Save Rooms" -> {
                rooms.forEach(room -> {
                    switch (room.getStatus()) {
                        case 'n': rooms.insertRoom(room);
                        case 'u': rooms.updateRoom(room);
                    }
                });
            }
            case "Add Room" -> {
                rooms.add(rooms.size(), new Room());
            }
            case "Delete Room" -> {
                // Delete action
                logger.info("Deleting selected room");
                int row = roomTable.getSelectedRow();
                if (row != -1) {
                    rooms.remove(row);
                }
            }
        }
        roomTable.repaint();
    }

}
