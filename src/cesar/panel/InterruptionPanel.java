package cesar.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import cesar.display.LedDisplay;

public class InterruptionPanel extends JPanel {
    private static final long serialVersionUID = 888881706787737318L;

    private static final BufferedImage COMPUTER_ICON;

    static {
        BufferedImage icon = null;
        try {
            InputStream stream = InterruptionPanel.class.getResourceAsStream("/cesar/images/icons/computer.bmp");
            icon = ImageIO.read(stream);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao ler a image de ícone do computador");
            System.exit(1);
        }
        COMPUTER_ICON = icon;
    }

    private LedDisplay display;

    public InterruptionPanel() {
        super(true);

        display = new LedDisplay();
        display.setTurnedOn(false);

        JPanel computerPanel = new JPanel(true);
        computerPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        computerPanel.setLayout(new GridBagLayout());
        JLabel computerImage = new JLabel(new ImageIcon(COMPUTER_ICON));
        computerPanel.add(computerImage);

        GridBagLayout grid = new GridBagLayout();
        setLayout(grid);
        grid.columnWidths  = new int[] { 0, 5, 0 };
        grid.columnWeights = new double[] { 1.0, 0.0, 0.0 };
        grid.rowWeights    = new double[] { 1.0 };

        GridBagConstraints c = new GridBagConstraints();
        c.gridx  = 0;
        c.gridy  = 0;
        c.fill   = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        add(computerPanel, c);

        c.gridx = 2;
        c.fill  = GridBagConstraints.VERTICAL;
        add(LedDisplay.wrap(display, "IS", new Insets(22, 2, 8, 2)), c);

        setPreferredSize(getPreferredSize());
    }
}