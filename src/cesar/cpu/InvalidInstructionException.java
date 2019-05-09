package cesar.cpu;

public class InvalidInstructionException extends CPUException {
    private static final long serialVersionUID = -3157595622103160748L;

    private InvalidInstructionException(String message) {
        super(message);
    }

    public static InvalidInstructionException withInstruction(int instruction) {
        String bin = Integer.toBinaryString(instruction);
        String hex = Integer.toHexString(instruction);
        String format = "Instrução inválida 0b%s (0x%s)";
        String message = String.format(format, bin, hex);
        return new InvalidInstructionException(message);
    }
}
