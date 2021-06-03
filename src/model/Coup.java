package model;

import java.util.ArrayList;

public class Coup {
	Escrimeur escrimeur;
	Carte[] cartes;
	int action;
	ArrayList<Integer> indicesCartesJouees;
	
	public static final int AVANCER = 0;
	public static final int RECULER = 1;
	public static final int ATTAQUEDIRECTE = 2;
	public static final int ATTAQUEINDIRECTE = 3;
	public static final int PARER = 4;
	public static final int ESQUIVER = 5;
	
	public Coup(Escrimeur e, Carte[] c, int a) {
		escrimeur = e;
		cartes = c;
		action = a;
		indicesCartesJouees = new ArrayList<>();
	}
	
	public void viderCartesJouees() {
		indicesCartesJouees = new ArrayList<>();
	}

	public ArrayList<Integer> getIndicesCartesJouees() {
		return indicesCartesJouees;
	}
	
	/*
	 * remet chaque carte de la main de l'escrimeur à l'indice ou elle etait avant que l'escrimeur ne joue son coup
	 * equivaut a annuler le decallage qui se fait chaque fois qu'on defausse une carte
	 */
	public void remettreCartesDansLordre() {
		for (int i = indicesCartesJouees.size() -1; i >= 0; i--) {
			for (int j = indicesCartesJouees.get(i); j < getEscrimeur().getNbCartes() - 1; j++) {
				getEscrimeur().getCartes()[j+1] = getEscrimeur().getCartes()[j];
			}
			getEscrimeur().getCartes()[indicesCartesJouees.get(i)] = null;
		}
	}
	
	public void addCarteJouee(int indice) {
		indicesCartesJouees.add(indice);
	}

	public Escrimeur getEscrimeur() {
		return escrimeur;
	}

	public void setEscrimeur(Escrimeur escrimeur) {
		this.escrimeur = escrimeur;
	}

	public Carte[] getCartes() {
		return cartes;
	}

	public void setCartes(Carte[] cartes) {
		this.cartes = cartes;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
	
	

}
