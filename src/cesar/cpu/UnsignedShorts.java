package cesar.cpu;

public class UnsignedShorts {

    private UnsignedShorts() {}

    public static int toInt(short s) {
        return 0xFFFF & s;
    }

    public static short bytesToShort(byte a, byte b) {
        return (short) (((0xFF & a) << 8) | (0xFF & b));
    }
}
