package model;

import java.util.ArrayList;
import java.util.Arrays;

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
	
	public Coup(Coup c, Escrimeur[] escrimeurs) {
		this.action = c.action;
		this.escrimeur = escrimeurs[c.getEscrimeur().getIndice()];
		this.cartes = new Carte[c.cartes.length];
		for (int i = 0; i < c.getCartes().length; i++) {
			this.cartes[i] = c.cartes[i].copySimple();
		}
		this.indicesCartesJouees = new ArrayList<>();
		for (Integer integer : c.getIndicesCartesJouees()) {
	        this.indicesCartesJouees.add(integer);
	    }
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
	
	
	public void printCoup() {
		System.out.println("--------------------------------------------");
		System.out.println("coup : " + this.getAction());
		System.out.println("escrimeur : " + this.getEscrimeur().getIndice());
		System.out.println("cartes : " + this.getCartes().length + " distance : "+this.getCartes()[0].getDistance());
		System.out.println("nb cartesJouees :" +this.getIndicesCartesJouees().size());
		System.out.println("--------------------------------------------");
	}
	
	public String toString() {
		return "{" +escrimeur.getNom() + ", " + Arrays.toString(cartes) + ", " + action + ", " + Arrays.toString(indicesCartesJouees.toArray()) + "}";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()){
            return false;
		}
		Coup other = (Coup) obj;
		//true si 2 attaques du meme type et de la meme longueur ou si aucune attaque
		return (this.getAction() == Coup.ATTAQUEDIRECTE && other.getAction() == Coup.ATTAQUEDIRECTE && this.getCartes().length == other.getCartes().length)
				|| (this.getAction() == Coup.ATTAQUEINDIRECTE && other.getAction() == Coup.ATTAQUEINDIRECTE && this.getCartes().length == other.getCartes().length)
				|| (this.getAction() != Coup.ATTAQUEDIRECTE && this.getAction() != Coup.ATTAQUEINDIRECTE && other.getAction() != Coup.ATTAQUEDIRECTE && other.getAction() != Coup.ATTAQUEINDIRECTE);
	}

}