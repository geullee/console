package kr.geul.console;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class PublicWriter {

	static PrintWriter writer;
	static File file;
	
	public static void closeWriter() {
		writer.close();
	}
	
	public static void write(String logContent) throws FileNotFoundException {
		writer = new PrintWriter(new FileOutputStream(file, true));
		writer.println(logContent);
		writer.close();
	}
	
	public static void setWriter(File f) {
		file = f;	
	}
	
}
