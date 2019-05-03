package cesar.cpu;

public class Memory {
    private static final int MEMORY_SIZE = 1 << 16;
    private byte[] data;

    public Memory() {
        data = new byte[MEMORY_SIZE];
    }

    public int size() {
        return MEMORY_SIZE;
    }

    public byte[] getData() {
        return data;
    }

    public void setBytes(byte[] newData) {
        int size = Math.min(newData.length, MEMORY_SIZE);
        int offset = newData.length > MEMORY_SIZE ? newData.length - MEMORY_SIZE: 0;
        for (int i = 0; i < size; ++i) {
            data[i] = newData[i + offset];
        }
    }

    public byte read(short address) {
        return data[UnsignedShorts.toInt(address)];
    }

    public byte read(int address) {
        return data[address];
    }

    public void write(short address, byte value) {
        data[UnsignedShorts.toInt(address)] = value;
    }
}
