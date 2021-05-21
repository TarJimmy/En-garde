package model;

import java.util.ArrayList;
import java.util.Iterator;

import Patterns.Observable;

public class Plateau extends Observable {
	// Position des case: [1, max]
	private int positionDroitier;
	private int positionGaucher;
	private int nbCase;
	
	public Plateau(int positionGaucher, int positionDroitier, int nbCase) throws IncorrectPlateauException {
		if (positionGaucher >= positionDroitier) {
			throw new IncorrectPlateauException("Position gaucher (n°" + positionGaucher + ") >= position droitier (n°" + positionDroitier + ")" );
		} else if (nbCase < 0) {
			throw new IncorrectPlateauException("Nombre de case < 0 : " + nbCase);
		} else if(positionGaucher < 1 || positionDroitier > nbCase){
			throw new IncorrectPlateauException("Un des joueurs n'est pas sur le plateau");
		}else {
			this.nbCase = nbCase;
			setPosition(true, positionGaucher);
			setPosition(false, positionDroitier);
		}
	}
	
	public Plateau(int nbCase) throws IncorrectPlateauException {
		this.nbCase = nbCase;
		setPosition(true, 1);
		setPosition(false, nbCase);
	}
	
	private int getValeurOrientation(Escrimeur e, int deplacement) {
		return e.getIsGaucher() ? deplacement : -deplacement;
	}

	public int getPositionDroitier() {
		return positionDroitier;
	}
	
	public int[] casesJouables(Escrimeur e) {
		int [] res = new int[7];
		return res;
	}

	public void setPositionDroitier(int positionDroitier) {
		this.positionDroitier = positionDroitier;
	}

	public int getPositionGaucher() {
		return positionGaucher;
	}

	public void setPosition(Boolean isGaucher, int position) throws IncorrectPlateauException {
		if (position > 0 && position <= nbCase) {
			if (isGaucher) {
				this.positionGaucher = position; 
			} else {
				this.positionDroitier = position; 
			}
		} else {
			throw new IncorrectPlateauException("Position " + position + " n'appartient pas à [1 ; " + nbCase + "]");
		}
	}

	public int getNbCase() {
		return nbCase;
	}

	public void setNbCase(int nbCase) {
		this.nbCase = nbCase;
	}
	
	public Boolean deplacerEscrimeur(Escrimeur e, int distance) {
		int casesADeplacer = getValeurOrientation(e, distance);
		
		if (e.getIsGaucher()) {
			if (positionGaucher + casesADeplacer < positionDroitier && positionGaucher + casesADeplacer > 0) {
				positionGaucher += casesADeplacer;
				return true;
			} else {
				return false;
			}
		} else {
			if (positionDroitier + casesADeplacer > positionGaucher && positionDroitier + casesADeplacer <= nbCase) {
				positionDroitier += casesADeplacer;
				return true;
			} else {
				return false;
			}
		}
	}
	
	public Boolean attaquerEscrimeur(Escrimeur e, int distance) {
		int distanceAttaque = getValeurOrientation(e, distance);
		
		if (e.getIsGaucher()) {
			if (positionGaucher + distanceAttaque == positionDroitier) {
				return true;
			} else {
				return false;
			}
		} else {
			if (positionDroitier + distanceAttaque == positionGaucher) {
				
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean mouvementPossible(Escrimeur e, int distance) {
		int casesADeplacer = getValeurOrientation(e, distance);
		
		if (e.getIsGaucher()) {
			if (positionGaucher + casesADeplacer < positionDroitier && positionGaucher + casesADeplacer > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			if (positionDroitier + casesADeplacer > positionGaucher && positionDroitier + casesADeplacer <= nbCase) {
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	
	public byte getTypeCarte(Carte c, Escrimeur e) {
		int distance = getValeurOrientation(e, c.getDistance());
		byte res = 0b0;
		if (e.getIsGaucher()) {
			if (positionGaucher - distance > 0 ) {
				res |= Carte.CARTE_RECULER;
			}
			if (positionGaucher + distance == positionDroitier) {
				res |= Carte.CARTE_ATTAQUER;
			} else if (positionGaucher + distance < positionDroitier) {
				res |= Carte.CARTE_AVANCER;
			}	
		} else {
			if (positionDroitier - distance < nbCase ) {
				res |= Carte.CARTE_RECULER;
			}
			if (positionDroitier + distance == positionGaucher) {
				res |= Carte.CARTE_ATTAQUER;
			} else if (positionDroitier + distance < positionGaucher) {
				res |= Carte.CARTE_AVANCER;
			}	
		} 
		return res;
	}
}
