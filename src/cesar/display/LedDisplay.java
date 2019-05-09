package cesar.display;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class LedDisplay extends JPanel {
    private static final long serialVersionUID = 3909006919802599588L;

    private static final int WIDTH = 15;
    private static final int HEIGHT = 15;
    private static final BufferedImage[] lights;

    private boolean isOn;

    static {
        lights = new BufferedImage[2];
        try {
            lights[0] = ImageIO.read(LedDisplay.class.getResourceAsStream("/cesar/images/icons/light_off.png"));
            lights[1] = ImageIO.read(LedDisplay.class.getResourceAsStream("/cesar/images/icons/light_on.png"));
        }
        catch (IOException e) {
            System.err.println("Erro ao ler as imagens do LED.");
            e.printStackTrace();
        }
    }

    public LedDisplay() {
        super(true);
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setSize(size);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        isOn = false;
    }

    public void setTurnedOn(boolean isOn) {
        this.isOn = isOn;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(isOn ? lights[1] : lights[0], 0, 0, WIDTH, HEIGHT, null);
    }
}
