package model;

import java.io.File;
import java.io.IOException;

import Global.Configuration;

public class Carte {
	public static final byte CARTE_ATTAQUER = 0b01;
	public static final byte CARTE_AVANCER = 0b10;
	public static final byte CARTE_RECULER = 0b100;
	
	private int distance;
	
	public Carte(int distance) throws IncorrectCarteException {
		if (distance < 0) {
			throw new IncorrectCarteException("Distance ");
		} else {
			this.distance = distance;
		}
	}

	public int getDistance() {
		return this.distance;
	}
}
