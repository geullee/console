package kr.geul.console;

import kr.geul.console.exception.DuplicateCommandException;

public abstract class ExpansionPack {
	
	abstract protected CommandLibrary getCommandLibrary();
	
	public void include() throws DuplicateCommandException {
		Console.addCommandLibrary(getCommandLibrary());
	}
	
}
