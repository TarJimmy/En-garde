package model;

import java.util.ArrayList;

public class Coup {
	Escrimeur escrimeur;
	Carte[] cartes;
	int action;
	ArrayList<Integer> indicesCartesJouees;
	ArrayList<Integer> indicesCartesJoueesDefense;
	boolean attaqueEsquivee;
	
	public static final int AVANCER = 0;
	public static final int RECULER = 1;
	public static final int ATTAQUEDIRECTE = 2;
	public static final int ATTAQUEINDIRECTE = 3;
	
	public Coup(Escrimeur e, Carte[] c, int a) {
		escrimeur = e;
		cartes = c;
		action = a;
		indicesCartesJouees = new ArrayList<>();
		indicesCartesJoueesDefense = new ArrayList<>();
		attaqueEsquivee = false;
	}
	
	public boolean isAttaqueEsquivee() {
		return attaqueEsquivee;
	}
	
	public void nullifierCartesJouees() {
		indicesCartesJouees = new ArrayList<>();
		indicesCartesJoueesDefense = new ArrayList<>();
	}

	public void setAttaqueEsquivee(boolean attaqueEsquivee) {
		this.attaqueEsquivee = attaqueEsquivee;
	}

	public ArrayList<Integer> getIndicesCartesJouees() {
		return indicesCartesJouees;
	}
	
	public void addCarteJouee(int indice) {
		indicesCartesJouees.add(indice);
	}
	
	public ArrayList<Integer> getIndicesCartesJoueesDefense() {
		return indicesCartesJoueesDefense;
	}
	
	public void addCarteJoueeDefense(int indice) {
		indicesCartesJoueesDefense.add(indice);
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
