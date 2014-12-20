package kr.geul.console.command;

import java.util.ArrayList;

import kr.geul.console.PublicWriter;

public class CloseWriter extends Command {

	public CloseWriter(ArrayList<String> arguments) {
		super(arguments);	
	}

	@Override
	protected String getCommandName() {
		return "closewriter";
	}

	@Override
	protected int[] getValidNumberOfArguments() {
		int[] numbers = {0};
		return numbers;
	}

	@Override
	protected void runCommand() throws Exception {
		PublicWriter.closeWriter();	
	}
	
}
