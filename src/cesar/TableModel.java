package cesar;

import javax.swing.table.AbstractTableModel;

public abstract class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = 8671470346716518079L;
	
	public static final int MEMORY_SIZE = 1 << 16;
	protected String[] columnNames;
	protected Object[][] data;
	
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

}
