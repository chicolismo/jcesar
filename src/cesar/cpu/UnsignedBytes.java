package cesar.cpu;

public class UnsignedBytes {
    private static final int BYTE_MASK = 0xFF;

    private UnsignedBytes() {}

    public static int toInt(byte b) {
        return BYTE_MASK & b;
    }
}
