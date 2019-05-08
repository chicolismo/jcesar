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
	private final TableCellRenderer centeredRenderer;
	private final TableCellRenderer hexadecimalByteRenderer;
	private final TableCellRenderer decimalByteRenderer;
	private final TableCellRenderer hexadecimalShortRenderer;
	private final TableCellRenderer decimalShortRenderer;
	private final TableCellRenderer defaultRenderer;

	Table() {
		defaultRenderer = new DefaultTableCellRenderer();
		centeredRenderer = new CenteredTableCellRenderer();
		decimalByteRenderer = new DecimalByteRenderer();
		hexadecimalByteRenderer = new HexadecimalByteRenderer();
		decimalShortRenderer = new DecimalShortRenderer();
		hexadecimalShortRenderer = new HexadecimalShortRenderer();
		isDecimal = true;

		setDoubleBuffered(true);
		setColumnSelectionAllowed(false);

		JTableHeader header = getTableHeader();
		header.setDefaultRenderer(new CenteredTableCellRenderer());
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
		var rect = new Rectangle(0, (rowNumber - 1) * rowHeight + parentHeight, getWidth(), rowHeight);
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

	protected class CenteredTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 5810811798031094957L;

		public CenteredTableCellRenderer() {
			super();
			setHorizontalAlignment(JLabel.CENTER);
		}
	}

	protected class DecimalByteRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -8860753344065220474L;

		public DecimalByteRenderer() {
			super();
			setHorizontalAlignment(JLabel.RIGHT);
		}

		@Override
		public void setValue(Object value) {
			assert value instanceof Byte;
			setText(Integer.toString(Byte.toUnsignedInt((byte) value)));
		}
	}

	protected class HexadecimalByteRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 5810811798031094957L;

		public HexadecimalByteRenderer() {
			super();
			setHorizontalAlignment(JLabel.RIGHT);
		}

		@Override
		public void setValue(Object value) {
			assert value instanceof Byte;
			setText(Integer.toHexString(Byte.toUnsignedInt((Byte) value)));
		}
	}

	protected class DecimalShortRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -8860753344065220474L;

		public DecimalShortRenderer() {
			super();
			setHorizontalAlignment(JLabel.RIGHT);
		}

		@Override
		public void setValue(Object value) {
			assert value instanceof Short;
			setText(Integer.toString(Short.toUnsignedInt((short) value)));
		}
	}

	protected class HexadecimalShortRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 5810811798031094957L;

		public HexadecimalShortRenderer() {
			super();
			setHorizontalAlignment(JLabel.RIGHT);
		}

		@Override
		public void setValue(Object value) {
			assert value instanceof Short;
			setText(Integer.toHexString(Short.toUnsignedInt((Short) value)));
		}
	}
}
