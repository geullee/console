package kr.geul.console.command;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.text.BadLocationException;

import kr.geul.console.Console;
import kr.geul.console.exception.InvalidNumberOfArgumentsException;

public abstract class Command implements Runnable {

	private boolean isNew;
	private double progress;
	private long startTime, endTime;

	protected ArrayList<String> arguments;

	protected Command(ArrayList<String> arguments) {	
		this.arguments = arguments;		
	}

	protected abstract String getCommandName();
	protected abstract int[] getValidNumberOfArguments();
	protected abstract void runCommand() throws Exception;

	public boolean checkArguments() throws BadLocationException {

		for (int i = 0; i < getValidNumberOfArguments().length; i++) {
			if (arguments.size() == getValidNumberOfArguments()[i])
				return true;
		}

		return false;

	}

	public void enterCommand(String command) {
		Console.enterCommand(command);
	}

	public void run() {

		try {	

			if (getValidNumberOfArguments() == null || checkArguments() == true) 	
				runCommand();
			else
				throw new InvalidNumberOfArgumentsException
				(getCommandName(), getValidNumberOfArguments());

		} 

		catch (Exception e) {

			StackTraceElement[] stackTraceElements = e.getStackTrace();

			Console.printErrorMessage("\n<EXCEPTION>", getCommandName());
			Console.printErrorMessage(e.getClass().getCanonicalName(), getCommandName());
			Console.printErrorMessage(e.getMessage(), getCommandName());
			for (int i = 0; i < stackTraceElements.length; i++) {
				Console.printErrorMessage(e.getStackTrace()[i].toString(), getCommandName());
			}

		} 

	}

	protected void setProgress(double value) {
		progress = value;
		isNew = true;
	}

	protected void tic() {	
		startTime = System.currentTimeMillis();		
	}

	protected void toc() throws BadLocationException {

		endTime = System.currentTimeMillis();
		System.out.println(" DONE, Elapsed time: "
				+ (double) (((double) endTime - startTime) / 1000.00)
				+ " seconds");

	}

	protected void updateProgress(int current, int total) {
		double value = (double) (Math.round((double) current * 1000.0 / (double) total)) / 10.0;

		if (value > progress) {

			progress = value;
			int valueLength = String.valueOf(value).length();

			if (isNew == false) {

				for (int i = 0; i < valueLength + 2; i++) {
					System.out.print("\b");
				}

			}

			else 
				System.out.print("  ");	

			System.out.print(" " + value + "%");
			isNew = false;

		}

	}

	protected void writeLog(String content) throws FileNotFoundException {
		
		Calendar currentTime = Calendar.getInstance();
		currentTime.getTime();
		SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		
		String log = "[" + timeFormat.format(currentTime.getTime()) + "] (" +
				getCommandName() + ") " + content;
		
		Console.writeLog(log);
				
	}
	
}
