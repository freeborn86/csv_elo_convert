package elo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class EloConversionDefaultSettings {

	static final String configFile = "cnv.ini";

	String eloSourceExportPath;
	String eloGeneratedExportPath;
	int metadataLimit;

	public EloConversionDefaultSettings() throws IOException{
		readSettings();
	}

	public void readSettings() throws IOException {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("source_folder=")) {
					this.eloSourceExportPath = line.substring(line.indexOf("=") + 1, line.length());
				}
				if (line.startsWith("generate_folder=")) {
					this.eloGeneratedExportPath = line.substring(line.indexOf("=") + 1, line.length());
				}
				if (line.startsWith("metadata_limit=")) {
					this.metadataLimit = Integer.parseInt(line.substring(line.indexOf("=") + 1, line.length()));
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
