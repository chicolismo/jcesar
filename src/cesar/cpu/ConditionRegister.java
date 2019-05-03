package cesar.cpu;

public class ConditionRegister {
    private int register;

    public ConditionRegister(int initialValue) {
        this.register = initialValue;
    }

    public void setConditions(byte b) {
        register |= (0x0F & b);
    }

    public void clearConditions(byte b) {
        register &= (0x0F & ~b);
    }

    public boolean isNegative() {
        return (register & 0b1000) > 0;
    }

    public boolean isZero() {
        return (register & 0b0100) > 0;
    }

    public boolean isOverflow() {
        return (register & 0b0010) > 0;
    }

    public boolean isCarry() {
        return (register & 0b0001) > 0;
    }

    public boolean shouldJump(int conditionCode) {
        // NOTE: Nem todos os valores são códigos válidos. Será que tem que
        // lançar exceção?
        switch (conditionCode) {
            case 0b0000:
                // 0) BR (always)
                return true;
            case 0b0001:
                // 1) BNE (Not Equal) z = 0
                return !isZero();
            case 0b0010:
                // 2) BEQ (EQual) z = 1
                return isZero();
            case 0b0011:
                // 3) BPL (Plus) n = 0
                return !isNegative();
            case 0b0100:
                // 4) BMI (Minus) n = 1
                return isNegative();
            case 0b0101:
                // 5) BVC (oVerflow Clear) v = 0
                return !isOverflow();
            case 0b0110:
                // 6) BVS (oVerflow Set) v = 1
                return isOverflow();
            case 0b0111:
                // 7) BCC (Carry Clear) c = 0
                return !isCarry();
            case 0b1000:
                // 8) BCS (Carry Set) c = 1
                return isCarry();
            case 0b1001:
                // 9) BGE (Greater or Equal) n = v
                return isNegative() == isOverflow();
            case 0b1010:
                // 10) BLT (Less Than) n <> v
                return isNegative() != isOverflow();
            case 0b1011:
                // 11) BGT (GreaTer) n = v and z = 0
                return isNegative() == isOverflow() && !isZero();
            case 0b1100:
                // 12) BLE (Less or Equal) n <> v or z = 1
                return isNegative() != isOverflow() || isZero();
            case 0b1101:
                // 13) BHI (Higher) c = 0 and z = 0
                return !isCarry() && !isZero();
            case 0b1110:
                // 14) BLS (Lower or Same) c = 1 or z = 1
                return isCarry() || isZero();
            default:
                // Lançar exceção??
                return false;
        }
    }
}
