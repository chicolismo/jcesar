package cesar;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser extends JFileChooser {
	private static final long serialVersionUID = -9063819185165301860L;

	public FileChooser() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos de memória", "mem");
		this.addChoosableFileFilter(filter);
	}
}
