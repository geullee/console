package kr.geul.console.command;

import java.util.ArrayList;

import kr.geul.console.Console;

public class Quit extends Command {
	
	public Quit(ArrayList<String> arguments) {
		super(arguments);
	}

	protected String getCommandName() {
		return "Quit";
	}
	
	protected int[] getValidNumberOfArguments() {
		int[] numbers = {0};
		return numbers;
	}
	
	protected void runCommand() {
		Console.quit();	
	}
		
}
