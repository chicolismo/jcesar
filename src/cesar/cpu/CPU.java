package cesar.cpu;

import cesar.panel.ConditionsPanel;
import cesar.panel.DisplayPanel;
import cesar.panel.RegisterPanel;
import cesar.table.DataTable;
import cesar.table.ProgramTable;

import javax.swing.table.AbstractTableModel;
import java.util.Objects;

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

	public CPU() {
		this.memory = new Memory(this);
		this.registers = new short[8];
		this.conditionRegister = new ConditionRegister();
		this.alu = new ALU(this.conditionRegister);
	}

	public void notifyMemoryChange(int index, byte b) {
		Byte value = Byte.valueOf(b);
		programTable.setValueAtAndUpdate(value, index, 2);
		dataTable.setValueAtAndUpdate(value, index, 1);
	}

	public short[] getRegisters() {
		return registers;
	}

	public void setRegisterValue(int regNumber, short value) {
		registers[regNumber] = value;
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

	private void incrementRegister(int registerNumber, int amount) {
		registers[registerNumber] = (short) (registers[registerNumber] + amount);
	}

	private void decrementRegister(int registerNumber, int amount) {
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
		byte value = memory.readByte(registers[7]);
		incrementRegister(7, 1);
		return value;
	}

	public void executeNextInstruction() throws InvalidInstructionException {
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

		case 0b0101: /* Instrução de controle de laço (SOB) */
			break;

		case 0b0110: /* Instrução de desvio para sub-rotina (JSR) */
			break;

		case 0b0111: /* Instrução de retorno de sub-rotina (RTS) */
			break;

		case 0b1000: { /* Instruções de 1 operando */
			byte secondByte = fetchByte();
			executeOneOperandInstruction(firstByte, secondByte);
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
			executeTowOperandInstruction(word);
			break;
		}

		case 0b1111: /* Instrução de parada (HLT) */
			break;

		default:
			// A instrução não é válida.
			String message = String.format("Código de instrução inválido: %d\nValor do R7: %d", opCode,
					0xFFFF & registers[7]);
			throw new InvalidInstructionException(message);
		}

		// TODO: Implementar isto.
		// Verifica se a interface deve ser atualizada com os novos valores dos
		// registradores e códigos de condição. Bem como se as tabelas devem ser
		// alteradas.
		updateRegisterDisplays();
	}

	private void executeOneOperandInstruction(byte firstByte, byte secondByte) {
		// 0b1000_CCCC
		int code = 0x0F & firstByte;

		// 0bXXMM_MRRR
		int addressMode = (0b0011_1000 & secondByte) >> 3; // Modo de endereçamento

		if (addressMode > 7) {
			System.err.println("Modo de endereçamento inválido.");
			System.exit(1);
		}

		int reg = 0b0000_0111 & secondByte; // O número do registrador

		switch (ADDRESS_MODES[addressMode]) {
		case REGISTER: {
			short operand = registers[reg];
			registers[reg] = alu.executeInstruction(code, operand);
			break;
		}
		case REGISTER_POST_INCREMENTED: {
			short operand = memory.readWord(registers[reg]);
			short result = alu.executeInstruction(code, operand);
			memory.writeWord(registers[reg], result);
			incrementRegister(reg, 2);
			break;
		}
		case REGISTER_PRE_DECREMENTED: {
			decrementRegister(reg, 2);
			short operand = memory.readWord(registers[reg]);
			short result = alu.executeInstruction(code, operand);
			memory.writeWord(registers[reg], result);
			break;
		}
		case INDEXED: {
			short offset = memory.readWord(registers[7]);
			incrementRegister(7, 2);
			short operand = memory.readWord((short) (registers[reg] + offset));
			short result = alu.executeInstruction(code, operand);
			memory.writeWord((short) (registers[reg] + offset), result);
			break;
		}
		case REGISTER_INDIRECT: {
			short address = registers[reg];
			short operand = memory.readWord(address);
			short result = alu.executeInstruction(code, operand);
			memory.writeWord(address, result);
			break;
		}
		case POST_INCREMENTED_INDIRECT: {
			short address = memory.readWord(registers[reg]);
			short operand = memory.readWord(address);
			short result = alu.executeInstruction(code, operand);
			memory.writeWord(address, result);
			incrementRegister(reg, 2);
			break;
		}
		case PRE_DECREMENTED_INDIRECT: {
			decrementRegister(reg, 2);
			short address = memory.readWord(registers[reg]);
			short operand = memory.readWord(address);
			short result = alu.executeInstruction(code, operand);
			memory.writeWord(address, result);
			break;
		}
		case INDEX_INDIRECT: {
			short offset = memory.readWord(registers[7]);
			incrementRegister(7, 2);
			short address = memory.readWord((short) (registers[reg] + offset));
			short operand = memory.readWord(address);
			short result = alu.executeInstruction(code, operand);
			memory.writeWord(address, result);
			break;
		}
		default:
			// TODO: Tratar caso de código inválido.
			break;
		}
	}

	public void executeTowOperandInstruction(short word) {
		              // 1CCC_MMMR_RRMM_MRRR
		int code    = (0b0111_0000_0000_0000 & word) >> 12;
		int srcMode = (0b0000_1110_0000_0000 & word) >> 9;
		int srcReg  = (0b0000_0001_1100_0000 & word) >> 6;
		int dstMode = (0b0000_0000_0011_1000 & word) >> 3;
		int dstReg  = (0b0000_0000_0000_0111 & word);

		// Primeiro operando
		short src = 0;
		switch (ADDRESS_MODES[srcMode]) {
		case REGISTER: {
			src = registers[srcReg];
			break;
		}
		case REGISTER_POST_INCREMENTED:
			break;
		case REGISTER_PRE_DECREMENTED:
			break;
		case INDEXED:
			break;
		case REGISTER_INDIRECT:
			break;
		case POST_INCREMENTED_INDIRECT:
			break;
		case PRE_DECREMENTED_INDIRECT:
			break;
		case INDEX_INDIRECT:
			break;
		}

		short dst = 0;
		switch (ADDRESS_MODES[dstMode]) {
		case REGISTER: {
			dst = registers[dstReg];
			short result = alu.executeInstruction(code, src, dst);
			if (code != CMP) {
				registers[dstReg] = result;
			}
			break;
		}
		case REGISTER_POST_INCREMENTED:
			break;
		case REGISTER_PRE_DECREMENTED:
			break;
		case INDEXED:
			break;
		case REGISTER_INDIRECT:
			break;
		case POST_INCREMENTED_INDIRECT:
			break;
		case PRE_DECREMENTED_INDIRECT:
			break;
		case INDEX_INDIRECT:
			break;
		}
	}

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
	private void jmp(short word) {
		// 54 3210
		// 0b0100_XXXX_XXMM_MRRR

		// Modo de endereçamentos são os bits 3, 4, e 5.
		int addressMode = (0b0000_0000_0011_1000 & word) >> 3;

		// O número do registrador são os bits 0, 1 e 2
		int registerNumber = 0b0000_0000_0000_0111 & word;

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
			short word = memory.readWord(registers[7]);
			incrementRegister(7, 2);
			operand = (short) (registers[reg] + word);
			break;
		}

		case REGISTER_INDIRECT:
			operand = memory.readWord(registers[reg]);
			break;

		case POST_INCREMENTED_INDIRECT:
			operand = memory.readWord(registers[reg]);
			incrementRegister(reg, 2);
			break;

		case PRE_DECREMENTED_INDIRECT:
			decrementRegister(reg, 2);
			operand = memory.readWord(registers[reg]);
			break;

		case INDEX_INDIRECT: {
			short word = memory.readWord(registers[7]);
			incrementRegister(7, 2);
			operand = memory.readWord((short) (registers[reg] + word));
			break;
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
			short value = (short) (registers[7] + offsetByte);
			registers[7] = value;
		}
	}

	private void updateRegisterDisplays() {
		for (int i = 0; i < 8; ++i) {
			registerPanels[i].setValue(Short.toUnsignedInt(registers[i]));
		}
		conditionsPanel.setNegative(conditionRegister.isNegative());
		conditionsPanel.setZero(conditionRegister.isZero());
		conditionsPanel.setCarry(conditionRegister.isCarry());
		conditionsPanel.setOverflow(conditionRegister.isOverflow());
	}
}