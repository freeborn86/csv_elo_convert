package elo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import elo.exceptions.InvalidInputException;

public class EloConverter {

	private int lengthIdPadded;
	private String eloSourceExportPath;
	private String eloGeneratedExportPath;
	private String currentDestinationDirectory;

	// The below 2 fields are obtained by running getExportMetaData()
	private Long hexIdRootRecord;
	private ArrayList<MetaDataField> metaDataFields;

	// The last existing record in the folder
	private Long hexIdLastRecord;

	private int numberOfSubItems;

	// The quasi constant repeating part of each record
	private String recordHeader;

	public EloConverter(String eloSourceExportPath, String eloGeneratedExportPath) {
		this.eloSourceExportPath = eloSourceExportPath;
		this.eloGeneratedExportPath = eloGeneratedExportPath;
		
		this.lengthIdPadded = -1;
		this.currentDestinationDirectory = "n/a";
		this.hexIdRootRecord = -1L;
		this.numberOfSubItems = -1;
		this.recordHeader = "n/a";
	}

	public EloConverter() {
		this(DefaultConversionSettings.eloSourceExportPath, DefaultConversionSettings.eloGeneratedExportPath);
	}

	public String  toString(){
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
		ret += "\n\nRecord header:\n " + this.recordHeader;
		return ret;
	}

	public void convert() throws IOException {
		copyEloExport(this.eloSourceExportPath, createDestinationDirectory(this.eloGeneratedExportPath));
		readEloExportData();
	}

	// Each time the conversion is run it copies the source export Directory to
	// another destination instance with a timestamp
	// nice2have : destination folder could involve the source of import csv
	// file
	private String createDestinationDirectory(String path) throws IOException {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss_SSS").format(new Date());
		String destinationDirectory = Files.createDirectory(Paths.get(path + "\\" + timeStamp + "_convert")).toString();
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
			if (Files.isRegularFile(filePath) && filePath.endsWith(convertLongToHexString(hexIdRootRecord) + ".ESW")) {
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
							// imporvecode some condition could be written here
							// to assert format if the file does not end with
							// records
							// counting subitems also depends on the file ONLY
							// containing suitems after [SUBITEMS] tag
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

	// codeimprove this is procedural, and should be OOP style in JAVA
	// Converts a long number to a lenght of hex string used by the elo export
	private String convertLongToHexString(Long num) {
		return String.format("%0" + this.lengthIdPadded + "x", num);
	}

	private Long convertToLongNumber(String hexString) {
		return Long.decode("0x" + hexString);
	}

	private String getAclString() {
		return null;
	}

	private Integer getNumberOfSubItems() {
		return null;
	}

	void getIndicesOld() {
		BufferedReader br = null;
		String input = this.eloSourceExportPath;
		try {
			br = new BufferedReader(new FileReader(input));
			String tmp = br.readLine();
			if (tmp != null && tmp.length() == this.lengthIdPadded) {
				// this.hexIdRecordFolder = Inte
			} else if (tmp.length() != this.lengthIdPadded) {
				throw new InvalidInputException();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (InvalidInputException e) {
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

	// Copying the constant repeating part of each ELO record based on the first
	// record
	void getRecordHeader() {
		BufferedReader br = null;
		String input = this.eloSourceExportPath;
		try {
			br = new BufferedReader(new FileReader(input));
			String tmp = br.readLine();
			if (tmp != null && tmp.length() == this.lengthIdPadded) {
				// this.hexIdRecordFolder = Inte
			} else if (tmp.length() != this.lengthIdPadded) {
				throw new InvalidInputException();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (InvalidInputException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				br.close();
				System.out.println("finally");
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
}

/*
 * Files.walk(Paths.get("c:/users/born/Desktop")).forEach(filePath ->{ if (
 * Files.isRegularFile(filePath) || Files.isDirectory(filePath) ) {
 * System.out.println(filePath); } });
 */

/*
 * package hextest;
 * 
 * public class HexTest {
 * 
 * public static void main (String[] args){ String firstHexRecord =
 * "0000fffffffff"; long decNum = Long.decode("0x" + firstHexRecord);
 * System.out.println(decNum); System.out.println(String.format("%08x",decNum));
 * } }
 */
