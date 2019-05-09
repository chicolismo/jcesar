package cesar.cpu;

public class InvalidOpCodeException extends CPUException {
    private static final long serialVersionUID = -2613719579326923927L;

    private InvalidOpCodeException(String message) {
        super(message);
    }

    public static InvalidOpCodeException withOpCode(int opCode) {
        String bin = Integer.toBinaryString(opCode);
        String hex = Integer.toHexString(opCode);
        String format = "OpCode inválido 0b%s (0x%s)";
        String message = String.format(format, bin, hex);
        return new InvalidOpCodeException(message);
    }
}
