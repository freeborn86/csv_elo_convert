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
		final String delimiterConfigFile = "csv.ini";

		try {
			readSettings(csvFolderPath, delimiterConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static char delimiter;
	static String csvInputFile = "input.csv";
	static String folderPath;
	static boolean hasHeader;

	public static void readSettings(String csvFolderPath, String delimiterConfigFile) throws IOException {
		folderPath = csvFolderPath;
		delimiter = ',';
		Files.walk(Paths.get(folderPath)).forEach(filePath -> {
			if (Files.isRegularFile(filePath) && filePath.endsWith(delimiterConfigFile)) {
				BufferedReader br = null;
				try {
					br = new BufferedReader(
							new InputStreamReader(new FileInputStream(folderPath + "\\" + delimiterConfigFile)));
					String line = null;
					while ((line = br.readLine()) != null) {
						if (line.startsWith("delimiter=")){
							delimiter = line.charAt(line.length()-1);
						}
						if(line.startsWith("header=")){
							if (line.substring(line.length()-4, line.length()).equals("true")){
								hasHeader = true;
							}
							else{
								hasHeader = false;
							}
						}
						
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
