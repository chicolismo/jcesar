package cesar;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class DisplayPanel extends JDialog {
	private static final long serialVersionUID = -4447642329893150278L;

	CharDisplay display;
	
	public DisplayPanel(JFrame parent, String title) {
		super(parent, title);
		display = new CharDisplay();
		setContentPane(display);
		setSize(display.getPreferredSize());
		pack();
	}
	
	public void setValue(String string) {
		display.setValue(string);
		display.repaint();
	}
}
