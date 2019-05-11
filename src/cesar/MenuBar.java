package cesar;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

class MenuBar extends JMenuBar {
    private static final long serialVersionUID = -687929457632692467L;

    private JMenuItem menuItemLoadFile;
    private JMenuItem menuItemSaveFile;
    private JMenuItem menuItemSaveFileAs;
    private JMenuItem menuItemExit;

    private JMenuItem menuItemAbout;

    MenuBar(Window parent) {
        setBorderPainted(false);

        JMenu fileMenu = new JMenu("Arquivo");
        JMenu editMenu = new JMenu("Editar");
        JMenu viewMenu = new JMenu("Visualizar");
        JMenu helpMenu = new JMenu("?");

        fileMenu.setMnemonic('a');
        editMenu.setMnemonic('e');
        viewMenu.setMnemonic('v');
        helpMenu.setMnemonic('h');

        menuItemLoadFile   = new JMenuItem("Carregar...", KeyEvent.VK_C);
        menuItemSaveFile   = new JMenuItem("Salvar", KeyEvent.VK_S);
        menuItemSaveFileAs = new JMenuItem("Salvar como...", KeyEvent.VK_V);
        menuItemExit       = new JMenuItem("Sair", KeyEvent.VK_R);

        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
//        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        menuItemLoadFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, mask));
        menuItemSaveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, mask));
        menuItemSaveFileAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, mask | InputEvent.SHIFT_DOWN_MASK));
        menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, mask));

        fileMenu.add(getMenuItemLoadFile());
        fileMenu.add(getMenuItemSaveFile());
        fileMenu.add(getMenuItemSaveFileAs());
        fileMenu.addSeparator();
        fileMenu.add(getMenuItemExit());

        menuItemAbout = new JMenuItem("Sobre", KeyEvent.VK_S);

        helpMenu.add(menuItemAbout);

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

    public JMenuItem getMenuItemAbout() {
        return menuItemAbout;
    }
}
