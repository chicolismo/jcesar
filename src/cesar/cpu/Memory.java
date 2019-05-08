package cesar.cpu;

import java.util.Arrays;

public class Memory {
    private static final int MEMORY_SIZE = 1 << 16;

    private final CPU cpu;
    private byte[] data;
    private static final int DISPLAY_START_ADDRESS = 65500;
    private static final int DISPLAY_END_ADDRESS = 65535;
    private long bytesRead;
    private long bytesWritten;

    public Memory(CPU cpu) {
        this.cpu          = cpu;
        this.data         = new byte[MEMORY_SIZE];
        this.bytesRead    = 0;
        this.bytesWritten = 0;
    }

    public int size() {
        return MEMORY_SIZE;
    }

    private static boolean isDisplayAddress(short address) {
        return address >= DISPLAY_START_ADDRESS && address <= DISPLAY_END_ADDRESS;
    }

    public void setBytes(byte[] newData) {
        int size = Math.min(newData.length, MEMORY_SIZE);
        int offset = newData.length > MEMORY_SIZE ? newData.length - MEMORY_SIZE : 0;
        for (int i = 0; i < size; ++i) {
            data[i] = newData[i + offset];
        }
    }

    public byte readByte(short address) {
        ++bytesRead;
        return data[Short.toUnsignedInt(address)];
    }

    public short readWord(short address) {
        byte msb;
        byte lsb;

        if (isDisplayAddress(address)) {
            msb = (byte) 0;
            lsb = readByte(address);
        }
        else {
            msb = readByte(address);
            lsb = readByte((short) (address + 1));
        }
        return Memory.bytesToShort(msb, lsb);
    }

    public void writeByte(short address, byte value) {
        ++bytesWritten;
        int index = Short.toUnsignedInt(address);
        data[index] = value;
        cpu.notifyMemoryChange(index, value);
    }

    public void writeWord(short address, short value) {
        byte msb = (byte) ((0xFF00 & value) >> 8);
        byte lsb = (byte) (0x00FF & value);

        if (isDisplayAddress(address)) {
            // Se estivermos escrevendo uma palavra num endereço que pertence ao display,
            // apenas o byte menos significativo será escrito no endereço fornecido.
            writeByte(address, lsb);
        }
        else {
            writeByte(address, msb);
            writeByte((short) (address + 1), lsb);
        }
    }

    public byte[] getDisplayBytes() {
        return Arrays.copyOfRange(data, DISPLAY_START_ADDRESS, DISPLAY_END_ADDRESS + 1);
    }

    public long getNumberOfBytesRead() {
        return bytesRead;
    }

    public long getNumberOfBytesWritten() {
        return bytesWritten;
    }

    public long getNumberOfMemoryAccesses() {
        return getNumberOfBytesRead() + getNumberOfBytesWritten();
    }

    public static short bytesToShort(byte a, byte b) {
        return (short) (((0xFF & a) << 8) | (0xFF & b));
    }
}
