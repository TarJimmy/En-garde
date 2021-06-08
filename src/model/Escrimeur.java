package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

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
	private LinkedList<Integer> indicesCartesModifierRecemment;
	private LinkedList<Integer> distancesCartesModifierRecemment;
	public boolean isIA;
	public IA m_IA;
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
		this.indicesCartesModifierRecemment = new LinkedList<>();
		this.distancesCartesModifierRecemment = new LinkedList<>();
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
				System.out.println("indice carte changer recemment : " + Arrays.toString(indicesCartesModifierRecemment.toArray()));
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
	
	public LinkedList<Integer> getIndicesCartesModifierRecemment() {
		return indicesCartesModifierRecemment;
	}
	
	public LinkedList<Integer> getDistancesCartesModifierRecemment() {
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
}
