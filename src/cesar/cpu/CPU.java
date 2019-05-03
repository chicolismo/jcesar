package cesar.cpu;

import cesar.panel.DisplayPanel;
import cesar.table.DataTable;
import cesar.table.ProgramTable;

import javax.swing.table.AbstractTableModel;

public class CPU {
    private enum AddressMode {
        REGISTER,
        REGISTER_POST_INCREMENTED,
        REGISTER_PRE_DECREMENTED,
        INDEXED,
        REGISTER_INDIRECT,
        POST_INCREMENTED_INDIRECT,
        PRE_DECREMENTED_INDIRECT,
        INDEX_INDIRECT
    }

    private static final AddressMode[] ADDRESS_MODES = AddressMode.values();
    private static final int DISPLAY_START_ADDRESS = 65500;
    private static final int DISPLAY_END_ADDRESS = 65535;

    private ProgramTable programTable;
    private DataTable dataTable;
    private DisplayPanel displayPanel;

    private final Memory memory;
    private final short[] registers;
    private final ConditionRegister conditionRegister;

    CPU() {
        memory = new Memory();
        registers = new short[8];
        conditionRegister = new ConditionRegister(0b0000);
    }

    public void setProgramTable(ProgramTable programTable) {
        this.programTable = programTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public void setDisplayPanel(DisplayPanel displayPanel) {
        this.displayPanel = displayPanel;
    }

    private short concatBytesToWord(byte a, byte b) {
        return (short) (((0xFF & a) << 8) | (0xFF & b));
    }

    private void incrementRegister(int registerNumber, int amount) {
        registers[registerNumber] = (short) (registers[registerNumber] + amount);
    }

    private void incrementRegister(int registerNumber, byte amount) {
        registers[registerNumber] = (short) (registers[registerNumber] + amount);
    }

    private void decrementRegister(int registerNumber, int amount) {
        registers[registerNumber] = (short) (registers[registerNumber] - amount);
    }

    private void decrementRegister(int registerNumber, byte amount) {
        registers[registerNumber] = (short) (registers[registerNumber] - amount);
    }

    public void setBytes(byte[] data) {
        memory.setBytes(data);
        updateTables();
        updateDisplay();
    }

    private void updateTables() {
        final int memorySize = memory.size();
        for (int i = 0; i < memorySize; ++i) {
            final int value = UnsignedBytes.toInt(memory.read(i));
            programTable.setValueAt(value, i, 2);
            dataTable.setValueAt(value, i, 1);
        }
        ((AbstractTableModel) programTable.getModel()).fireTableDataChanged();
        ((AbstractTableModel) dataTable.getModel()).fireTableDataChanged();
    }

    private void updateDisplay() {
        for (int counter = DISPLAY_START_ADDRESS, i = 0; counter <= DISPLAY_END_ADDRESS; ++counter, ++i) {
            displayPanel.setValueAt(i, (char) memory.read(counter));
        }
        displayPanel.repaint();
    }

    void executeNextInstruction() {
        byte firstByte = memory.read(registers[7]);
        incrementRegister(7, 1);

        // Copia os 4 bits mais significativos
        int opCode = 0x0F & (firstByte >> 4);

        switch (opCode) {
            case 0b0000: /* NOP */
                break;

            case 0b0001: /* Código de condição */
                ccc(firstByte);
                break;

            case 0b0010: /* Código de condição */
                scc(firstByte);
                break;

            case 0b0011: { /* Desvio condicional */
                byte secondByte = memory.read(registers[7]);
                incrementRegister(7, 1);
                conditionalBranch(firstByte, secondByte);
            }
            break;

            case 0b0100: { /* Desvio incondicional (JMP) */
                byte secondByte = memory.read(registers[7]);
                short word = concatBytesToWord(firstByte, secondByte);
                jmp(word);
            }
            break;

            case 0b0101: /* Instrução de controle de laço (SOB)*/
                break;

            case 0b0110: /* Instrução de desvio para sub-rotina (JSR) */
                break;

            case 0b0111: /* Instrução de retorno de sub-rotina (RTS) */
                break;

            case 0b1000: /* Instruções de 1 operando */
                break;

            case 0b1001: /* MOV */
                break;

            case 0b1010: /* ADD */
                break;

            case 0b1011: /* SUB */
                break;

            case 0b1100: /* CMP */
                break;

            case 0b1101: /* AND */
                break;

            case 0b1110: /* OR */
                break;

            case 0b1111: /* Instrução de parada (HLT) */
                break;
        }

        // TODO: Implementar isto.
        // Verifica se a interface deve ser atualizada com os novos valores dos
        // registradores e códigos de condição.  Bem como se as tabelas devem ser
        // alteradas.
    }

    /**
     * Liga os códigos de condição contidos nos 4 bits menos significativos da palavra fornecida.
     * Os quatros bits representam as condições n z v c.
     *
     * @param value
     */
    private void scc(byte value) {
        conditionRegister.setConditions(value);
    }

    /**
     * Desliga os códigos de condição contidos nos 4 bits menos significativos da palavra fornecida.
     * Os quatros bits representam as condições n z v c.
     *
     * @param value
     */
    private void ccc(byte value) {
        conditionRegister.clearConditions(value);
    }

    /* Instruções */
    private void jmp(short word) {
        //               54 3210
        // 0b0100_XXXX_XXMM_MRRR

        // Modo de endereçamentos são os bits 3, 4, e 5.
        int addressMode = (0b0011_1000 & word) >> 3;

        // O número do registrador são os bits 0, 1 e 2
        int registerNumber = 0b0111 & word;

        registers[7] = getOperand(addressMode, registerNumber);
    }

    private short getOperand(int addressMode, int reg) {
        if (addressMode < 0 || addressMode > 7) {
            System.err.println("Modo de endereçamento inválido.");
            System.exit(1);
        }

        short operand = 0;

        switch (ADDRESS_MODES[addressMode]) {

            case REGISTER:
                operand = registers[reg];
                break;

            case REGISTER_POST_INCREMENTED:
                operand = registers[reg];
                incrementRegister(reg, 2);
                break;

            case REGISTER_PRE_DECREMENTED:
                decrementRegister(reg, 2);
                operand = registers[reg];
                break;

            case INDEXED: {
                byte firstByte = memory.read(registers[7]);
                incrementRegister(7, 1);
                byte secondByte = memory.read(registers[7]);
                incrementRegister(7, 1);
                short word = concatBytesToWord(firstByte, secondByte);
                operand = (short) (registers[reg] + word);
            }
            break;

            case REGISTER_INDIRECT:
                operand = memory.read(registers[reg]);
                break;

            case POST_INCREMENTED_INDIRECT:
                operand = memory.read(registers[reg]);
                incrementRegister(reg, 2);
                break;

            case PRE_DECREMENTED_INDIRECT:
                decrementRegister(reg, 2);
                operand = memory.read(registers[reg]);
                break;

            case INDEX_INDIRECT: {
                byte firstByte = memory.read(registers[7]);
                incrementRegister(7, 1);
                byte secondByte = memory.read(registers[7]);
                incrementRegister(7, 1);
                short word = concatBytesToWord(firstByte, secondByte);
                operand = memory.read((short) (registers[reg] + word));
            }
            break;
        }

        return operand;
    }

    /**
     * Implementa todas as funções de desvio condicional.
     *
     * @param instructionByte O byte que contém o código de condição nos 4 bits menos significativos.
     * @param offsetByte      O byte que contém o deslocamento a ser somado de R7.
     */
    private void conditionalBranch(byte instructionByte, byte offsetByte) {
        // O código do desvio condicional são so 4 bits menos significativos do firstByte
        int code = 0x0F & instructionByte;

        if (conditionRegister.shouldJump(code)) {
            short value = (short) (registers[7] + offsetByte);
            registers[7] = value;
        }
    }
}
