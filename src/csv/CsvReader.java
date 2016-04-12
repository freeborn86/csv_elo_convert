package csv;

import java.nio.file.Files;
import java.nio.file.Paths;

public class CsvReader {
	private String delimiter;
	private String csvInputFilePath;
	private 

	CsvReader() {
		this.delimiter = CsvImportDefaultSettings.delimiter;
		this.csvInputFilePath = CsvImportDefaultSettings.csvInputFilePath;
	}

	// test
	public static void readCsvToCollection() {

	}
}

/*
 * Files.walk(Paths.get("c:/users/born/Desktop")).forEach(filePath ->{ if (
 * Files.isRegularFile(filePath) || Files.isDirectory(filePath) ) {
 * System.out.println(filePath); } });
 */
