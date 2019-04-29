package cesar.table;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class Table extends JTable {
	private static final long serialVersionUID = 5510916208167787328L;
	
	protected static class HeaderRenderer implements TableCellRenderer {
		private DefaultTableCellRenderer renderer;
		
		public HeaderRenderer(JTable table) { 
			renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
			renderer.setHorizontalAlignment(JLabel.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
	
	public Table() {
		JTableHeader header = getTableHeader();
		header.setDefaultRenderer(new HeaderRenderer(this));

		ListSelectionModel selectionModel = getSelectionModel();
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		setDoubleBuffered(true);
		setColumnSelectionAllowed(false);
		setFont(new Font("monospaced", Font.PLAIN, 12));
	}
}
