package Tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {
	public static void write() {

		// The name of the file to open.
		String fileName = "Remember.txt";

		try {
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(fileName);

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Note that write() does not automatically
			// append a newline character.
			bufferedWriter.write("Hello there,");
			bufferedWriter.write(" here is some text.");
			bufferedWriter.newLine();
			bufferedWriter.write("We are writing");
			bufferedWriter.write(" the text to the file.");

			// Always close files.
			bufferedWriter.close();
		} catch(IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}
}
