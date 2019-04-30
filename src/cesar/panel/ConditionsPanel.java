package cesar.panel;

import cesar.display.LedDisplay;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ConditionsPanel extends JPanel {
	private static final long serialVersionUID = -7989953222534549887L;

	private LedDisplay[] displays;

	public ConditionsPanel() {
		super(true);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		String[] titles = { "N", "Z", "V", "C" };
		displays = new LedDisplay[] { new LedDisplay(), new LedDisplay(), new LedDisplay(), new LedDisplay() };
		for (int i = 0; i < 4; ++i) {
			TitledBorder border = new TitledBorder(titles[i]);
			border.setTitleJustification(TitledBorder.CENTER);
			JPanel panel = new JPanel(true);
			panel.setBorder(border);
			panel.add(displays[i]);
			this.add(panel);
		}
		
		setSize(getPreferredSize());
		setMaximumSize(getPreferredSize());
		setMinimumSize(getPreferredSize());
	}
	
	public void setNegative(boolean value) {
		displays[0].setTurnedOn(value);
		displays[0].repaint();
	}
	
	public void setZero(boolean value) {
		displays[1].setTurnedOn(value);
		displays[1].repaint();
	}
	
	public void setOverflow(boolean value) {
		displays[2].setTurnedOn(value);
		displays[2].repaint();
	}
	
	public void setCarry(boolean value) {
		displays[3].setTurnedOn(value);
		displays[3].repaint();
	}
}
