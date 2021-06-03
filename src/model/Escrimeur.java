package model;

import Patterns.Observable;

public class Escrimeur extends Observable {
	private String nom;
	private TypeEscrimeur type;
	private int indice; //0 si l'escrimeur est gaucher, 1 sinon
	private Carte[] cartes;
	private int nbCartes;
	public static final int GAUCHER = 0;
	public static final int DROITIER = 1;
	private int mancheGagner;
	/**
	 * Constructeur lors d'unne nouvelle partie
	 * @param nom
	 * @param type
	 * @param indice
	 * @param nbCarte
	 */
	public Escrimeur(String nom, TypeEscrimeur type, int indice, int nbCarte) { //indice = 0 pour gaucher, 1 pour droitier
		setNom(nom);
		this.type = type;
		this.indice = indice;
		this.cartes = new Carte[nbCarte];
		this.nbCartes = nbCarte;
		this.mancheGagner = 0;
	}
	
	/**
	 * Constructeur lors du chargement d'une partie
	 * @param nom
	 * @param type
	 * @param indice
	 * @param distances
	 * @param mancheGagner
	 */
	public Escrimeur(String nom, TypeEscrimeur type, int indice, int[] distances, int mancheGagner) {
		setNom(nom);
	}
	
	public int getNbCartes() {
		return this.nbCartes;
	}
	
	public boolean manqueCarte() {
		int i = 0;
		while (i < nbCartes && cartes[i] != null) {
			i++;
		}
		return i < nbCartes;
	}
	
	private void setNom(String nom) {
		if (nom == null || nom.equals("")) {
			this.nom = "null";
		} else {
			this.nom = nom;
		}
	}
	
	public Boolean ajouterCarte(Carte c) {
		for (int i = 0; i < cartes.length; i++) {
			if (cartes[i] == null) {
				cartes[i] = c;
				metAJour();
				return true;
			}
		}
		return false;
	}
	
	
	
	public Boolean ajouterCarteAIndiceVide(Carte c, int indice) {
		if (cartes[indice] == null) {
			cartes[indice] = c;
			return true;
		}
		return false;
	}
	
	public int supprimerCarte(Carte c) {
		for (int i = 0; i < cartes.length; i++) {
			if (cartes[i] == c) {
				for (int j = i; j < cartes.length - 1; j++) {
					cartes[j] = cartes [j+1];
				}
				cartes[cartes.length - 1] = null;
				metAJour();
				return i;
			}
		}
		return -1;
	}
	
	public Carte[] getCartes() {
		return cartes;
	}
	
	public Carte getCarte(int i) {
		return cartes[i];
	}

	public String getNom() {
		return nom;
	}

	public TypeEscrimeur getType() {
		return type;
	}

	public int getIndice() {
		return indice;
	}

	public static int getGaucher() {
		return GAUCHER;
	}

	public static int getDroitier() {
		return DROITIER;
	}

	public int getMancheGagner() {
		return mancheGagner;
	}
	
	public Boolean getIsGaucher() {
		return indice == GAUCHER;
	}
	
	public void addMancheGagnee() {
		mancheGagner++;
	}	
	
	public int getNbCartes(int distance) {
		int res = 0;
		for (int i = 0; i < cartes.length; i++) {
			if (cartes[i] != null &&  cartes[i].getDistance()== distance) {
				res++;
			}
		}
		return res;
	}
}
