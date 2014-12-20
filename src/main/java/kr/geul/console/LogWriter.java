package kr.geul.console;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class LogWriter {

	static PrintWriter writer;
	static File logFile;
	
	public static void write(String logContent) throws FileNotFoundException {
		writer = new PrintWriter(new FileOutputStream(logFile, true));
		writer.println(logContent);
		writer.close();
	}
	
	public static void setWriter(File file) {
		logFile = file;	
	}
	
}
