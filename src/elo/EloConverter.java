package elo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import elo.exceptions.InvalidInputException;

public class EloConverter {
	// The identifier of the folder storing the records to be imported
	int hexIdRecordFolder;

	// The last existing record in the folder
	int hexIdLastRecord;

	// The quasi constant repeating part of each record
	String recordHeader;

	void getIndicesOld() {
		BufferedReader br = null;
		String input = EloImportDefaultSettings.pathToRecordIds;
		try {
			br = new BufferedReader(new FileReader(input));
			String tmp = br.readLine();
			if (tmp != null && tmp.length() == EloImportDefaultSettings.lengthIdPadded) {
				// this.hexIdRecordFolder = Inte
			} else if (tmp.length() != EloImportDefaultSettings.lengthIdPadded) {
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
		String input = EloImportDefaultSettings.pathToRecordIds;
		try {
			br = new BufferedReader(new FileReader(input));
			String tmp = br.readLine();
			if (tmp != null && tmp.length() == EloImportDefaultSettings.lengthIdPadded) {
				// this.hexIdRecordFolder = Inte
			} else if (tmp.length() != EloImportDefaultSettings.lengthIdPadded) {
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
