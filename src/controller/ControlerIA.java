package controller;

import model.Carte;
import model.DeckDefausse;
import model.DeckPioche;
import model.Escrimeur;
import model.Jeu;
import model.Plateau;

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

			@Override
			public Carte piocher() {
				// redefinir
				return super.piocher(); // carte la plus probable
			}
		}
	}
}
