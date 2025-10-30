package uk.ac.ncl.dwa.view;

import javax.swing.*;

public final class FocusUtils {
	private FocusUtils() {}

	public static void lastRow(JTable table, JScrollPane scrollPane) {
		SwingUtilities.invokeLater(() -> {
			int lastRow = table.getRowCount() - 1;
			if (lastRow >= 0) {
				table.setRowSelectionInterval(lastRow, lastRow);
				table.scrollRectToVisible(table.getCellRect(lastRow, 0, true));
				table.requestFocusInWindow();
			}

			JScrollBar vertical = scrollPane.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum());
		});
	}

	public static int getLastRowIndex(JTable table) {
		return table.getRowCount() - 1;
	}
}


