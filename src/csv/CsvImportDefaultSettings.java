package csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CsvImportDefaultSettings {

	static {
		final String csvFolderPath = "csv";
		final String delimiterConfigFile = "delimiter.txt";

		try {
			readSettings(csvFolderPath, delimiterConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String delimiter;
	public static String csvInputFile = "input.csv";
	public static String folderPath;

	public static void readSettings(String csvFolderPath, String delimiterConfigFile) throws IOException {
		folderPath = csvFolderPath;
		delimiter = ",";
		Files.walk(Paths.get(folderPath)).forEach(filePath -> {
			if (Files.isRegularFile(filePath) && filePath.endsWith(delimiterConfigFile)) {
				BufferedReader br = null;
				try {
					br = new BufferedReader(
							new InputStreamReader(new FileInputStream(folderPath + "\\" + delimiterConfigFile)));
					String line = null;
					while ((line = br.readLine()) != null) {
						delimiter = line;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				} finally {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
				}
				return;
			}
		});

	}
}
