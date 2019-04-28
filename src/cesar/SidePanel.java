package cesar;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.Insets;

public class SidePanel extends JDialog {
	private static final long serialVersionUID = -5287184935159813862L;

	private JTable table;
	private JPanel panel;
	private JLabel label;
	private JTextField textField;
	private GridBagConstraints c_1;

	public SidePanel(JFrame parent, String title) {
		super(parent, title);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		panel = new JPanel();
		setContentPane(panel);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));

		// TODO: Mudar a fonte da tabela!
		table = new JTable();
		table.setDoubleBuffered(true);
		table.setColumnSelectionAllowed(false);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setDoubleBuffered(true);
		scrollPane.setViewportView(table);

		label = new JLabel("0");
		textField = new JTextField(10);
		var dim = new Dimension(100, 20);
		textField.setSize(dim);
		textField.setMinimumSize(dim);
		textField.setMaximumSize(dim);
		textField.setPreferredSize(dim);
		JPanel innerPanel = new JPanel();
		var layout = new FlowLayout(FlowLayout.RIGHT);
		//innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
		innerPanel.setLayout(layout);
		innerPanel.add(label);
		innerPanel.add(textField);

		GridBagLayout grid = new GridBagLayout();
		grid.columnWidths = new int[]{0};
		grid.rowHeights = new int[]{0, 0};
		grid.columnWeights = new double[]{1.0};
		grid.rowWeights = new double[]{1.0, 0.0};
		panel.setLayout(grid);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(scrollPane, c);
		
		c_1 = new GridBagConstraints();
		c_1.anchor = GridBagConstraints.EAST;
		c_1.fill = GridBagConstraints.VERTICAL;
		c_1.gridx = 0;
		c_1.gridy = 1;
		panel.add(innerPanel, c_1);
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
		
	}
	
	public JTable getTable() {
		return table;
	}
}
