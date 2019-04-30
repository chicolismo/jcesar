package cesar.panel;

import cesar.display.DigitalDisplay;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ExecutionPanel extends JPanel {
	private static final long serialVersionUID = -4010296867969223401L;

	private DigitalDisplay accessDisplay;
	private DigitalDisplay instructionsDisplay;

	public ExecutionPanel() {
		super(true);

		TitledBorder titledBorder = new TitledBorder("Execuções");
		EmptyBorder emptyBorder = new EmptyBorder(0, 4, 4, 3);
		setBorder(new CompoundBorder(titledBorder, emptyBorder));
		
		//GridLayout grid = new GridLayout(2, 2, 5, 0);
		// TODO: Talvez trocar isto.
		GridLayout grid = new GridLayout(2, 2, 5, 5);
		this.setLayout(grid);
		
		JLabel accessLabel = new JLabel("Acessos:");
		accessLabel.setHorizontalAlignment(JLabel.RIGHT);
		JLabel instructionsLabel = new JLabel("Instruções:");
		instructionsLabel.setHorizontalAlignment(JLabel.RIGHT);
		this.accessDisplay = new DigitalDisplay();
		this.instructionsDisplay = new DigitalDisplay();
		
		add(accessLabel);
		add(accessDisplay);
		add(instructionsLabel);
		add(instructionsDisplay);
		
		setSize(getPreferredSize());
		setMaximumSize(getSize());
	}
}
