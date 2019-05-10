package cesar.table;

import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public abstract class Table extends JTable {
    private static final long serialVersionUID = 5510916208167787328L;

    private boolean isDecimal;
    private final TableCellRenderer bigCenteredRenderer;
    private final TableCellRenderer centeredRenderer;
    private final TableCellRenderer hexadecimalByteRenderer;
    private final TableCellRenderer decimalByteRenderer;
    private final TableCellRenderer hexadecimalShortRenderer;
    private final TableCellRenderer decimalShortRenderer;
    private final TableCellRenderer defaultRenderer;

    protected Table() {
        defaultRenderer          = new DefaultTableCellRenderer();
        bigCenteredRenderer      = new CellRenderer.BigCenteredTableCellRenderer();
        centeredRenderer         = new CellRenderer.CenteredTableCellRenderer();
        decimalByteRenderer      = new CellRenderer.DecimalByteRenderer();
        hexadecimalByteRenderer  = new CellRenderer.HexadecimalByteRenderer();
        decimalShortRenderer     = new CellRenderer.DecimalShortRenderer();
        hexadecimalShortRenderer = new CellRenderer.HexadecimalShortRenderer();
        isDecimal                = true;

        setDoubleBuffered(true);
        setColumnSelectionAllowed(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = getTableHeader();
        Font headerFont = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
        header.setFont(headerFont);
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        header.setReorderingAllowed(false);

        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        assert getColumnClass(col).isInstance(value);
        super.setValueAt(value, row, col);
    }

    public void setDecimal(boolean isDecimal) {
        if (this.isDecimal != isDecimal) {
            this.isDecimal = isDecimal;
            ((TableModel) getModel()).fireTableDataChanged();
        }
    }

    protected TableCellRenderer getDefaultRenderer() {
        return defaultRenderer;
    }

    protected TableCellRenderer getDecimalByteRenderer() {
        return decimalByteRenderer;
    }

    protected TableCellRenderer getHexadecimalByteRenderer() {
        return hexadecimalByteRenderer;
    }

    protected TableCellRenderer getDecimalShortRenderer() {
        return decimalShortRenderer;
    }

    protected TableCellRenderer getHexadecimalShortRenderer() {
        return hexadecimalShortRenderer;
    }

    protected TableCellRenderer getBigCenteredRenderer() {
        return bigCenteredRenderer;
    }

    protected TableCellRenderer getCenteredRenderer() {
        return centeredRenderer;
    }

    protected boolean isDecimal() {
        return isDecimal;
    }

    public void scrollToRow(int rowNumber, boolean showOnTop) {
        int rowHeight = getRowHeight();
        Rectangle rect;
        if (showOnTop) {
            int parentHeight = getParent().getHeight();
            rect = new Rectangle(0, (rowNumber - 1) * rowHeight + parentHeight, getWidth(), rowHeight);
        }
        else {
            rect = new Rectangle(0, rowNumber * rowHeight, getWidth(), rowHeight);
        }
        scrollRectToVisible(rect);
    }

    public void scrollToRow(int rowNumber) {
        scrollToRow(rowNumber, true);
    }

    @Override
    public abstract Class<?> getColumnClass(int col);

    public abstract short getAddressAtRow(int row);

    public abstract byte getValueAtRow(int row);

    public void setValueAtAndUpdate(Object value, int row, int col) {
        ((TableModel) getModel()).setValueAtAndUpdate(value, row, col);
    }

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
