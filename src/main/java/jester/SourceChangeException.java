package jester;

@SuppressWarnings("serial")
public class SourceChangeException extends Exception {

	public SourceChangeException(String message) {
		super(message);
	}

	public SourceChangeException(String message, Throwable cause) {
		super(message, cause);
	}
}