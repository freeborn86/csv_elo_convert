package csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvReader {
	private String csvInputFilePath;
	private char delimiter;
	private boolean hasHeader;
	private ArrayList<ArrayList<String>> headerData;

	private ArrayList<ArrayList<String>> clientData;

	public CsvReader() throws IOException {
		CsvImportDefaultSettings csvst = new CsvImportDefaultSettings();
		this.delimiter = csvst.delimiter;
		this.csvInputFilePath = csvst.folderPath + "\\" + csvst.csvInputFile;
		this.hasHeader = csvst.hasHeader;
	}

	public ArrayList<ArrayList<String>> readCsvToCollection() {
		clientData = new ArrayList<ArrayList<String>>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(this.csvInputFilePath)));
			String line = null;
			// If the first line is header
			if (this.hasHeader == true) {
				if ((line = br.readLine()) != null) {
					String[] columns = line.split(String.valueOf(delimiter));
					ArrayList<String> headerData = new ArrayList<String>();
					for (String c : columns) {
						headerData.add(c);
					}
				}
			}
			while ((line = br.readLine()) != null) {
				String[] columns = line.split(String.valueOf(delimiter));
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

	public ArrayList<ArrayList<String>> getLastHeaderData() {
		return this.headerData;
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
