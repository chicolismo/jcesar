package cesar;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cesar.panel.ConditionsPanel;
import cesar.panel.ExecutionPanel;

public class SubPanel extends JPanel {
	private GridBagConstraints c_1;

	/**
	 * Create the panel.
	 */
	public SubPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0};
		setLayout(gridBagLayout);
		
		ExecutionPanel executionPanel = new ExecutionPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		//c.insets = new Insets(0, 0, 0, 5);
		c.gridheight = 2;
		c.gridx = 0;
		c.gridy = 0;
		add(executionPanel, c);
		
		ConditionsPanel conditionsPanel = new ConditionsPanel();
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHEAST;
		//c.insets = new Insets(0, 0, 5, 0);
		c.gridx = 1;
		c.gridy = 0;
		add(conditionsPanel, c);
		
		JPanel buttonsPanel = new JPanel();
		c_1 = new GridBagConstraints();
		c_1.anchor = GridBagConstraints.SOUTH;
		c_1.fill = GridBagConstraints.HORIZONTAL;
		c_1.gridx = 1;
		c_1.gridy = 1;
		add(buttonsPanel, c_1);
		//btnTools.setMargin(new Insets(0, 0, 0, 0));
		//btnTools.setBorder(null);
		BufferedImage image = null;
		try {
			image = ImageIO.read(SubPanel.class.getResourceAsStream("images/icons/tools.bmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		
		JButton btnTools = new JButton("");
		btnTools.setIcon(new ImageIcon(image));
		//btnTools.setIcon();
		buttonsPanel.add(btnTools);
		
		JButton button = new JButton("Fuck");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Fuck you!!!!");
			}
		});
		buttonsPanel.add(button);
	}
}
