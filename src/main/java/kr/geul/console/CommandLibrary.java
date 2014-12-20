package kr.geul.console;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandLibrary {

	protected Map<String, String> commands;
	protected String libraryName;
	
	public CommandLibrary(String libraryName) {
		commands = new LinkedHashMap<String, String>();
		this.libraryName = libraryName; 
	}
	
	public void addCommand(String command, String className) {
		commands.put(command, className);
	}
	
	public String getCommand(int index) {
		String[] stringArray = {""};
		String[] commandArray = commands.keySet().toArray(stringArray);
		return commandArray[index];
	}
	
	public String getName() {
		return libraryName;
	}
	
	public Class<? extends Runnable> getRunnable(String name)
	throws ClassNotFoundException {
		
		Class<? extends Runnable> runnable = null;
		String className = commands.get(name);
		
		if (className != null)
			runnable = (Class<? extends Runnable>) Class.forName(className).
					asSubclass(Runnable.class);
			
		return runnable;
		
	};
	
	public boolean hasCommand(String command) {
		
		if (commands.containsKey(command))
			return true;
		else 
			return false;
		
	}
	
	public int size() {
		return commands.size();
	}
	
}
