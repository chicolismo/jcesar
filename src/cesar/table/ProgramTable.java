package cesar.table;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class ProgramTable extends Table {
	private static final long serialVersionUID = 2477981247131807536L;
	
	private int programCounterRow = 0;

	private static final Class<?>[] COLUMN_CLASSES = new Class[] { String.class, Short.class, Byte.class,
			String.class };

	public ProgramTable() {
		super();
		setModel(new ProgramTable.ProgramTableModel());

		setProgramCounterRow(0);

		TableColumnModel columnModel = getColumnModel();
		TableColumn column;

		// PC
		column = columnModel.getColumn(0);
		column.setMaxWidth(34);
		column.setWidth(column.getPreferredWidth());
		column.setResizable(false);

		// Endereço
		column = columnModel.getColumn(1);
		column.setMaxWidth(70);
		column.setWidth(column.getPreferredWidth());
		column.setResizable(false);

		// Dado
		column = columnModel.getColumn(2);
		column.setMaxWidth(70);
		column.setWidth(column.getPreferredWidth());
		column.setResizable(false);
		
	}
	
	public void setProgramCounterRow(int row) {
	    int oldValue = programCounterRow;
	    programCounterRow = row;
	    
	    //
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int col) {
		switch (col) {
		case 0:
			return getCenteredRenderer();
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
	public String getAddressAtRow(int row) {
		return String.valueOf(getValueAt(row, 1));
	}

	@Override
	public String getValueAtRow(int row) {
		return String.valueOf(getValueAt(row, 2));
	}

	private static class ProgramTableModel extends Table.TableModel {
		private static final long serialVersionUID = -7167972103457974892L;

		ProgramTableModel() {
			columnNames = new String[] { "PC", "Endereço", "Dados", "Mnemônico" };
			data = new Object[MEMORY_SIZE][4];
			for (int i = 0; i < MEMORY_SIZE; ++i) {
				data[i] = new Object[] { "", (short) i, (byte) 0, "" };
			}
		}
	}
}
