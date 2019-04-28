package cesar;

import java.awt.event.WindowEvent;
import javax.swing.*;

public class Controller {
	
	JFrame window;
	
	public Controller(JFrame window) {
		this.window = window;
	}

	public void openFile() {
		JOptionPane.showMessageDialog(this.window, "Hello, world");
	}
	
	public void saveFile() {
		
	}
	
	public void saveFileAs() {
		
	}
	
	public void exit() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}
}
