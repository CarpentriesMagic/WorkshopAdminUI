package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.model.Setting;
import uk.ac.ncl.dwa.model.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(RoomPanel.class);
    SettingTable settingTable = new SettingTable();

    public SettingPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(settingTable);
        scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JButton btn_save = new JButton("Save Settings");
        JButton btn_add = new JButton("Add Setting");
        JButton btn_del = new JButton("Delete Setting");
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
        Settings settings = settingTable.getModel().getSettings();
        switch (e.getActionCommand()) {
            case "Save Settings" -> {
                settings.forEach(setting -> {
                    System.out.println(setting.toString());
                    switch (setting.getStatus()) {
                        case 'n':
                            if (settings.insertSetting(setting)) {
                                setting.setStatus('s');
                                setting.setOriginal_keyValue(setting.getKey());
                            }
                            break;
                        case 'u':
                            settings.updateSettings(setting);
                            break;
                    }
                });
                settingTable.repaint();
            }
            case "Add Setting" -> {
                logger.info("Adding new Setting");
                settings.add(settings.size(), new Setting());
                settingTable.repaint();

            }
            case "Delete Setting" -> {
                int row = settingTable.getSelectedRow();
                if (row != -1) {
                    settings.remove(row);
                } else {
                    JOptionPane.showMessageDialog(this, "No row selected");
                }
                settingTable.repaint();
            }
        }
    }
}
