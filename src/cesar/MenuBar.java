package cesar;

import javax.swing.*;

class MenuBar extends JMenuBar {
	private static final long serialVersionUID = -687929457632692467L;

	private JMenuItem menuItemOpenFile;
	private JMenuItem menuItemSaveFile;
	private JMenuItem menuItemSaveFileAs;
	private JMenuItem menuItemExit;

	MenuBar() {
		var fileMenu = new JMenu("Arquivo");
		var editMenu = new JMenu("Editar");
		var viewMenu = new JMenu("Visualizar");
		var helpMenu = new JMenu("?");

		menuItemOpenFile = new JMenuItem("Abrir...");
		menuItemSaveFile = new JMenuItem("Salvar");
		menuItemSaveFileAs = new JMenuItem("Salvar como...");
		menuItemExit = new JMenuItem("Sair");

		fileMenu.add(getMenuItemOpenFile());
		fileMenu.add(getMenuItemSaveFile());
		fileMenu.add(getMenuItemSaveFileAs());
		fileMenu.addSeparator();
		fileMenu.add(getMenuItemExit());

		this.add(fileMenu);
		this.add(editMenu);
		this.add(viewMenu);
		this.add(helpMenu);
	}

	public JMenuItem getMenuItemOpenFile() {
		return menuItemOpenFile;
	}

	public JMenuItem getMenuItemSaveFile() {
		return menuItemSaveFile;
	}

	public JMenuItem getMenuItemSaveFileAs() {
		return menuItemSaveFileAs;
	}

	public JMenuItem getMenuItemExit() {
		return menuItemExit;
	}
}
