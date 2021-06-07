package model;

import java.util.Stack;

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
	
	public Carte[] defausseTableau() {
		Carte[] cartes2  = new Carte[this.nbCartes()];
		//System.out.println("size of stacks :" + cartes.size());
		int sizeOfCartes = cartes.size();
		for(int k = 0; k<sizeOfCartes; k++) {
			cartes2[k] = cartes.pop();
			//System.out.println("Cartes attrapée :" + cartes2[k].getDistance() + " à l'indice" + k );
		} 
		
		for(int k = cartes2.length -1;k>=0;k--) {
			cartes.add(cartes2[k]);
		}
		return cartes2;
	}
	
	
}
