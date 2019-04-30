package cesar.panel;

import cesar.table.Table;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;

public class SidePanel extends JDialog {
	private static final long serialVersionUID = -5287184935159813862L;

	private Table table;
	private JPanel panel;
	private JLabel label;
	private JTextField textField;

	public SidePanel(JFrame parent, String title, Table table) {
		super(parent, title, Dialog.ModalityType.MODELESS);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		panel = new JPanel();
		setContentPane(panel);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.table = table;
		JScrollPane scrollPane = new JScrollPane(this.table);
		scrollPane.setDoubleBuffered(true);
		this.table.setFillsViewportHeight(true);

		label = new JLabel("0");
		textField = new JTextField(10);
		textField.setHorizontalAlignment(JTextField.RIGHT);
		var dim = new Dimension(100, 20);
		textField.setSize(dim);
		textField.setMinimumSize(dim);
		textField.setMaximumSize(dim);
		textField.setPreferredSize(dim);

		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
		innerPanel.add(label);
		innerPanel.add(textField);

		GridBagLayout grid = new GridBagLayout();
		grid.columnWidths = new int[] { 0 };
		grid.rowHeights = new int[] { 0, 0 };
		grid.columnWeights = new double[] { 1.0 };
		grid.rowWeights = new double[] { 1.0, 0.0 };
		panel.setLayout(grid);

		GridBagConstraints c;

		c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(scrollPane, c);

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 1;
		panel.add(innerPanel, c);

		initEvents();
	}

	public JTable getTable() {
		return table;
	}

	private void initEvents() {
		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.addListSelectionListener((ListSelectionEvent event) -> {
			int index = table.getSelectedRow();
			String address = table.getAddressAtRow(index);
			String value = table.getValueAtRow(index);
			label.setText(address);
			textField.setText(value);
			textField.grabFocus();
			textField.selectAll();
		});
	}
}
