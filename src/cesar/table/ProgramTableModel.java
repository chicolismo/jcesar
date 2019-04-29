package cesar.table;

public class ProgramTableModel extends TableModel {
	private static final long serialVersionUID = -7167972103457974892L;

	public ProgramTableModel() {
		columnNames = new String[] { "PC", "Endereço", "Dados", "Mnemônico" };
		data = new Object[MEMORY_SIZE][4];
		for (int i = 0; i < MEMORY_SIZE; ++i) {
			data[i] = new Object[] { "", i, 0, "" };
		}
	}
}
