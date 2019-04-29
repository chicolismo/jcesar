package cesar.panel;

import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ButtonsPanel extends JPanel {
	private static final long serialVersionUID = 5342775832156361567L;

	JToggleButton btnDecimal;
	JToggleButton btnHexadecimal;

	public ButtonsPanel() {
		super(true);
		var layout = new FlowLayout(FlowLayout.CENTER, 0, 0);
		setLayout(layout);

		btnDecimal = new JToggleButton("0..9");
		btnDecimal.setMargin(new Insets(0, 0, 0, 0));

		btnHexadecimal = new JToggleButton("0..F");
		btnHexadecimal.setMargin(new Insets(0, 0, 0, 0));

		btnDecimal.addActionListener((event) -> {
			btnHexadecimal.setSelected(!btnDecimal.isSelected());
		});

		btnHexadecimal.addActionListener((event) -> {
			btnDecimal.setSelected(!btnHexadecimal.isSelected());
		});

		add(btnDecimal);
		add(btnHexadecimal);
	}
}
