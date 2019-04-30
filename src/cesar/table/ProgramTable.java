package cesar.table;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class ProgramTable extends Table {
	private static final long serialVersionUID = 2477981247131807536L;

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
		column.setMaxWidth(30);
		column.setCellRenderer(CENTER_RENDERER);

		// Endereço
		column = columnModel.getColumn(1);
		column.setMaxWidth(80);
		column.setCellRenderer(RIGHT_RENDERER);

		// Dado
		column = columnModel.getColumn(2);
		column.setMaxWidth(80);
		column.setCellRenderer(RIGHT_RENDERER);

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
