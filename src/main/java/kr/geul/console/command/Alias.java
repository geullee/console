package kr.geul.console.command;

import java.util.ArrayList;

import kr.geul.console.Console;

public class Alias extends Command {

	public Alias(ArrayList<String> arguments) {
		super(arguments);
	}

	protected String getCommandName() {
		return "Alias";
	}
	
	protected int[] getValidNumberOfArguments() {
		return null;
	}
	
	protected void runCommand() {
		
		switch (arguments.size()) {
		
		case 0:
			Console.showAliasList_static();
			break;
		case 1:
			Console.removeAlias_static(arguments.get(0));
			break;
		default:
			String contents = "";
			
			for (int i = 1; i < arguments.size(); i++) {
				contents += arguments.get(i);
				if (i < arguments.size() - 1)
					contents += " ";
			}
			
			Console.addAlias_static(arguments.get(0), contents);
		
		}
		
	}

}
