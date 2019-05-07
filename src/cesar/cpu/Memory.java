package cesar.cpu;

import java.util.Arrays;

public class Memory {
    private static final int MEMORY_SIZE = 1 << 16;
    private byte[] data;
    private static final int DISPLAY_START_ADDRESS = 65500;
    private static final int DISPLAY_END_ADDRESS = 65535;
    private long bytesRead;
    private long bytesWritten;

    public Memory() {
        data = new byte[MEMORY_SIZE];
        bytesRead = 0;
        bytesWritten = 0;
    }

    public int size() {
        return MEMORY_SIZE;
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
        return data[UnsignedShorts.toInt(address)];
    }

    public byte readByte(int address) {
        ++bytesRead;
        return data[address];
    }

    public short readWord(short address) {
        byte msb, lsb;
        if (address >= DISPLAY_START_ADDRESS && address <= DISPLAY_END_ADDRESS) {
            msb = (byte) 0;
            lsb = readByte(address);
        } else {
            msb = readByte(address);
            lsb = readByte((short) (address + 1));
        }
        return UnsignedShorts.bytesToShort(msb, lsb);
    }

    public void writeByte(short address, byte value) {
        ++bytesWritten;
        data[UnsignedShorts.toInt(address)] = value;
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
}
