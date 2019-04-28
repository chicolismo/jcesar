package cesar;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ApplicationMenu extends JMenuBar {
	private static final long serialVersionUID = -687929457632692467L;

	private Window parent;

	public ApplicationMenu(Window parent) {
		this.parent = parent;
		initFileMenu();
	}

	private void initFileMenu() {
		var controller = parent.getController();

		var fileMenu = new JMenu("Arquivo");

		var menuItemOpenFile = new JMenuItem("Abrir...");
		menuItemOpenFile.addActionListener(event -> controller.openFile());

		var menuItemSaveFile = new JMenuItem("Salvar");
		menuItemSaveFile.addActionListener(event -> controller.saveFile());

		var menuItemSaveFileAs = new JMenuItem("Salvar como...");
		menuItemSaveFileAs.addActionListener(event -> controller.saveFileAs());

		var menuItemExit = new JMenuItem("Sair");
		menuItemExit.addActionListener(event -> controller.exit());

		fileMenu.add(menuItemOpenFile);
		fileMenu.add(menuItemSaveFile);
		fileMenu.add(menuItemSaveFileAs);
		fileMenu.addSeparator();
		fileMenu.add(menuItemExit);

		this.add(fileMenu);
	}
}
