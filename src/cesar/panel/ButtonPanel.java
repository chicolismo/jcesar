package cesar.panel;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

public class ButtonPanel extends JPanel {
    private static final long serialVersionUID = 5342775832156361567L;
    private JToggleButton btnDecimal;
    private JToggleButton btnHexadecimal;
    private JToggleButton btnRun;
    private JButton btnNext;

    private static ImageIcon runIcon;
    private static ImageIcon nextIcon;

    static {
        try {
            runIcon  = new ImageIcon(ImageIO.read(ButtonPanel.class.getResource("/cesar/images/icons/config.bmp")));
            nextIcon = new ImageIcon(ImageIO.read(ButtonPanel.class.getResource("/cesar/images/icons/tools.bmp")));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Erro ao carregar os ícones dos botões");
            System.exit(1);
        }
    }

    ButtonPanel() {
        super(true);
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);

        setBorder(new EmptyBorder(3, 3, 3, 3));

        btnDecimal = new JToggleButton("0..9");
        btnDecimal.putClientProperty("JButton.buttonType", "square");
        btnDecimal.putClientProperty("JComponent.sizeVariant", "small");
        btnDecimal.setMargin(new Insets(2, 2, 2, 2));
        btnDecimal.setFocusPainted(false);

        btnHexadecimal = new JToggleButton("0..F");
        btnHexadecimal.putClientProperty("JButton.buttonType", "square");
        btnHexadecimal.putClientProperty("JComponent.sizeVariant", "small");
        btnHexadecimal.setMargin(new Insets(2, 2, 2, 2));
        btnHexadecimal.setFocusPainted(false);

        btnRun = new JToggleButton();
        btnRun.putClientProperty("JButton.buttonType", "square");
        btnRun.setMargin(new Insets(2, 2, 2, 2));
        btnRun.setIcon(runIcon);
        btnRun.setSize(new Dimension(16, 16));
        btnRun.setFocusPainted(false);

        btnNext = new JButton();
        btnNext.putClientProperty("JButton.buttonType", "square");
        btnNext.setMargin(new Insets(2, 2, 2, 2));
        btnNext.setIcon(nextIcon);
        btnNext.setSize(new Dimension(16, 16));
        btnNext.setFocusPainted(false);

        btnDecimal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                btnDecimal.setSelected(true);
                btnHexadecimal.setSelected(false);
            }
        });

        btnHexadecimal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                btnHexadecimal.setSelected(true);
                btnDecimal.setSelected(false);
            }
        });

        add(btnDecimal);
        add(btnHexadecimal);
        add(Box.createHorizontalGlue());
        add(btnRun);
        add(btnNext);
    }

    public JToggleButton getBtnDecimal() {
        return btnDecimal;
    }

    public JToggleButton getBtnHexadecimal() {
        return btnHexadecimal;
    }

    public JToggleButton getBtnRun() {
        return btnRun;
    }

    public JButton getBtnNext() {
        return btnNext;
    }
}
