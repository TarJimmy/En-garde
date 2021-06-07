package model;


public class DeckDefausse extends Deck {

	public DeckDefausse(Carte[] cartes) {
		super(cartes);
	}
	
	public DeckDefausse() {
		super();
	}
	
	public void defausser(Carte c) {
		cartes.push(c);
	}
	
	public Carte reprendreDerniereCarte() {
		return cartes.pop();
	}
	
	public Carte consulterCarteVisible() {
		return cartes.peek();
	}
	
	public DeckDefausse copySimple() {
		return new DeckDefausse((Carte[])cartes.clone());
	}
}
