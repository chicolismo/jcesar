package cesar.panel;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cesar.table.Table;
import cesar.util.Bytes;
import cesar.util.Shorts;

public class SidePanel extends JDialog {
    private static final long serialVersionUID = -5287184935159813862L;

    private boolean isDecimal;
    private int addressValue;
    private int datumValue;
    private Table table;
    private JLabel label;
    private JTextField textField;

    public SidePanel(JFrame parent, String title, Table table) {
        super(parent, title, Dialog.ModalityType.MODELESS);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.addressValue = 0;
        this.datumValue   = 0;
        this.isDecimal    = true;
        this.table        = table;

        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(this.table);
        scrollPane.setDoubleBuffered(true);
        this.table.setFillsViewportHeight(true);

        label     = new JLabel("0");
        textField = new JTextField("0", 6);
        // NOTE: O Cesar original não alinha a caixa de texto à direita...
        // textField.setHorizontalAlignment(JTextField.RIGHT);

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        innerPanel.add(label);
        innerPanel.add(textField);

        GridBagLayout grid = new GridBagLayout();
        grid.columnWidths  = new int[] { 0 };
        grid.rowHeights    = new int[] { 0, 0 };
        grid.columnWeights = new double[] { 1.0 };
        grid.rowWeights    = new double[] { 1.0, 0.0 };
        panel.setLayout(grid);

        GridBagConstraints c;

        c        = new GridBagConstraints();
        c.insets = new Insets(0, 0, 5, 0);
        c.fill   = GridBagConstraints.BOTH;
        c.gridx  = 0;
        c.gridy  = 0;
        panel.add(scrollPane, c);

        c        = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.fill   = GridBagConstraints.HORIZONTAL;
        c.gridx  = 0;
        c.gridy  = 1;
        panel.add(innerPanel, c);

        initEvents();
    }

    public Table getTable() {
        return table;
    }

    public JTextField getTextField() {
        return textField;
    }

    public JLabel getLabel() {
        return label;
    }

    private void initEvents() {
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                int rowIndex = table.getSelectedRow();
                if (rowIndex != -1) {
                    addressValue = Shorts.toUnsignedInt(table.getAddressAtRow(rowIndex));
                    datumValue   = Bytes.toUnsignedInt(table.getValueAtRow(rowIndex));
                    updateControls(addressValue, datumValue, true);
                }
            }
        });
    }

    /**
     * Atualiza o rótulo de endereço e o campo de texto com os valores de endereço e
     * dado fornecidos.
     * 
     * A representação numérica irá respeitar o valor atual de {@code isDecimal}.
     * 
     * @param address
     * @param value
     */
    private void updateControls(int address, int value, boolean grabFocus) {
        String addressText = isDecimal ? Integer.toString(address) : Integer.toHexString(address);
        String valueText = isDecimal ? Integer.toString(value) : Integer.toHexString(value);
        label.setText(addressText);
        textField.setText(valueText);
        if (grabFocus) {
            textField.grabFocus();
            textField.selectAll();
        }
    }

    public void setDecimal(boolean isDecimal) {
        this.isDecimal = isDecimal;
        table.setDecimal(isDecimal);
        updateControls(this.addressValue, this.datumValue, false);
    }
}
