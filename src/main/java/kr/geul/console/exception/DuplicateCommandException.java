package kr.geul.console.exception;

public class DuplicateCommandException extends Exception {

	private static final long serialVersionUID = -4724489544495633824L;
	String command, className, className2;
	
	public DuplicateCommandException(String command, String className,
			String className2) {
		this.command = command;
		this.className = className;
		this.className2 = className2;
	}

	@Override
	public String getMessage() {
		String message = 
				"Command '" + command + "' has been found in more than two command libraries: " +
				className + ", " + className2;
		return message;
	}
	
}
