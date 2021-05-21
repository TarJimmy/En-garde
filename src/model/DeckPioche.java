package model;

import java.util.Collections;

public class DeckPioche extends Deck {

	public DeckPioche(Carte[] cartes) {
		super(cartes);
	}
	
	public int nbCartes() {
		return cartes.size();
	}
	
	public Carte piocher() {
		return cartes.pop();
	}
	
	public void reposerCarte(Carte c) {
		cartes.push(c);
	}
	
	public void melanger() {
		Collections.shuffle(cartes);
	}
}
