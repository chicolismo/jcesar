package cesar;

import cesar.cpu.CPU;
import cesar.cpu.InvalidInstructionException;
import cesar.panel.DisplayPanel;
import cesar.panel.MainPanel;
import cesar.panel.RegisterPanel;
import cesar.panel.SidePanel;
import cesar.table.DataTable;
import cesar.table.ProgramTable;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class Controller {
	private boolean isRunning;
	private boolean isDecimal;

	private final Window parent;
	private final JFileChooser fileChooser;
	private final MainPanel mainPanel;
	private final RegisterPanel[] registerPanels;
	private final SidePanel programPanel;
	private final SidePanel dataPanel;
	private final DisplayPanel displayPanel;
	private final MenuBar menuBar;
	private final CPU cpu;

	public Controller(Window parent, MainPanel mainPanel, SidePanel programPanel, SidePanel dataPanel,
			DisplayPanel displayPanel, MenuBar menuBar) {

		this.isRunning = false;
		this.isDecimal = true;
		this.parent = Objects.requireNonNull(parent);
		this.mainPanel = Objects.requireNonNull(mainPanel);
		this.registerPanels = Objects.requireNonNull(mainPanel.getRegisters());
		this.programPanel = Objects.requireNonNull(programPanel);
		this.dataPanel = Objects.requireNonNull(dataPanel);
		this.displayPanel = Objects.requireNonNull(displayPanel);
		this.menuBar = Objects.requireNonNull(menuBar);
		this.fileChooser = new JFileChooser();
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
		menuBar.getMenuItemLoadFile().addActionListener(event -> this.openFile());
		menuBar.getMenuItemSaveFile().addActionListener(event -> this.saveFile());
		menuBar.getMenuItemSaveFileAs().addActionListener(event -> this.saveFileAs());
		menuBar.getMenuItemExit().addActionListener(event -> this.exit());
	}

	private void initButtonEvents() {
		var buttons = mainPanel.getButtonPanel();

		JToggleButton btnDecimal = buttons.getBtnDecimal();
		JToggleButton btnHexadecimal = buttons.getBtnHexadecimal();
		JToggleButton btnRun = buttons.getBtnRun();
		JButton btnNext = buttons.getBtnNext();

		// Botão de base 10 começa selecionado.
		btnDecimal.setSelected(true);

		btnDecimal.addActionListener((event) -> {
			if (!btnDecimal.isSelected()) {
				return;
			}
			isDecimal = true;
			for (var panel : registerPanels) {
				panel.setDecimal(true);
				panel.repaint();
			}
			programPanel.setDecimal(true);
			dataPanel.setDecimal(true);
		});

		btnHexadecimal.addActionListener((event) -> {
			if (!btnHexadecimal.isSelected()) {
				return;
			}
			isDecimal = false;
			for (var panel : registerPanels) {
				panel.setDecimal(false);
				panel.repaint();
			}
			programPanel.setDecimal(false);
			dataPanel.setDecimal(false);
		});

		// Só funciona se o programa não estiver rodando automaticamente.
		btnNext.addActionListener((event) -> {
			if (!btnRun.isSelected()) {
				try {
					cpu.executeNextInstruction();
				} catch (InvalidInstructionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// O botão de roda deve ser tipo toggle. Quando apertado, dispara uma thread
		// que chama "executeNextInstruction" do cpu e testa se deve parar.
		// A thread usará uma função sincronizada para consultar a flag.
		//
		// Quando o botão é apertado novamente, altera uma flag que fará a thread parar.
		// Tem que usar uma função sincronizada para alterar essa flag.
		btnRun.addActionListener((event) -> {
			if (btnRun.isSelected()) {
				// Executa uma thread que consulta se deve parar.
				// Isso deve ser feito de forma sincronizada.
				setRunning(true);
				Thread thread = new Thread(() -> {
					while (isRunning()) {
						try {
							cpu.executeNextInstruction();
						} catch (InvalidInstructionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				thread.start();
			} else {
				// Desliga a flag e consequentemente termina a thread que executa o programa
				// automaticamente.
				setRunning(false);
			}
		});
	}

	private void initRegisterPanelsEvents() {
		for (int index = 0; index < registerPanels.length; ++index) {
			addEventsToRegisterPanel(registerPanels[index], index);
		}
	}

	private void addEventsToRegisterPanel(final RegisterPanel panel, final int index) {
		final String decimalMessage = String.format("Digite um valor decimal para %s", panel.getTitle());
		final String hexadecimalMessage = String.format("Digite um valor hexadecimal para %s", panel.getTitle());

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					String message = isDecimal ? decimalMessage : hexadecimalMessage;
					String currentValue = isDecimal ? Integer.toString(panel.getValue())
							: Integer.toHexString(panel.getValue());
					String newValue = JOptionPane.showInputDialog(panel, message, currentValue);
					if (newValue != null) {
						try {
							int value = 0xFFFF & Integer.parseInt(newValue, isDecimal ? 10 : 16);
							panel.setValue(value);
							cpu.setRegisterValue(index, (short) value);
						} catch (NumberFormatException e) {
							//e.printStackTrace();
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
}
