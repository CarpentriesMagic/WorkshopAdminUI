package uk.ac.ncl.dwa.view;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * A mouse listener class which is used to handle mouse clicking event
 * on column headers of a JTable.
 * @author www.codejava.net
 *
 */
public class TableHeaderMouseListener extends MouseAdapter {

    private final WorkshopTable table;
    private WorkshopPanel panel;
    private boolean workshops = false;

    public TableHeaderMouseListener(WorkshopPanel panel) {
        this.table = panel.getWorkshopTable();
        this.panel = panel;
    }

    public void mouseClicked(MouseEvent event) {
        Point point = event.getPoint();
        int column = table.columnAtPoint(point);

        panel.setWorkshopOrder(!workshops);
        table.getModel().fireTableDataChanged();

        JOptionPane.showMessageDialog(table, "Column header #" + column + " is clicked. Change order to " + (workshops ?"ascending":"descending"));
        // do your real thing here...
    }
}