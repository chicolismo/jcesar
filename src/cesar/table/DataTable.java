package cesar.table;

public class DataTable extends Table {
	private static final long serialVersionUID = -3030887157789883813L;

	public DataTable() {
		super();
		setModel(new DataTableModel());
	}
}
