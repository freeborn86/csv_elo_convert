package csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvReader {
	private String delimiter;
	private String csvInputFilePath;

	private ArrayList<ArrayList<String>> clientData;

	public CsvReader() {
		this.delimiter = CsvImportDefaultSettings.delimiter;
		this.csvInputFilePath = CsvImportDefaultSettings.folderPath + "\\" + CsvImportDefaultSettings.csvInputFile;
	}

	public ArrayList<ArrayList<String>> readCsvToCollection() {
		clientData = new ArrayList<ArrayList<String>>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(this.csvInputFilePath)));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] columns = line.split(this.delimiter);
				ArrayList<String> currentClientDataRow = new ArrayList<String>();
				for (String c : columns) {
					currentClientDataRow.add(c);
				}
				clientData.add(currentClientDataRow);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
				return clientData;
			}
		}
		return clientData;
	}

	public void printClientData() {
		if (clientData != null) {
			for (ArrayList<String> cdr : this.clientData) {
				for (String att : cdr) {
					System.out.print(att + " ");
				}
				System.out.print("\n");
				;
			}
		} else {
			System.out.println("client data is null");
		}
	}
}
