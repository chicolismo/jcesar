package cesar.cpu;

public class UnsignedShorts {

    private UnsignedShorts() {}

    public static int toInt(short s) {
        return 0xFFFF & s;
    }
}
