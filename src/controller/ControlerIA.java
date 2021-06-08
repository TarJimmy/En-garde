package controller;

import model.Carte;
import model.Coup;
import model.DeckPioche;
import model.Escrimeur;
import model.Jeu;
import model.Plateau;
import model.Jeu.Action;

public class ControlerIA extends ControlerJeu {
	
	int eWinner;

	public ControlerIA(Jeu jeu) {
		super(jeu, true, false);
		eWinner = Jeu.NONE;
	}
	
	
	public JeuIA generateNewJeuIA(Jeu jeu) {
		return (JeuIA)jeu.copySimple();
	}
	
	@Override
	public void finDeManche(Escrimeur w) {
		Escrimeur winner = w;
		if (winner == null) {
			int action = jeu.getHistorique().voirDernierCoup().getAction();
			Escrimeur gaucher = jeu.getEscrimeurGaucher();
			Escrimeur droitier = jeu.getEscrimeurDroitier();
			Plateau plateau = jeu.getPlateau();
			if ((action == Coup.ATTAQUEDIRECTE  || action == Coup.ATTAQUEINDIRECTE) && !jeu.casesJouables().isEmpty()) {
					jeu.setDernierTour(true);
			} else {
				if (action == Coup.ATTAQUEDIRECTE  || action == Coup.ATTAQUEINDIRECTE) {
					winner = jeu.getHistorique().voirDernierCoup().getEscrimeur();
				} else {
					int avantageGaucher = 0;
					int nbCartes = gaucher.getNbCartes();
					int distanceAttaque = plateau.getPosition(Escrimeur.DROITIER) - plateau.getPosition(Escrimeur.GAUCHER);
					Carte[] cartesGaucher = gaucher.getCartes();
					//checker le joueur qui a le plus de carte permettant une attaque directe en main
					for (int i = 0; i < nbCartes; i++) {
						if (cartesGaucher[i] != null && cartesGaucher[i].getDistance() == distanceAttaque) {
							avantageGaucher ++;
						}
					}
					nbCartes = droitier.getNbCartes();
					Carte[] cartesDroitier = droitier.getCartes();
					for (int i = 0; i < nbCartes; i++) {
						if (cartesDroitier[i] != null && cartesDroitier[i].getDistance() == distanceAttaque) {
							avantageGaucher --;
						}
					}
					if (avantageGaucher == 0) {
						//checker le joueur qui a le plus avance
						avantageGaucher = (plateau.getPosition(Escrimeur.GAUCHER)-1) - (plateau.getNbCase() - plateau.getPosition(Escrimeur.DROITIER));
					}
					if (avantageGaucher > 0) {
						winner = gaucher;
					} else if (avantageGaucher < 0) {
						winner = droitier;
					}
				}
				if (winner != null) {
					eWinner = winner.getIndice();
				} else {
					eWinner = Jeu.EGALITE;
				}
			}
		} else {
			eWinner = winner.getIndice();
		}
	}
	
	public int getEWinner() {
		return this.eWinner;
	}
	
	public class JeuIA extends Jeu {
		public JeuIA() {
			super();
			deckPioche = (DeckPiocheIA)deckPioche; // => permet a la pioche de se comporter avec les probas
		}
		
		@Override
		public void modifieVue(Action action) {}
		
		@Override
		public DeckPiocheIA getDeckPioche() {
			return (DeckPiocheIA)super.getDeckPioche();
		}
		
		@Override
		public void modifieVueAnimation(Action action) {}
		
		@Override
		public int hashCode() {
			int hash = 5;
			hash = 89 * hash + (this.getDeckPioche() != null ? this.getDeckPioche().hashCode() : 0);
			hash = 89 * hash + (this.getDeckDefausse() != null ? this.getDeckDefausse().hashCode() : 0);
			hash = 89 * hash + this.getIndiceCurrentEscrimeur();
			hash = 89 * hash + (this.getEscrimeurGaucher() != null ? this.getEscrimeurGaucher().hashCode() : 0);
			hash = 89 * hash + (this.getEscrimeurDroitier() != null ? this.getEscrimeurDroitier().hashCode() : 0);
			hash = 89 * hash + (this.getPlateau() != null ? this.getPlateau().hashCode() : 0);
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
			JeuIA other = (JeuIA) obj;
			return this.getDeckPioche().equals(other.getDeckPioche())
					&& this.getDeckDefausse().equals(other.getDeckDefausse())
					&& this.getIndiceCurrentEscrimeur() == other.getIndiceCurrentEscrimeur()
					&& this.getEscrimeurGaucher().equals(other.getEscrimeurGaucher())
					&& this.getEscrimeurDroitier().equals(other.getEscrimeurDroitier())
					&& this.getPlateau().equals(other.getPlateau());
		}
		
		

		public class DeckPiocheIA extends DeckPioche {

			@Override
			public Carte piocher() {
				// redefinir
				return super.piocher(); // carte la plus probable
			}
			
			@Override
			public int hashCode() {
				int hash = 5;
				hash = 89 * hash + this.nbCartes();
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
				DeckPiocheIA other = (DeckPiocheIA) obj;
				return this.nbCartes() == other.nbCartes();
			}
		}
	}
}
