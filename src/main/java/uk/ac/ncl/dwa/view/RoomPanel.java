package uk.ac.ncl.dwa.view;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new MigLayout("","[50%][50%]","[fill][fill]"));

        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add components to the frame
        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JButton btn_save = new JButton("Save");
        JButton btn_add = new JButton("Add");
        JButton btn_del = new JButton("Delete");
        btn_save.addActionListener(this);
        btn_add.addActionListener(this);
        btn_del.addActionListener(this);
        panel.add(btn_save);
        panel.add(btn_add);
        panel.add(btn_del);

        // Add panel and scroll pane to the frame
        this.add(panel, "wrap");
        this.add(scrollPane, "span, grow, push, wrap");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info(e.getActionCommand());
        Rooms rooms = roomTable.getModel().getRooms();
        switch (e.getActionCommand()) {
            case "Save" -> {
                logger.info(rooms.size() + " rooms loaded");
                rooms.forEach(room -> {
                    logger.info(room.getRoom_id());
                    if (room.getRoom_id() == null || room.getRoom_id().equals("")) {
                        JOptionPane.showMessageDialog(this, "Record with empty Room ID could not be saved. Fix and save again. Other records should all be saved.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        switch (room.getStatus()) {
                            case 'n':
                                rooms.insertRoom(room);
                                break;
                            case 'u':
                                rooms.updateRoom(room);
                                break;
                        }
                        if (room.getStatus() == 'n') {
                            room.setRoom_id("s");
                            room.setKey(room.getRoom_id());
                        }
                    }
                });
            }
            case "Add" -> {
                rooms.add(rooms.size(), new Room());
            }
            case "Delete" -> {
                int row = roomTable.getSelectedRow();
                if (row != -1) {
                    if (rooms.remove(row) == null) {
                        JOptionPane.showMessageDialog(this, "The room could not be removed.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No row selected");
                }
            }
        }
        roomTable.repaint();
    }

}
