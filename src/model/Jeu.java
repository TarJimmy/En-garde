package model;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

import Patterns.Observable;
import controller.ControlerJeu;

public class Jeu extends Observable {
	private Boolean modeSimple; 
	private Plateau plateau;
	private DeckPioche deckPioche;
	private DeckDefausse deckDefausse;
	private int indiceCurrentEscrimeur;
	private Escrimeur[] escrimeurs;
	private int winner;
	private Historique historique;

	public static final int EGAUCHER = 0;
	public static final int EDROITIER = 1;
	public static final int NONE = 2;
	public static final int EGALITE = 3;

	public Jeu(Boolean modeSimple, Plateau plateau, DeckPioche deckPioche, DeckDefausse deckDefausse,
			Escrimeur eGaucher, Escrimeur eDroitier) {
		super();
		this.modeSimple = modeSimple;
		this.plateau = plateau;
		this.deckPioche = deckPioche;
		this.deckDefausse = deckDefausse;
		this.indiceCurrentEscrimeur = 0;
		this.escrimeurs = new Escrimeur[2];
		this.escrimeurs[0] = eGaucher;
		this.escrimeurs[1] = eDroitier;
		this.winner = 2;
	}

	public Jeu(Boolean modeSimple, Plateau plateau, DeckPioche deckPioche, DeckDefausse deckDefausse,
			Boolean isTourGaucher) {
		super();
		this.modeSimple = modeSimple;
		this.plateau = plateau;
		this.deckPioche = deckPioche;
		this.deckDefausse = deckDefausse;
		this.indiceCurrentEscrimeur = 0;
		this.winner = 2;
	}

	public void setHistorique(Historique h) {
		this.historique = h;
	}

	public Boolean getModeSimple() {
		return modeSimple;
	}

	public Escrimeur getCurrentEscrimeur() {
		return this.escrimeurs[this.indiceCurrentEscrimeur];
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
		}
	}

	/*
	 * return 1 si la partie doit se terminer ?? (a passer en public void et checker
	 * les trucs dans le controller je pense)
	 */
	public int changerTour() {
		piocher(getCurrentEscrimeur());
		indiceCurrentEscrimeur = (indiceCurrentEscrimeur + 1) % 2;
		if (deckPioche.deckVide()) {
			System.out.println("piocheVide");
			return 1;
		}
		if (!plateau.peutJouer(getCurrentEscrimeur())) {
			winner = ((indiceCurrentEscrimeur + 1) % 2);
			return 1;
		}
		return 0;
	}

	/**
	 * retire une carte de la main de l'escrimeur, l'ajoute à la défausse et serre
	 * les cartes de l'escrimeur à gauche.
	 * 
	 * @param e, escrimeur qui doit defausser la carte c
	 * @param c, carte que l'escrimeur e doit defausser
	 * @return si -1 -> echec, sinon indice de la carte qui a été défaussée
	 */
	public int defausser(Escrimeur e, Carte c) {
		int res = e.supprimerCarte(c);
		if (res == -1) {
			return res;
		}
		deckDefausse.defausser(c);
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
				indiceDefausse = defausser(c.getEscrimeur(), c.getCartes()[0]);
				if (indiceDefausse != -1) {
					c.addCarteJouee(indiceDefausse);
					historique.ajouterCoup(c);
					if (!rejoueCoupAnnule) {
						historique.viderCoupsAnnules();
					}
					metAJour();
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
			if (!plateau.attaquerEscrimeur(c.getEscrimeur(), valeurAttaque)) {
				System.err.println("impossible de jouer un coup d'attaque car les distances ne correspondent pas");
				return false;
			}
			int i = 0;
			boolean defausseOk = true;
			while (i < c.getCartes().length && c.getCartes()[i].getDistance() == valeurAttaque && defausseOk) {
				indiceDefausse = defausser(c.getEscrimeur(), c.getCartes()[i]);
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
			Coup passe = new Coup(getCurrentEscrimeur(),new Carte[0], Coup.PASSERTOUR);
			jouer(passe,false);
			metAJour();
			return true;
			
		case Coup.PARER :
			int attaque = historique.voirDernierCoup().getCartes().length;
			int j = 0;
			int nbcartes = getCurrentEscrimeur().getNbCartes();
			while (j < nbcartes && attaque > 0) {
				if (plateau.attaquerEscrimeur(getCurrentEscrimeur(),
						getCurrentEscrimeur().getCartes()[j].getDistance())) {
					indiceDefausse = defausser(getCurrentEscrimeur(), getCurrentEscrimeur().getCartes()[j]);
					nbcartes--;// si on defausse la carte on reste au meme indice mais on ira voir une carte
								// moins loin car defausser() decalle les cartes vers la gauche
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
			metAJour();
			return true;
			
		case Coup.PASSERTOUR :
			changerTour();
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
	public HashSet<Integer> casesJouables(){
		//cases jouables doit dependre du dernier coup effectué, modifier plateau.casesJouables pour qu'il retourne que les coups acceptés
		Coup dernierCoup = historique.voirDernierCoup();
		switch(dernierCoup.getAction()) {
		case Coup.ATTAQUEDIRECTE :
			return plateau.casesJouables(getCurrentEscrimeur(), 0x1000);//defendre
		case Coup.ATTAQUEINDIRECTE :
			if(modeSimple) {
				return plateau.casesJouables(getCurrentEscrimeur(), 0x1000);//defendre
			}else {
				return plateau.casesJouables(getCurrentEscrimeur(), 0x1010);//defendre ou esquiver
			}
		case Coup.AVANCER :
			if(dernierCoup.getEscrimeur() == getCurrentEscrimeur() && modeSimple) {
				return plateau.casesJouables(getCurrentEscrimeur(), 0x10000);//passer
			}else if(dernierCoup.getEscrimeur() == getCurrentEscrimeur()) {
				return plateau.casesJouables(getCurrentEscrimeur(), 0x10100);//attaquer ou passer
			}else {
				return plateau.casesJouables(getCurrentEscrimeur(), 0x111);//avancer ou reculer ou attaquer
			}
		case Coup.RECULER :
			if(dernierCoup.getEscrimeur() == getCurrentEscrimeur()) {
				return plateau.casesJouables(getCurrentEscrimeur(), 0x10000);//passer
			}else {
				return plateau.casesJouables(getCurrentEscrimeur(), 0x111);//avancer ou reculer ou attaquer
			}
		case Coup.ESQUIVER :
		case Coup.PARER :
			return plateau.casesJouables(getCurrentEscrimeur(), 0x111);//avancer ou reculer ou attaquer
		default :
			System.err.println("coup inconnu");
			return plateau.casesJouables(getCurrentEscrimeur(), 0x0);
		}
	}

	public static void main(String[] args) {
		Carte cartes[] = new Carte[25];

		for (int i = 0; i < cartes.length; i++) {
			try {
				cartes[i] = new Carte((i % 5) + 1);
			} catch (IncorrectCarteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		DeckPioche deckPioche = new DeckPioche(cartes);
		deckPioche.melanger();
		DeckDefausse deckDefausse = new DeckDefausse();
		Escrimeur eGaucher = new Escrimeur("Gaucher", TypeEscrimeur.HUMAIN, Escrimeur.GAUCHER, 5);
		Escrimeur eDroitier = new Escrimeur("Droitier", TypeEscrimeur.HUMAIN, Escrimeur.DROITIER, 5);
		Jeu jeu = null;
		try {
			jeu = new Jeu(true, new Plateau(23), deckPioche, deckDefausse, eGaucher, eDroitier);
		} catch (IncorrectPlateauException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jeu.setHistorique(new Historique(jeu));

		jeu.piocher(eDroitier);
		jeu.piocher(eGaucher);

		Scanner myObj = new Scanner(System.in);

		System.out.println("0 = avancer, 1 = reculer, 2 = attaquer, 3 = annuler coup");
		System.out.println("4 = rejouer coup, 5 = taille de la pioche?, 6 = derniereCartePioche");

		jeu.afficherEtatJeu();
		while (jeu.winner == 2) {
			System.out.println("action collée à indice de la ou les cartes a jouer : ");
			String caj = myObj.nextLine();
			if (Integer.parseInt(String.valueOf(caj.charAt(0))) == 3) {
				jeu.historique.annulerCoup();
			} else if (Integer.parseInt(String.valueOf(caj.charAt(0))) == 4) {
				jeu.historique.rejouerCoupAnnule();
			} else if (Integer.parseInt(String.valueOf(caj.charAt(0))) == 7) {
				jeu.afficherEtatJeu();
			} else if (Integer.parseInt(String.valueOf(caj.charAt(0))) == 5) {
				System.out.println(deckPioche.nbCartes());
			} else if (Integer.parseInt(String.valueOf(caj.charAt(0))) == 6) {
				System.out.println(deckDefausse.consulterCarteVisible().getDistance());
			} else {
				Carte[] cartesAJouer = new Carte[caj.length() - 1];
				for (int i = 1; i < caj.length(); i++) {
					cartesAJouer[i - 1] = jeu.getCurrentEscrimeur()
							.getCarte(Integer.parseInt(String.valueOf(caj.charAt(i))));
				}
				Coup c;
				c = new Coup(jeu.getCurrentEscrimeur(), cartesAJouer, Integer.parseInt(String.valueOf(caj.charAt(0))));
				jeu.jouer(c, false);
			}
			jeu.afficherEtatJeu();
		}
		if (jeu.winner == 0) {
			System.out.println("fin de manche, winner : gaucher");
		} else if (jeu.winner == 1) {
			System.out.println("fin de manche, winner : droitier");
		} else {
			System.out.println("fin de manche, egalité");
		}

	}
}
