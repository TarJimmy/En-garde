package controller;

import java.util.Arrays;

import java.util.Stack;

import model.Carte;
import model.Coup;
import model.DeckPioche;
import model.Escrimeur;
import model.Historique;
import model.IncorrectCarteException;
import model.Jeu;
import model.Plateau;

public class ControlerIA extends ControlerJeu {
	
	int eWinner;

	public ControlerIA(Jeu jeu) {
		super(jeu, false, false); 
		eWinner = Jeu.NONE;
	}
	
	@Override
	protected void initIA() {}
	
	
	public JeuIA generateNewJeuIA(Jeu jeu) {
		
		JeuIA j = new JeuIA(jeu);
		System.out.println("tcsicndiv"+ jeu.getHistorique().equals(j.getHistorique()));
		return j;
	}
	
	public JeuIA generateNewJeuIA(Jeu jeu, int indiceEscrimeurCartesConnues) {
		return new JeuIA(jeu, indiceEscrimeurCartesConnues);
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
		
		public JeuIA(Jeu jeu) {
			init(jeu);
		}
		
		public JeuIA(Jeu jeu, int indiceEscrimeurCartesConnues) {
			init(jeu);
			Escrimeur e = this.escrimeurs[(indiceEscrimeurCartesConnues + 1) %2] ;
			System.out.println(Arrays.toString(jeu.getEscrimeurs()[(indiceEscrimeurCartesConnues + 1) %2].getCartes()));
			System.out.println("taille pioche : "+ deckPioche.nbCartes());
			for (int i = 0; i < e.getNbCartes(); i++) {
				if(e.getCarte(i) != null) {
					System.out.println("on est la");
					deckPioche.reposerCarte(e.getCarte(i));
					e.supprimerCarte(e.getCarte(i));
				}
			}
			System.out.println("taille pioche : "+ deckPioche.nbCartes());
			for (int i = 0; i < e.getNbCartes(); i++) {
				e.ajouterCarte(deckPioche.piocher());
			}
		}
		
		public void init(Jeu jeu) { 
			this.escrimeurs = new Escrimeur[2];
			this.escrimeurs[Escrimeur.GAUCHER] = jeu.getEscrimeurGaucher().copySimple();
			this.escrimeurs[Escrimeur.DROITIER] = jeu.getEscrimeurDroitier().copySimple();
			this.plateau = jeu.getPlateau().copySimple();
			this.deckDefausse = jeu.getDeckDefausse().copySimple();
			this.deckPioche = new DeckPiocheIA(jeu.getDeckPioche().copySimple());
			this.indiceCurrentEscrimeur = jeu.getIndiceCurrentEscrimeur();
			this.historique = new Historique(jeu.getHistorique(), jeu);
			System.out.println(Arrays.toString(jeu.getHistorique().getHistorique().toArray()));
			this.positionsDeparts = jeu.getPositionsDepart().clone();
			this.modeSimple = jeu.getModeSimple();
		}
		

		@Override
		public void modifieVue(Action action) {}
		
		@Override
		public DeckPiocheIA getDeckPioche() {

			return (DeckPiocheIA) deckPioche;
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
					&& this.getPlateau().equals(other.getPlateau())
					&& this.getHistorique().equals(other.getHistorique());
		}
		

		public class DeckPiocheIA extends DeckPioche {
				
			public DeckPiocheIA(DeckPioche deck) {
				super();
				cartes = new Stack<>();
				Stack<Carte> carteDeckOriginel = deck.getCartes();
				try {
					for (Carte c : carteDeckOriginel) {
						cartes.push(new Carte(c.getDistance()));
					}
				} catch (IncorrectCarteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			
			@Override
			public Carte piocher() {
				System.out.println("Piocher Deck-IA");
				return super.piocher();
			}
		}
	}
}
