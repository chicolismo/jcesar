package cesar.cpu;

public class HaltedException extends Exception {
    private static final long serialVersionUID = 2040895932455566628L;

    public HaltedException() {
        super("Instrução HTL atingida");
    }
}
