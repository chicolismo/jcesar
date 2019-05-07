package cesar.table;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class ProgramTable extends Table {
	private static final long serialVersionUID = 2477981247131807536L;

	private static final Class<?>[] COLUMN_CLASSES = new Class[] { String.class, Integer.class, Integer.class, String.class };

	private static class ProgramTableModel extends Table.TableModel {
		private static final long serialVersionUID = -7167972103457974892L;

		ProgramTableModel() {
			columnNames = new String[] { "PC", "Endereço", "Dados", "Mnemônico" };
			data = new Object[MEMORY_SIZE][4];
			for (int i = 0; i < MEMORY_SIZE; ++i) {
				data[i] = new Object[] { "", i, 0, "" };
			}
		}
	}

	public ProgramTable() {
		super();
		setModel(new ProgramTable.ProgramTableModel());

		TableColumnModel columnModel = getColumnModel();
		TableColumn column;

		// PC
		column = columnModel.getColumn(0);
		column.setMaxWidth(34);
		column.setWidth(column.getPreferredWidth());
		column.setCellRenderer(CENTER_RENDERER);
		column.setResizable(false);

		// Endereço
		column = columnModel.getColumn(1);
		column.setMaxWidth(70);
		column.setWidth(column.getPreferredWidth());
		column.setCellRenderer(RIGHT_RENDERER);
		column.setResizable(false);

		// Dado
		column = columnModel.getColumn(2);
		column.setMaxWidth(70);
		column.setWidth(column.getPreferredWidth());
		column.setCellRenderer(RIGHT_RENDERER);
		column.setResizable(false);

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
}
