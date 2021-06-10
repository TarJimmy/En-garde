package model;

import java.util.Collections;
import java.util.Stack;

public class DeckPioche extends Deck {

	public DeckPioche(Carte[] cartes) {
		super(cartes);
	}
	
	public DeckPioche() {
		super();
	}
	
	public Stack<Carte> getCartes(){
		return this.cartes;
	}
	
	public Carte piocher() {
		if (this.deckVide() ) {
			return null;
		}
		return cartes.pop();
		
	}
	
	public void reposerCarte(Carte c) {
		cartes.push(c);
	}
	
	public void melanger() {
		Collections.shuffle(cartes);
	}

	@Override
	public DeckPioche copySimple() {
		int s = cartes.size();
		Carte[] newCartes = new Carte[s];
		for(int i = 0; i < s; i++) {
			try {
				newCartes[i] = new Carte(cartes.elementAt(i).getDistance());
			} catch (IncorrectCarteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new DeckPioche(newCartes);
	}
	
	
}
