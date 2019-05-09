package cesar;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import cesar.panel.DisplayPanel;
import cesar.panel.MainPanel;
import cesar.panel.RegisterPanel;
import cesar.panel.SidePanel;
import cesar.table.DataTable;
import cesar.table.ProgramTable;
import cesar.table.Table;

class Window extends JFrame {
    private static final long serialVersionUID = 7470189528132411359L;

    /**
     * Espaço entre a janela principal e os painéis laterais.
     */
    private static final int WINDOW_SPACING = 10;

    private SidePanel programPanel;
    private SidePanel dataPanel;
    private DisplayPanel displayPanel;
    private MainPanel mainPanel;

    @SuppressWarnings("unused")
    private Controller controller;

    Window() {
        super("Cesar");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);

        JPanel contentPane = new JPanel(true);
        Border inner = new CompoundBorder(new BevelBorder(BevelBorder.LOWERED), new EmptyBorder(5, 5, 5, 5));
        Border border = new CompoundBorder(new EmptyBorder(2, 2, 2, 2), inner);
        contentPane.setBorder(border);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        MainPanel mainPanel = new MainPanel();
        this.mainPanel = mainPanel;
        contentPane.add(mainPanel);

        SidePanel programPanel = createSidePanel("Programa", new ProgramTable());
        SidePanel dataPanel = createSidePanel("Dados", new DataTable());
        DisplayPanel displayPanel = new DisplayPanel(this, "Mostrador");

        this.programPanel = programPanel;
        this.dataPanel    = dataPanel;
        this.displayPanel = displayPanel;
        MenuBar menuBar = new MenuBar(this);
        this.setJMenuBar(menuBar);

        Controller controller = new Controller(this, mainPanel, programPanel, dataPanel, displayPanel, menuBar);
        this.controller = controller;
        initEvents();
        pack();
        center();
        updatePositions();
        setVisible(true);
        this.programPanel.setVisible(true);
        this.dataPanel.setVisible(true);
        this.displayPanel.setVisible(true);
        programPanel.setSize(350, 500);
        dataPanel.setSize(160, 500);
        dataPanel.getTable().scrollToRow(1024);
//        setResizable(false);
    }

    private SidePanel createSidePanel(String title, Table table) {
        SidePanel sidePanel = new SidePanel(this, title, table);
        sidePanel.setSize(sidePanel.getPreferredSize());
        return sidePanel;
    }

    private void updatePositions() {
        Point location = getLocation();
        programPanel.setLocation(location.x - programPanel.getWidth() - WINDOW_SPACING, location.y);
        dataPanel.setLocation(location.x + getWidth() + WINDOW_SPACING, location.y);
        displayPanel.setLocation(location.x - programPanel.getWidth() - WINDOW_SPACING,
            location.y + programPanel.getHeight() + WINDOW_SPACING);
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

        addMouseMotionListener(new MouseAdapter() {
            private RegisterPanel[] registers = Window.this.mainPanel.getRegisters();

            @Override
            public void mouseMoved(MouseEvent event) {
                Point point = event.getPoint();
                displayPanel.setValue(point.toString());
                registers[0].setValue(point.x);
                registers[1].setValue(point.y);
            }
        });
    }
}
