package app;

import java.io.IOException;

import csv.CsvReader;
import elo.EloConverter;

public class Main {

	public static void main(String[] args) throws IOException {
		EloConverter e = new EloConverter();
		e.convert();
		System.out.println(e.toString());
		CsvReader cr = new CsvReader();
		cr.readCsvToCollection();
		cr.printClientData();

	}

}
