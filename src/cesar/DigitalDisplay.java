package cesar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class DigitalDisplay extends JPanel {
	public static final long serialVersionUID = -527728462654031001L;

	private static final int DIGIT_WIDTH = 12;
	private static final int DIGIT_HEIGHT = 17;
	private static final int DIGIT_OFFSET = DIGIT_WIDTH + 2;
	private static final int WIDTH = 78;
	private static final int HEIGHT = 27;
	private static final int START_X = 61;
	private static final int START_Y = 5;


	private static final BufferedImage[] displayImages;
	private int value;
	private int base;
	private int numberOfDigits;

	static {
		displayImages = new BufferedImage[17];
		try {
			displayImages[0] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_0.bmp"));
			displayImages[1] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_1.bmp"));
			displayImages[2] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_2.bmp"));
			displayImages[3] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_3.bmp"));
			displayImages[4] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_4.bmp"));
			displayImages[5] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_5.bmp"));
			displayImages[6] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_6.bmp"));
			displayImages[7] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_7.bmp"));
			displayImages[8] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_8.bmp"));
			displayImages[9] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_9.bmp"));
			displayImages[10] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_a.bmp"));
			displayImages[11] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_b.bmp"));
			displayImages[12] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_c.bmp"));
			displayImages[13] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_d.bmp"));
			displayImages[14] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_e.bmp"));
			displayImages[15] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_f.bmp"));
			displayImages[16] = ImageIO.read(DigitalDisplay.class.getResourceAsStream("icons/cesar_null.bmp"));
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

	void setValue(int value) {
		this.value = value;
	}

	void setDecimal(boolean isDecimal) {
		base = isDecimal ? 10 : 16;
		numberOfDigits = isDecimal ? 5 : 4;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);;

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
			g.drawImage(displayImages[16], x, START_Y, DIGIT_WIDTH, DIGIT_HEIGHT, null);
			x -= DIGIT_OFFSET;
			++currentDigit;
		}

	}
}
