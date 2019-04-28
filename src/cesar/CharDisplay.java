package cesar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class CharDisplay extends JPanel {
	private static final long serialVersionUID = 4304849380002674639L;

	private static final int SIZE = 36;
	private static final int CHAR_WIDTH = 20;
	private static final int CHAR_HEIGHT = 28;
	private static final int START_Y = 4;
	private static final int START_X = 1;
	private static final int CHAR_OFFSET = CHAR_WIDTH + 1;
	private static final int WIDTH = CHAR_OFFSET * SIZE + 1;
	private static final int HEIGHT = CHAR_HEIGHT + 8;

	private static final BufferedImage[] charImages;
	private char[] value;

	static {
		charImages = new BufferedImage[95];
		String format = "tiles/character_%02d.png";
		try {
			for (int i = 0; i < 95; ++i) {
				charImages[i] = ImageIO.read(CharDisplay.class.getResourceAsStream(String.format(format, i)));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public CharDisplay() {
		super(true);
		value = new char[SIZE];
		for (int i = 0; i < SIZE; ++i) {
			value[i] = ' ';
		}
		Dimension dim = new Dimension(WIDTH, HEIGHT);
		setSize(dim);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
	}

	public void setValueAt(int index, char value) {
		this.value[index] = value;
	}

	public void setValue(String string) {
		char[] newValue = string.toCharArray();
		int size = Math.min(newValue.length, SIZE);
		int i;
		for (i = 0; i < size; ++i) {
			value[i] = newValue[i];
		}
		while (i < SIZE) {
			value[i] = ' ';
			++i;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		int x = START_X;
		for (int i = 0; i < SIZE; ++i) {
			g.drawImage(charImages[value[i] - 32], x, START_Y, CHAR_WIDTH, CHAR_HEIGHT, null);
			x += CHAR_OFFSET;
		}
	}
}
