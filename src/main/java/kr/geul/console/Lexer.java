package kr.geul.console;

import java.util.ArrayList;

public class Lexer {

	static String[] getTokens(String command) {

		ArrayList<String> tokensList = new ArrayList<String>();
		
		int startLocation = 0, endLocation = 0;
		
		do {
			
			endLocation = getEndLocation(command, startLocation);
			tokensList.add(removeTab(command.substring(startLocation, endLocation + 1)));
			
			startLocation = getStartLocation(command, endLocation);
			
		} while (endLocation < command.length() - 1);
			
		String[] tokens = new String[tokensList.size()];
		
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokensList.get(i);
		}
		
		return tokens;	
		
	}

	private static int getStartLocation(String command, int endLocation) {
		
		int length = command.length();
		
		if (endLocation == length - 1)
			return endLocation;
		
		else {
		
			boolean isStartLocationReached = false;
			int startLocation = endLocation + 1;
			
			
			do {
				
				char character = command.charAt(startLocation);
				
				if (startLocation == length - 1 || (character != ' ' && character != '\t'))
					isStartLocationReached = true;
				else
					startLocation++;
				
			} while (isStartLocationReached == false);
			
			return startLocation;
			
		}
				
	}

	private static int getEndLocation(String command, int startLocation) {
		
		boolean isEndLocationReached = false;
		int endLocation = startLocation;
		int length = command.length();
		
		do {
			
			char character = command.charAt(endLocation);
			
			if (endLocation == length - 1 || character == ';' || command.charAt(endLocation + 1) == ' ' || command.charAt(endLocation + 1) == '\t' || 
					command.charAt(endLocation + 1) == ';')
				isEndLocationReached = true;
			
			else
				endLocation++;
			
		} while (isEndLocationReached == false);
		
		return endLocation;
		
	}

	private static String removeTab(String rawString) {
		
		String result = "";
		
		for (int i = 0; i < rawString.length(); i++) {
			
			if (rawString.charAt(i) != '\t')
				result += rawString.charAt(i);
			
		}
		
		return result;
		
	}
	
}

