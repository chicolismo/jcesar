package cesar;

import cesar.panel.DisplayPanel;
import cesar.table.DataTable;
import cesar.table.ProgramTable;

import javax.swing.table.AbstractTableModel;

class CPU {
    private static final int MEMORY_SIZE = 1 << 16;

    private static final int DISPLAY_START_ADDRESS = 65500;
    private static final int DISPLAY_END_ADDRESS = 65535;

    //private byte[] memory;
    private int[] memory;
    private ProgramTable programTable;
    private DataTable dataTable;
    private DisplayPanel displayPanel;

    private int[] registers;

    CPU() {
        //memory = new byte[MEMORY_SIZE];
        memory = new int[MEMORY_SIZE];
        registers = new int[8];
    }

    void setBytes(byte[] data) {
        int size = Math.min(data.length, MEMORY_SIZE);
        int offset = data.length > MEMORY_SIZE ? data.length - MEMORY_SIZE : 0;
        for (int i = 0; i < size; ++i) {
            memory[i] = 0xff & data[i + offset];
        }
        updateTables();
        updateDisplay();
    }

    private void updateTables() {
        for (int i = 0; i < MEMORY_SIZE; ++i) {
            int value = memory[i];
            programTable.setValueAt(value, i, 2);
            dataTable.setValueAt(value, i, 1);
        }
        ((AbstractTableModel) programTable.getModel()).fireTableDataChanged();
        ((AbstractTableModel) dataTable.getModel()).fireTableDataChanged();
    }

    private void updateDisplay() {
        for (int counter = DISPLAY_START_ADDRESS, i = 0; counter <= DISPLAY_END_ADDRESS; ++counter, ++i) {
            displayPanel.setValueAt(i, (char) memory[counter]);
        }
        displayPanel.repaint();
    }

    void setProgramTable(ProgramTable programTable) {
        this.programTable = programTable;
    }

    void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    void setDisplayPanel(DisplayPanel displayPanel) {
        this.displayPanel = displayPanel;
    }

}
