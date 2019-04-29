package cesar.table;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class ProgramTable extends Table {
	private static final long serialVersionUID = 2477981247131807536L;

	public ProgramTable() {
		super();
		setModel(new ProgramTableModel());

		TableColumnModel columnModel = getColumnModel();
		TableColumn column;
		column = columnModel.getColumn(0);
		column.setMaxWidth(30);

		column = columnModel.getColumn(1);
		column.setMaxWidth(80);

		column = columnModel.getColumn(2);
		column.setMaxWidth(80);
	}
}
