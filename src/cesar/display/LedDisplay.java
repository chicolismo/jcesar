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
	private static BufferedImage[] leds;
	
	private boolean isTurnedOn;
	
	static {
		leds = new BufferedImage[2];
		try {
			leds[0] = ImageIO.read(LedDisplay.class.getResourceAsStream("../images/icons/light_off.png"));
			leds[1] = ImageIO.read(LedDisplay.class.getResourceAsStream("../images/icons/light_on.png"));
		} catch (IOException e) {
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
		isTurnedOn = false;
	}
	
	public void setTurnedOn(boolean turnedOn) {
		isTurnedOn = turnedOn;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(isTurnedOn ? leds[1] : leds[0], 0, 0, WIDTH, HEIGHT, null);
	}
}
