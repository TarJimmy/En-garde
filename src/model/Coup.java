package model;

import java.util.ArrayList;

public class Coup implements Cloneable {
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
	
	private Coup() {}
	
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
			for (int j = getEscrimeur().getNbCartes() - 1; j > indicesCartesJouees.get(i); j--) {
                getEscrimeur().getCartes()[j] = getEscrimeur().getCartes()[j-1];
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
	
	public Coup copySimple(Escrimeur[] escrimeurs) {
		Coup coup = new Coup();
		coup.action = action;
		coup.escrimeur = escrimeurs[escrimeur.getIndice()];
		Carte[] newCartes = new Carte[cartes.length];
		for (int i = 0; i < cartes.length; i++) {
			newCartes[i] = cartes[i].copySimple();
		}
		coup.indicesCartesJouees = new ArrayList<>();
		for (Integer integer : indicesCartesJouees) {
	        coup.indicesCartesJouees.add(integer);
	    }
		return coup;
	}
	
	public void printCoup() {
		System.out.println("--------------------------------------------");
		System.out.println("coup : " + this.getAction());
		System.out.println("escrimeur : " + this.getEscrimeur().getIndice());
		System.out.println("cartes : " + this.getCartes().length + " distance : "+this.getCartes()[0].getDistance());
		System.out.println("nb cartesJouees :" +this.getIndicesCartesJouees().size());
		System.out.println("--------------------------------------------");
	}

}
