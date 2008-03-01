package jester;

public class ConfigurationException extends SourceChangeException {

	// TODO not logically a SourceChangeException - need to sort out exceptions
	// properly

	public ConfigurationException(String message) {
		super(message);
	}
}
