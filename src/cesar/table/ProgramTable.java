package cesar.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class ProgramTable extends Table {
    private static final long serialVersionUID = 2477981247131807536L;

    private TableCellRenderer pcColumnRenderer;
    private int programCounterRow = 0;

    private static final Class<?>[] COLUMN_CLASSES = new Class[] { String.class, Short.class, Byte.class,
        String.class };

    public ProgramTable() {
        super();
        setModel(new ProgramTable.ProgramTableModel());

        TableColumnModel columnModel = getColumnModel();
        TableColumn column;

        // PC
        column = columnModel.getColumn(0);
        column.setMaxWidth(26);
        column.setWidth(column.getPreferredWidth());
        column.setResizable(false);

        // Endereço
        column = columnModel.getColumn(1);
        column.setMaxWidth(64);
        column.setWidth(column.getPreferredWidth());
        column.setResizable(false);

        // Dado
        column = columnModel.getColumn(2);
        column.setMaxWidth(46);
        column.setWidth(column.getPreferredWidth());
        column.setResizable(false);

        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent event) {
                setProgramCounterRow(0);
            }
        });

        DefaultTableCellRenderer pcColumnRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 4346935574861281970L;

            private final Color selectedColor = new Color(0x00FF00);
            private final Color unselectedColor = new Color(0x007F00);
            private final Font font = new Font(Font.MONOSPACED, Font.BOLD, 14);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(font);
                setHorizontalAlignment(JLabel.CENTER);
                setForeground(isSelected ? selectedColor : unselectedColor);
                return this;
            }
        };
        this.pcColumnRenderer = pcColumnRenderer;
    }

    public void setProgramCounterRow(int row) {
        int oldRow = programCounterRow;
        programCounterRow = row;
        setValueAtAndUpdate("", oldRow, 0);
        setValueAtAndUpdate("➔", row, 0);
        setRowSelectionInterval(row, row);
        scrollToRow(row, false);
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int col) {
        switch (col) {
        case 0:
            return pcColumnRenderer;
        case 1:
            return isDecimal() ? getDecimalShortRenderer() : getHexadecimalShortRenderer();
        case 2:
            return isDecimal() ? getDecimalByteRenderer() : getHexadecimalByteRenderer();
        default:
            return getDefaultRenderer();
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return COLUMN_CLASSES[column];
    }

    @Override
    public short getAddressAtRow(int row) {
        return (short) getValueAt(row, 1);
    }

    @Override
    public byte getValueAtRow(int row) {
        return (byte) getValueAt(row, 2);
    }

    private static class ProgramTableModel extends Table.TableModel {
        private static final long serialVersionUID = -7167972103457974892L;

        ProgramTableModel() {
            columnNames = new String[] { "PC", "Endereço", "Dado", "Mnemônico" };
            data        = new Object[MEMORY_SIZE][4];
            for (int i = 0; i < MEMORY_SIZE; ++i) {
                data[i] = new Object[] { "", (short) i, (byte) 0, "" };
            }
        }
    }
}
