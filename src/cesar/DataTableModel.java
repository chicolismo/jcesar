package cesar;

// TODO: Adicionar um render para as colunas de acordo com a visualização selecionada
public class DataTableModel extends TableModel {
	private static final long serialVersionUID = -1657410804127435495L;
	
	public DataTableModel() {
		columnNames = new String[] { "Endereço", "Dado" };
		data = new Object[MEMORY_SIZE][2];
		for (int i = 0; i < MEMORY_SIZE; ++i) {
			data[i] = new Object[] { i, 0 };
		}
	}
}