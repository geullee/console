package kr.geul.console;

import java.util.ArrayList;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

public class RMI {

	static Rengine engine;
	
	public static void addVariable(ArrayList<Double> values, String name) {
		
		String command = name + " <- c(";
		
		for (int i = 0; i < values.size(); i++) {
			
			command += values.get(i);
			if (i < values.size() - 1)
				command += ", ";
			
		}
		
		command += ")";

		engine.eval(command);
		
	}
	
	public static void close() {
		engine.end();
	}
	
	public static void initialize() {
		
		String[] Rargs = {"--vanilla"};
		engine = new Rengine(Rargs, false, null);
		
		if (!engine.waitForR()) {
			System.out.println("Cannot load R");
			return;
		}
		
	}

	public static double getDouble(String command) {
		REXP exp = engine.eval(command);
		return exp.asDouble();
	}
	
	public static void run(String command) {
		engine.eval(command);
	}

}
