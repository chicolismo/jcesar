package cesar.cpu;

import java.util.Objects;

import javax.swing.table.AbstractTableModel;

import cesar.panel.ConditionsPanel;
import cesar.panel.DisplayPanel;
import cesar.panel.RegisterPanel;
import cesar.table.DataTable;
import cesar.table.ProgramTable;
import cesar.util.Shorts;

public class CPU {
    private enum AddressMode {
        REGISTER, REGISTER_POST_INCREMENTED, REGISTER_PRE_DECREMENTED, INDEXED, REGISTER_INDIRECT,
        POST_INCREMENTED_INDIRECT, PRE_DECREMENTED_INDIRECT, INDEX_INDIRECT
    }

    private static final AddressMode[] ADDRESS_MODES = AddressMode.values();
    private static final int CMP = 4;

    private RegisterPanel[] registerPanels;
    private ProgramTable programTable;
    private DataTable dataTable;
    private DisplayPanel displayPanel;
    private ConditionsPanel conditionsPanel;

    private final Memory memory;
    private final short[] registers;
    private final ConditionRegister conditionRegister;
    private final ALU alu;
    private boolean isHalted;

    public CPU() {
        this.memory            = new Memory(this);
        this.registers         = new short[8];
        this.conditionRegister = new ConditionRegister();
        this.alu               = new ALU(this.conditionRegister);
        this.isHalted          = false;
    }

    public Memory getMemory() {
        return memory;
    }

    public short[] getRegisters() {
        return registers;
    }

    public short getRegister(int regNumber) {
        return registers[regNumber];
    }

    public void setRegister(int regNumber, short value) {
        registers[regNumber] = value;
    }

    public short getPC() {
        return getRegister(7);
    }

    public void setPC(short value) {
        setRegister(7, value);
    }

    /**
     * Incrementa PC = PC + 2
     */
    void incrementPC() {
        incrementPC(2);
    }

    void incrementPC(int amount) {
        incrementRegister(7, amount);
    }

    void incrementRegister(int registerNumber) {
        incrementRegister(registerNumber, 2);
    }

    void incrementRegister(int registerNumber, int amount) {
        setRegister(registerNumber, (short) (getRegister(registerNumber) + amount));
    }

    void decrementRegister(int registerNumber, int amount) {
        setRegister(registerNumber, (short) (getRegister(registerNumber) - amount));
    }

    void decrementRegister(int registerNumber) {
        setRegister(registerNumber, (short) (getRegister(registerNumber) - 2));
    }

    public void notifyMemoryChange(int index, byte b) {
        Byte value = Byte.valueOf(b);
        programTable.setValueAtAndUpdate(value, index, 2);
        dataTable.setValueAtAndUpdate(value, index, 1);
    }

    public void setRegisterPanels(RegisterPanel[] panels) {
        registerPanels = Objects.requireNonNull(panels);
    }

    public void setConditionsPanel(ConditionsPanel panel) {
        this.conditionsPanel = Objects.requireNonNull(panel);
    }

    public void setProgramTable(ProgramTable programTable) {
        this.programTable = Objects.requireNonNull(programTable);
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = Objects.requireNonNull(dataTable);
    }

    public void setDisplayPanel(DisplayPanel displayPanel) {
        this.displayPanel = Objects.requireNonNull(displayPanel);
    }

    public void setBytes(byte[] data) {
        memory.setBytes(data);
        updateTables();
        updateDisplay();
    }

    private void updateTables() {
        final int memorySize = memory.size();
        for (int i = 0; i < memorySize; ++i) {
            Byte value = Byte.valueOf(memory.readByte((short) i));
            programTable.setValueAt(value, i, 2);
            dataTable.setValueAt(value, i, 1);
        }
        ((AbstractTableModel) programTable.getModel()).fireTableDataChanged();
        ((AbstractTableModel) dataTable.getModel()).fireTableDataChanged();
    }

    private void updateDisplay() {
        displayPanel.setValue(memory.getDisplayBytes());
        displayPanel.repaint();
    }

    /**
     * Lê o byte cujo endereço está no R7. Incrementa o R7 em 1.
     *
     * @return byte da memória cujo endereço se encontra atualmente no PC.
     */
    private byte fetchByte() {
        byte value = memory.readByte(getPC());
        incrementPC(1);
        return value;
    }

    public void executeNextInstruction() throws InvalidInstructionException, InvalidOpCodeException, HaltedException {
        isHalted = false;

        byte firstByte = fetchByte();

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
            byte secondByte = fetchByte();
            executeConditionalBranch(firstByte, secondByte);
            break;
        }
        case 0b0100: { /* Desvio incondicional (JMP) */
            byte secondByte = fetchByte();
            short word = Memory.bytesToShort(firstByte, secondByte);
            jmp(word);
            break;
        }
        case 0b0101: { /* Instrução de controle de laço (SOB) */
            byte secondByte = fetchByte();
            short word = Memory.bytesToShort(firstByte, secondByte);
            sob(word);
            break;
        }
        case 0b0110: { /* Instrução de desvio para sub-rotina (JSR) */
            byte secondByte = fetchByte();
            short word = Memory.bytesToShort(firstByte, secondByte);
            jsr(word);
            break;
        }
        case 0b0111: { /* Instrução de retorno de sub-rotina (RTS) */
            rts(firstByte);
            break;
        }
        case 0b1000: { /* Instruções de 1 operando */
            byte secondByte = fetchByte();
            try {
                executeOneOperandInstruction(firstByte, secondByte);
            }
            catch (InvalidAddressModeException e) {
                e.printStackTrace();
                short instruction = Memory.bytesToShort(firstByte, secondByte);
                throw InvalidInstructionException.withInstruction(instruction);
            }
            break;
        }
        case 0b1001: /* MOV */
        case 0b1010: /* ADD */
        case 0b1011: /* SUB */
        case 0b1100: /* CMP */
        case 0b1101: /* AND */
        case 0b1110: /* OR */ {
            byte secondByte = fetchByte();
            short word = Memory.bytesToShort(firstByte, secondByte);
            try {
                executeTowOperandInstruction(word);
            }
            catch (InvalidAddressModeException e) {
                e.printStackTrace();
                throw InvalidInstructionException.withInstruction(word);
            }
            break;
        }
        case 0b1111: /* Instrução de parada (HLT) */
            isHalted = true;
            break;
        default: {
            throw InvalidOpCodeException.withOpCode(opCode);
        }
        }

        // TODO: Implementar isto.
        // Verifica se a interface deve ser atualizada com os novos valores dos
        // registradores e códigos de condição. Bem como se as tabelas devem ser
        // alteradas.
        updateRegisterDisplays();

        if (isHalted) {
            throw new HaltedException();
        }
    }

    private void executeOneOperandInstruction(byte firstByte, byte secondByte) throws InvalidAddressModeException {
        // 0b1000_CCCC
        int code = 0x0F & firstByte;
        // 0bXXMM_MRRR
        int addressMode = (0b0011_1000 & secondByte) >> 3; // Modo de endereçamento
        int reg = (0b0000_0111 & secondByte); // O número do registrador

        switch (ADDRESS_MODES[addressMode]) {
        case REGISTER: {
            short operand = getRegister(reg);
            setRegister(reg, alu.executeInstruction(code, operand));
            break;
        }
        case REGISTER_POST_INCREMENTED: {
            short address = getRegister(reg);
            short operand = memory.readWord(address);
            short result = alu.executeInstruction(code, operand);
            memory.writeWord(address, result);
            incrementRegister(reg);
            break;
        }
        case REGISTER_PRE_DECREMENTED: {
            decrementRegister(reg);
            short address = getRegister(reg);
            short operand = memory.readWord(address);
            short result = alu.executeInstruction(code, operand);
            memory.writeWord(address, result);
            break;
        }
        case INDEXED: {
            short offset = memory.readWord(getPC());
            incrementPC();
            short address = (short) (getRegister(reg) + offset);
            short operand = memory.readWord(address);
            short result = alu.executeInstruction(code, operand);
            memory.writeWord(address, result);
            break;
        }
        case REGISTER_INDIRECT: {
            short address = getRegister(reg);
            short operand = memory.readWord(address);
            short result = alu.executeInstruction(code, operand);
            memory.writeWord(address, result);
            break;
        }
        case POST_INCREMENTED_INDIRECT: {
            short address = memory.readWord(getRegister(reg));
            short operand = memory.readWord(address);
            short result = alu.executeInstruction(code, operand);
            memory.writeWord(address, result);
            incrementRegister(reg);
            break;
        }
        case PRE_DECREMENTED_INDIRECT: {
            decrementRegister(reg);
            short address = memory.readWord(getRegister(reg));
            short operand = memory.readWord(address);
            short result = alu.executeInstruction(code, operand);
            memory.writeWord(address, result);
            break;
        }
        case INDEX_INDIRECT: {
            short offset = memory.readWord(getPC());
            incrementPC();
            short address = memory.readWord((short) (getRegister(reg) + offset));
            short operand = memory.readWord(address);
            short result = alu.executeInstruction(code, operand);
            memory.writeWord(address, result);
            break;
        }
        default:
            throw InvalidAddressModeException.withAddressMode(addressMode);
        }
    }

    public void executeTowOperandInstruction(short word) throws InvalidAddressModeException {
        // 1CCC_MMMR_RRMM_MRRR
        int code = (0b0111_0000_0000_0000 & word) >> 12;
        int srcMode = (0b0000_1110_0000_0000 & word) >> 9;
        int srcReg = (0b0000_0001_1100_0000 & word) >> 6;

        // Primeiro operando
        short src = 0;
        switch (ADDRESS_MODES[srcMode]) {
        case REGISTER: {
            src = getRegister(srcReg);
            break;
        }
        case REGISTER_POST_INCREMENTED: {
            src = memory.readWord(getRegister(srcReg));
            incrementRegister(srcReg);
            break;
        }
        case REGISTER_PRE_DECREMENTED: {
            decrementRegister(srcReg);
            src = memory.readWord(getRegister(srcReg));
            break;
        }
        case INDEXED: {
            short offset = memory.readWord(getPC());
            incrementPC();
            src = memory.readWord((short) (getRegister(srcReg) + offset));
            break;
        }
        case REGISTER_INDIRECT: {
            short address = getRegister(srcReg);
            src = memory.readWord(address);
            break;
        }
        case POST_INCREMENTED_INDIRECT: {
            short address = memory.readWord(getRegister(srcReg));
            src = memory.readWord(address);
            incrementRegister(srcReg);
            break;
        }
        case PRE_DECREMENTED_INDIRECT: {
            decrementRegister(srcReg);
            short address = memory.readWord(getRegister(srcReg));
            src = memory.readWord(address);
            break;
        }
        case INDEX_INDIRECT: {
            short offset = memory.readWord(getPC());
            incrementPC();
            short address = memory.readWord((short) (getRegister(srcReg) + offset));
            src = memory.readWord(address);
            break;
        }
        default: {
            throw InvalidAddressModeException.withAddressMode(srcMode);
        }
        }

        // 1CCC_MMMR_RRMM_MRRR
        int dstMode = (0b0000_0000_0011_1000 & word) >> 3;
        int dstReg = (0b0000_0000_0000_0111 & word);

        switch (ADDRESS_MODES[dstMode]) {
        case REGISTER: {
            short dst = getRegister(dstReg);
            short result = alu.executeInstruction(code, src, dst);
            if (code != CMP) {
                setRegister(dstReg, result);
            }
            break;
        }
        case REGISTER_POST_INCREMENTED: {
            short address = getRegister(dstReg);
            short dst = memory.readWord(address);
            short result = alu.executeInstruction(code, src, dst);
            if (code != CMP) {
                memory.writeWord(address, result);
            }
            incrementRegister(dstReg);
            break;
        }
        case REGISTER_PRE_DECREMENTED: {
            incrementRegister(dstReg);
            short address = getRegister(dstReg);
            short dst = memory.readWord(address);
            short result = alu.executeInstruction(code, src, dst);
            if (code != CMP) {
                memory.writeWord(address, result);
            }
            break;
        }
        case INDEXED: {
            short offset = memory.readWord(getPC());
            incrementPC();
            short address = (short) (getRegister(dstReg) + offset);
            short dst = memory.readWord(address);
            short result = alu.executeInstruction(code, src, dst);
            if (code != CMP) {
                memory.writeWord(address, result);
            }
            break;
        }
        case REGISTER_INDIRECT: {
            short address = getRegister(dstReg);
            short dst = memory.readWord(address);
            short result = alu.executeInstruction(code, src, dst);
            if (code != CMP) {
                memory.writeWord(address, result);
            }
            break;
        }
        case POST_INCREMENTED_INDIRECT: {
            short address = memory.readWord(getRegister(dstReg));
            short dst = memory.readWord(address);
            short result = alu.executeInstruction(code, src, dst);
            if (code != CMP) {
                memory.writeWord(address, result);
            }
            incrementRegister(dstReg);
            break;
        }
        case PRE_DECREMENTED_INDIRECT: {
            decrementRegister(dstReg);
            short address = memory.readWord(getRegister(dstReg));
            short dst = memory.readWord(address);
            short result = alu.executeInstruction(code, src, dst);
            if (code != CMP) {
                memory.writeWord(address, result);
            }
            break;
        }
        case INDEX_INDIRECT: {
            short offset = memory.readWord(getPC());
            incrementPC();
            short address = memory.readWord((short) (getRegister(dstReg) + offset));
            short dst = memory.readWord(address);
            short result = alu.executeInstruction(code, src, dst);
            if (code != CMP) {
                memory.writeWord(address, result);
            }
            break;
        }
        default:
            throw InvalidAddressModeException.withAddressMode(dstMode);
        }
    }

    /*
     * TODO: Transferir todas as operações para a ALU
     */

    /**
     * Liga os códigos de condição contidos nos 4 bits menos significativos da
     * palavra fornecida. Os quatros bits representam as condições n z v c.
     *
     * @param value
     */
    private void scc(byte value) {
        conditionRegister.setConditions(value);
    }

    /**
     * Desliga os códigos de condição contidos nos 4 bits menos significativos da
     * palavra fornecida. Os quatros bits representam as condições n z v c.
     *
     * @param value
     */
    private void ccc(byte value) {
        conditionRegister.clearConditions(value);
    }

    /* Instruções */
    private void jmp(short word) throws InvalidInstructionException {
        // 54 3210
        // 0b0100_XXXX_XXMM_MRRR

        // Modo de endereçamentos são os bits 3, 4, e 5.
        int addressMode = (0b0000_0000_0011_1000 & word) >> 3;

        // O número do registrador são os bits 0, 1 e 2
        int registerNumber = 0b0000_0000_0000_0111 & word;

        try {
            setPC(getOperand(addressMode, registerNumber));
        }
        catch (InvalidAddressModeException e) {
            e.printStackTrace();
            throw InvalidInstructionException.withInstruction(word);
        }
    }

    private void sob(short instruction) {
        int registerNumber = (0b0000_0111_0000_0000 & instruction) >> 8;
        short offset = (short) (0b0000_0000_1111_1111 & instruction);

        decrementRegister(registerNumber, 1);
        if (getRegister(registerNumber) != 0) {
            setPC((short) (getPC() - offset));
        }
    }

    /**
     * Os bits rrr da primeira palavra indicam um registrador, enquanto a segunda
     * palavra é utilizada para calcular o endereço da sub-rotina, de modo idêntico
     * à instrução de desvio incondicional (JMP). O desvio para a sub-rotina é
     * realizado conforme a sequência de operações a seguir:
     * 
     * <pre>
     *      temporário  <- endereço da subrotina
     *      pilha       <- registrador
     *      registrador <- R7
     *      R7          <- temporário
     * </pre>
     * 
     * Com isso, o registrador indicado na primeira palavra é colocado na pilha, a
     * seguir o PC atual é salvo neste registrador e, finalmente, o PC recebe o
     * enderço da sub-rotina. Noe que, se o registrador for o próprio PC, o efeito
     * do desvio resume-se a salvar o PC atual na pilha e, então, desviar para a
     * sub-rotina. Assim como na instrução de JMP, o uso do modo zero para o cálculo
     * do endereço da sub-rotina é tratado como NOP.
     */
    private void jsr(short instruction) {
        // 0110_xrrr_xxmm_mrrr

        int reg = (0b0000_0111_0000_0000 & instruction) >> 8;
        int addressMode = (0b0000_0000_0011_1000 & instruction) >> 3;
        int registerNumber = (0b0000_0000_0000_0111 & instruction);
        try {
            // o endereço da sub-rotina é lido no valor temporário
            short temp = getOperand(addressMode, registerNumber);
            memory.push(getRegister(reg));
            setRegister(reg, getPC());
            setPC(temp);
        }
        catch (InvalidAddressModeException e) {
            // NOP
        }
    }

    /**
     * A instrução de retorno de sub-rotina (RTS) ocupa 1 único byte e tem o formato
     * 0111_xrrr. O bit x pode ser qualquer valor, e <code>rrr</code> indica o
     * registrador de retorno. A instrução realiza as seguintes operações
     * necessárias para desfazer o efeito de uma instrução de desvio para a
     * sub-rotina:
     * 
     * <pre>
     *      R7          <- registrador
     *      registrador <- topo da pilha
     * </pre>
     * 
     * Ou seja, o PC é atualizado com o conteúdo do registrador indicado e, a
     * seguir, este registrador é carregado com o conteúdo do topo da pilha. Se o
     * registrador for o próprio PC, a instrução resume-se a atualizar o PC com o
     * conteúdo do topo da pilha.
     * 
     * @param instruction
     */
    private void rts(byte instruction) {
        // 0111_xrrr
        int returnRegister = (0b0000_0111 & instruction);
        setPC(getRegister(returnRegister));
        setRegister(returnRegister, memory.pop());
    }

    private short getOperand(int addressMode, int reg) throws InvalidAddressModeException {
        short operand = 0;
        switch (ADDRESS_MODES[addressMode]) {
        case REGISTER: {
            operand = getRegister(reg);
            break;
        }
        case REGISTER_POST_INCREMENTED: {
            short address = getRegister(reg);
            operand = memory.readWord(address);
            incrementRegister(reg);
            break;
        }
        case REGISTER_PRE_DECREMENTED: {
            decrementRegister(reg);
            short address = getRegister(reg);
            operand = memory.readWord(address);
            break;
        }
        case INDEXED: {
            short offset = memory.readWord(getPC());
            incrementPC();
            short address = (short) (getRegister(reg) + offset);
            operand = memory.readWord(address);
            break;
        }
        case REGISTER_INDIRECT: {
            short address = getRegister(reg);
            operand = memory.readWord(address);
            break;
        }
        case POST_INCREMENTED_INDIRECT: {
            short address = memory.readWord(getRegister(reg));
            operand = memory.readWord(address);
            incrementRegister(reg);
            break;
        }
        case PRE_DECREMENTED_INDIRECT: {
            decrementRegister(reg);
            short address = memory.readWord(getRegister(reg));
            operand = memory.readWord(address);
            break;
        }
        case INDEX_INDIRECT: {
            short offset = memory.readWord(getPC());
            incrementPC();
            short address = memory.readWord((short) (getRegister(reg) + offset));
            operand = memory.readWord(address);
            break;
        }
        default: {
            throw InvalidAddressModeException.withAddressMode(addressMode);
        }
        }

        return operand;
    }

    /**
     * Implementa todas as funções de desvio condicional.
     *
     * @param instructionByte O byte que contém o código de condição nos 4 bits
     *                        menos significativos.
     * @param offsetByte      O byte que contém o deslocamento a ser somado de R7.
     */
    private void executeConditionalBranch(byte instructionByte, byte offsetByte) {
        // O código do desvio condicional são so 4 bits menos significativos do
        // firstByte
        int code = 0x0F & instructionByte;

        if (conditionRegister.shouldJump(code)) {
            short value = (short) (getPC() + offsetByte);
            setPC(value);
        }
    }

    public void updateRegisterDisplays() {
        for (int i = 0; i < 8; ++i) {
            registerPanels[i].setValue(Shorts.toUnsignedInt(getRegister(i)));
        }
        conditionsPanel.setNegative(conditionRegister.isNegative());
        conditionsPanel.setZero(conditionRegister.isZero());
        conditionsPanel.setCarry(conditionRegister.isCarry());
        conditionsPanel.setOverflow(conditionRegister.isOverflow());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("== REGISTRADORES ==========\n  ");
        for (int i = 0; i < 8; ++i) {
            int value = Shorts.toUnsignedInt(registers[i]);
            String binary = Integer.toBinaryString(value);
            String hex = Integer.toHexString(value);
            String s = String.format("  R%d: %d (%s) (%s)\n", i, value, hex, binary);
            builder.append(s);
        }
        builder.append("== CONDIÇÕES ==========\n");
        int n = conditionRegister.isNegative() ? 1 : 0;
        int z = conditionRegister.isZero() ? 1 : 0;
        int v = conditionRegister.isOverflow() ? 1 : 0;
        int c = conditionRegister.isCarry() ? 1 : 0;
        builder.append(String.format("  N: %d\n  Z: %d\n  V: %d\n  C: %d\n", n, z, v, c));
        return builder.toString();
    }

}
