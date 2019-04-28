package cesar;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class Window extends JFrame {
	private static final long serialVersionUID = 7470189528132411359L;
	
	private Controller controller;
	private SidePanel programPanel;
	private SidePanel dataPanel;
	private DisplayPanel display;
	private RegisterPanel[] registers;

	public Window() {
		super("Cesar");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridLayout grid = new GridLayout(3, 3, 5, 5);
		contentPane.setLayout(grid);
		setContentPane(contentPane);

		this.controller = new Controller(this);

		initMenu();
		initPanels();
		initRegisters();
		initEvents();
		
		
		center();
		updatePositions();
		pack();
		setVisible(true);
		setResizable(false);
	}
	
	private void initRegisters() {
		registers = new RegisterPanel[8];
		registers[0] = new RegisterPanel("R0:");
		registers[1] = new RegisterPanel("R1:");
		registers[2] = new RegisterPanel("R2:");
		registers[3] = new RegisterPanel("R3:");
		registers[4] = new RegisterPanel("R4:");
		registers[5] = new RegisterPanel("R5:");
		registers[6] = new RegisterPanel("R6 (SP):");
		registers[7] = new RegisterPanel("R7 (PC):");
		
		var contentPane = this.getContentPane();
		contentPane.add(registers[0]);
		contentPane.add(registers[1]);
		contentPane.add(registers[2]);
		contentPane.add(registers[3]);
		contentPane.add(registers[4]);
		contentPane.add(registers[5]);
		contentPane.add(registers[6]);
		contentPane.add(new JPanel());
		contentPane.add(registers[7]);
	}

	private void initMenu() {
		this.setJMenuBar(new ApplicationMenu(this));
	}

	private void initPanels() {
		ProgramTableModel programModel = new ProgramTableModel();
		programPanel = new SidePanel(this, "Programa");
		programPanel.getTable().setModel(programModel);
		programPanel.setSize(programPanel.getPreferredSize());
		programPanel.setVisible(true);

		DataTableModel dataModel = new DataTableModel();
		dataPanel = new SidePanel(this, "Dados");
		dataPanel.getTable().setModel(dataModel);
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
				registers[0].setValue(p.x);
				registers[1].setValue(p.y);
				display.setValue(p.toString());
			}
		});
	}

	public Controller getController() {
		return controller;
	}
}
