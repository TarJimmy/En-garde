package model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

import Patterns.Observable;
import controller.ControlerJeu;
import model.Jeu.Action;

public class Jeu extends Observable {
	
	public enum Action {
		ACTUALISER_JEU,
		ACTUALISER_DECK,
		ACTUALISER_PLATEAU,
		ACTUALISER_ESCRIMEUR,
		ACTUALISER_ESCRIMEUR_DROITIER,
		ACTUALISER_ESCRIMEUR_GAUCHER,
		ANIMATION_PIOCHER,
		ANIMATION_DEFAUSSER,
		ANIMATION_DEBUT_MANCHE,
		ANIMATION_FIN_MANCHE,
		ANIMATION_LANCER,
		ANIMATION_DEPLACER_ESCRIMEUR,
	}
	
	private Boolean modeSimple; 
	public Boolean peutPasserTour;
	private Plateau plateau;
	private DeckPioche deckPioche;
	private DeckDefausse deckDefausse;
	private int indiceCurrentEscrimeur;
	private int indicePremierJoueur;
	
	private Escrimeur[] escrimeurs;
	private int winner;
	private Historique historique;
	private boolean dernierTour;
	private int nbManchesPourVictoire;
	
	private LinkedList<Action> listeActions;
	private Boolean actionEnCours;
	
	public static final int EGAUCHER = 0;
	public static final int EDROITIER = 1;
	public static final int EGALITE = 2;
	public static final int NONE = 3;

	private int lastWinner;
	public Jeu(Boolean modeSimple, Plateau plateau, DeckPioche deckPioche, DeckDefausse deckDefausse, int nbManchesPourVictoire, int indiceCurrentEscrimeur, Escrimeur gaucher, Escrimeur droitier) {
		super();
		this.indiceCurrentEscrimeur = 0;
		setHistorique(new Historique(this));
		init(modeSimple, plateau, deckPioche, deckDefausse, nbManchesPourVictoire, gaucher, droitier);
		modifieVue(Action.ACTUALISER_JEU);
		indicePremierJoueur = indiceCurrentEscrimeur;
		peutPasserTour = false;
	}

	public Jeu(Boolean modeSimple, Plateau plateau, DeckPioche deckPioche, DeckDefausse deckDefausse, int nbManchesPourVictoire, int indiceCurrentEscrimeur, Escrimeur gaucher, Escrimeur droitier, Action action, Historique historique) {
		super();
		this.indiceCurrentEscrimeur = indiceCurrentEscrimeur;
		setHistorique(historique);
		init(modeSimple, plateau, deckPioche, deckDefausse, nbManchesPourVictoire, gaucher, droitier);
		modifieVue(action);
		indicePremierJoueur = indiceCurrentEscrimeur;
		peutPasserTour = false;
	}
	
	private void init(Boolean modeSimple, Plateau plateau, DeckPioche deckPioche, DeckDefausse deckDefausse, int nbManchesPourVictoire, Escrimeur gaucher, Escrimeur droitier) {
		this.modeSimple = modeSimple;
		this.nbManchesPourVictoire = nbManchesPourVictoire;
		this.plateau = plateau;
		this.deckPioche = deckPioche;
		this.deckDefausse = deckDefausse;
		this.escrimeurs = new Escrimeur[2];
		this.escrimeurs[0] = gaucher;
		this.escrimeurs[1] = droitier;
		this.listeActions = new LinkedList<>();
		this.lastWinner = NONE;
		this.actionEnCours = false;
		this.dernierTour = false;
		this.winner = NONE;
	}
	
	public boolean isDernierTour() {
		return dernierTour;
	}

	public void setDernierTour(boolean dernierTour) {
		this.dernierTour = dernierTour;
	}

	public void setHistorique(Historique h) {
		this.historique = h;
	}
	
	public Historique getHistorique() {
		return this.historique;
	}

	public Boolean getModeSimple() {
		return modeSimple;
	}

	public Escrimeur getCurrentEscrimeur() {
		return this.escrimeurs[this.indiceCurrentEscrimeur];
	}
	
	public Escrimeur getNotCurrentEscrimeur() {
		return this.escrimeurs[((indiceCurrentEscrimeur + 1) % 2)];
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public DeckPioche getDeckPioche() {
		return deckPioche;
	}

	public DeckDefausse getDeckDefausse() {
		return deckDefausse;
	}

	public int getIndiceCurrentEscrimeur() {
		return indiceCurrentEscrimeur;
	}

	public Boolean getIsTourGaucher() {
		return indiceCurrentEscrimeur == Escrimeur.GAUCHER;
	}
	
	public Escrimeur getEscrimeurGaucher() {
		return escrimeurs[0];
	}

	public Escrimeur getEscrimeurDroitier() {
		return escrimeurs[1];
	}

	public void piocher(Escrimeur e) {
		while (e.manqueCarte() && !deckPioche.deckVide()) {
			e.ajouterCarte(deckPioche.piocher());
			//modifieVue(Action.ANIMATION_PIOCHER);
			modifieVue(Action.ACTUALISER_DECK);
		}
	}

	public int changerTour() {
		piocher(getCurrentEscrimeur());
		indiceCurrentEscrimeur = (indiceCurrentEscrimeur + 1) % 2;
		peutPasserTour = false;
		modifieVue(Action.ACTUALISER_JEU);
		return 1;
	}

	/**
	 * retire une carte de la main de l'escrimeur, l'ajoute à la défausse et serre
	 * les cartes de l'escrimeur à gauche.
	 * 
	 * @param e, escrimeur qui doit ACTION_DEFAUSSER la carte c
	 * @param c, carte que l'escrimeur e doit ACTION_DEFAUSSER
	 * @return si -1 -> echec, sinon indice de la carte qui a été défaussée
	 */
	public int ACTION_DEFAUSSER(Escrimeur e, Carte c) {
		int res = e.supprimerCarte(c);
		if (res == -1) {
			return res;
		}
		deckDefausse.defausser(c);
		//modifieVue(Action.DEFAUSSER);
		modifieVue(Action.ACTUALISER_DECK);
		return res;
	}

	public boolean jouer(Coup c, boolean rejoueCoupAnnule) {
		// cette fonction termine automatiquement un tour
		// appeler avec rejoueCoupAnnule a false sauf si on rejoue un coup annule
		// ce booleen sert a ne pas clean la pile des coups annulés si le coup vient de
		// la fonction rejouerCoupAnnule()
		if (c.getCartes().length < 1) {
			System.err.println("impossible de jouer un coup sans carte");
			return false;
		}
		for (int i = 0; i < c.getCartes().length; i++) {
			if (c.getCartes()[i] == null) {
				System.err.println("erreur : le coup contient des emplacements de cartes nuls");
				return false;
			}
		}
		if (c.getEscrimeur() != getCurrentEscrimeur()) {
			System.err.println("impossible de jouer un coup si ce n'est pas au tour de ce joueur");
			return false;
		}
		
		int indiceDefausse;
		
		switch(c.getAction()) {
			case Coup.AVANCER : 
			case Coup.RECULER :
			case Coup.ESQUIVER :
				int sens = ((c.getAction() % 2) == 0 ? 1 : -1);
	
				if (plateau.deplacerEscrimeur(c.getEscrimeur(), c.getCartes()[0].getDistance() * sens)) {
					indiceDefausse = ACTION_DEFAUSSER(c.getEscrimeur(), c.getCartes()[0]);
					if (indiceDefausse != -1) {
						c.addCarteJouee(indiceDefausse);
						historique.ajouterCoup(c);
						if (!rejoueCoupAnnule) {
							historique.viderCoupsAnnules();
						}
						if(c.getAction() != Coup.ESQUIVER) {
							peutPasserTour = true;
						}
						modifieVue(Action.ACTUALISER_ESCRIMEUR);
						modifieVue(Action.ACTUALISER_DECK);
						modifieVue(Action.ANIMATION_DEPLACER_ESCRIMEUR);
						modifieVue(Action.ACTUALISER_PLATEAU);
						return true;
					} else {
						// le coup ne va pas etre joué, on ramene donc le joueur a sa position initiale
						plateau.deplacerEscrimeur(c.getEscrimeur(), (c.getCartes()[0].getDistance()) * (-sens));
						System.err.println("impossible de jouer un coup si le joueur n'a pas la carte en main");
						return false;
					}
				} else {
					System.err.println("impossible de jouer un coup si le deplacement n'est pas possible");
					return false;
				}
			case Coup.ATTAQUEDIRECTE :
			case Coup.ATTAQUEINDIRECTE :
				int valeurAttaque = c.getCartes()[0].getDistance();
				if (!plateau.escrimeurPeutAttaquer(c.getEscrimeur(), valeurAttaque)) {
					System.err.println("impossible de jouer un coup d'attaque car les distances ne correspondent pas");
					return false;
				}
				int i = 0;
				boolean defausseOk = true;
				while (i < c.getCartes().length && c.getCartes()[i].getDistance() == valeurAttaque && defausseOk) {
					indiceDefausse = ACTION_DEFAUSSER(c.getEscrimeur(), c.getCartes()[i]);
					if (indiceDefausse == -1) {
						defausseOk = false;
					} else {
						c.addCarteJouee(indiceDefausse);
						i++;
					}
				}
				if (i != c.getCartes().length) {
					System.err.println("les cartes de l'attaque ne sont pas toutes de même valeur ou l'attaquant ne les a pas toutes en main");
					c.remettreCartesDansLordre();
					while (i != 0) {
						if (!c.getEscrimeur().ajouterCarteAIndiceVide(deckDefausse.reprendreDerniereCarte(),
								c.getIndicesCartesJouees().get(i - 1))) {
							System.err.println("probleme lors de l'annulation du coup impossible");
						}
						i--;
					}
					return false;
				}
				historique.ajouterCoup(c);
				if (!rejoueCoupAnnule) {
					historique.viderCoupsAnnules();
				}
				changerTour();
				System.out.println("le joueur"+ c.getEscrimeur().getIndice()+1 + "a attaqué puis passé");
				return true;
				
			case Coup.PARER :
				int attaque = historique.voirDernierCoup().getCartes().length;
				int j = 0;
				int nbcartes = getCurrentEscrimeur().getNbCartes();
				while (j < nbcartes && attaque > 0) {
					if (plateau.escrimeurPeutAttaquer(getCurrentEscrimeur(),
							getCurrentEscrimeur().getCartes()[j].getDistance())) {
						indiceDefausse = ACTION_DEFAUSSER(getCurrentEscrimeur(), getCurrentEscrimeur().getCartes()[j]);
						nbcartes--;// si on defausse la carte on reste au meme indice mais on ira voir une carte
									// moins loin car ACTION_DEFAUSSER() decalle les cartes vers la gauche
						c.addCarteJouee(indiceDefausse);
						attaque--;
					} else {
						j++;
					}
				}
				historique.ajouterCoup(c);
				if (!rejoueCoupAnnule) {
					historique.viderCoupsAnnules();
				}
				if (attaque != 0) {
					System.err.println("le defenseur n'a pas tout defendu");
					historique.annulerCoup();
					return false;
				}
				modifieVue(Action.ACTUALISER_PLATEAU);
				modifieVue(Action.ACTUALISER_ESCRIMEUR);
				return true;
			
			default : 
				System.err.println("coup inconnu");
				return false;
			}
	}

	public void afficherEtatJeu() {
		System.out.println("position gaucher : " + getPlateau().getPosition(EGAUCHER));
		System.out.print("jeu gaucher : ");
		for (int i = 0; i < escrimeurs[0].getCartes().length; i++) {
			if (escrimeurs[0].getCarte(i) == null) {
				System.out.print("N ");
			} else {
				System.out.print(escrimeurs[0].getCarte(i).getDistance() + " ");
			}
		}
		System.out.println("");
		System.out.println("position droitier : " + getPlateau().getPosition(EDROITIER));
		System.out.print("jeu droitier : ");
		for (int i = 0; i < escrimeurs[1].getCartes().length; i++) {
			if (escrimeurs[1].getCarte(i) == null) {
				System.out.print("N ");
			} else {
				System.out.print(escrimeurs[1].getCarte(i).getDistance() + " ");
			}
		}
		System.out.println("\n");
	}
	
	/**
	 * renvoie les cases sur lesquelles le joueur peut cliquer
	 * si il clique sur lui le coup correspondant est une defense, 
	 * si il clique sur l'adversaire le coup correspondant est une attaque (directe ou indirecte, voir suivant le dernier coup de l'historique),
	 * si il clique sur une autre case le coup correspondant est AVANCER, RECULER ou ESQUIVER
	 * -1 correspond au bouton passer son tour
	 * @return un HashSet correspondant à toutes les cases sur lesquelles le joueur dont c'est le tour peut cliquer
	 */
	@SuppressWarnings("unused")
	public HashSet<Integer> casesJouables(){
		Coup dernierCoup = historique.voirDernierCoup();
		int atk;
		int code;
		if (dernierCoup == null) {
			atk = getCurrentEscrimeur().getNbCartes();
			code = 0x111;
		} else {
			atk = dernierCoup.getCartes().length;
			switch(dernierCoup.getAction()) {
				case Coup.ATTAQUEDIRECTE :
					code =  0x1000; // defendre
					break;
				case Coup.ATTAQUEINDIRECTE :
					code = 0x1010; //defendre ou esquiver
					break;
				case Coup.AVANCER :
					if (dernierCoup.getEscrimeur() == getCurrentEscrimeur()) {
						code = modeSimple ? 0x10000 : 0x10100; // passer/passer ou attaquer
					} else {
						code = 0x111; // avancer ou reculer ou attaquer
					}
					break;
				case Coup.RECULER :
					code = dernierCoup.getEscrimeur() == getCurrentEscrimeur() ? 0x10000 : 0x111;
					break;
				case Coup.ESQUIVER :
				case Coup.PARER :
					code = 0x111;// avancer ou reculer ou attaquer
					break;
				default :
					System.err.println("coup inconnu");
					code = 0x0;
					break;
			}
		}
		return plateau.casesJouables(getCurrentEscrimeur(), code, atk);
	}


	public void nouvelleManche() {
		modifieVue(Action.ANIMATION_FIN_MANCHE);
		while(!deckDefausse.deckVide()) {
			deckPioche.reposerCarte(deckDefausse.reprendreDerniereCarte());
		}		
		int nbCartesMain = getCurrentEscrimeur().getNbCartes();
		for (int i = 0; i < nbCartesMain; i++) {
			for (int j = 0; j < 2; j++) {
				Carte currentCard = escrimeurs[j].getCarte(i);
				if (currentCard != null) {
					deckPioche.reposerCarte(currentCard);
				}
				escrimeurs[j].getCartes()[i] = null;
			}
		}
		deckPioche.melanger();
		piocher(getEscrimeurDroitier());
		piocher(getEscrimeurGaucher());
		try {
			plateau.setPosition(1, Escrimeur.GAUCHER);
			plateau.setPosition(plateau.getNbCase(), Escrimeur.DROITIER);
		} catch (IncorrectPlateauException e) {
			System.err.println(e.getMessage());
		};
		indicePremierJoueur = (indicePremierJoueur + 1) % 2;
		indiceCurrentEscrimeur = indicePremierJoueur;
		historique.vider();
		//mettre a jour toute la vue (mains, pioche, defausse, plateau, manches gagn�es)
		modifieVue(Action.ACTUALISER_JEU);
		System.out.println("Jeu actualiser");
	}
	
	public void modifieVue(Action action) {
		listeActions.add(action);
		demarreActionSuivante();
	}
	
	public void demarreActionSuivante() {
		if (!actionEnCours && !listeActions.isEmpty()) {
			actionEnCours = true;
			metAJour();
		}
	}
	
	public void actionTerminer() {
		actionEnCours = false;
		if (!listeActions.isEmpty()) {
			listeActions.removeFirst();
		}
		demarreActionSuivante();
	}
	
	public Action getActionCourante() {
		return listeActions.peek();
	}
	
	public void setIndiceCurrentEscrimeur(int i) {
		if (i >= 0 && i < 2) {
			indiceCurrentEscrimeur = i;
		}
	}
	
	public Boolean getPeutPasserTour() {
		return peutPasserTour;
	}
	
	public Escrimeur[] getEscrimeurs() {
		return escrimeurs;
	}
	
	public int getNbManchesPourVictoire() {
		return nbManchesPourVictoire;
	}
	
	public int getIndiceWinnerManche() {
		return lastWinner;
	}
	
	public void setIndiceWinnerManche(int lastWinner ) {
		this.lastWinner = lastWinner;
	}
}
