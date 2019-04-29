package cesar;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import cesar.panel.ButtonsPanel;
import cesar.panel.ConditionsPanel;
import cesar.panel.DisplayPanel;
import cesar.panel.ExecutionPanel;
import cesar.panel.RegisterPanel;
import cesar.panel.SidePanel;
import cesar.table.DataTable;
import cesar.table.ProgramTable;

public class Window extends JFrame {
	private static final long serialVersionUID = 7470189528132411359L;

	private Controller controller;
	private SidePanel programPanel;
	private SidePanel dataPanel;
	private DisplayPanel display;
	private ExecutionPanel executionPanel;
	private ConditionsPanel conditionsPanel;
	private RegisterPanel[] registers;
	private FileChooser fileChooser;

	public Window() {
		super("Cesar");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		// GridLayout grid = new GridLayout(3, 3, 5, 5);
		// contentPane.setLayout(grid);
		BoxLayout layout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
		contentPane.setLayout(layout);
		setContentPane(contentPane);

		this.controller = new Controller(this);
		this.fileChooser = new FileChooser();

		initMenu();
		initFloatingPanels();
		initMainPanel();
		initEvents();

		center();
		updatePositions();
		pack();
		setVisible(true);
		setResizable(false);
	}

	private void initMainPanel() {
		JPanel registerPanel = new JPanel(true);
		GridLayout grid = new GridLayout(3, 3, 5, 5);
		registerPanel.setLayout(grid);

		registers = new RegisterPanel[8];
		registers[0] = new RegisterPanel("R0:");
		registers[1] = new RegisterPanel("R1:");
		registers[2] = new RegisterPanel("R2:");
		registers[3] = new RegisterPanel("R3:");
		registers[4] = new RegisterPanel("R4:");
		registers[5] = new RegisterPanel("R5:");
		registers[6] = new RegisterPanel("R6 (SP):");
		registers[7] = new RegisterPanel("R7 (PC):");

		registerPanel.add(registers[0]);
		registerPanel.add(registers[1]);
		registerPanel.add(registers[2]);
		registerPanel.add(registers[3]);
		registerPanel.add(registers[4]);
		registerPanel.add(registers[5]);
		registerPanel.add(registers[6]);
		registerPanel.add(new JPanel());
		registerPanel.add(registers[7]);

		executionPanel = new ExecutionPanel();
		conditionsPanel = new ConditionsPanel();

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0 };

		JPanel middlePanel = new JPanel(true);
		middlePanel.setLayout(gridBagLayout);

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		// c.insets = new Insets(0, 0, 0, 5);
		c.gridheight = 2;
		c.gridx = 0;
		c.gridy = 0;
		middlePanel.add(executionPanel, c);

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHEAST;
		// c.insets = new Insets(0, 0, 5, 0);
		c.gridx = 1;
		c.gridy = 0;
		middlePanel.add(conditionsPanel, c);

		//JPanel buttonsPanel = new JPanel();
		ButtonsPanel buttonsPanel = new ButtonsPanel();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.SOUTH;
		c.gridx = 1;
		c.gridy = 1;
		middlePanel.add(buttonsPanel, c);

		var contentPane = this.getContentPane();
		contentPane.add(registerPanel);
		contentPane.add(middlePanel);
	}

	private void initMenu() {
		this.setJMenuBar(new Menu(this));
	}

	private void initFloatingPanels() {
		ProgramTable programTable = new ProgramTable();
		programPanel = new SidePanel(this, "Programa", programTable);
		programPanel.setSize(programPanel.getPreferredSize());
		programPanel.setVisible(true);

		DataTable dataTable = new DataTable();
		dataPanel = new SidePanel(this, "Dados", dataTable);
		dataPanel.setSize(dataPanel.getPreferredSize());
		dataPanel.setVisible(true);

		display = new DisplayPanel(this, "Mostrador");
		display.setSize(display.getPreferredSize());
		display.setVisible(true);
		display.setValue("Fuck you!!!");
	}

	public void updatePositions() {
		int spacing = 10;
		Point location = getLocation();
		programPanel.setLocation(location.x - programPanel.getWidth() - spacing, location.y);
		dataPanel.setLocation(location.x + getWidth() + spacing, location.y);

		int height = getHeight();
		programPanel.setSize(programPanel.getWidth(), height);
		dataPanel.setSize(dataPanel.getWidth(), height);
	}

	private void center() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);
	}

	private void initEvents() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent event) {
				updatePositions();
			}
		});

		getContentPane().addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent event) {
				Point p = event.getPoint();
				conditionsPanel.setNegative(p.x % 2 == 0);
				conditionsPanel.setCarry(p.y % 2 == 0);
				registers[0].setValue(p.x);
				registers[1].setValue(p.y);
				display.setValue(p.toString());
			}
		});
	}

	public Controller getController() {
		return controller;
	}

	// Controller
	public void openFile() {
		// displayPanel
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			JOptionPane.showMessageDialog(this, file.getName());
			display.setValue(file.getName());
		}
	}

	public void saveFile() {

	}

	public void saveFileAs() {

	}

	public void exit() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
