package elo.exceptions;

public class InvalidEloInput extends InvalidInputException {

	private static final long serialVersionUID = 1L;

	public String toString()
	{
		return "Elo Input is of invalid format";
	}
}
