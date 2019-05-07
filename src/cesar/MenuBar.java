package cesar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

class MenuBar extends JMenuBar {
    private static final long serialVersionUID = -687929457632692467L;

    private Window parent;

    private JMenuItem menuItemLoadFile;
    private JMenuItem menuItemSaveFile;
    private JMenuItem menuItemSaveFileAs;
    private JMenuItem menuItemExit;

    MenuBar(Window parent) {
        this.parent = parent;
        var fileMenu = new JMenu("Arquivo");
        var editMenu = new JMenu("Editar");
        var viewMenu = new JMenu("Visualizar");
        var helpMenu = new JMenu("?");

        fileMenu.setMnemonic('a');
        editMenu.setMnemonic('e');
        viewMenu.setMnemonic('v');
        helpMenu.setMnemonic('?');

        menuItemLoadFile = new JMenuItem("Carregar...", KeyEvent.VK_C);
        menuItemSaveFile = new JMenuItem("Salvar", KeyEvent.VK_S);
        menuItemSaveFileAs = new JMenuItem("Salvar como...", KeyEvent.VK_V);
        menuItemExit = new JMenuItem("Sair", KeyEvent.VK_R);

        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        menuItemLoadFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, mask));
        menuItemSaveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, mask));
        menuItemSaveFileAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, mask | InputEvent.SHIFT_DOWN_MASK));
        menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, mask));

        fileMenu.add(getMenuItemLoadFile());
        fileMenu.add(getMenuItemSaveFile());
        fileMenu.add(getMenuItemSaveFileAs());
        fileMenu.addSeparator();
        fileMenu.add(getMenuItemExit());

        this.add(fileMenu);
        this.add(editMenu);
        this.add(viewMenu);
        this.add(helpMenu);
    }

    public JMenuItem getMenuItemLoadFile() {
        return menuItemLoadFile;
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
