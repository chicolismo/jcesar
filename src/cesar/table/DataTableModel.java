package cesar.table;

// TODO: Adicionar um render para as colunas de acordo com a visualização selecionada
public class DataTableModel extends TableModel {
	private static final long serialVersionUID = -1657410804127435495L;
	
	public DataTableModel() {
		this.columnNames = new String[] { "Endereço", "Dado" };
		this.data = new Object[MEMORY_SIZE][2];

		for (int i = 0; i < MEMORY_SIZE; ++i) {
			this.data[i] = new Object[] { i, 0 };
		}
	}
}