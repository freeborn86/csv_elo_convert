package csv;

public class CsvReader {
	private String delimiter;
	private String csvInputFilePath;
	
	CsvReader(){
		this.delimiter = CsvImportDefaultSettings.delimiter;
		this.csvInputFilePath = CsvImportDefaultSettings.csvInputFilePath;
	}
	
	
	//test
	public static void readCsvToCollection(){
		
	}
}
