package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.model.Helper;
import uk.ac.ncl.dwa.model.Helpers;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelperPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(HelperPanel.class);
    HelperTable helperTable = new HelperTable();

    public HelperPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(helperTable);
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
        Helpers helpers = helperTable.getModel().getHelpers();
        switch (e.getActionCommand()) {
            case "Save" -> {
                helpers.forEach(helper -> {
                    switch (helper.getStatus()) {
                        case 'n':
                            helper.setStatus('s');
                            if (helpers.insertHelpers(helper)) {
                                helper.setKey_slug(helper.getSlug());
                                helper.setKey_person_id(helper.getPerson_id());
                            } else {
                                JOptionPane.showMessageDialog(this, "This is a duplicate entry. Please correct before trying to save again.", "Error", JOptionPane.ERROR_MESSAGE);
                                helper.setStatus('n');
                            }
                            break;
                        case 'u':
//                            helper.updateHelper(helper);
                            break;
                    }
                    helperTable.repaint();
                });
            }
            case "Add" -> {
                logger.info("Adding new Person");
                helpers.add(helpers.size(), new Helper());
                helperTable.repaint();
            }
            case "Delete" -> {
                int row = helperTable.getSelectedRow();
                if (row != -1) {
                    helpers.remove(row);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row");
                }
                helperTable.repaint();
            }
        }
    }

}
