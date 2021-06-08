package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.Timer;

import Patterns.Observable;
import controller.ControlerJeu;
import model.Jeu.Action;

public class Jeu extends Observable {
	
	public enum Action {
		ACTUALISER_JEU,
		ACTUALISER_DECK,
		ACTUALISER_PLATEAU,
		ACTUALISER_PLATEAU_SANS_CASE,
		ACTUALISER_PLATEAU_MEME_MODE,
		ACTUALISER_ESCRIMEUR,
		ACTUALISER_ESCRIMEUR_DROITIER,
		ACTUALISER_ESCRIMEUR_GAUCHER,
		ACTUALISER_ESCRIMEUR_MEME_MODE,
		ANIMATION_PIOCHER,
		ANIMATION_DEFAUSSER,
		ANIMATION_DEBUT_MANCHE,
		ANIMATION_FIN_MANCHE,
		ANIMATION_LANCER,
		ANIMATION_DEPLACER_ESCRIMEUR,
		ANIMATION_CHANGER_TOUR
	}
	
	protected Boolean modeSimple; 
	public Boolean peutPasserTour;
	
	protected Plateau plateau;
	protected DeckPioche deckPioche;
	protected DeckDefausse deckDefausse;
	protected int indiceCurrentEscrimeur;

	protected int indicePremierJoueur;
	protected Boolean animationAutoriser;
	
	protected Escrimeur[] escrimeurs;
	protected int winner;
	protected Historique historique;
	protected boolean dernierTour;
	protected int nbManchesPourVictoire;
	
	protected LinkedList<Action> listeActions;
	protected LinkedList<Carte[]> cartesShowEscrimeurs[];
	protected LinkedList<Integer> listeIndiceEscrimeurDefausseCarte;
	protected LinkedList<Integer> listeIndiceEscrimeurPiocheCarte;
	protected LinkedList<Integer[]> carteShowDeck;
	protected LinkedList<LinkedList<Integer>> listeIndiceDefausseRecemment[];
	protected LinkedList<LinkedList<Integer>> listeDistancesDefausseRecemment[];
	protected LinkedList<Carte[]> listeEtatMainDefausse[];
	protected LinkedList<LinkedList<Integer>> listeIndicesPiocheRecemment[];
	protected LinkedList<LinkedList<Integer>> listeDistancesPiocheRecemment[];
	protected Boolean actionEnCours;
	protected Boolean showGraphique;
	
	protected int[] positionsDeparts;
	
	public static final int EGAUCHER = 0;
	public static final int EDROITIER = 1;
	public static final int EGALITE = 2;
	public static final int NONE = 3;

	protected int lastWinner;
	private boolean showAllCartes;
	private int idJeu;
	
	protected HashMap<TypeEscrimeur, IA> listIA;

	protected Jeu() {}
	
	public Jeu(Boolean modeSimple, Plateau plateau, DeckPioche deckPioche, DeckDefausse deckDefausse, int nbManchesPourVictoire, int indiceCurrentEscrimeur, Escrimeur gaucher, Escrimeur droitier, int[] positionsDeparts, boolean animationAutoriser) {
		super();
		this.indiceCurrentEscrimeur = 0;
		setHistorique(new Historique(this));
		init(modeSimple, plateau, deckPioche, deckDefausse, nbManchesPourVictoire, gaucher, droitier, positionsDeparts, animationAutoriser);
		indicePremierJoueur = indiceCurrentEscrimeur;
		peutPasserTour = false;
		idJeu = -1;
	}

	public Jeu(Boolean modeSimple, Plateau plateau, DeckPioche deckPioche, DeckDefausse deckDefausse, int nbManchesPourVictoire, int indiceCurrentEscrimeur, Escrimeur gaucher, Escrimeur droitier, int[] positionsDeparts, int indicePremierJoueur, Historique historique, boolean animationAutoriser,int idJeu) {
		super();
		this.indiceCurrentEscrimeur = indiceCurrentEscrimeur;
		if(historique != null) {
			setHistorique(historique);
		} else {
			setHistorique(new Historique(this));
		}
		init(modeSimple, plateau, deckPioche, deckDefausse, nbManchesPourVictoire, gaucher, droitier, positionsDeparts, animationAutoriser);
		this.indicePremierJoueur = indicePremierJoueur;
		peutPasserTour = false;
		this.idJeu = idJeu;
	}
	
	@SuppressWarnings("unchecked")
	protected void init(Boolean modeSimple, Plateau plateau, DeckPioche deckPioche, DeckDefausse deckDefausse, int nbManchesPourVictoire, Escrimeur gaucher, Escrimeur droitier, int[] positionsDeparts, boolean animationAutoriser) {
		this.modeSimple = modeSimple;
		this.showGraphique = true;
		this.nbManchesPourVictoire = nbManchesPourVictoire;
		this.plateau = plateau;
		this.deckPioche = deckPioche;
		this.deckDefausse = deckDefausse;
		this.animationAutoriser = animationAutoriser;
		this.escrimeurs = new Escrimeur[2];
		this.escrimeurs[0] = gaucher;
		this.escrimeurs[1] = droitier;
		this.positionsDeparts = positionsDeparts;
		this.listeActions = new LinkedList<>();
		this.listeIndiceEscrimeurDefausseCarte = new LinkedList<>();
		this.listeIndiceEscrimeurPiocheCarte = new LinkedList<>();
		this.listeIndiceDefausseRecemment = new LinkedList[] { new LinkedList<>(), new LinkedList<>()};
		this.listeIndicesPiocheRecemment = new LinkedList[] { new LinkedList<>(), new LinkedList<>()};
		this.listeDistancesDefausseRecemment = new LinkedList[] { new LinkedList<>(), new LinkedList<>()};
		this.listeDistancesPiocheRecemment = new LinkedList[] { new LinkedList<>(), new LinkedList<>()};
		this.cartesShowEscrimeurs = new LinkedList[] {new LinkedList<>(), new LinkedList<>()};
		this.listeEtatMainDefausse = new LinkedList[] {new LinkedList<>(), new LinkedList<>()};
		this.carteShowDeck = new LinkedList<>();
		this.lastWinner = NONE;
		this.actionEnCours = false;
		this.dernierTour = false;
		this.winner = NONE;
		this.showAllCartes = false;
		listIA = new HashMap<TypeEscrimeur, IA>();
		listIA.put(TypeEscrimeur.IA_FACILE, new IA_Facile(this));
		listIA.put(TypeEscrimeur.IA_MOYENNE, new IA_Moyenne(this));
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
	
	public int getIdJeu() {
		return idJeu;
	}

	public void setIdJeu(int idJeu) {
		this.idJeu = idJeu;
	}
	
	public int getIndiceCurrentEscrimeur() {
		return indiceCurrentEscrimeur;
	}
	
	public int getIndicePremierJoueur() {
		return indicePremierJoueur;
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
		e.prepareAjouteCartes();
		while (e.manqueCarte() && !deckPioche.deckVide()) {
			e.ajouterCarte(deckPioche.piocher());
		}
		listeIndiceEscrimeurPiocheCarte.add(e.getIndice());
		System.out.println("On doit animer pour l'indice : " + e.getIndice());
		listeIndicesPiocheRecemment[e.getIndice()].add(e.getIndicesCartesAjouterRecemment());
		listeDistancesPiocheRecemment[e.getIndice()].add(e.getDistancesCartesAjouterRecemment());
		modifieVueAnimation(Action.ANIMATION_PIOCHER);
		modifieVue(e.getIndice() == Escrimeur.GAUCHER ? Action.ACTUALISER_ESCRIMEUR_GAUCHER : Action.ACTUALISER_ESCRIMEUR_DROITIER);
		modifieVue(Action.ACTUALISER_DECK);
	}

	public int changerTour() {
		piocher(getCurrentEscrimeur());
		modifieVueAnimation(Action.ANIMATION_CHANGER_TOUR);
		indiceCurrentEscrimeur = (indiceCurrentEscrimeur + 1) % 2;
		peutPasserTour = false;
		if (getCurrentEscrimeur().getType() != TypeEscrimeur.HUMAIN) {
			System.out.println("IA doit jouer");
			modifieVue(Action.ACTUALISER_PLATEAU_SANS_CASE);
			modifieVue(Action.ACTUALISER_DECK);
			modifieVue(Action.ACTUALISER_ESCRIMEUR_MEME_MODE);
		} else {
			System.out.println("Humain doit jouer");
			modifieVue(Action.ACTUALISER_JEU);
		}
		return 1;
	} 

	/**
	 * retire une carte de la main de l'escrimeur, l'ajoute à la défausse et serre
	 * les cartes de l'escrimeur à gauche.
	 * 
	 * @param e, escrimeur qui doit defausser la carte c
	 * @param c, carte que l'escrimeur e doit defausser
	 * @return si -1 -> echec, sinon indice de la carte qui a été défaussée
	 */
	public int defausser(Escrimeur e, Carte c, boolean actualiser) {
		if (actualiser) {
			e.prepareSupprimeCartes();
		}
		
		int res = e.supprimerCarte(c);
		if (res == -1) {
			return res;
		}
		deckDefausse.defausser(c);
		
		if (actualiser) {
			animerDefausser(e.getIndice());
		}
		return res;
	}
	
	protected void animerDefausser(int indice) {
		listeIndiceEscrimeurDefausseCarte.add(indice);
		listeIndiceDefausseRecemment[indice].add(escrimeurs[indice].getIndicesCartesSupprimerRecemment());
		listeDistancesDefausseRecemment[indice].add(escrimeurs[indice].getDistancesCartesSupprimerRecemment());
		listeEtatMainDefausse[indice].add(escrimeurs[indice].popMainCourante());
		modifieVueAnimation(Action.ANIMATION_DEFAUSSER);
		modifieVue(Action.ACTUALISER_DECK);
		modifieVue(indice == Escrimeur.GAUCHER ? Action.ACTUALISER_ESCRIMEUR_GAUCHER : Action.ACTUALISER_ESCRIMEUR_DROITIER);
	}

	public boolean jouer(Coup c, boolean rejoueCoupAnnule) {
		setCaseAide(-1);
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
		Escrimeur escrimeur = c.getEscrimeur();
		switch(c.getAction()) {
			case Coup.AVANCER : 
			case Coup.RECULER :
			case Coup.ESQUIVER :
				int sens = ((c.getAction() % 2) == 0 ? 1 : -1);
	
				if (plateau.deplacerEscrimeur(escrimeur, c.getCartes()[0].getDistance() * sens)) {
					modifieVueAnimation(Action.ANIMATION_DEPLACER_ESCRIMEUR);
					modifieVue(Action.ACTUALISER_PLATEAU_SANS_CASE);
					indiceDefausse = defausser(escrimeur, c.getCartes()[0], true);
					if (indiceDefausse != -1) {
						c.addCarteJouee(indiceDefausse);
						historique.ajouterCoup(c);
						if (!rejoueCoupAnnule) {
							historique.viderCoupsAnnules();
						}
						if(c.getAction() != Coup.ESQUIVER) {
							peutPasserTour = true;
						}
						return true;
					} else {
						// le coup ne va pas etre joué, on ramene donc le joueur a sa position initiale
						plateau.deplacerEscrimeur(escrimeur, (c.getCartes()[0].getDistance()) * (-sens));
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
				if (!plateau.escrimeurPeutAttaquer(escrimeur, valeurAttaque)) {
					System.err.println("impossible de jouer un coup d'attaque car les distances ne correspondent pas");
					return false;
				}
				int i = 0;
				boolean defausseOk = true;
				if (defausseOk) {
					escrimeur.prepareSupprimeCartes(); // Pour la vue
				}
				while (i < c.getCartes().length && c.getCartes()[i].getDistance() == valeurAttaque && defausseOk) {
					indiceDefausse = defausser(escrimeur, c.getCartes()[i], false);
					if (indiceDefausse == -1) {
						defausseOk = false;
					} else {
						c.addCarteJouee(indiceDefausse);
						i++;
					}
				}
				animerDefausser(escrimeur.getIndice());
				if (i != c.getCartes().length) {
					System.err.println("les cartes de l'attaque ne sont pas toutes de même valeur ou l'attaquant ne les a pas toutes en main");
					c.remettreCartesDansLordre();
					while (i != 0) {
						if (!escrimeur.ajouterCarteAIndiceVide(deckDefausse.reprendreDerniereCarte(),
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
				return true;
				
			case Coup.PARER :
				int attaque = historique.voirDernierCoup().getCartes().length;
				int j = 0;
				int nbcartes = getCurrentEscrimeur().getNbCartes();
				escrimeur.prepareSupprimeCartes();
				while (j < nbcartes && attaque > 0) {
					if (plateau.escrimeurPeutAttaquer(getCurrentEscrimeur(), getCurrentEscrimeur().getCartes()[j].getDistance())) {
						indiceDefausse = defausser(getCurrentEscrimeur(), getCurrentEscrimeur().getCartes()[j], false);
						nbcartes--;// si on defausse la carte on reste au meme indice mais on ira voir une carte
									// moins loin car defausser() decalle les cartes vers la gauche
						c.addCarteJouee(indiceDefausse);
						attaque--;
					} else {
						j++;
					}
				}
				animerDefausser(escrimeur.getIndice());
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
		resetManche();
		indicePremierJoueur = (indicePremierJoueur + 1) % 2;
		indiceCurrentEscrimeur = indicePremierJoueur;
		modifieVue(Action.ACTUALISER_JEU);
		piocher(escrimeurs[indiceCurrentEscrimeur]);
		piocher(escrimeurs[(indiceCurrentEscrimeur + 1) % 2]);
		modifieVue(Action.ACTUALISER_JEU);
	}
	
	public Integer[] popLastCarteDeck() {
		return carteShowDeck.pop();
	}
	
	public void modifieVue(Action action) {
		if (showGraphique) {
			switch (action) {
			case ACTUALISER_JEU:
			case ACTUALISER_ESCRIMEUR:
			case ACTUALISER_ESCRIMEUR_GAUCHER:
				cartesShowEscrimeurs[Escrimeur.GAUCHER].add(escrimeurs[Escrimeur.GAUCHER].getCartes().clone());
				if (action == Action.ACTUALISER_ESCRIMEUR_GAUCHER) {
					break;
				}
			case ACTUALISER_ESCRIMEUR_DROITIER:
				cartesShowEscrimeurs[Escrimeur.DROITIER].add(escrimeurs[Escrimeur.DROITIER].getCartes().clone());
				if (action == Action.ACTUALISER_ESCRIMEUR_DROITIER) {
					break;
				}
			case ACTUALISER_DECK:
				int distanceCarteVisible = deckDefausse.deckVide() ? -1 : deckDefausse.consulterCarteVisible().getDistance();
				carteShowDeck.add(new Integer[] {deckPioche.nbCartes(), deckDefausse.nbCartes(), distanceCarteVisible});
			default:
				break;
		}
		listeActions.add(action);
		demarreActionSuivante();
		}
		
	}
	
	public void modifieVueAnimation(Action action) {
		if (animationAutoriser && showGraphique) {
			Iterator<Action> it = listeActions.iterator();
			int index = 0;
			while (it.hasNext()) {
				Action actCourant = it.next();
				if (!actCourant.name().contains("ANIMATION") || (actCourant == Action.ANIMATION_LANCER && index > 0)) {
					break;
				}
				index++;
			}
	
			listeActions.add(index, action);
			modifieVue(Action.ANIMATION_LANCER);
		}
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
	
	public void nouvellePartie() {
		setCaseAide(-1);
		escrimeurs[Escrimeur.GAUCHER].resetMancheGagner();
		escrimeurs[Escrimeur.DROITIER].resetMancheGagner();
		//Droitier car il seront inverser dans nouvelle manche
		indicePremierJoueur = Escrimeur.DROITIER;
		indiceCurrentEscrimeur = Escrimeur.DROITIER;
		nouvelleManche();
	}

	public int[] getPositionsDepart() {
		return positionsDeparts;
	}
	
	public int popIndiceEscrimeurPiocherCarte() {
		return listeIndiceEscrimeurPiocheCarte.pop();
	}
	
	public int popIndiceEscrimeurDefausserCarte() {
		return listeIndiceEscrimeurDefausseCarte.pop();
	}
	
	public Carte[] popShowCarte(int indice) {
		return cartesShowEscrimeurs[indice].pop(); 
	}
	
	public void toggleAnimationAutoriser() {
		animationAutoriser = !animationAutoriser;
	}
	
	public Boolean getAnimationAutoriser() {
		return animationAutoriser;
	}
	
	public void resetAction() {
		listeActions.clear();
		actionEnCours = false;
	}
	
	public void resetManche() {
		setDernierTour(false);
		cartesShowEscrimeurs[Escrimeur.GAUCHER].clear();
		cartesShowEscrimeurs[Escrimeur.DROITIER].clear();
		listeIndiceEscrimeurDefausseCarte.clear();
		listeIndiceEscrimeurPiocheCarte.clear();
		this.listeDistancesDefausseRecemment[Escrimeur.GAUCHER].clear();
		this.listeDistancesDefausseRecemment[Escrimeur.DROITIER].clear();
		this.listeDistancesPiocheRecemment[Escrimeur.GAUCHER].clear();
		this.listeEtatMainDefausse[Escrimeur.GAUCHER].clear();
		this.listeEtatMainDefausse[Escrimeur.DROITIER].clear();
		this.listeDistancesPiocheRecemment[Escrimeur.DROITIER].clear();
		this.listeIndiceDefausseRecemment[Escrimeur.GAUCHER].clear();
		this.listeIndiceDefausseRecemment[Escrimeur.DROITIER].clear();
		this.listeDistancesPiocheRecemment[Escrimeur.GAUCHER].clear();
		this.listeDistancesPiocheRecemment[Escrimeur.DROITIER].clear();
		escrimeurs[Escrimeur.DROITIER].clear();
		escrimeurs[Escrimeur.GAUCHER].clear();

		historique.vider();
		// Reset deck
		while(!deckDefausse.deckVide()) {
			deckPioche.reposerCarte(deckDefausse.reprendreDerniereCarte());
		}
		deckPioche.melanger();
		
		// Reset Mains Escrimeurs
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
		try {
			plateau.setPosition(positionsDeparts[Escrimeur.GAUCHER], Escrimeur.GAUCHER);
			plateau.setPosition(positionsDeparts[Escrimeur.DROITIER], Escrimeur.DROITIER);
		} catch (IncorrectPlateauException e) {
			System.err.println(e.getMessage());
		};
	}

	public Jeu copySimple() {
		Jeu jeu = new Jeu();
		jeu.escrimeurs = new Escrimeur[2];
		jeu.escrimeurs[Escrimeur.GAUCHER] = escrimeurs[Escrimeur.GAUCHER].copySimple();
		jeu.escrimeurs[Escrimeur.DROITIER] = escrimeurs[Escrimeur.DROITIER].copySimple();
		jeu.plateau = plateau.copySimple();
		jeu.deckDefausse = deckDefausse.copySimple();
		jeu.deckPioche = deckPioche.copySimple();
		jeu.indiceCurrentEscrimeur = indiceCurrentEscrimeur;
		jeu.historique = historique.copySimple(jeu);
		return jeu;
	}

	public void setDeckPioche(DeckPioche deckPioche) {
		this.deckPioche = deckPioche;
	}

	public void echangeEscrimeurs() {
		String sauvG = escrimeurs[Escrimeur.GAUCHER].getNom();
		TypeEscrimeur sauvType = escrimeurs[Escrimeur.GAUCHER].getType();
		escrimeurs[Escrimeur.GAUCHER].setNom(escrimeurs[Escrimeur.DROITIER].getNom());
		escrimeurs[Escrimeur.GAUCHER].setType(escrimeurs[Escrimeur.DROITIER].getType());
		escrimeurs[Escrimeur.DROITIER].setNom(sauvG);
		escrimeurs[Escrimeur.DROITIER].setType(sauvType);
	}
	
	public void toggleShowAllCartes() {
		showAllCartes = !showAllCartes;
		modifieVue(Action.ACTUALISER_ESCRIMEUR);
	}
	
	public boolean getShowAllCartes() {
		return showAllCartes;
	}
	
	public void setCaseAide(int numCase) {
		if (numCase != plateau.getCaseAide()) {
			plateau.setCaseAide(numCase);
			modifieVue(Action.ACTUALISER_PLATEAU_MEME_MODE);
		}
	}

	public boolean aideEstMontrer() {
		return plateau.getCaseAide() < 1;
	}
	
	public void setShowGraphique(Boolean b) {
		this.showGraphique = b;
	}

	public Integer popListeIndiceEscrimeurDefausseCarte() {
		return listeIndiceEscrimeurDefausseCarte.pop();
	}

	public Integer popListeIndiceEscrimeurPiocheCarte() {
		return listeIndiceEscrimeurPiocheCarte.pop();
	}

	public LinkedList<Integer> popListeIndiceDefausseRecemment(int indice) {
		return listeIndiceDefausseRecemment[indice].pop();
	}

	public LinkedList<Integer> popListeDistancesDefausseRecemment(int indice) {
		return listeDistancesDefausseRecemment[indice].pop();
	}

	public LinkedList<Integer> popListeIndicesPiocheRecemment(int indice) {
		return listeIndicesPiocheRecemment[indice].pop();
	}

	public LinkedList<Integer> popListeDistancesPiocheRecemment(int indice) {
		return listeDistancesPiocheRecemment[indice].pop();
	}
	
	public Carte[] popEtatMainDefausse(int indice) {
		return listeEtatMainDefausse[indice].pop();
	}
}
