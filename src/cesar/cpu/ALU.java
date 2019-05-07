package cesar.cpu;

import java.util.Objects;

/*
 * Quando ao incrementar um número ele se tornar negativo, temos um caso de
 * overflow.  Isso é, não temos espaço suficiente para representar o sinal do
 * número.
 *
 * Ou seja, quando o bit mais significativo passa de 0 para 1 durante uma soma.
 *
 * Quando não há espaço para enviar o vai-um, temos um carry.
 */
public class ALU {
    private ConditionRegister register;

    public ALU(ConditionRegister register) {
        this.register = Objects.requireNonNull(register);
    }

    public boolean isOverflow(short a, short b, short c) {
        return (a > 0 && b > 0 && c < 0) || (a < 0 && b < 0 && c > 0);
    }

    public boolean isCarryAdd(short a, short b) {
        return UnsignedShorts.toInt(a) + UnsignedShorts.toInt(b) > 0xFFFF;
    }

    public boolean isCarrySub(short a, short b) {
        return UnsignedShorts.toInt(a) - UnsignedShorts.toInt(b) < 0;
    }

    /**
     * Zera o operando.
     * N = t
     * Z = t
     * V = 0
     * C = 0
     * Testa as condições N e Z, e zera as condições V e C.
     * @param operand
     * @return 0
     */
    public short clr(short operand) {
        register.setNegative(false);
        register.setZero(true);
        register.setCarry(false);
        register.setOverflow(false);
        return (short) 0;
    }

    /**
     * Nega o operando.
     * N = t
     * Z = t
     * C = 1
     * O = 0
     * @param operand
     * @return ~(operand)
     */
    public short not(short operand) {
        operand = (short) ~operand;
        register.setNegative(operand < 0);
        register.setZero(operand == 0);
        register.setCarry(true);
        register.setOverflow(false);
        return operand;
    }

    /**
     * Incrementa o operando.
     * @param operand
     * @return operand + 1
     */
    public short inc(short operand) {
        short newValue = (short) (operand + 1);
        register.setNegative(newValue < 0);
        register.setZero(newValue == 0);
        register.setCarry(isCarryAdd(operand, (short) 1));
        register.setOverflow(isOverflow(operand, (short) 1, newValue));
        return newValue;
    }

    public short dec(short operand) {
        short newValue = (short) (operand - 1);
        register.setNegative(newValue < 0);
        register.setZero(newValue == 0);
        register.setCarry(isCarrySub(operand, (short) 1));
        register.setOverflow(isOverflow(operand, (short) -1, newValue));
        return newValue;
    }

    public short neg(short operand) {
        short newValue = (short) (~operand + 1);
        register.setZero(newValue == 0);
        register.setNegative(newValue < 0);
        register.setCarry(isCarryAdd((short) ~operand, (short) 1));
        register.setOverflow(isOverflow((short) ~operand, (short) 1, newValue));
        return newValue;
    }

    public short tst(short operand) {
        register.setZero(operand == 0);
        register.setNegative(operand < 0);
        register.setCarry(false);
        register.setOverflow(false);
        return operand;
    }

    public short ror(short operand) {
        int lsb = operand & 1;
        operand = (short) (operand >> 1);
        boolean carry = lsb == 1;
        if (carry) {
            operand = (short) (0b10000000 | operand);
        }
        boolean zero = operand == 0;
        register.setZero(zero);
        register.setNegative(operand < 0);
        register.setCarry(carry);
        register.setOverflow(zero != carry);
        return operand;
    }

    public short rol(short operand) {
        int msb = operand & 0b10000000;
        operand = (short) (operand << 1);
        boolean carry = msb == 0b10000000;
        if (carry) {
            operand = (short) (1 | operand);
        }
        boolean zero = operand == 0;
        register.setZero(zero);
        register.setNegative(operand < 0);
        register.setCarry(carry);
        register.setOverflow(zero != carry);
        return operand;
    }

    public short asr(short operand) {
        short msb = (short) (0b10000000 & operand);
        short lsb = (short) (0b00000001 & operand);
        boolean carry = lsb > 0;
        boolean negative = msb > 0;
        operand = (short) (operand >> 1);
        if (negative) {
            operand = (short) (0b10000000 | operand);
        }
        boolean zero = operand == 0;
        register.setZero(zero);
        register.setNegative(negative);
        register.setCarry(carry);
        register.setOverflow(zero != carry);
        return operand;
    }

    public short asl(short operand) {
        short msb = (short) (0b10000000 & operand);
        boolean negative = msb > 0;
        boolean carry = negative;
        operand = (short) (operand << 1);
        if (negative) {
            operand = (short) (0b10000000 | operand);
        }
        boolean zero = operand == 0;
        register.setZero(zero);
        register.setNegative(negative);
        register.setCarry(carry);
        register.setOverflow(zero != carry);
        return operand;
    }

    public short adc(short operand) {
        boolean carry = register.isCarry();
        short newValue = operand;
        if (carry) {
            newValue = (short) (operand + 1);
        }
        register.setZero(newValue == 0);
        register.setNegative(newValue < 0);
        if (carry) {
            register.setCarry(isCarryAdd(operand, (short) 1));
            register.setOverflow(isOverflow(operand, (short) 1, newValue));
        }
        else {
            register.setCarry(false);
            register.setOverflow(false);
        }
        return newValue;
    }

    public short sbc(short operand) {
        boolean carry = register.isCarry();
        short newValue = operand;
        if (carry) {
            newValue = (short) (operand - 1);
        }
        register.setZero(newValue == 0);
        register.setNegative(newValue < 0);
        if (carry) {
            register.setCarry(isCarrySub(operand, (short) 1));
            register.setOverflow(isOverflow(operand, (short) 1, newValue));
        }
        else {
            register.setCarry(false);
            register.setOverflow(false);
        }
        return newValue;
    }
}
