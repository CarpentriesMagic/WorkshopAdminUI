package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.Room;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomPanel extends JPanel implements ActionListener {
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
        if (e.getActionCommand().equals("Save")) {
            // Save action
            logger.info("Saving changes");
            boolean success1 = true;
            boolean success2 = true;
            if (!globals.getEditedRows("rooms").isEmpty()) {
                success1 = globals.getRooms().updateRooms();
                if (!success1) {
                    JOptionPane.showMessageDialog(this, "Error updating rooms");
                }
            }
            if (!globals.getInsertedRows("rooms").isEmpty()) {
                success2 = globals.getRooms().insertRooms();
            }
            if (success1 && success2) {
                globals.setDirty(false);
            }
            if (success1) {
                globals.getEditedRows("rooms").clear();
            }
            if (success2) {
                globals.getInsertedRows("rooms").clear();
            }
        } else if (e.getActionCommand().equals("Add")) {
            // Add action
            logger.info("Adding new Room");
            Globals.getInstance().getRooms().add(new Room());
            logger.debug("Setting dirty to true");
            globals.setDirty(true);
            globals.getInsertedRows("rooms").add(globals.getRooms().size() - 1);
            roomTable.repaint();
        }
    }

}
