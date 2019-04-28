package cesar;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			try {
				new Window();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
