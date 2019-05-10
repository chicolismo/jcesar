package cesar.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class Buttons {
    private static final Insets CUSTOM_INSETS = new Insets(4, 4, 4, 4);

    private static final Border raisedBorder = new BevelBorder(BevelBorder.RAISED, null, null, null, null) {
        private static final long serialVersionUID = -1249034374067912355L;

        @Override
        public Insets getBorderInsets(Component c) {
            return CUSTOM_INSETS;
        }
    };

    private static final Border loweredBorder = new BevelBorder(BevelBorder.LOWERED, null, null, null, null) {
        private static final long serialVersionUID = -2126676772246091808L;

        @Override
        public Insets getBorderInsets(Component c) {
            return CUSTOM_INSETS;
        }
    };

    public static class BevelButton extends JButton {
        private static final long serialVersionUID = 7072719102086837786L;

        public BevelButton(String text) {
            super(text);
        }

        public BevelButton() {
            super();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getModel().isArmed()) {
                g.setColor(getBackground());
                setBorder(loweredBorder);
            }
            else {
                g.setColor(Color.lightGray);
                setBorder(raisedBorder);
            }
            super.paintComponent(g);
        }
    }

    public static class BevelToggleButton extends JToggleButton {
        private static final long serialVersionUID = 7072719102086837786L;

        public BevelToggleButton(String text) {
            super(text);
        }

        public BevelToggleButton() {
            super();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getModel().isSelected()) {
                g.setColor(getBackground());
                setBorder(loweredBorder);
            }
            else {
                g.setColor(Color.lightGray);
                setBorder(raisedBorder);
            }
            super.paintComponent(g);
        }
    }
}
