package cesar.cpu;

public class InvalidInstructionException extends Exception {
	private static final long serialVersionUID = -3157595622103160748L;
	
	public InvalidInstructionException(String message) {
		super(message);
	}
}
