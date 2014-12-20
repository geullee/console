package kr.geul.console;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class AliasLibrary {

	private Map<String, String> aliases;
	
	public AliasLibrary() {
		aliases = new TreeMap<String, String>(); 
	}
	
	public void addAlias(String alias, String content) {
		aliases.put(alias, content);
	}
	
	public String readAlias(String alias) {
		String result = aliases.get(alias);
		return result;
	}
	
	public ArrayList<String> readAlias_list(String alias) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		String result = aliases.get(alias);
		String[] resultArray = result.split(" ", -1);
		
		for (int i = 0; i < resultArray.length; i++) {
			list.add(resultArray[i]);
		}
		
		return list;
		
	}
	
	public void removeAlias(String alias) {
		aliases.remove(alias);
	}
	
	public void showAliasList() {
		
		Console.printMessage("<List of aliases>");
		
		if (aliases.size() == 0)
			Console.printMessage("No aliases");
		
		else {
		
			String[] dummyArray = {""};
			String[] aliasArray = aliases.keySet().toArray(dummyArray);
			
			for (int i = 0; i < aliasArray.length; i++) {
				Console.printMessage(aliasArray[i] + ": " + aliases.get(aliasArray[i]));
			}
			
		}
		
	}
	
}
