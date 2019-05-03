package cesar;

import cesar.cpu.CPU;
import cesar.panel.DisplayPanel;
import cesar.panel.SidePanel;
import cesar.table.DataTable;
import cesar.table.ProgramTable;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Controller {

    private final Window parent;
    private final JFileChooser fileChooser;
    private final SidePanel programPanel;
    private final SidePanel dataPanel;
    private final DisplayPanel displayPanel;
    private final MenuBar menuBar;
    private final CPU cpu;

    public Controller(Window parent, SidePanel programPanel, SidePanel dataPanel, DisplayPanel displayPanel, MenuBar menuBar) {
        this.parent = parent;
        this.programPanel = programPanel;
        this.dataPanel = dataPanel;
        this.displayPanel = displayPanel;
        this.menuBar = menuBar;
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos de memória", "mem");
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);

        cpu = new CPU();
        cpu.setDataTable((DataTable) dataPanel.getTable());
        cpu.setProgramTable((ProgramTable) programPanel.getTable());
        cpu.setDisplayPanel(displayPanel);
        initMenuBarEvents();
    }

    private void openFile() {
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileInputStream inputStream = new FileInputStream(file)) {
                int size = (int) file.length();
                byte[] buffer = new byte[size];
                inputStream.readNBytes(buffer, 0, size);
                cpu.setBytes(buffer);
            } catch (IOException e) {
                String message = String.format("Erro ao tentar ler o arquivo %s.", file.getPath());
                JOptionPane.showMessageDialog(parent, message, "Erro de leitura", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void initMenuBarEvents() {
        menuBar.getMenuItemOpenFile().addActionListener(event -> this.openFile());
        menuBar.getMenuItemSaveFile().addActionListener(event -> this.saveFile());
        menuBar.getMenuItemSaveFileAs().addActionListener(event -> this.saveFileAs());
        menuBar.getMenuItemExit().addActionListener(event -> this.exit());
    }

    private void saveFile() {
    }

    private void saveFileAs() {

    }

    private void exit() {
        parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
    }
}
