package model;

import java.io.File;
import java.io.IOException;

import Global.Configuration;

public class Carte {	
	public int distance;
	
	public Carte(int distance) throws IncorrectCarteException {
		if (distance < 0) {
			throw new IncorrectCarteException("Distance < 0");
		} else {
			this.distance = distance;
		}
	}

	public int getDistance() {
		return this.distance;
	}
	
	public Carte copySimple() {
		try {
			return new Carte(distance);
		} catch (IncorrectCarteException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String toString() { 
        return Integer.toString(distance);
     } 
}
