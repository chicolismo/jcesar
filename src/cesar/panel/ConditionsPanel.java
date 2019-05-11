package cesar.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import cesar.display.LedDisplay;

public class ConditionsPanel extends JPanel {
    private static final long serialVersionUID = -7989953222534549887L;

    private final LedDisplay negative;
    private final LedDisplay zero;
    private final LedDisplay overflow;
    private final LedDisplay carry;

    public ConditionsPanel() {
        super(true);

        this.negative = new LedDisplay();
        this.zero     = new LedDisplay();
        this.overflow = new LedDisplay();
        this.carry    = new LedDisplay();

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths  = new int[] { 0, 0, 0, 0, 0, 0, 0 };
        gridBagLayout.rowHeights    = new int[] { 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0 };
        gridBagLayout.rowWeights    = new double[] { 0.0 };
        setLayout(gridBagLayout);

        GridBagConstraints c_0 = new GridBagConstraints();
        c_0.gridx = 0;
        c_0.gridy = 0;
        add(wrap(negative, "N"), c_0);

        GridBagConstraints c_1 = new GridBagConstraints();
        c_1.gridx = 2;
        c_1.gridy = 0;
        add(wrap(zero, "Z"), c_1);

        GridBagConstraints c_2 = new GridBagConstraints();
        c_2.gridx = 4;
        c_2.gridy = 0;
        add(wrap(overflow, "V"), c_2);

        GridBagConstraints c_3 = new GridBagConstraints();
        c_3.gridx = 6;
        c_3.gridy = 0;
        add(wrap(carry, "C"), c_3);

        setSize(getPreferredSize());
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
    }

    public void setNegative(boolean value) {
        negative.setTurnedOn(value);
        negative.repaint();
    }

    public void setZero(boolean value) {
        zero.setTurnedOn(value);
        zero.repaint();
    }

    public void setOverflow(boolean value) {
        overflow.setTurnedOn(value);
        overflow.repaint();
    }

    public void setCarry(boolean value) {
        carry.setTurnedOn(value);
        carry.repaint();
    }

    private static JPanel wrap(LedDisplay display, String title) {
        return LedDisplay.wrap(display, title);
    }
}
