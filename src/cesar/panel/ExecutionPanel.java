package cesar.panel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import cesar.display.DigitalDisplay;

public class ExecutionPanel extends JPanel {
    private static final long serialVersionUID = -4010296867969223401L;

    private DigitalDisplay accessDisplay;
    private DigitalDisplay instructionsDisplay;

    public ExecutionPanel() {
        super(true);

        TitledBorder titledBorder = new TitledBorder("Execuções");
        EmptyBorder emptyBorder = new EmptyBorder(0, 3, 3, 3);
        setBorder(new CompoundBorder(titledBorder, emptyBorder));

        this.instructionsDisplay = new DigitalDisplay();
        this.accessDisplay       = new DigitalDisplay();
        JLabel accessLabel = new JLabel("Acessos:");
        accessLabel.setHorizontalAlignment(JLabel.RIGHT);
        JLabel instructionsLabel = new JLabel("Instruções:");
        instructionsLabel.setHorizontalAlignment(JLabel.RIGHT);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths  = new int[] { 0, 0 };
        gridBagLayout.rowHeights    = new int[] { 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 0.0 };
        gridBagLayout.rowWeights    = new double[] { 0.0, 0.0 };
        setLayout(gridBagLayout);

        GridBagConstraints c;

        c        = new GridBagConstraints();
        c.fill   = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 5, 5);
        c.gridx  = 0;
        c.gridy  = 0;
        add(accessLabel, c);

        c        = new GridBagConstraints();
        c.fill   = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 5, 0);
        c.gridx  = 1;
        c.gridy  = 0;
        add(accessDisplay, c);

        c        = new GridBagConstraints();
        c.fill   = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 5);
        c.gridx  = 0;
        c.gridy  = 1;
        add(instructionsLabel, c);

        c       = new GridBagConstraints();
        c.fill  = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
        add(instructionsDisplay, c);

        Dimension size = getPreferredSize();
        setPreferredSize(size);
        setSize(size);
        setMaximumSize(size);
    }
}
