package elo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import elo.exceptions.InvalidInputException;

public class EloConverter {

	private int lengthIdPadded;
	private String eloSourceExportPath;
	private String eloGeneratedExportPath;

	// The identifier of the folder storing the records to be imported
	private Long hexIdRootRecord;

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
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Date());
		String destinationDirectory = Files.createDirectory(Paths.get(path + "\\" + timeStamp + "_convert")).toString();
		// currentDestinationDirectory = destinationDirectory;
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

	private void readEloStaticParts() {
		getRootRecord();
	}
	
	private Long getRootRecord(){
		
		return null;
	}
	
	private Long getLastrecord(){
		return null;
	}
	
	private String getAclString(){
		return null;
	}
	
	private Integer getNumberOfSubItems(){
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
 * 
 * } }
 */
