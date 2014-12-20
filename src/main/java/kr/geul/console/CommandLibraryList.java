package kr.geul.console;

import java.util.ArrayList;
import java.util.List;

import kr.geul.console.exception.DuplicateCommandException;

public class CommandLibraryList {

	List<CommandLibrary> libraryList;
	
	public CommandLibraryList() {
		libraryList = new ArrayList<CommandLibrary>();
	}
	
	public boolean addLibrary(CommandLibrary library) throws DuplicateCommandException {
		
		for (int i = 0; i < libraryList.size(); i++) {
			
			CommandLibrary existingLibrary = libraryList.get(i);
			
			for (int j = 0; j < library.size(); j++) {
				String command = library.getCommand(j);
				if (existingLibrary.hasCommand(command))
					throw new DuplicateCommandException
						(command, existingLibrary.getName(),
						library.getName());
			}
			
		}
		
		return libraryList.add(library);
		
	}
	
	public CommandLibrary getLibrary(int i) {
		return libraryList.get(i);
	}
	
	public Class<? extends Runnable> getRunnable(String command)
	throws ClassNotFoundException {
		
		Class<? extends Runnable> runnable = null;
		
		for (int i = 0; i < listSize(); i++) {
			
			CommandLibrary library = getLibrary(i);
			runnable = library.getRunnable(command);
			
			if (runnable != null)
				return runnable;
			
		}
		
		return runnable;
		
	}
	
	public void removeLibrary(int i) {
		libraryList.remove(i);
	}
	
	public int listSize() {
		return libraryList.size();
	}
	
}
