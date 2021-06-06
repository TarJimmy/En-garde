package model;

import java.io.File;
import java.io.IOException;

import Global.Configuration;

public class Carte {	
	public int distance;
	
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
