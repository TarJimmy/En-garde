package model;

import java.util.HashSet;

import Patterns.Observable;

public class Plateau extends Observable {
	// Position des case: [1, max]
	private int positionJoueurs[]; // gaucher a l'indice 0, droitier a l'indice 1
	private int nbCase;
	
	public Plateau(int positionGaucher, int positionDroitier, int nbCase) throws IncorrectPlateauException {
		if (positionGaucher >= positionDroitier) {
			throw new IncorrectPlateauException("Position gaucher (n°" + positionGaucher + ") >= position droitier (n°" + positionDroitier + ")");
		} else if (nbCase < 0) {
			throw new IncorrectPlateauException("Nombre de case < 0 : " + nbCase);
		} else if (positionGaucher < 1 || positionDroitier > nbCase) {
			throw new IncorrectPlateauException("Un des joueurs n'est pas sur le plateau");
		} else {
			positionJoueurs = new int[2];
			this.nbCase = nbCase;
			positionJoueurs[0] = positionGaucher;
			positionJoueurs[1] = positionDroitier;
		}
	}

	public Plateau(int nbCase) throws IncorrectPlateauException {
		positionJoueurs = new int[2];
		this.nbCase = nbCase;
		positionJoueurs[0] = 1;
		positionJoueurs[1] = nbCase;
	}

	private int getValeurOrientation(Escrimeur e, int deplacement) {
		return e.getIndice() == 0 ? deplacement : -deplacement;
	}

	/*
	 * joueur = 0 -> gaucher, joueur = 1 -> droitier
	 */
	public int getPosition(int joueur) {
		return positionJoueurs[joueur];
	}

	public void setPosition(int position, int joueur) throws IncorrectPlateauException {
		if (position > 0 && position <= nbCase) {
			this.positionJoueurs[joueur] = position;
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

	public boolean memeSigne(int i, int j) {
		return ((i > 0 && j > 0) || (i < 0 && j < 0) || (i == 0 && j == 0));
	}

	public Boolean deplacerEscrimeur(Escrimeur e, int distance) {
		int casesADeplacer = getValeurOrientation(e, distance);
		int currentIndice = e.getIndice();
		
		if (mouvementPossible(e, distance)) {
			positionJoueurs[currentIndice] += casesADeplacer;
			metAJour();
			return true;
		} else {
			System.err.println("dans plateau.deplacerEscrimeur : deplacementImpossible");
			return false;
		}

	}

	public Boolean escrimeurPeutAttaquer(Escrimeur e, int distance) {
		int distanceAttaque = getValeurOrientation(e, distance);
		int currentIndice = e.getIndice();
		int indiceAutre = (currentIndice + 1) % 2;

		if (positionJoueurs[currentIndice] + distanceAttaque == positionJoueurs[indiceAutre]) {
			return true;
		} else {
			return false;
		}
	}

	public boolean mouvementPossible(Escrimeur e, int distance) {
		int casesADeplacer = getValeurOrientation(e, distance);
		int currentIndice = e.getIndice();
		int indiceAutre = (currentIndice + 1) % 2;

		return (positionJoueurs[currentIndice] + casesADeplacer > 0
				&& positionJoueurs[currentIndice] + casesADeplacer <= nbCase
				&& memeSigne(positionJoueurs[currentIndice] - positionJoueurs[indiceAutre],
						positionJoueurs[currentIndice] + casesADeplacer - positionJoueurs[indiceAutre]));

	}

	public boolean peutJouer(Escrimeur e) {
		boolean res = false;
		int i = 0;
		while( i < e.getNbCartes() && res == false) {
			if (e.getCarte(i) != null && (escrimeurPeutAttaquer(e, e.getCartes()[i].getDistance()) || mouvementPossible(e, e.getCartes()[i].getDistance()))) {
				res = true;
			}
			i++;
		}
		return res;
	}
	
	/**
	 *
	 * @param e, typeCoupsPossibles donne à la fonction les coups autorisés, 0x1 = avancer, 0x10 = reculer/esquiver, 0x100 = attaquer, 0x1000 = defendre, 0x10000 = passer son tour
	 * @return
	 */
	public HashSet<Integer> casesJouables(Escrimeur e, int typesCoupsPossibles, int valeurDefense) {
		int escrimeur = (e.getIsGaucher() ? 0 : 1);
		int cartesDefense = 0;
		HashSet<Integer> res = new HashSet<>();
		for (int i = 0; i < e.getNbCartes(); i++) {
			if (e.getCarte(i) != null) {
				int distance = e.getCarte(i).getDistance();
				if (mouvementPossible(e, distance) && (typesCoupsPossibles | 0x1) == typesCoupsPossibles) {
					res.add(getPosition(escrimeur) + getValeurOrientation(e, distance));
				}
				if (mouvementPossible(e, -distance) && (typesCoupsPossibles | 0x10) == typesCoupsPossibles) {
					res.add(getPosition(escrimeur) - getValeurOrientation(e, distance));
				}
				if (escrimeurPeutAttaquer(e, distance)) {
					if ((typesCoupsPossibles | 0x100) == typesCoupsPossibles) {
						res.add(getPosition(escrimeur) + getValeurOrientation(e, distance));
					}
					if ((typesCoupsPossibles | 0x1000) == typesCoupsPossibles) {
						cartesDefense += 1;
					}
				}
			}
		}
		if ((typesCoupsPossibles | 0x1000) == typesCoupsPossibles && cartesDefense >= valeurDefense) {
			res.add(getPosition(escrimeur));
		}
		if ((typesCoupsPossibles | 0x10000) == typesCoupsPossibles) {
			res.add(-1);
		}
		return res;
	}
}
