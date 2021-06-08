package controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import model.Carte;
import model.Coup;
import model.DeckDefausse;
import model.DeckPioche;
import model.Escrimeur;
import model.Historique;
import model.IncorrectCarteException;
import model.IncorrectPlateauException;
import model.Jeu;
import model.Plateau;
import model.TypeEscrimeur;
import model.Jeu.Action;

public class ControlerIA extends ControlerJeu {
	
	int eWinner;

	public ControlerIA(Jeu jeu) {
		super(jeu, true, false);
		eWinner = Jeu.NONE;
	}
	
	@Override
	protected void initIA() {}
	
	
	public JeuIA generateNewJeuIA(Jeu jeu) {
		return new JeuIA(jeu);
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
		
		public JeuIA(Jeu jeu) {
			// TODO Auto-generated constructor stub
			this.escrimeurs = new Escrimeur[2];
			this.escrimeurs[Escrimeur.GAUCHER] = jeu.getEscrimeurGaucher().copySimple();
			this.escrimeurs[Escrimeur.DROITIER] = jeu.getEscrimeurDroitier().copySimple();
			this.plateau = jeu.getPlateau().copySimple();
			this.deckDefausse = jeu.getDeckDefausse().copySimple();
			this.deckPioche = jeu.getDeckPioche().copySimple();
			this.indiceCurrentEscrimeur = jeu.getIndiceCurrentEscrimeur();
			this.historique = jeu.getHistorique().copySimple(jeu);
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
				
			public DeckPiocheIA() {
				super();
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
	
	
	public class JeuConsole extends Jeu {

		@SuppressWarnings("unused")
		public JeuConsole() {
			super();
			// TODO Auto-generated constructor stub
		}

		@SuppressWarnings("unused")
		public JeuConsole(Boolean modeSimple, Plateau plateau, DeckPioche deckPioche, DeckDefausse deckDefausse,
				int nbManchesPourVictoire, int indiceCurrentEscrimeur, Escrimeur gaucher, Escrimeur droitier,
				int[] positionsDeparts, boolean animationAutoriser) {
			super(modeSimple, plateau, deckPioche, deckDefausse, nbManchesPourVictoire, indiceCurrentEscrimeur, gaucher, droitier,
					positionsDeparts, animationAutoriser);
			// TODO Auto-generated constructor stub
		}

		@SuppressWarnings("unused")
		public JeuConsole(Boolean modeSimple, Plateau plateau, DeckPioche deckPioche, DeckDefausse deckDefausse,
				int nbManchesPourVictoire, int indiceCurrentEscrimeur, Escrimeur gaucher, Escrimeur droitier,
				int[] positionsDeparts, int indicePremierJoueur, Historique historique, boolean animationAutoriser,
				int idJeu) {
			super(modeSimple, plateau, deckPioche, deckDefausse, nbManchesPourVictoire, indiceCurrentEscrimeur, gaucher, droitier,
					positionsDeparts, indicePremierJoueur, historique, animationAutoriser, idJeu);
			// TODO Auto-generated constructor stub
		}
		
		////////////////////////////////////A SUPPRIMER
		@Override
		public int changerTour() {
			piocher(getCurrentEscrimeur());
			indiceCurrentEscrimeur = (indiceCurrentEscrimeur + 1) % 2;
			peutPasserTour = false;
			Scanner scan = new Scanner(System.in);
			HashSet<Integer> caseJouables = casesJouables();
			int numCase;
			int nbCarte = 1;
			do {
				System.out.println("Case Accessibles : " + Arrays.toString(caseJouables.toArray()));
				System.out.print("Choix de la case : ");
				numCase = scan.nextInt();
			} while(caseJouables.contains(numCase));
			if (numCase == plateau.getPosition((indiceCurrentEscrimeur + 1 ) % 2)) {
				Escrimeur e = getCurrentEscrimeur();
				int nbCarteMax = e.getNbCartes(Math.abs(numCase - plateau.getPosition(e.getIndice())));
				do {
					System.out.println("Nombre de carte attaque (max => " + nbCarteMax + " ) :");
					nbCarte = scan.nextInt(nbCarte);
				} while(nbCarte > 0 && nbCarte <= nbCarteMax);
			}
			clickCase(numCase, nbCarte);
			scan.close();
			return 1;
		} 
	}
	public static void main(String[]args) {
		Carte[] cartes = new Carte[25];
		
		for (int i = 0; i < cartes.length; i++) {
				try {
					cartes[i] = new Carte(i % 5);
				} catch (IncorrectCarteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		Jeu jeu;
		try {
			jeu = new JeuConsole(false, new Plateau(23), new DeckPioche(cartes), new DeckDefausse(), 5, 0, new Escrimeur("Gaucher", TypeEscrimeur.HUMAIN, 0, 5), new Escrimeur("Droitier", TypeEscrimeur.HUMAIN, 1, 5), new int[] {1, 23}, false);
			new ControlerIA((Jeu)jeu);
			jeu.nouvellePartie();
		} catch (IncorrectPlateauException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
