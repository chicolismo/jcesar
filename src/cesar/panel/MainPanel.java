package cesar.panel;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class MainPanel extends JPanel {
    private static final long serialVersionUID = -8181221815362621489L;

    private final JPanel registersPanel;
    private final RegisterPanel[] registers;
    private final ExecutionPanel executionPanel;
    private final ButtonPanel buttonPanel;
    private final ConditionsPanel conditionsPanel;

    public MainPanel() {
        super(true);
        RegisterPanel r0 = new RegisterPanel("R0:");
        RegisterPanel r1 = new RegisterPanel("R1:");
        RegisterPanel r2 = new RegisterPanel("R2:");
        RegisterPanel r3 = new RegisterPanel("R3:");
        RegisterPanel r4 = new RegisterPanel("R4:");
        RegisterPanel r5 = new RegisterPanel("R5:");
        RegisterPanel r6 = new RegisterPanel("R6 (SP):");
        RegisterPanel r7 = new RegisterPanel("R7 (PC):");
        registers      = new RegisterPanel[] { r0, r1, r2, r3, r4, r5, r6, r7 };

        registersPanel = new JPanel(true);
        registersPanel.setBounds(0, 0, 308, 202);
        registersPanel.setLayout(new GridLayout(3, 3, 4, 4));
        registersPanel.add(r0);
        registersPanel.add(r1);
        registersPanel.add(r2);
        registersPanel.add(r3);
        registersPanel.add(r4);
        registersPanel.add(r5);
        registersPanel.add(r6);
        JPanel panel = new JPanel();
        registersPanel.add(panel);
        registersPanel.add(r7);

        registersPanel.setPreferredSize(registersPanel.getPreferredSize());

        executionPanel = new ExecutionPanel();
        executionPanel.setBounds(0, 204, 171, 81);
        executionPanel.setPreferredSize(executionPanel.getPreferredSize());

        conditionsPanel = new ConditionsPanel();
        conditionsPanel.setBounds(171, 204, 137, 41);
        conditionsPanel.setPreferredSize(conditionsPanel.getPreferredSize());

        buttonPanel = new ButtonPanel();
        buttonPanel.setBounds(171, 250, 137, 35);
        buttonPanel.setPreferredSize(buttonPanel.getPreferredSize());
        setLayout(null);

        add(registersPanel);
        add(executionPanel);
        add(conditionsPanel);
        add(buttonPanel);

        Dimension size = new Dimension(308, 204 + 81);
        setPreferredSize(size);
        setSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }

    public ExecutionPanel getExecutionPanel() {
        return executionPanel;
    }

    public ConditionsPanel getConditionsPanel() {
        return conditionsPanel;
    }

    public RegisterPanel[] getRegisters() {
        return registers;
    }

    public ButtonPanel getButtonPanel() {
        return buttonPanel;
    }
}
