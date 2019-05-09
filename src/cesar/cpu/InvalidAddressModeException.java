package cesar.cpu;

public class InvalidAddressModeException extends CPUException {
    private static final long serialVersionUID = -8576598319553930184L;

    public static InvalidAddressModeException withAddressMode(int addressMode) {
        String bin = Integer.toBinaryString(addressMode);
        String hex = Integer.toHexString(addressMode);
        String format = "Modo de endereçamento inválido %d (0b%s) (0x%s)";
        String message = String.format(format, addressMode, bin, hex);
        return new InvalidAddressModeException(message);
    }

    private InvalidAddressModeException(String message) {
        super(message);
    }
}
