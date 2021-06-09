package model;

public class IncorrectCarteException extends Exception{
	public IncorrectCarteException(String message) {
		super("Error : \"\\u001B[31m\"" + message);
	}
}
