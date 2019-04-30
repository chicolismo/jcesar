package cesar.panel;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private RegisterPanel[] registers;
    private ExecutionPanel executionPanel;
    private ConditionsPanel conditionsPanel;

    public MainPanel()  {
        super(true);
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
        gridBagLayout.columnWidths = new int[]{0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0};

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

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(registerPanel);
        this.add(middlePanel);
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
}
