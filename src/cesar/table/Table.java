package cesar.table;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public abstract class Table extends JTable {
    private static final long serialVersionUID = 5510916208167787328L;

    static final DefaultTableCellRenderer RIGHT_RENDERER;
    static final DefaultTableCellRenderer CENTER_RENDERER;

    static {
        RIGHT_RENDERER = new DefaultTableCellRenderer();
        RIGHT_RENDERER.setHorizontalAlignment(JLabel.RIGHT);
        CENTER_RENDERER = new DefaultTableCellRenderer();
        CENTER_RENDERER.setHorizontalAlignment(JLabel.RIGHT);
    }

    static class HeaderRenderer implements TableCellRenderer {
        private DefaultTableCellRenderer renderer;

        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    Table() {
        setDoubleBuffered(true);

        JTableHeader header = getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(this));

        // Disabilita arrastar as colunas
        header.setReorderingAllowed(false);

        setColumnSelectionAllowed(false);

        ListSelectionModel selectionModel = getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setFont(new Font("monospaced", Font.PLAIN, 12));
    }

    public void scrollToRow(int rowNumber) {
        int rowHeight = getRowHeight();
        int parentHeight = getParent().getHeight();
        var rect = new Rectangle(0, (rowNumber - 1) * rowHeight + parentHeight, getWidth(), rowHeight);
        scrollRectToVisible(rect);
    }

    public abstract String getAddressAtRow(int row);

    public abstract String getValueAtRow(int row);

    protected static abstract class TableModel extends AbstractTableModel {
        private static final long serialVersionUID = 8671470346716518079L;

        static final int MEMORY_SIZE = 1 << 16;
        String[] columnNames;
        Object[][] data;

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
        }

        public void setValueAtAndUpdate(Object value, int row, int col) {
            setValueAt(value, row, col);
            fireTableCellUpdated(row, col);
        }
    }
}
