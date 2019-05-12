package cesar;

import java.awt.Event;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import cesar.cpu.HaltedException;
import cesar.panel.ButtonPanel;
import cesar.panel.DisplayPanel;
import cesar.panel.MainPanel;
import cesar.panel.RegisterPanel;
import cesar.panel.SidePanel;
import cesar.table.DataTable;
import cesar.table.ProgramTable;
import cesar.table.Table;
import cesar.util.Shorts;

public class Controller {
    private static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("os x");

    private boolean isRunning;
    private boolean isDecimal;
    private boolean waitingKey;

    private final Window parent;
    private final CPU cpu;
    private final MenuBar menuBar;
    private final MainPanel mainPanel;
    private final SidePanel programPanel;
    private final SidePanel dataPanel;
    private final DisplayPanel displayPanel;
    private final JFileChooser fileChooser;

    private final JToggleButton btnDecimal;
    private final JToggleButton btnHexadecimal;
    private final JToggleButton btnRun;
    private final JButton btnNext;

    public Controller(Window parent, MainPanel mainPanel, SidePanel programPanel, SidePanel dataPanel,
        DisplayPanel displayPanel, MenuBar menuBar) {

        this.isRunning    = false;
        this.isDecimal    = true;
        this.waitingKey   = false;
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

        ((ProgramTable) programPanel.getTable()).setProgramCounterRow(0);

        ButtonPanel buttons = mainPanel.getButtonPanel();
        this.btnDecimal     = buttons.getBtnDecimal();
        this.btnHexadecimal = buttons.getBtnHexadecimal();
        this.btnRun         = buttons.getBtnRun();
        this.btnNext        = buttons.getBtnNext();

        initMenuBarEvents();
        initSidePanelsEvents();
        initButtonEvents();
        initRegisterPanelsEvents();
        initKeyboardEvents();
    }


    private synchronized boolean isRunning() {
        return isRunning;
    }


    private synchronized void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
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

        String key = IS_MAC ? Character.toString((char) 0x2318) : "Ctrl";
        btnDecimal.setToolTipText(String.format("Decimal %s+D", key)); // Ctrl-D
        btnHexadecimal.setToolTipText(String.format("Hexadecimal %s+H", key)); // Ctrl-H
        btnRun.setToolTipText("Rodar F9"); // F9
        btnNext.setToolTipText("Passo-a-passo F8"); // F8

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

        final ProgramTable programTable = (ProgramTable) programPanel.getTable();

        btnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnRun.isSelected()) {
                    // Executa uma thread que consulta se deve parar. Isso deve ser feito de forma
                    // sincronizada.
                    setRunning(true);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (isRunning()) {
                                try {
                                    cpu.executeNextInstruction();
                                }
                                catch (HaltedException e) {
                                    btnRun.doClick();
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
                    programTable.setProgramCounterRow(Shorts.toUnsignedInt(address));
                }
            }
        });

        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // Só funciona se o programa não estiver rodando automaticamente.
                if (!btnRun.isSelected()) {
                    try {
                        cpu.executeNextInstruction();
                        short address = cpu.getPC();
                        programTable.setProgramCounterRow(Shorts.toUnsignedInt(address));
                    }
                    catch (HaltedException e) {
                        setRunning(false);
                    }
                    catch (CPUException e) {
                        e.printStackTrace();
                        System.err.println(cpu.toString());
                    }
                }
            }
        });
    }


    private void initSidePanelsEvents() {
        final ProgramTable table = (ProgramTable) programPanel.getTable();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                // Se der um clique duplo em alguma linha da tabela de programa, a linha clicada
                // passa a indicar o PC.
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

        for (final SidePanel panel : new SidePanel[] { programPanel, dataPanel }) {
            panel.getTextField().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    String addressText = panel.getLabel().getText();
                    String valueText = panel.getTextField().getText();
                    try {
                        short address = (short) Integer.parseInt(addressText, isDecimal ? 10 : 16);
                        byte value = (byte) Integer.parseInt(valueText, isDecimal ? 10 : 16);
                        cpu.getMemory().writeByte(address, value, false);
                    }
                    catch (NumberFormatException e) {
                        // NOP
                    }
                    Table table = panel.getTable();
                    table.selectRow(table.getSelectedRow() + 1);
                }
            });
        }
    }


    private void initRegisterPanelsEvents() {
        RegisterPanel[] registerPanels = mainPanel.getRegisters();
        for (int index = 0; index < registerPanels.length; ++index) {
            addEventsToRegisterPanel(registerPanels[index], index);
        }
    }


    /*
     * Quando houver um clique duplo em cima de algum dos registradores, será
     * exibido um diálogo para ler o novo valor desse registrador. Esse novo valor
     * deverá ser repassado para o cpu.
     * 
     * O valor digitado deverá respeitar a base numérica sendo exibida atualmente. A
     * base numérica esperada será informada no diálogo. Caso o texto digitado não
     * seja um valor válido para a representação em vigor, será exibida uma mensagem
     * de erro e o valor original será usado. Se o usuário cancelar, também será
     * preservado o valor atual.
     */
    private void addEventsToRegisterPanel(final RegisterPanel panel, final int registerNumber) {
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
                            cpu.setRegister(registerNumber, (short) value);
                            short address = cpu.getPC();
                            table.setProgramCounterRow(Shorts.toUnsignedInt(address));
                        }
                        catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(panel, "Valor inválido", "Atenção",
                                JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        });
    }


    private synchronized boolean isWaitingKey() {
        return waitingKey;
    }


    private synchronized void setWaitingKey(boolean isWaitingKey) {
        this.waitingKey = isWaitingKey;
    }


    private void initKeyboardEvents() {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                int id = e.getID();
                int keyCode = e.getKeyCode();
                boolean commandDown = (e.isControlDown() || e.isMetaDown());

                if (id == Event.KEY_RELEASE && isWaitingKey()) {
                    // TODO: Enviar tecla para a CPU
                    setWaitingKey(false);
                }
                else if (keyCode == KeyEvent.VK_D && commandDown) {
                    btnDecimal.doClick();
                }
                else if (keyCode == KeyEvent.VK_H && commandDown) {
                    btnHexadecimal.doClick();
                }
                else if (keyCode == KeyEvent.VK_F9) {
                    btnRun.doClick();
                }
                else if (keyCode == KeyEvent.VK_F8) {
                    btnNext.doClick();
                }
                return false;
            }
        });
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
