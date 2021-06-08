package controller;

import model.Carte;
import model.DeckPioche;
import model.Jeu;

public class ControlerIA extends ControlerJeu {

	public ControlerIA(Jeu jeu) {
		super(jeu, true, false);
	}
	
	
	public JeuIA generateNewJeuIA(Jeu jeu) {
		return (JeuIA)jeu.copySimple();
	}
	
	public class JeuIA extends Jeu {
		public JeuIA() {
			super();
			deckPioche = (DeckPiocheIA)deckPioche; // => permet a la pioche de se comporter avec les probas
		}

		public class DeckPiocheIA extends DeckPioche {
				
			public DeckPiocheIA() {
				super();
			}
		}
	}
}
