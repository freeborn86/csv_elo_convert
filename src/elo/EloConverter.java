package elo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import csv.CsvReader;

public class EloConverter {

	private int lengthIdPadded;
	private String eloSourceExportPath;
	private String eloGeneratedExportPath;
	private String currentDestinationDirectory;

	// The below 2 fields are obtained by running getExportMetaData()
	private Long hexIdRootRecord;
	private ArrayList<MetaDataField> metaDataFields;

	private Long hexIdUpComigRecord;
	private ArrayList<ArrayList<String>> clientData;

	// The last existing record in the folder
	private Long hexIdLastRecord;

	private Integer numberOfSubItems;

	// The quasi constant repeating part of each record
	private String recordHeader;

	public EloConverter(String eloSourceExportPath, String eloGeneratedExportPath) {
		this.eloSourceExportPath = eloSourceExportPath;
		this.eloGeneratedExportPath = eloGeneratedExportPath;

		// invalid initial values signaling being not initialized yet
		this.lengthIdPadded = -1;
		this.currentDestinationDirectory = "n/a";
		this.hexIdRootRecord = -1L;
		this.numberOfSubItems = -1;
		this.recordHeader = "n/a";
		this.hexIdUpComigRecord = -1L;
		this.clientData = new ArrayList<ArrayList<String>>();
	}

	public EloConverter() {
		this(DefaultConversionSettings.eloSourceExportPath, DefaultConversionSettings.eloGeneratedExportPath);
	}

	public String toString() {
		String ret = "";
		ret += "Elo Converter state ifnromation\n";
		ret += "-------------------------------\n";
		ret += "Length of records strings: " + this.lengthIdPadded;
		ret += "\nSource Elo Export path: " + this.eloSourceExportPath;
		ret += "\nGenerated Elo Export path: " + this.eloGeneratedExportPath;
		ret += "\nCurrent time stamped Export Directory: " + this.currentDestinationDirectory;
		ret += "\nHex ID of Root Record: " + this.hexIdRootRecord;
		ret += "\nHex ID of Last Record: " + this.hexIdLastRecord;
		ret += "\nNumber of Subitems: " + this.numberOfSubItems;
		ret += "\n\nRecord header:\n" + this.recordHeader;

		ret += "\n\nMetaData Fields:\n";
		ret += "-------------------------------\n";
		for (MetaDataField m : metaDataFields) {
			ret += m.toString();

			// TODO : listing clientData
		}
		return ret;
	}

	public void convert(CsvReader cr) throws IOException {
		copyEloExport(this.eloSourceExportPath, createDestinationDirectory(this.eloGeneratedExportPath));
		readEloExportData();
		generateEloExport(cr);
	}

	// Each time the conversion is run it copies the source export Directory to
	// another destination instance with a timestamp
	// nice2have : destination folder could involve the source of import csv
	// file
	private String createDestinationDirectory(String path) throws IOException {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss_SSS").format(new Date());
		String destinationDirectory = Files.createDirectories(Paths.get(path + "\\" + timeStamp + "_convert"))
				.toString();
		this.currentDestinationDirectory = destinationDirectory;
		return destinationDirectory;
	}

	private void copyEloExport(String src, String dst) {
		try {
			Files.walk(Paths.get(src)).forEach(path -> {
				try {
					Files.copy(path, Paths.get(path.toString().replace(src, dst)));
				} catch (Exception e) {
					// commented this Exception printing, since exceptions are
					// being thrown :(
					// e.printStackTrace();
				}
			});
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		;
	}

	private void readEloExportData() throws IOException {
		getExportMetaData();
		getRecordsInfoFromExport();
		getRecordHeader();
	}

	// improvecode
	// This function HAS A SIDEEFFECT, IT IS SETTING legnthIdPadded (this should
	// be changed or the function name
	private void getExportMetaData() throws IOException {
		Files.walk(Paths.get(this.currentDestinationDirectory)).forEach(filePath -> {

			// Iterating through ExpInfo.ini
			if (Files.isRegularFile(filePath) && filePath.endsWith("ExpInfo.ini")) {
				// debug - showing the ExpInfo.ini file path
				// System.out.println(filePath.toString());
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.toString()), "UTF-16"));
					String line = null;
					boolean rootRecordobtained = false;
					boolean mask1Enctountered = false;
					metaDataFields = new ArrayList<MetaDataField>();
					while ((line = br.readLine()) != null) {
						if (line.equals("[ENTRIES]") && !rootRecordobtained) {
							String rootRecordLine = br.readLine();
							String rootRecord = rootRecordLine.substring(0, rootRecordLine.indexOf('='));
							this.lengthIdPadded = rootRecord.length();
							hexIdRootRecord = Long.decode("0x" + rootRecord);
							rootRecordobtained = true;
						}

						// Gathering meta data elements into the meta data list
						// this condition could be refined by a more specific
						// regex for more "security"
						if (line.equals("[MASK1]"))
							mask1Enctountered = true;

						if (line.startsWith("MaskLine") && (mask1Enctountered == true)) {
							String[] iniMetaData = line.split(",");
							String keyNumProc = iniMetaData[0];
							keyNumProc = keyNumProc.replace("MaskLine", "");
							keyNumProc = keyNumProc
									.replace(keyNumProc.substring(keyNumProc.indexOf("="), keyNumProc.length()), "");
							metaDataFields.add(new MetaDataField(iniMetaData[5], "", Integer.parseInt(keyNumProc) + 1,
									iniMetaData[6], iniMetaData[8]));
							// debug
							// System.out.println(metaDataFields.get(metaDataFields.size()
							// - 1).toString() + "\n");
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return;
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
			}
		});
	}

	private Long getRecordsInfoFromExport() throws IOException {
		Files.walk(Paths.get(this.currentDestinationDirectory)).forEach(filePath -> {
			if (Files.isRegularFile(filePath)
					&& filePath.endsWith(convertLongToHexUpperString(hexIdRootRecord) + ".ESW")) {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.toString()), "UTF-16"));
					String line = null;
					boolean subitemsReached = false;
					numberOfSubItems = 0;
					String lastLine = "";
					while ((line = br.readLine()) != null) {
						if (line.equals("[SUBITEMS]")) {
							subitemsReached = true;
						} else if (subitemsReached == true) {
							// improve code some condition could be written here
							// to assert format if the file does not end with
							// records
							// counting sub items also depends on the file ONLY
							// containing sub items after [SUBITEMS] tag
							lastLine = line;
							numberOfSubItems++;
						}
					}
					int startIndex = lastLine.indexOf('\"') + 1;
					hexIdLastRecord = convertToLongNumber(lastLine.substring(startIndex, startIndex + lengthIdPadded));
					// debug
					// System.out.println(hexIdLastRecord);
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
			}
		});
		return this.hexIdLastRecord;
	}

	// this function reads the last Elo recrod and stores the constant header in
	// a String field of Eloconverter class
	public void getRecordHeader() {
		this.recordHeader = "";
		BufferedReader br = null;
		String currentLastRecordPath = this.currentDestinationDirectory + "\\"
				+ convertLongToHexUpperString(this.hexIdRootRecord) + "\\"
				+ convertLongToHexUpperString(this.hexIdLastRecord) + ".ESW";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(currentLastRecordPath), "UTF-16"));
			String line = null;
			while ((line = br.readLine()) != null) {
				this.recordHeader += line + "\n\r";
				if (line.startsWith("MAPCOUNT=\"")) {
					break;
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

	private void generateEloExport(CsvReader cr) throws IOException {
		generateIndices(generateRecords(cr));
	}

	private void generateIndices(int numOfIndices) throws IOException {

		//
		// Generating the filename
		String recordFilename = this.currentDestinationDirectory + "\\"
				+ convertLongToHexUpperString(this.hexIdRootRecord) + ".ESW";

		// Copying the original content of the file before overwriting it (yeah
		// this is not nice but BOM screws me over)
		String originalContent = "";
		String toWrite ="";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(recordFilename), "UTF-16"));
			String line = null;
			while ((line = br.readLine()) != null) {
				originalContent += line + "\n\r";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// end of copy
		//Adding the originalContent to the temp string
		toWrite += originalContent;

		// Creating the Output stream writer and writing the file
		// TODO use string builder here?
		OutputStreamWriter indexWriter = null;
		try {
			indexWriter = new OutputStreamWriter(new FileOutputStream(recordFilename), "UTF-16");
			// writing the amount of line required
			for (Integer i = 0; i < numOfIndices; i++) {
				toWrite += String.valueOf(this.numberOfSubItems + i) + "=\""
						+ convertLongToHexUpperString(Long.valueOf(this.hexIdLastRecord + 1 + i)) + "\"\r\n";
			}
			indexWriter.write(toWrite);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				indexWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private int generateRecords(CsvReader cr) throws IOException {
		readClientData(cr);
		int numOfRecrodsGenerated = 0;
		for (ArrayList<String> clientRecord : this.clientData) {
			generateRecordFile(clientRecord);
			numOfRecrodsGenerated++;
		}
		return numOfRecrodsGenerated;
	}

	private String generateRecordFile(ArrayList<String> clientRecord) throws IOException {
		// TODO generating the output file here

		// Setting the ID for the upcoming record to be generated
		if (this.hexIdUpComigRecord == -1L) {
			this.hexIdUpComigRecord = this.hexIdLastRecord + 1;
		} else {
			this.hexIdUpComigRecord++;
		}
		// Generating the filename
		String recordFilename = this.currentDestinationDirectory + "\\"
				+ convertLongToHexUpperString(this.hexIdRootRecord) + "\\"
				+ convertLongToHexUpperString(hexIdUpComigRecord) + ".ESW";

		// Creating the Output stream writer and writing the file
		OutputStreamWriter recordWriter = null;
		String toWrite = "";
		//Adding the header
		toWrite += this.recordHeader;
		Iterator<String> i = clientRecord.iterator();
		int keynum = 1;
		for (MetaDataField mdf : metaDataFields){
			if (i.hasNext()){
				mdf.setText(i.next());
				toWrite += mdf.toString(keynum);
				keynum++;
			}
			else{
				toWrite += mdf.toString("", keynum);
				keynum++;
			}
		}
		try {
			recordWriter = new OutputStreamWriter(new FileOutputStream(recordFilename), "UTF-16");
			// Writing into the file
			recordWriter.write(toWrite);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				recordWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return recordFilename;
	}

	private void readClientData(CsvReader cr) {
		this.clientData = cr.readCsvToCollection();
	}

	// codeimprove this is procedural, and should be OOP style in JAVA
	// Converts a long number to a hex string of the length used by the elo
	// export
	private String convertLongToHexUpperString(Long num) {
		return String.format("%0" + this.lengthIdPadded + "x", num).toUpperCase();
	}

	private Long convertToLongNumber(String hexString) {
		return Long.decode("0x" + hexString);
	}

	// this function might not be used at all
	/*
	 * private String getAclString() { return null; }
	 */
}
