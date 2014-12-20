package kr.geul.console;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kr.geul.console.exception.DuplicateCommandException;

public class Console {

	//
	//	Non-static variables declaration 
	//
	
	private boolean logMode = false;
	
	/**
	 *	When starting its operation after the method {@code start()} being called,
	 *	a Console object first displays the name and version of program being run,
	 *	as well as the default path with which file I/O operations will be conducted. 
	 *	These string objects can be modified via public non-static methods 
	 *	{@code setDefaultPath()}, {@code setProgramName()}, and 
	 *	{@code setProgramVersion()}, respectively.
	 */
	private String defaultPath = "",
			programName = "GLconsole", 
			programVersion = "0.1";
	/**
	 *	An AliasLibrary object manages aliases which stand for several String objects.
	 *	By using aliases, the number of required arguments for some commands can be fixed.
	 *	AliasLibrary can add, remove, and return aliases, and show a list of them.  
	 */
	private AliasLibrary aliasLibrary;
	/**
	 *	A list of arguments is collected from a command line by removing the first word,
	 *	which is deemed to be the command itself, from the line and taking the rest.
	 */
	private ArrayList<String> arguments;
	/**
	 *  A list of tokens is maintained until the method {@code flushTokenBuffer()} is 
	 *  triggered by a semicolon and the tokens are passed to executor. This procedure is
	 *  required in order to enable multi-line command inputs via a script file.
	 */
	private ArrayList<String> tokenBuffer;
	/**
	 *	A list of CommandLibrary is used to incorporate multiple CommandLibrary objects
	 *	so that multiple command libraries can be adopted by a single program. 
	 */
	private CommandLibraryList commandLibraryList;
	/**
	 *  {@code Command} class for GLConsole implements {@code Runnable}, 
	 *  and every command class for GLConsole must extend {@code Command}.
	 *  When command is entered via the command line or a script file,
	 *  commandLibrary finds the corresponding command class, which is runnable,
	 *  and then returns an instance of that class.
	 */
	private Runnable runnable;
	/**	 Retrieves command line inputs.	 */
	private Scanner commandLineScanner;
	/**
	 *	Every command is first transformed into a {@code Runnable} instance,
	 *	and then passed to {@code serialExecutor} which let these runnables
	 *	run in sequence. This sequence is important when a command relies on
	 *	the output of previous commands. */
	private SerialExecutor serialExecutor;
	/**	 Executes runnables inside {@code serialExecutor}.	*/
	private ThreadPoolExecutor executor;

	//
	//	Static variables declaration 
	//
	
	/**
	 *  A Console object generated via the method {@code getConsole()} is 
	 *  automatically passed to a static Console variable staticConsole,
	 *  so that static methods can pass the process to this static Console.  
	 */
	private static Console staticConsole;
	
	//
	//	Constructor
	//
	
	private Console() {

		aliasLibrary = new AliasLibrary();
		arguments = new ArrayList<String>();
		tokenBuffer = new ArrayList<String>();
		commandLibraryList = new CommandLibraryList();
		commandLineScanner = new Scanner(System.in);
		executor = 
				new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, 
						new LinkedBlockingQueue<Runnable>());
		serialExecutor = new SerialExecutor(executor);
		
		try {
			commandLibraryList.addLibrary(getDefaultLibrary());
		} 
		
		catch (DuplicateCommandException e) {
			System.err.println(e.getMessage());
		}
		
	}

	//                   	 
	//	Non-static methods	
	//                     	 	 
	 
	private void addAlias(String alias, String content) {
		aliasLibrary.addAlias(alias, content);
	}
	
	private void addTokens(String command)  {

		String[] tokens = Lexer.getTokens(command);

		for (int i = 0; i < tokens.length - 1; i++) {			
			tokenBuffer.add(tokens[i]);
		}

		if (tokens[tokens.length - 1].equals(";")) {
			flushTokenBuffer();
		}

	}

	private void display(String content) {
		System.out.println(content);		
	}
	
	private void displayCommand(String command) {
		serialExecutor.execute(new Thread(new CommandDisplayer(command)));
	}
	
	private void displayError(String content, String source) {
		System.err.println(content + " [" + source + "]");		
	}
	
	private void end() {
		System.exit(0);		
	}
	
	private void flushTokenBuffer() {
		
		setAction(tokenBuffer);
		
		if (runnable != null)
			serialExecutor.execute(new Thread(runnable));
		
		tokenBuffer.clear();

	}

	private void getCommand() {

		ready();

		String command = null;

		command = commandLineScanner.nextLine();

		if (command.length() > 0)
			addTokens(command + ";");
		
		getCommand();

	}
	
	private CommandLibrary getDefaultLibrary() {
		
		CommandLibrary defaultLibrary = new CommandLibrary("Default");
		defaultLibrary.addCommand("alias", "kr.geul.console.command.Alias");
		defaultLibrary.addCommand("closewriter", "kr.geul.console.command.CloseWriter");
		defaultLibrary.addCommand("quit", "kr.geul.console.command.Quit");
		
		return defaultLibrary;
		
	}
	
	@SuppressWarnings("unused")
	private String getLineString(ArrayList<String> arrayList) {

		String lineString = "";

		for (int i = 0; i < arrayList.size() - 1; i++) {
			lineString += arrayList.get(i) + " ";			
		}

		lineString += arrayList.get(arrayList.size() - 1);
		return lineString;

	}

	private void ready() {
		serialExecutor.execute(new Thread(new PointerIndicator()));
	}

	private void removeAlias(String alias) {
		aliasLibrary.removeAlias(alias);		
	}

	private void setAction(ArrayList<String> tokens) {

		String firstToken = tokens.get(0);
		arguments = new ArrayList<String>();

		if (tokens.size() > 1) {

			for (int i = 1; i < tokens.size(); i++) {		
				arguments.add(tokens.get(i));			
			}

		}

		try {

			Class<? extends Runnable> runnableClass = commandLibraryList.getRunnable(firstToken);

			if (runnableClass != null) 
				runnable = runnableClass.getDeclaredConstructor(ArrayList.class).newInstance(arguments);		
			else {
				System.err.println("Invalid command: '" + firstToken + "'");
				runnable = null;
			}
		}
		
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
				IllegalArgumentException | InvocationTargetException | NoSuchMethodException | 
				SecurityException e) {
			System.err.println("Command library error: Unable to launch a thread for the command '" + firstToken + "'");
			e.printStackTrace();
			runnable = null;			
		}

	}

	public void setLogMode(boolean mode) {
		logMode = mode;		
	}
	
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	public void setProgramVersion(String programVersion) {
		this.programVersion = programVersion;
	}

	private void showAliasList() {
		aliasLibrary.showAliasList();
	}
	
	public void start() throws IOException {

		Calendar startTime = Calendar.getInstance();
		startTime.getTime();
		SimpleDateFormat startTimeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

		if (defaultPath.isEmpty()) 
			defaultPath = new java.io.File( "." ).getCanonicalPath();

		System.out.println("[" + programName + " " + programVersion + "]");
		System.out.println("Session start time: " + startTimeFormat.format(startTime.getTime()));
		System.out.println("Default path: " + defaultPath);		

		getCommand();

	}
	
	//
	//	Static methods
	//
	
	public static void addAlias_static(String alias, String content) {
		staticConsole.addAlias(alias, content);
	}

	public static void addCommandLibrary(CommandLibrary library) throws DuplicateCommandException {
		staticConsole.commandLibraryList.addLibrary(library);
	}
	
	public static void enterCommand(String command) {
		staticConsole.displayCommand(command);
		staticConsole.addTokens(command);		
	}
	
	public static String getAliasContents(String alias) {
		return staticConsole.aliasLibrary.readAlias(alias);
	}
	
	public static ArrayList<String> getAliasContentsList(String alias) {
		return staticConsole.aliasLibrary.readAlias_list(alias);
	}
	
	public static Console getConsole() {
		
		Console console = new Console();
		staticConsole = console;
		return console;
		
	}
	
	public static String getDefaultPath() {
		return staticConsole.defaultPath;
	}
	
	public static void printErrorMessage(String content, String source) {
		staticConsole.displayError(content, source);
	}
	
	public static void printMessage(String content) {
		staticConsole.display(content);
	}
	
	public static void quit() {
		staticConsole.end();
	}
	
	public static void removeAlias_static(String alias) {
		staticConsole.removeAlias(alias);
	}

	public static void showAliasList_static() {
		staticConsole.showAliasList();
	}
	
	//
	//	Inner classes
	//

	class CommandDisplayer implements Runnable {
	
		String command;
		
		public CommandDisplayer(String command) {
			this.command = command;
		}
		
		@Override
		public void run() {
			System.out.println("> " + command);			
		}
		
	}
	
	class PointerIndicator implements Runnable {
	
		public PointerIndicator() {}
		
		@Override
		public void run() {
			System.out.print("> ");			
		}
		
	}

	public static void writeLog(String string) throws FileNotFoundException {
		if (staticConsole.logMode == true)
			LogWriter.write(string);
	}
	
}
