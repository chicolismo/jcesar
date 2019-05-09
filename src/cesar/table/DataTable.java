package cesar.table;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class DataTable extends Table {
    private static final long serialVersionUID = -3030887157789883813L;

    private static final Class<?>[] COLUMN_CLASSES = new Class[] { Short.class, Byte.class };

    public DataTable() {
        super();
        setModel(new DataTable.DataTableModel());

        TableColumnModel columnModel = getColumnModel();
        TableColumn column;

        // Endereço
        column = columnModel.getColumn(0);
        column.setWidth(column.getPreferredWidth());
        column.setResizable(false);

        // Dados
        column = columnModel.getColumn(1);
        column.setWidth(column.getPreferredWidth());
        column.setResizable(false);
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int col) {
        switch (col) {
        case 0:
            return isDecimal() ? getDecimalShortRenderer() : getHexadecimalShortRenderer();
        case 1:
            return isDecimal() ? getDecimalByteRenderer() : getHexadecimalByteRenderer();
        default:
            System.err.println("Erro na hora de obter o table renderer. Isto não deve executar!");
            return getDefaultRenderer();
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return COLUMN_CLASSES[column];
    }

    @Override
    public String getAddressAtRow(int row) {
        return String.valueOf(getValueAt(row, 0));
    }

    @Override
    public String getValueAtRow(int row) {
        return String.valueOf(getValueAt(row, 1));
    }

    private static class DataTableModel extends Table.TableModel {
        private static final long serialVersionUID = -1657410804127435495L;

        DataTableModel() {
            this.columnNames = new String[] { "Endereço", "Dado" };
            this.data        = new Object[MEMORY_SIZE][2];

            for (int i = 0; i < MEMORY_SIZE; ++i) {
                this.data[i] = new Object[] { (short) i, (byte) 0 };
            }
        }
    }
}
