package cesar.util;

public class Shorts {
    private Shorts() {
    }

    public static int toUnsignedInt(short value) {
        return 0xFFFF & (value);
    }
}
