package cesar.panel;

import cesar.display.BinaryDisplay;
import cesar.display.DigitalDisplay;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.awt.Dimension;

public class RegisterPanel extends JPanel {
	private static final long serialVersionUID = 2138122907978072925L;

	private DigitalDisplay digitalDisplay;
	private BinaryDisplay binaryDisplay;
	private String title;
	private int value;

	public RegisterPanel(String title) {
		TitledBorder titledBorder = new TitledBorder(title);
		EmptyBorder emptyBorder = new EmptyBorder(0, 4, 4, 3);
		setBorder(new CompoundBorder(titledBorder, emptyBorder));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.title = title;
		this.value = 0;
		this.digitalDisplay = new DigitalDisplay();
		this.binaryDisplay = new BinaryDisplay();
		add(digitalDisplay);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(binaryDisplay);
	}

	public void setValue(int value) {
		this.value = value;
		digitalDisplay.setValue(value);
		binaryDisplay.setValue(value);
		digitalDisplay.repaint();
		binaryDisplay.repaint();
	}
	
	public int getValue() {
		return value;
	}
	
	public String getTitle() {
		return title;
	}

	public void setDecimal(boolean isDecimal) {
		digitalDisplay.setDecimal(isDecimal);
	}
}
