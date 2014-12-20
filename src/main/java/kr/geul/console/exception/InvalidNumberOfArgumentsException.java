package kr.geul.console.exception;

public class InvalidNumberOfArgumentsException extends Exception {

	private static final long serialVersionUID = 2815661130364764335L;
	int[] validNumberOfArguments;
	String commandName;
	
	public InvalidNumberOfArgumentsException(String name, int[] numberOfArguments) {
		commandName = name;
		validNumberOfArguments = numberOfArguments;
	}

	public String getMessage() {
		String message = "Command '" + commandName + "' only can have ";
		
		for (int i = 0; i < validNumberOfArguments.length; i++) {
			message += validNumberOfArguments[i];
			if (i < validNumberOfArguments.length - 1)
				message += " or ";
		}
		
		message += " argument(s).";
		return message;
		
	}
	
}
