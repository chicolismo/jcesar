package cesar.panel;

import cesar.display.BinaryDisplay;
import cesar.display.DigitalDisplay;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPanel extends JPanel {
	private static final long serialVersionUID = 2138122907978072925L;

	private DigitalDisplay digitalDisplay;
	private BinaryDisplay binaryDisplay;
	private String registerName;
	private int value;
	private boolean isDecimal;

	public RegisterPanel(String title) {
		TitledBorder titledBorder = new TitledBorder(title);
		EmptyBorder emptyBorder = new EmptyBorder(0, 4, 4, 3);
		setBorder(new CompoundBorder(titledBorder, emptyBorder));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.registerName = title;
		this.value = 0;
		this.isDecimal = true;
		this.digitalDisplay = new DigitalDisplay();
		this.binaryDisplay = new BinaryDisplay();
		add(digitalDisplay);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(binaryDisplay);
		initEvents();
	}

	public void setValue(int value) {
		this.value = value;
		digitalDisplay.setValue(value);
		binaryDisplay.setValue(value);
		digitalDisplay.repaint();
		binaryDisplay.repaint();
	}

	public void setDecimal(boolean isDecimal) {
		this.isDecimal = isDecimal;
		digitalDisplay.setDecimal(isDecimal);
	}

	private void initEvents() {
		RegisterPanel panel = this;
		String decimalMessage = String.format("Digite um valor decimal para %s", registerName);
		String hexadecimalMessage = String.format("Digite um valor hexadecimal para %s", registerName);

		// Num duplo-clique o painel exibe um diálogo para alterar seu valor.
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					String message = isDecimal ? decimalMessage : hexadecimalMessage;
					String currentValue = isDecimal ? Integer.toString(value) : Integer.toHexString(value);
					String newValue = JOptionPane.showInputDialog(panel, message, currentValue);
					if (newValue != null) {
						try {
							int value = Integer.parseInt(newValue, isDecimal ? 10 : 16);
							panel.setValue(value);
						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(panel, "Valor inválido", "Atenção",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		});
	}
}
