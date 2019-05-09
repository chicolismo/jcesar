package cesar.table;

import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public abstract class Table extends JTable {
    private static final long serialVersionUID = 5510916208167787328L;

    private boolean isDecimal;
    private final TableCellRenderer centeredRenderer;
    private final TableCellRenderer hexadecimalByteRenderer;
    private final TableCellRenderer decimalByteRenderer;
    private final TableCellRenderer hexadecimalShortRenderer;
    private final TableCellRenderer decimalShortRenderer;
    private final TableCellRenderer defaultRenderer;

    protected Table() {
        defaultRenderer          = new DefaultTableCellRenderer();
        centeredRenderer         = new CellRenderer.CenteredTableCellRenderer();
        decimalByteRenderer      = new CellRenderer.DecimalByteRenderer();
        hexadecimalByteRenderer  = new CellRenderer.HexadecimalByteRenderer();
        decimalShortRenderer     = new CellRenderer.DecimalShortRenderer();
        hexadecimalShortRenderer = new CellRenderer.HexadecimalShortRenderer();
        isDecimal                = true;

        setDoubleBuffered(true);
        setColumnSelectionAllowed(false);

        JTableHeader header = getTableHeader();
        header.setDefaultRenderer(new CellRenderer.CenteredTableCellRenderer());
        header.setReorderingAllowed(false);

        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setFont(new Font("monospaced", Font.PLAIN, 12));
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

    protected TableCellRenderer getCenteredRenderer() {
        return centeredRenderer;
    }

    protected boolean isDecimal() {
        return isDecimal;
    }

    public void scrollToRow(int rowNumber) {
        int rowHeight = getRowHeight();
        int parentHeight = getParent().getHeight();
        Rectangle rect = new Rectangle(0, (rowNumber - 1) * rowHeight + parentHeight, getWidth(), rowHeight);
        scrollRectToVisible(rect);
    }

    @Override
    public abstract Class<?> getColumnClass(int col);

    public abstract String getAddressAtRow(int row);

    public abstract String getValueAtRow(int row);

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
