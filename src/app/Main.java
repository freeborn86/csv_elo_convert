package app;

import java.io.IOException;

import csv.CsvReader;
import elo.EloConverter;

public class Main {

	public static void main(String[] args) throws IOException {		
		CsvReader cr = new CsvReader();
		EloConverter e = new EloConverter();
		e.convert(cr);
		//debug
		//cr.printClientData();
		System.out.println(e.toString());

	}

}
