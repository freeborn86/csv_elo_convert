package csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class CsvImportDefaultSettings {

	static final String configFile = "csv.ini";

	char delimiter;
	String csvInputFile;
	String folderPath;
	boolean hasHeader;
	
	public CsvImportDefaultSettings() throws IOException {
		readSettings();
	}
	
	public void readSettings() throws IOException {
		delimiter = ',';
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("folder=")) {
					this.folderPath = line.substring(line.indexOf("=") + 1, line.length());
				}
				if (line.startsWith("file=")) {
					this.csvInputFile = line.substring(line.indexOf("=") + 1, line.length());
				}
				if (line.startsWith("delimiter=")) {
					this.delimiter = line.charAt(line.length() - 1);
				}
				if (line.startsWith("header=")) {
					if (line.substring(line.length() - 4, line.length()).equals("true")) {
						this.hasHeader = true;
					} else {
						this.hasHeader = false;
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
}
