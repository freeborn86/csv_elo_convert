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

	// The identifier of the folder storing the records to be imported
	private Long hexIdRootRecord;

	// this collection stores
	private ArrayList<MetaDataField> metaDataFields;

	// The last existing record in the folder
	private Long hexIdLastRecord;

	// The quasi constant repeating part of each record
	private String recordHeader;

	public EloConverter(int lengthIdPadded, String eloSourceExportPath, String eloGeneratedExportPath) {
		this.lengthIdPadded = lengthIdPadded;
		this.eloSourceExportPath = eloSourceExportPath;
		this.eloGeneratedExportPath = eloGeneratedExportPath;
	}

	public EloConverter() {
		this(DefaultConversionSettings.lengthIdPadded, DefaultConversionSettings.eloSourceExportPath,
				DefaultConversionSettings.eloGeneratedExportPath);
	}

	public void convert() throws IOException {
		copyEloExport(this.eloSourceExportPath, createDestinationDirectory(this.eloGeneratedExportPath));
		readEloStaticParts();
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

	private void readEloStaticParts() throws IOException {
		getExportMetaData();
	}

	// This function HAS A SIDEEFFECT, IT IS SETTING legnthIdPadded (this should
	// be changed or the function name)
	private Long getExportMetaData() throws IOException {
		Files.walk(Paths.get(this.currentDestinationDirectory)).forEach(filePath -> {
			
			//Iterating through ExpInfo.ini
			if (Files.isRegularFile(filePath) && filePath.endsWith("ExpInfo.ini")) {
				// debug - showing the ExpInfo.ini file path
				System.out.println(filePath.toString());
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
							System.out.println(
									"Root hex Id: " + String.format("%0" + this.lengthIdPadded + "x", hexIdRootRecord));
						}
						
						//Gathering metadata elements into the metadata list
						//this condition could be refined by a more specific regex for more "security"
						if (line.equals("[MASK1]"))
							mask1Enctountered = true;
						
						if (line.startsWith("MaskLine") && (mask1Enctountered == true) ){
							String[] iniMetaData = line.split(",");
							String keyNumProc = iniMetaData[0];
							keyNumProc = keyNumProc.replace("MaskLine", "");
							keyNumProc = keyNumProc.replace(keyNumProc.substring(keyNumProc.indexOf("="), keyNumProc.length()), "");
							metaDataFields.add(new MetaDataField(iniMetaData[5], "", Integer.parseInt(keyNumProc)+1, iniMetaData[6], iniMetaData[8]));
							System.out.println(metaDataFields.get(metaDataFields.size()-1).toString());
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
		return null;
	}

	private Long getLastrecord() {
		return null;
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
				System.out.println("finally");
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
