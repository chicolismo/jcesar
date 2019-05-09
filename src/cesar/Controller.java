package cesar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import cesar.cpu.CPU;
import cesar.cpu.CPUException;
import cesar.panel.ButtonPanel;
import cesar.panel.DisplayPanel;
import cesar.panel.MainPanel;
import cesar.panel.RegisterPanel;
import cesar.panel.SidePanel;
import cesar.table.DataTable;
import cesar.table.ProgramTable;
import cesar.util.Shorts;

public class Controller {
    private boolean isRunning;
    private boolean isDecimal;

    private final Window parent;
    private final CPU cpu;
    private final MenuBar menuBar;
    private final MainPanel mainPanel;
    private final SidePanel programPanel;
    private final SidePanel dataPanel;
    private final DisplayPanel displayPanel;
    private final JFileChooser fileChooser;

    public Controller(Window parent, MainPanel mainPanel, SidePanel programPanel, SidePanel dataPanel,
        DisplayPanel displayPanel, MenuBar menuBar) {

        this.isRunning    = false;
        this.isDecimal    = true;
        this.parent       = Objects.requireNonNull(parent);
        this.mainPanel    = Objects.requireNonNull(mainPanel);
        this.programPanel = Objects.requireNonNull(programPanel);
        this.dataPanel    = Objects.requireNonNull(dataPanel);
        this.displayPanel = Objects.requireNonNull(displayPanel);
        this.menuBar      = Objects.requireNonNull(menuBar);
        this.fileChooser  = new JFileChooser();
        this.fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos de memória", "mem"));

        // TODO: remover
        this.fileChooser.setCurrentDirectory(new File("/Users/chico/Projects/"));

        this.cpu = new CPU();
        this.cpu.setDataTable((DataTable) this.dataPanel.getTable());
        this.cpu.setProgramTable((ProgramTable) this.programPanel.getTable());
        this.cpu.setDisplayPanel(this.displayPanel);
        this.cpu.setRegisterPanels(this.mainPanel.getRegisters());
        this.cpu.setConditionsPanel(this.mainPanel.getConditionsPanel());

        initMenuBarEvents();
        initSidePanelsEvents();
        initButtonEvents();
        initRegisterPanelsEvents();
    }

    private synchronized boolean isRunning() {
        return isRunning;
    }

    private synchronized void setRunning(boolean state) {
        isRunning = state;
    }

    private void openFile() {
        parent.toFront();
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileInputStream inputStream = new FileInputStream(file)) {
                int size = (int) file.length();
                byte[] buffer = new byte[size];
                // inputStream.readNBytes(buffer, 0, size);
                inputStream.read(buffer, 0, size);
                cpu.setBytes(buffer);
            }
            catch (IOException e) {
                String message = String.format("Erro ao tentar ler o arquivo %s.", file.getPath());
                JOptionPane.showMessageDialog(parent, message, "Erro de leitura", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void initMenuBarEvents() {
        menuBar.getMenuItemLoadFile().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Controller.this.openFile();
            }
        });
        menuBar.getMenuItemSaveFile().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Controller.this.saveFile();
            }
        });
        menuBar.getMenuItemSaveFileAs().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Controller.this.saveFileAs();
            }
        });
        menuBar.getMenuItemExit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Controller.this.exit();
            }
        });

        menuBar.getMenuItemAbout().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Controller.this.showAbout();
            }
        });
    }

    private void initButtonEvents() {
        final RegisterPanel[] registerPanels = mainPanel.getRegisters();
        final ButtonPanel buttons = mainPanel.getButtonPanel();
        final JToggleButton btnDecimal = buttons.getBtnDecimal();
        final JToggleButton btnHexadecimal = buttons.getBtnHexadecimal();
        final JToggleButton btnRun = buttons.getBtnRun();
        final JButton btnNext = buttons.getBtnNext();

        // Botão de base 10 começa selecionado.
        btnDecimal.setSelected(true);

        btnDecimal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!btnDecimal.isSelected()) {
                    return;
                }
                isDecimal = true;
                for (RegisterPanel panel : registerPanels) {
                    panel.setDecimal(true);
                    panel.repaint();
                }
                programPanel.setDecimal(true);
                dataPanel.setDecimal(true);
            }
        });

        btnHexadecimal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!btnHexadecimal.isSelected()) {
                    return;
                }
                isDecimal = false;
                for (RegisterPanel panel : registerPanels) {
                    panel.setDecimal(false);
                    panel.repaint();
                }
                programPanel.setDecimal(false);
                dataPanel.setDecimal(false);
            }
        });

        final ProgramTable table = (ProgramTable) programPanel.getTable();
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // Só funciona se o programa não estiver rodando automaticamente.
                if (!btnRun.isSelected()) {
                    try {
                        cpu.executeNextInstruction();
                        short address = cpu.getPC();
                        table.setProgramCounterRow(Shorts.toUnsignedInt(address));
                    }
                    catch (CPUException e) {
                        e.printStackTrace();
                        System.err.println(cpu.toString());
                    }
                }
            }
        });

        // O botão de roda deve ser tipo toggle. Quando apertado, dispara uma thread
        // que chama "executeNextInstruction" do cpu e testa se deve parar.
        // A thread usará uma função sincronizada para consultar a flag.
        //
        // Quando o botão é apertado novamente, altera uma flag que fará a thread parar.
        // Tem que usar uma função sincronizada para alterar essa flag.
        btnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (btnRun.isSelected()) {
                    // Executa uma thread que consulta se deve parar.
                    // Isso deve ser feito de forma sincronizada.
                    setRunning(true);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (isRunning()) {
                                try {
                                    cpu.executeNextInstruction();
                                }
                                catch (CPUException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    thread.start();
                }
                else {
                    // Desliga a flag e consequentemente termina a thread que executa o programa
                    // automaticamente.
                    setRunning(false);
                    short address = cpu.getPC();
                    table.setProgramCounterRow(Shorts.toUnsignedInt(address));
                }
            }
        });
    }

    private void initSidePanelsEvents() {
        final ProgramTable table = (ProgramTable) programPanel.getTable();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                // Se der um clique duplo em alguma linha da tabela de programa,
                // a linha clicada passa a indicar o PC.
                if (event.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        cpu.setPC((short) (0xFFFF & selectedRow));
                        cpu.updateRegisterDisplays();
                        table.setProgramCounterRow(selectedRow);
                    }
                }
            }
        });
    }

    private void initRegisterPanelsEvents() {
        RegisterPanel[] registerPanels = mainPanel.getRegisters();
        for (int index = 0; index < registerPanels.length; ++index) {
            addEventsToRegisterPanel(registerPanels[index], index);
        }
    }

    private void addEventsToRegisterPanel(final RegisterPanel panel, final int index) {
        final String decMessage = String.format("Digite um valor decimal para %s", panel.getTitle());
        final String hexMessage = String.format("Digite um valor hexadecimal para %s", panel.getTitle());
        final ProgramTable table = (ProgramTable) programPanel.getTable();

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    String message = isDecimal ? decMessage : hexMessage;
                    String currentValue = isDecimal ? Integer.toString(panel.getValue())
                        : Integer.toHexString(panel.getValue());
                    String newValue = JOptionPane.showInputDialog(panel, message, currentValue);
                    if (newValue != null) {
                        try {
                            int value = 0xFFFF & Integer.parseInt(newValue, isDecimal ? 10 : 16);
                            panel.setValue(value);
                            cpu.setRegister(index, (short) value);
                            short address = cpu.getPC();
                            table.setProgramCounterRow(Shorts.toUnsignedInt(address));
                        }
                        catch (NumberFormatException e) {
                            // e.printStackTrace();
                            JOptionPane.showMessageDialog(panel, "Valor inválido", "Atenção",
                                JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private void saveFile() {
    }

    private void saveFileAs() {

    }

    private void exit() {
        parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
    }

    private void showAbout() {
        String version = System.getProperty("java.version");
        String message = String.format("Versão do Java: %s", version);
        JOptionPane.showMessageDialog(parent, message);
    }
}
