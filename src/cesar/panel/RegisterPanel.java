package cesar.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import cesar.display.BinaryDisplay;
import cesar.display.DigitalDisplay;

public class RegisterPanel extends JPanel {
    private static final long serialVersionUID = 2138122907978072925L;

    private DigitalDisplay digitalDisplay;
    private BinaryDisplay binaryDisplay;
    private String title;
    private int value;

    public RegisterPanel(String title) {
        setBorder(new RegisterBorder(title));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.title          = title;
        this.value          = 0;
        this.digitalDisplay = new DigitalDisplay();
        this.binaryDisplay  = new BinaryDisplay();

        add(digitalDisplay);
        add(Box.createRigidArea(new Dimension(0, 4)));
        add(binaryDisplay);

        setPreferredSize(getPreferredSize());
    }

    public void setValue(int value) {
        this.value = value;
        digitalDisplay.setValue(value);
        binaryDisplay.setValue(value);
        digitalDisplay.repaint();
        binaryDisplay.repaint();
    }

    public int getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public void setDecimal(boolean isDecimal) {
        digitalDisplay.setDecimal(isDecimal);
    }

    private static class RegisterBorder extends TitledBorder {
        private static final long serialVersionUID = 884333724581658425L;

        public RegisterBorder(String title) {
            super(title);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            Insets customInsets = new Insets(18, 6, 10, 6);
            return customInsets;
        }
    };
}
