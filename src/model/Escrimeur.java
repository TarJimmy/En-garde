package model;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;

import Patterns.Observable;

public class Escrimeur {
	private String nom;
	private TypeEscrimeur type;
	private int indice; //0 si l'escrimeur est gaucher, 1 sinon
	private Carte[] cartes;
	public static final int GAUCHER = 0;
	public static final int DROITIER = 1;
	private int mancheGagner;
	private ArrayList<Integer> indicesCartesModifierRecemment;
	private ArrayList<Integer> distancesCartesModifierRecemment;
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
		this.mancheGagner = 0;
		this.indicesCartesModifierRecemment = new ArrayList<>();
		this.distancesCartesModifierRecemment = new ArrayList<>();
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
	
	private Escrimeur() {}
	
	public int getNbCartes() {
		return this.cartes.length;
	}
	
	public boolean manqueCarte() {
		int i = 0;
		while (i < cartes.length && cartes[i] != null) {
			i++;
		}
		return i < cartes.length;
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
				indicesCartesModifierRecemment.add(i);
				distancesCartesModifierRecemment.add(cartes[i].getDistance());
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
				distancesCartesModifierRecemment.add(cartes[i].getDistance());
				for (int j = i; j < cartes.length - 1; j++) {
					cartes[j] = cartes [j + 1];
				}
				indicesCartesModifierRecemment.add(i);
				cartes[cartes.length - 1] = null;
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
	
	public void setMancheGagner(int nbWin) {
		this.mancheGagner = nbWin;
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
	
	public void prepareChangeCartes() {
		indicesCartesModifierRecemment.clear();
		distancesCartesModifierRecemment.clear();
	}
	
	public ArrayList<Integer> getIndicesCartesModifierRecemment() {
		return indicesCartesModifierRecemment;
	}
	
	public ArrayList<Integer> getDistancesCartesModifierRecemment() {
		return distancesCartesModifierRecemment;
	}
	
	public void resetMancheGagner() {
		mancheGagner = 0;
	}

	public Escrimeur copySimple() {
		Escrimeur e = new Escrimeur();
		e.nom = new String(nom);
		e.type = type;
		e.indice = indice;
		e.cartes = new Carte[cartes.length];
		for (int i = 0; i < cartes.length; i++) {
			e.cartes[i] = cartes[i].copySimple();
		}
		return e;
	}
	
	public int[] getArrayCartes() {
		int [] res = new int[cartes.length];
		for (int i = 0; i < cartes.length; i++) {
			if (cartes[i] != null) {
				res[i] = cartes[i].getDistance();
			} else {
				res[i] = 0;
			}
		}
		return res;
	}
	
	public void setCartes(JSONArray cartesInt,int NbCartes) {
		for (int i = 0; i < NbCartes; i++) {
			try {
				int temp = cartesInt.getInt(i);
				if (temp != 0) {
					cartes[i] = new Carte(cartesInt.getInt(i));
				} 
			} catch (JSONException | IncorrectCarteException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public int hashCode() {
		int hash = 5;
		int totalMain = 0;
		int s = this.cartes.length;
		for(int i = 0; i < s; i++) {
			totalMain += (this.cartes[i] != null ? this.cartes[i].getDistance() : 0);
		}
		hash = 89 * hash + totalMain;
		return hash;

	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()){
            return false;
		}
		Escrimeur other = (Escrimeur) obj;
		int s = this.cartes.length;
		if(other.cartes.length != s) {
			return false;
		}
		int[] element1 = new int[5];
		int[] element2 = new int [5];
		
		for(int i = 0; i < s; i++) {
			if(this.cartes[i] != null) {
				element1[this.cartes[i].getDistance() - 1] ++;
			}
			if(other.cartes[i] != null) {
				element2[other.cartes[i].getDistance() - 1] ++;
			}
		}
		boolean res = true;
		for(int i = 0; i < 5; i++) {
			if(element1[i] != element2[i]) {
				res = false;
			}
		}
		return res;
	}
	
	
	
	
}
