package cesar.display;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DigitalDisplay extends JPanel {
	public static final long serialVersionUID = -527728462654031001L;

	private static final int DIGIT_WIDTH = 12;
	private static final int DIGIT_HEIGHT = 17;
	private static final int DIGIT_OFFSET = DIGIT_WIDTH + 2;
	private static final int WIDTH = 78;
	private static final int HEIGHT = 27;
	private static final int START_X = 61;
	private static final int START_Y = 5;

	private static BufferedImage[] displayImages;
	private static BufferedImage displayNull;
	private int value;
	private int base;
	private int numberOfDigits;

	static {
		displayImages = new BufferedImage[16];
		displayNull = null;
		try {
			String format = "../images/icons/cesar_%1x.bmp";
			for (int i = 0; i < 16; ++i) {
				displayImages[i] = ImageIO.read(DigitalDisplay.class.getResourceAsStream(String.format(format, i)));
			}
			displayNull = ImageIO.read(DigitalDisplay.class.getResourceAsStream("../images/icons/cesar_null.bmp"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	};

	public DigitalDisplay() {
		super(true);
		numberOfDigits = 5;
		value = 0;
		base = 10;
		Dimension dim = new Dimension(WIDTH, HEIGHT);
		setSize(dim);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setDecimal(boolean isDecimal) {
		base = isDecimal ? 10 : 16;
		numberOfDigits = isDecimal ? 5 : 4;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		int x = START_X;
		int currentDigit = 0;
		int n = value;
		do {
			int digit = n % base;
			g.drawImage(displayImages[digit], x, START_Y, DIGIT_WIDTH, DIGIT_HEIGHT, null);
			x -= DIGIT_OFFSET;
			++currentDigit;
			n /= base;
		} while (n > 0);

		while (currentDigit < numberOfDigits) {
			g.drawImage(displayNull, x, START_Y, DIGIT_WIDTH, DIGIT_HEIGHT, null);
			x -= DIGIT_OFFSET;
			++currentDigit;
		}

	}
}
