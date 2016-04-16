package elo.exceptions;

public class MissingInputFileException extends InputException {
	private static final long serialVersionUID = 1L;

	private String missingFileName;

	public MissingInputFileException(String mfn) {
		this.missingFileName = mfn;
	}

	public MissingInputFileException() {
		this("N/A");
	}

	public String toString() {
		return "Missing essential input file: " + this.missingFileName;
	}
}
