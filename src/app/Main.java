package app;

import java.io.IOException;
import elo.ConversionSettings;
import elo.EloConverter;

public class Main {

	public static void main(String[] args) throws IOException {
		String dst = EloConverter.createDestinationDirectory(ConversionSettings.eloGeneratedExportPath);
		EloConverter.copyEloExport(ConversionSettings.eloSourceExportPath,dst);

	}

}
