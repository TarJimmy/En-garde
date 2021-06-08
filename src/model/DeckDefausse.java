package model;

import controller.ControlerIA.JeuIA.DeckPiocheIA;

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
	
	@Override
	public int hashCode() {
		int hash = 5;
		int totalDefausse = 0;
		int s = this.cartes.size();
		for(int i = 0; i < s; i++) {
			totalDefausse += this.cartes.elementAt(i).getDistance();
		}
		hash = 89 * hash + totalDefausse;
		return hash;

	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()){
            return false;
		}
		DeckDefausse other = (DeckDefausse) obj;
		int s = this.cartes.size();
		if(other.cartes.size() != s) {
			return false;
		}
		int[] element1 = new int[5];
		int[] element2 = new int [5];
		
		for(int i = 0; i < s; i++) {
			element1[this.cartes.elementAt(i).getDistance() - 1] ++;
			element2[other.cartes.elementAt(i).getDistance() - 1] ++;
		}
		boolean res = true;
		for(int i = 0; i < 5; i++) {
			if(element1[i] != element2[i]) {
				res = false;
			}
		}
		return res;
	}
}
