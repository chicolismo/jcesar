package cesar;

import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	public static void main(String[] args) {
		/*
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		*/

		SwingUtilities.invokeLater(() -> {
			try {
				new Window();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
