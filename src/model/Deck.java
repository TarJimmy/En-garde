package model;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

public abstract class Deck {

	Stack<Carte> cartes;
	
	Deck(Carte[] cartes) {
		this.cartes = new Stack<Carte>();
		for (Carte c : cartes) {
			this.cartes.push(c);
		}
	}
	
	Deck() {
		this.cartes = new Stack<Carte>();
	}
	
	public boolean deckVide() {
		return cartes.empty();
	}
	
	public int nbCartes() {
		return cartes.size();
	}
	
	public void addCarte(Carte c) {
		cartes.push(c);
	}
	
	public int[] getArray() {
		Carte[] arrayCartes = (Carte[]) cartes.toArray();
		int [] res = new int[arrayCartes.length];
		for (int i = 0; i < arrayCartes.length; i++) {
			res[i] = arrayCartes[i].getDistance();
		}
		return res;
	}
}
