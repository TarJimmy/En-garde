package model;

public class IncorrectPlateauException extends Exception{
	public IncorrectPlateauException(String message) {
		super("Error : \"\\u001B[31m\"" + message);
	}
}
