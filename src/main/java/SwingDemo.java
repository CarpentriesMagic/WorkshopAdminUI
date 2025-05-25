package my;
import java.awt.Color;
import java.awt.Font;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
public class SwingDemo {
    public static void main(String[] argv) throws Exception {
        JTable table = new JTable(5, 5);
        Font font = new Font("Verdana", Font.PLAIN, 12);
        table.setFont(font);
        table.setRowHeight(30);
        table.setBackground(Color.orange);
        table.setForeground(Color.white);
        TableColumn testColumn = table.getColumnModel().getColumn(0);
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Asia");
        comboBox.addItem("Europe");
        comboBox.addItem("North America");
        comboBox.addItem("South America");
        comboBox.addItem("Africa");
        comboBox.addItem("Antartica");
        comboBox.addItem("Australia");
        testColumn.setCellEditor(new DefaultCellEditor(comboBox));
        JFrame frame = new JFrame();
        frame.setSize(600, 400);
        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }
}