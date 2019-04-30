package cesar.panel;

import javax.swing.*;
import java.awt.*;

class ButtonsPanel extends JPanel {
    private static final long serialVersionUID = 5342775832156361567L;

    private JButton btnDecimal;
    private JButton btnHexadecimal;

    ButtonsPanel() {
        super(true);
        //var layout = new FlowLayout(FlowLayout.CENTER, 0, 0);
        var layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);

        btnDecimal = new JButton("0..9");
        btnDecimal.setMargin(new Insets(0, 0, 0, 0));
        btnDecimal.putClientProperty("JButton.buttonType", "segmented");
        btnDecimal.putClientProperty("JButton.segmentPosition", "first");
        btnDecimal.putClientProperty("JComponent.sizeVariant", "small");
        btnDecimal.setFocusPainted(false);

        btnHexadecimal = new JButton("0..F");
        btnHexadecimal.setMargin(new Insets(0, 0, 0, 0));
        btnHexadecimal.putClientProperty("JButton.buttonType", "segmented");
        btnHexadecimal.putClientProperty("JButton.segmentPosition", "last");
        btnHexadecimal.putClientProperty("JComponent.sizeVariant", "small");
        btnHexadecimal.setFocusPainted(false);

        btnDecimal.addActionListener((event) -> {
            btnDecimal.setSelected(true);
            btnHexadecimal.setSelected(false);
        });

        btnHexadecimal.addActionListener((event) -> {
            btnHexadecimal.setSelected(true);
            btnDecimal.setSelected(false);
        });

        add(btnDecimal);
        //add(Box.createRigidArea(new Dimension(0, 0)));
        add(btnHexadecimal);
    }
}
