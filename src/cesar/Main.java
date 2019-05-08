package cesar;

import javax.swing.*;

class Main {
	public static void main(String[] args) {
		/*
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if (info.getName().equals("Metal")) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
			// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		*/

		/*
		 * if (System.getProperty("os.name").contains("Mac")) {
		 * System.setProperty("apple.laf.useScreenMenuBar", "true"); }
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
