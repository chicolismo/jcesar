package cesar.panel;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import cesar.util.Buttons;

public class ButtonPanel extends JPanel {
    private static final long serialVersionUID = 5342775832156361567L;

    private final JToggleButton btnDecimal;
    private final JToggleButton btnHexadecimal;
    private final JToggleButton btnRun;
    private final JButton btnNext;

    private static ImageIcon runIcon;
    private static ImageIcon nextIcon;

    static {
        try {
            runIcon  = new ImageIcon(ImageIO.read(ButtonPanel.class.getResource("/cesar/images/icons/config.bmp")));
            nextIcon = new ImageIcon(ImageIO.read(ButtonPanel.class.getResource("/cesar/images/icons/tools.bmp")));
        }
        catch (Exception ex) {
            System.err.println("==== Erro ao carregar os ícones dos botões ====");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    ButtonPanel() {
        super(true);
        setBorder(new EmptyBorder(0, 3, 3, 3));
        final ButtonGroup buttonGroup = new ButtonGroup();

        btnDecimal = new Buttons.BevelToggleButton("0..9");
        btnDecimal.setToolTipText("Decimal");
        btnDecimal.setFocusPainted(false);
        buttonGroup.add(btnDecimal);

        btnHexadecimal = new Buttons.BevelToggleButton("0..F");
        btnHexadecimal.setToolTipText("Hexadecimal");
        btnHexadecimal.setFocusPainted(false);
        buttonGroup.add(btnHexadecimal);

        btnRun = new Buttons.BevelToggleButton();
        btnRun.setToolTipText("Rodar");
        btnRun.setIcon(runIcon);
        btnRun.setFocusPainted(false);

        btnNext = new Buttons.BevelButton();
        btnNext.setToolTipText("Passo-a-passo");
        btnNext.setIcon(nextIcon);
        btnNext.setFocusPainted(false);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(btnDecimal);
        add(btnHexadecimal);
        add(Box.createHorizontalGlue());
        add(btnRun);
        add(btnNext);

        setPreferredSize(getPreferredSize());
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
