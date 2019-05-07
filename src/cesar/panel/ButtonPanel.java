package cesar.panel;


import java.awt.FlowLayout;
import java.awt.Insets;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ButtonPanel extends JPanel {
	private static final long serialVersionUID = 5342775832156361567L;
	private JToggleButton btnDecimal;
	private JToggleButton btnHexadecimal;
	private JToggleButton btnRun;
	private JButton btnNext;

	private static ImageIcon runIcon;
	private static ImageIcon nextIcon;
	static {
		try {
			runIcon = new ImageIcon(ImageIO.read(ButtonPanel.class.getResource("../images/icons/config.bmp")));
			nextIcon = new ImageIcon(ImageIO.read(ButtonPanel.class.getResource("../images/icons/tools.bmp")));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Erro ao carregar os ícones dos botões");
			System.exit(1);
		}
	}

	ButtonPanel() {
		super(true);
		//var layout = new FlowLayout(FlowLayout.CENTER, 0, 0);
		var layout = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(layout);
		
		setBorder(new EmptyBorder(3, 3, 3, 3));

		btnDecimal = new JToggleButton("0..9");
		//btnDecimal.putClientProperty("JButton.buttonType", "gradient");
		//btnDecimal.putClientProperty("JButton.buttonType", "segmented");
		//btnDecimal.putClientProperty("JButton.segmentPosition", "first");
		btnDecimal.setMargin(new Insets(2, 2, 2, 2));
		btnDecimal.setFocusPainted(false);

		btnHexadecimal = new JToggleButton("0..F");
		//btnHexadecimal.putClientProperty("JButton.buttonType", "gradient");
		//btnHexadecimal.putClientProperty("JButton.buttonType", "segmented");
		//btnHexadecimal.putClientProperty("JButton.segmentPosition", "last");
		btnHexadecimal.setMargin(new Insets(2, 2, 2, 2));
		btnHexadecimal.setFocusPainted(false);

		btnRun = new JToggleButton();
		//btnRun.putClientProperty("JButton.buttonType", "segmentedCapsule");
		btnRun.setMargin(new Insets(2, 2, 2, 2));
		btnRun.setIcon(runIcon);
		btnRun.setFocusPainted(false);

		btnNext = new JButton();
		//btnNext.putClientProperty("JButton.buttonType", "gradient");
		btnNext.setMargin(new Insets(2, 2, 2, 2));
		btnNext.setIcon(nextIcon);
		btnNext.setFocusPainted(false);

		btnDecimal.addActionListener((event) -> {
			btnDecimal.setSelected(true);
			btnHexadecimal.setSelected(false);
		});

		btnHexadecimal.addActionListener((event) -> {
			btnHexadecimal.setSelected(true);
			btnDecimal.setSelected(false);
		});

		add(btnDecimal);
		add(btnHexadecimal);
		add(Box.createHorizontalGlue());
		add(btnRun);
		add(btnNext);
	}

	public JToggleButton getBtnDecimal() {
		return btnDecimal;
	}

	public JToggleButton getBtnHexadecimal() {
		return btnHexadecimal;
	}

	public JToggleButton getBtnRun() {
		return btnRun;
	}

	public JButton getBtnNext() {
		return btnNext;
	}
}
