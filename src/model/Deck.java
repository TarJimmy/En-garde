package model;

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
	
}
