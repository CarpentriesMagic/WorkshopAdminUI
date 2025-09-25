package uk.ac.ncl.dwa.view;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.SystemProperties;
import uk.ac.ncl.dwa.controller.Utilities;
import uk.ac.ncl.dwa.model.Setting;
import uk.ac.ncl.dwa.model.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SettingPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(RoomPanel.class);
    SettingTable settingTable = new SettingTable();
    JTextArea propertiesTextArea = new JTextArea();
    String propertiesFilename = "workshopadmin.properties";

    public SettingPanel() {
        super();
        setLayout(new MigLayout("", "[50%][50%]", "[fill][fill]"));

        JScrollPane settingsScrollPane = new JScrollPane(settingTable);
        settingsScrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        settingsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        settingsScrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        settingsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JSplitPane settingsSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, settingsScrollPane, propertiesTextArea);

        JPanel buttonPanel = new JPanel(new MigLayout("", "[50%][50%]", "[fill][fill]"));
        settingTable.setFillsViewportHeight(true);
        JButton btn_save = new JButton("Save Settings");
        JButton btn_add = new JButton("Add Setting");
        JButton btn_del = new JButton("Delete Setting");
        JButton btn_saveProperies = new JButton("Save Properties");
        btn_save.addActionListener(this);
        btn_add.addActionListener(this);
        btn_del.addActionListener(this);
        btn_saveProperies.addActionListener(this);
        buttonPanel.add(btn_save);
        buttonPanel.add(btn_add);
        buttonPanel.add(btn_del);
        buttonPanel.add(btn_saveProperies);
        String properties = SystemProperties.readPropertyFile(propertiesFilename);
        propertiesTextArea.setText(properties);

        settingsSplit.setOneTouchExpandable(true);
        settingsSplit.setDividerLocation(600);
        // Add buttonPanel and scroll pane to the frame
        this.add(buttonPanel, "wrap");
        this.add(settingsSplit, "span, grow, push, wrap");
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
            case "Save Properties" -> {
                Utilities.writeStringToFile(propertiesTextArea.getText(), new File(propertiesFilename));
            }
        }
    }
}
