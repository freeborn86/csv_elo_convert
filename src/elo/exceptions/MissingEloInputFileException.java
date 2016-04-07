package elo.exceptions;

public class MissingEloInputFileException extends MissingInputFileException {

	private static final long serialVersionUID = 1L;

	private String missingFileName;

	public MissingEloInputFileException(String mfn) {
		this.missingFileName = mfn;
	}

	public MissingEloInputFileException() {
		this("N/A");
	}

	public String toString() {
		return "Missing essential ELO input file: " + this.missingFileName;
	}

}
