package io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
	/* Questa è la classe che si occupa di scrivere nel file di output.
	 * Per ragioni di efficienza, si inserisce tutto l'output in una stringa di buffer e poi si scrive tutto insieme. */
	
	private BufferedWriter bufferedWriter;
	private String buffer;
	
	public Writer() throws IOException {
		  FileWriter fstream = new FileWriter("output.txt");
		  this.bufferedWriter = new BufferedWriter(fstream);
		  buffer = "";
	}
	
	public void addBuffer(String in) {
		buffer += in;
	}
	
	public void writeBuffer() throws IOException {
		bufferedWriter.write(buffer);
	}
	
	public void closeWriter() throws IOException {
		bufferedWriter.close();
	}
	
}
