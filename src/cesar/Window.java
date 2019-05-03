package cesar;

import cesar.panel.ConditionsPanel;
import cesar.panel.DisplayPanel;
import cesar.panel.ExecutionPanel;
import cesar.panel.MainPanel;
import cesar.panel.RegisterPanel;
import cesar.panel.SidePanel;
import cesar.table.DataTable;
import cesar.table.ProgramTable;
import cesar.table.Table;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

class Window extends JFrame {
    private static final long serialVersionUID = 7470189528132411359L;

    private Controller controller;
    private SidePanel programPanel;
    private SidePanel dataPanel;
    private DisplayPanel displayPanel;
    private ExecutionPanel executionPanel;
    private ConditionsPanel conditionsPanel;
    private RegisterPanel[] registers;

    Window() {
        super("Cesar");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);

        JPanel contentPane = new JPanel();
        BevelBorder outerBorder = new BevelBorder(BevelBorder.LOWERED);
        EmptyBorder innerBorder = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder border = new CompoundBorder(outerBorder, innerBorder);
        contentPane.setBorder(border);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        setContentPane(contentPane);

        MainPanel mainPanel = new MainPanel();
        contentPane.add(mainPanel);

        ExecutionPanel executionPanel = mainPanel.getExecutionPanel();
        ConditionsPanel conditionsPanel = mainPanel.getConditionsPanel();

        SidePanel programPanel = createSidePanel("Programa", new ProgramTable());
        SidePanel dataPanel = createSidePanel("Dados", new DataTable());
        DisplayPanel displayPanel = new DisplayPanel(this, "Mostrador");

        this.executionPanel = executionPanel;
        this.conditionsPanel = conditionsPanel;
        this.programPanel = programPanel;
        this.dataPanel = dataPanel;
        this.registers = mainPanel.getRegisters();
        this.displayPanel = displayPanel;
        MenuBar menuBar = new MenuBar(this);
        this.setJMenuBar(menuBar);

        this.controller = new Controller(this, programPanel, dataPanel, displayPanel, menuBar);

        initEvents();

        pack();
        center();
        updatePositions();
        setVisible(true);
        this.programPanel.setVisible(true);
        this.dataPanel.setVisible(true);
        this.displayPanel.setVisible(true);
        setResizable(false);
    }

    private SidePanel createSidePanel(String title, Table table) {
        SidePanel sidePanel = new SidePanel(this, title, table);
        sidePanel.setSize(sidePanel.getPreferredSize());
        return sidePanel;
    }

    private void updatePositions() {
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

        /*
        getContentPane().addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent event) {
                Point p = event.getPoint();
                conditionsPanel.setNegative(p.x % 2 == 0);
                conditionsPanel.setCarry(p.y % 2 == 0);
                registers[0].setValue(p.x);
                registers[1].setValue(p.y);
                displayPanel.setValue(p.toString());
            }
        });
        */
    }

    public Controller getController() {
        return controller;
    }
}
