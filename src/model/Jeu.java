package model;

import java.util.Scanner;
import java.util.Stack;

import Patterns.Observable;
import controller.ControlerJeu;

public class Jeu extends Observable {
	private Boolean modeSimple;
	private Plateau plateau;
	private DeckPioche deckPioche;
	private DeckDefausse deckDefausse;
	private Boolean isTourGaucher;
	private Escrimeur eGaucher;
	private Escrimeur eDroitier;
	private Stack<Coup> historique;
	private Stack<Coup> coupsAnnules;
	private int winner;
	boolean aPiocheDepuisDernierMouvement;
	
	public static final int NONE = 0;
	public static final int EGAUCHER = 1;
	public static final int EDROITIER = 2;
	public static final int EGALITE = 3;
	
	public static final int AVANCER = 0;
	public static final int RECULER = 1;
	public static final int ATTAQUEDIRECTE = 2;
	public static final int ATTAQUEINDIRECTE = 3;
	
	public Jeu(Boolean modeSimple, Plateau plateau, DeckPioche deckPioche, DeckDefausse deckDefausse, Escrimeur eGaucher, Escrimeur eDroitier) {
		super();
		this.historique = new Stack<Coup>();
		this.coupsAnnules = new Stack<Coup>();
		this.modeSimple = modeSimple;
		this.plateau = plateau;
		this.deckPioche = deckPioche;
		this.deckDefausse = deckDefausse;
		this.isTourGaucher = true;
		this.eGaucher = eGaucher;
		this.eDroitier = eDroitier;
		this.winner = 0;
		aPiocheDepuisDernierMouvement = true;
	}
	public Jeu(Boolean modeSimple, Plateau plateau, DeckPioche deckPioche, DeckDefausse deckDefausse, Boolean isTourGaucher) {
		super();
		this.modeSimple = modeSimple;
		this.plateau = plateau;
		this.deckPioche = deckPioche;
		this.deckDefausse = deckDefausse;
		this.isTourGaucher = isTourGaucher;
		this.winner = 0;
		aPiocheDepuisDernierMouvement = true;
	}
	public Boolean getModeSimple() {
		return modeSimple;
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
	public Boolean getIsTourGaucher() {
		return isTourGaucher;
	}
	public Escrimeur getEscrimeurGaucher() {
		return eGaucher;
	}
	public Escrimeur getEscrimeurDroitier() {
		return eDroitier;
	}
	
	public void piocher(Escrimeur e) {
		while(e.manqueCarte() && !deckPioche.deckVide()) {
			e.ajouterCarte(deckPioche.piocher());
		}	
	}
	
	public void finDeManche(int attaqueEnCours) {
		//cette fonction est appelée quand la manche doit se terminer car la pioche est vide
		//si le dernier joueur a avoir joué a terminé par une attaque on met le nombre de cartes utilisées en argument
		//sinon on met 0
		Escrimeur currentEscrimeur;
		if (isTourGaucher) {
			currentEscrimeur = eGaucher;
		}else {
			currentEscrimeur = eDroitier;
		}
		int distanceAttaque = plateau.getPositionDroitier() - plateau.getPositionGaucher();
		//1 - si une attaque est en cours
		if(attaqueEnCours != 0) {
			if(!historique.empty() &&  historique.peek().getEscrimeur() == currentEscrimeur) { //si c'est une attaque directe elle n'a pas été enregistrée dans l'historique tandis que si c'est une attaque indirecte le mouvement a été enregistré
				attaqueEnCours = defend(currentEscrimeur, attaqueEnCours, distanceAttaque, null);
				metAJour();
				if(attaqueEnCours > 0) {
					if(isTourGaucher) {
						winner = 1;
					}else {
						winner = 2;
					}
				}
			}else {//c'est donc une attaque indirecte car on a enregistré un mouvement de l'autre joueur avant l'attaque
				//currentEscrimeur defend ou esquive. s'il ne peut pas il a perdu
				//ef
			}
		}
		//2 - checker le joueur qui a le plus de carte permettant une attaque directe en main
		if (winner == 0) {
			int avantageGaucher = 0;
			for (int i = 0; i < eGaucher.getNbCartes(); i++) {
				if (eGaucher.getCarte(i) != null && eGaucher.getCarte(i).getDistance() == distanceAttaque) {
					avantageGaucher ++;
				}
			}
			for (int i = 0; i < eDroitier.getNbCartes(); i++) {
				if (eDroitier.getCarte(i) != null && eDroitier.getCarte(i).getDistance() == distanceAttaque) {
					avantageGaucher --;
				}
			}
			if(avantageGaucher > 0) {
				winner = 1;
			}else if(avantageGaucher < 0){
				winner = 2;
			}
			
			//3 - checker le joueur qui a le plus avancé
			if (winner == 0) {
				avantageGaucher = (plateau.getPositionGaucher()-1) - (plateau.getNbCase() - plateau.getPositionDroitier());
				if(avantageGaucher > 0) {
					winner = 1;
				}else if(avantageGaucher < 0){
					winner = 2;
				}
				
				//4 - si toujours aucun gagnant, match nul
				if (winner == 0) {
					winner = 3;
				}
			}
		}
	}
	
	public boolean peutJouer(Escrimeur e) {
		boolean res = false;
		int i = 0;
		while( i < e.getNbCartes() && res == false) {
			if (plateau.attaquerEscrimeur(e, e.getCartes()[i].getDistance()) || plateau.mouvementPossible(e, e.getCartes()[i].getDistance())) {
				res = true;
			}
			i++;
		}
		return res;
	}
	
	public int changerTour(int attaqueEnCours) { //return 1 si la manche se termine
		System.out.println("changement de tour");
		if (isTourGaucher) {
			piocher(eGaucher);
			System.out.println("le gaucher a pioché");
		}else {
			piocher(eDroitier);
			System.out.println("le droitier a pioché");
		}
		isTourGaucher=!isTourGaucher;
		aPiocheDepuisDernierMouvement = true;
		if(deckPioche.deckVide()) {
			System.out.println("piocheVide");
			finDeManche(attaqueEnCours);
			return 1;
		}
		if(isTourGaucher && !peutJouer(eGaucher)) {
			winner = 2;
			return 1;
		}
		if(!isTourGaucher && !peutJouer(eDroitier)) {
			winner = 1;
			return 1;
		}
		return 0;
	}
	
	public boolean effectuerDeplacement(Coup c, int sens, boolean rejoueCoupAnnule) {
		//sens = 1 pour avancer et -1 pour reculer
		
		if (plateau.deplacerEscrimeur(c.getEscrimeur(), c.getCartes()[0].getDistance() * sens)) {
			int indiceDefausse = defausser(c.getEscrimeur(), c.getCartes()[0]);
			if (indiceDefausse != -1) {
				c.addCarteJouee(indiceDefausse);
				historique.push(c);
				if  (!rejoueCoupAnnule) {
					while(!coupsAnnules.empty()) {
						coupsAnnules.pop();
					}
				}
				if (modeSimple){
					changerTour(0);
				}else {
					aPiocheDepuisDernierMouvement = false;
				}
				metAJour();
				return true;
			} else {
				//le coup ne va pas etre joué, on ramene donc le joueur a sa position initiale
				plateau.deplacerEscrimeur(c.getEscrimeur(), (c.getCartes()[0].getDistance())*(-sens));
				System.err.println("impossible de jouer un coup si le joueur n'a pas la carte en main");
				return false;
			}
		} else {
			System.err.println("impossible de jouer un coup si le deplacement n'est pas possible");
			return false;
		}
	}
	
	public int effectuerAttaque(Coup c, int valeurAttaque) {
		//return le nombre de cartes a defendre ou -1 si une erreur s'est produite
		if (!plateau.attaquerEscrimeur(c.getEscrimeur(), valeurAttaque)) {
			System.err.println("impossible de jouer un coup d'attaque car les distances ne correspondent pas");
			return 0;
		}
		
		int i = 0;
		//l'attaquant se defausse
		boolean defausseOk = true;
		int indiceDefausse;
		while(i < c.getCartes().length && c.getCartes()[i].getDistance() == valeurAttaque && defausseOk) {
			indiceDefausse = defausser(c.getEscrimeur(),c.getCartes()[i]);
			if(indiceDefausse == -1) {
				defausseOk = false;
			}else {
				c.addCarteJouee(indiceDefausse);
				i++;
			}
		}
		return i;
	}
	
	public int defend(Escrimeur e, int resAttaque, int valeurAttaque, Coup c) {
		//resAttaque est le nombre de cartes a defendre
		//valeurAttaque est la distance entre les 2 escrimeurs
		//le coup est la pour enregistrer les cartes jouées si on n'est pas en fin de partie
		int j = 0;
		int indiceDefausse;
		int nbcartes = e.getNbCartes();
		while (j < nbcartes && resAttaque > 0) {
			if(e.getCartes()[j].getDistance() == valeurAttaque) {
				indiceDefausse = defausser(e, e.getCartes()[j]);
				nbcartes --;//si on defausse la carte on reste au meme indice mais on ira voir une carte moins loin car defausser() decalle les cartes vers la gauche
				if(c != null) {
					c.addCarteJoueeDefense(indiceDefausse);
				}
				resAttaque--;
			}else {
				j++;
			}
		}
		return resAttaque;
	}
	
	public int defausser(Escrimeur e, Carte c) {
		int res = e.supprimerCarte(c);
		if(res == -1) {
			return res;
		}
		deckDefausse.defausser(c);
		return res;
	}
	
	public boolean jouer(Coup c, boolean rejoueCoupAnnule) {
		//cette fonction termine automatiquement un tour
		//appeler avec rejoueCoupAnnule a false sauf si on rejoue un coup annule
		//ce booleen sert a ne pas clean la pile des coups annulés si le coup vient de la fonction rejouerCoupAnnule()
		if(c.getCartes().length < 1) {
			System.err.println("impossible de jouer un coup sans carte");
			return false;
		}
		for(int i = 0; i < c.getCartes().length; i++) {
			if(c.getCartes()[i] == null) {
				System.err.println("erreur : le coup contient des emplacements de cartes nuls");
				return false;
			}
		}
		if((c.getEscrimeur() == eGaucher && !isTourGaucher) || (c.getEscrimeur() == eDroitier && isTourGaucher)) {
			System.err.println("impossible de jouer un coup si ce n'est pas au tour de ce joueur");
			return false;
		}
		int indiceDefausse;
		
		int valeurAttaque;//la distance en cas d'attaque
		int resAttaque;//le nombre de cartes a denfendre en cas d'attaque
		switch(c.getAction()) {
		case AVANCER :
			if(!historique.empty() && historique.peek().getEscrimeur() == c.getEscrimeur()) {
				System.err.println("le joueur essaye de se deplacer apres avoir deja joué");
				return false;
			}
			if(isTourGaucher) {
				System.out.println("le gaucher avance");
			}else {
				System.out.println("le droitier avance");
			}
			return effectuerDeplacement(c, 1, rejoueCoupAnnule);
		case RECULER :
			if(!historique.empty() &&  historique.peek().getEscrimeur() == c.getEscrimeur()) {
				System.err.println("le joueur essaye de se deplacer apres avoir deja joué");
				return false;
			}
			if(isTourGaucher) {
				System.out.println("le gaucher recule");
			}else {
				System.out.println("le droitier recule");
			}
			return effectuerDeplacement(c, -1, rejoueCoupAnnule);
		case ATTAQUEDIRECTE : 
			if(!historique.empty() &&  historique.peek().getEscrimeur() == c.getEscrimeur()) {
				System.err.println("impossible de lancer une attaque directe apres avoir deja effectué un coup ce tour");
				return false;
			}
			valeurAttaque = c.getCartes()[0].getDistance();
			resAttaque = effectuerAttaque(c, valeurAttaque);
			if (resAttaque == 0) {
				System.err.println("l'attaque a échouée");
				return false;
			}else {
				if(isTourGaucher) {
					System.out.println("le gaucher attaque");
				}else {
					System.out.println("le droitier attaque");
				}
			}
			//changement de tour------------------------------------------
			if (changerTour(resAttaque) == 1) { //fin de manche
				metAJour();
				System.out.println("partie terminée");
				return true;
			}
			//puis defense automatique--------------------------
			Escrimeur currentEscrimeur;
			if (isTourGaucher) {
				currentEscrimeur = eGaucher;
				System.out.println("le gaucher va defendre");
			}else {
				currentEscrimeur = eDroitier;
				System.out.println("le droitier va defendre");
			}			
			resAttaque = defend(currentEscrimeur, resAttaque, valeurAttaque, c);
			
			//si la defense n'est pas suffisante
			if(resAttaque > 0) {
				if(c.getEscrimeur() == eGaucher) {
					winner = 1;
				}else {
					winner = 2;
				}
				metAJour();
				return true;
			}
			
			historique.push(c);
			if(!rejoueCoupAnnule) {
				while(!coupsAnnules.empty()) {
					coupsAnnules.pop();
				}
			}
			metAJour();
			return true;
		
		case ATTAQUEINDIRECTE : 
			valeurAttaque = c.getCartes()[0].getDistance();
			resAttaque = effectuerAttaque(c, valeurAttaque);
			if (resAttaque == 0) {
				return false;
			}
			//changement de tour------------------------------------------
			if (changerTour(resAttaque) == 1) { //fin de manche
				metAJour();
				return true;
			}
			//puis defense ou esquive--------------------------
			//si esquive c.setAttaqueEsquivee(true);
			//jnnu
			
			
		 default:
			System.out.println("ACTION NON DEFINIE");
			return false;
		}
	}
	
	
	public boolean annulerCoup() {
		if (historique.empty()) {
			System.err.println("historique vide");
			return false;
		}else {
			System.out.println("coup annulé");
		}
		
		Coup c = historique.pop();
		
		switch(c.getAction()) {
		case AVANCER :
			System.out.println("aPiocheDepuisDernierMouvement = " + aPiocheDepuisDernierMouvement);
			if(aPiocheDepuisDernierMouvement) {
				//remettre la ou les cartes dans la pioche
				int cartesUtiliseesEnDefense = 0;
				if (!historique.empty()) {
					cartesUtiliseesEnDefense = historique.peek().getIndicesCartesJoueesDefense().size();
				}
				for( int i = 0; i < c.getIndicesCartesJouees().size() + cartesUtiliseesEnDefense; i++) {
					//remettre dans la pioche la carte d'index max - i
					deckPioche.reposerCarte( c.getEscrimeur().getCartes()[c.getEscrimeur().getNbCartes() - 1 -i]);
					c.getEscrimeur().supprimerCarte(c.getEscrimeur().getCartes()[c.getEscrimeur().getNbCartes() - 1 - i]);
				}
			}
			
			//redecaller les cartes
			for(int i = 0; i < c.getEscrimeur().getNbCartes(); i++) {
				if(c.getIndicesCartesJouees().contains(i)) {
					for (int j = c.getEscrimeur().getNbCartes() - 1; j > i; j--) {
						c.getEscrimeur().getCartes()[j] = c.getEscrimeur().getCartes()[j-1];
					}
					c.getEscrimeur().getCartes()[i] = null;
				}
			}
			//recuperer la derniere carte de la defausse
			c.getEscrimeur().ajouterCarte(deckDefausse.reprendreDerniereCarte());
			
			//reculer du nombre de cases de cette carte
			plateau.deplacerEscrimeur(c.getEscrimeur(), -(c.getCartes()[0].getDistance()));
			break;
			
		case RECULER :
			if(aPiocheDepuisDernierMouvement) {
				//remettre la ou les cartes dans la pioche
				int cartesUtiliseesEnDefense = 0;
				if (!historique.empty()) {
					cartesUtiliseesEnDefense = historique.peek().getIndicesCartesJoueesDefense().size();
				}
				for( int i = 0; i < c.getIndicesCartesJouees().size() + cartesUtiliseesEnDefense; i++) {
					//remettre dans la pioche la carte d'index max - i
					deckPioche.reposerCarte( c.getEscrimeur().getCartes()[c.getEscrimeur().getNbCartes() - 1 -i]);
					c.getEscrimeur().supprimerCarte(c.getEscrimeur().getCartes()[c.getEscrimeur().getNbCartes() - 1 - i]);
				}
			}
			
			
			//redecaller les cartes
			for(int i = 0; i < c.getEscrimeur().getNbCartes(); i++) {
				if(c.getIndicesCartesJouees().contains(i)) {
					for (int j = c.getEscrimeur().getNbCartes() - 1; j > i; j--) {
						c.getEscrimeur().getCartes()[j] = c.getEscrimeur().getCartes()[j-1];
					}
					c.getEscrimeur().getCartes()[i] = null;
				}
			}
			//recuperer la derniere carte de la defausse
			c.getEscrimeur().ajouterCarte(deckDefausse.reprendreDerniereCarte());
			
			//avancer du nombre de cases de cette carte
			plateau.deplacerEscrimeur(c.getEscrimeur(), c.getCartes()[0].getDistance());
			break;
			
		case ATTAQUEDIRECTE :
			//reposer les dernieres cartes piochée dans la pioche
			int cartesUtiliseesEnDefense = 0;
			if (!historique.empty()) {
				cartesUtiliseesEnDefense = historique.peek().getIndicesCartesJoueesDefense().size();
			}
			for( int i = 0; i < c.getIndicesCartesJouees().size() + cartesUtiliseesEnDefense; i++) {
				//remettre dans la pioche la carte d'index max - i
				//System.out.println("l'attaquant repose sur la pioche une carte de valeur : "+c.getEscrimeur().getCartes()[c.getEscrimeur().getNbCartes() - 1 -i].getDistance());
				deckPioche.reposerCarte( c.getEscrimeur().getCartes()[c.getEscrimeur().getNbCartes() - 1 -i]);
				c.getEscrimeur().supprimerCarte(c.getEscrimeur().getCartes()[c.getEscrimeur().getNbCartes() - 1 - i]);
			}
			
			//le defenseur recupere ses cartes de defense dans la defausse--------------------------------
			Escrimeur defenseur;
			if(c.getEscrimeur() == eGaucher) {
				defenseur = eDroitier;
			}else {
				defenseur = eGaucher;
			}
			//pour cela
			//d'abord remettre les cartes de sa main dans l'ordre precedent
			System.out.println("le defenseur remet ses cartes dans l'ordre");
			for (int i = 0; i < defenseur.getNbCartes(); i++) {
				if(c.getIndicesCartesJoueesDefense().contains(i)) {
					//tout decaller a droite et mettre l'indice i a null
					//System.out.println("indice a liberer : "+ i);
					for(int j = defenseur.getNbCartes() -1; j > i; j--) {
						defenseur.getCartes()[j] = defenseur.getCartes()[j-1];
					}
					defenseur.getCartes()[i] = null;
				}
				
			}
			
			//afficherEtatJeu();
			//puis le defenseur recupere ses cartes d'attaques dans la defausse
			for (int i = c.getIndicesCartesJoueesDefense().size()-1; i >= 0; i--) {
				if(!defenseur.ajouterCarteAIndiceVide(deckDefausse.reprendreDerniereCarte(),c.getIndicesCartesJoueesDefense().get(i))) {
					System.err.println("le defenseur recupere mal une carte dans la defausse");
					//remettre jeu comme il etait avant le debut de annuler coup
				}else {
					//System.out.println("le defenseur reprend dans la defausse " + defenseur.getCartes()[i]);
				}
			}
			//afficherEtatJeu();
			//---------------------------------------------------------------------------
			//l'attaquant recupere ses cartes d'attaques dans la defausse
			//d'abord remettre les cartes de sa main dans l'ordre
			for(int i = 0; i < c.getEscrimeur().getNbCartes(); i++) {
				if(c.getIndicesCartesJouees().contains(i)) {
					for (int j = c.getEscrimeur().getNbCartes() - 1; j > i; j--) {
						c.getEscrimeur().getCartes()[j] = c.getEscrimeur().getCartes()[j-1];
					}
					c.getEscrimeur().getCartes()[i] = null;
				}
			}
			//System.out.println("l'attaquand remet ses cartes dans l'ordre");
			//afficherEtatJeu();
			//puis recuperer les cartes dans la defausse
			for (int i = c.getIndicesCartesJouees().size()-1; i >= 0; i--) {
				if(!c.getEscrimeur().ajouterCarteAIndiceVide(deckDefausse.reprendreDerniereCarte(),c.getIndicesCartesJouees().get(i))) {
					System.err.println("l'attaquant recupere mal une carte dans la defausse");
					//remettre jeu comme il etait avant le debut de annuler coup
				}else {
					//System.out.println("l'attaquant reprend dans la defausse " + c.getCartes()[i]);
				}
			}
			break;
		 default:
			break;
		}
		
		if(c.escrimeur == eGaucher) {
			isTourGaucher = true;
		}else {
			isTourGaucher = false;
		}
		c.nullifierCartesJouees();
		metAJour();
		coupsAnnules.push(c);
		if(!historique.empty() &&  historique.peek().getEscrimeur() != c.getEscrimeur()) {
			aPiocheDepuisDernierMouvement = true;
		}else {
			aPiocheDepuisDernierMouvement = false;
		}
		return true;
	}
	
	
	public boolean rejouerCoupAnnule() {
		if(coupsAnnules.empty()) {
			System.err.println("aucun coup annulé");
			return false;
		}else {
			System.out.println("coup rejoué :");
		}
		Coup c = coupsAnnules.pop();
		jouer(c,true);
		return true;
	}
	
	public void afficherEtatJeu() {
		System.out.println("position gaucher : "+ getPlateau().getPositionGaucher());
		System.out.print("jeu gaucher : ");
		for (int i = 0; i < eGaucher.getCartes().length; i++) {
			if(eGaucher.getCarte(i) == null) {
				System.out.print("N ");
			}else {
				System.out.print(eGaucher.getCarte(i).getDistance() + " ");
			}
		}
		System.out.println("");
		System.out.println("position droitier : " + getPlateau().getPositionDroitier());
		System.out.print("jeu droitier : ");
		for (int i = 0; i < eDroitier.getCartes().length; i++) {
			if(eDroitier.getCarte(i) == null) {
				System.out.print("N ");
			}else {
				System.out.print(eDroitier.getCarte(i).getDistance() + " ");
			}
		}
		System.out.println("\n");
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
		Escrimeur eGaucher = new Escrimeur("Gaucher", TypeEscrimeur.HUMAIN, true, 5);
		Escrimeur eDroitier = new Escrimeur("Droitier", TypeEscrimeur.HUMAIN, false, 5);
		Jeu jeu = null;
		try {
			jeu = new Jeu(true, new Plateau(23), deckPioche, deckDefausse, eGaucher, eDroitier);
		} catch (IncorrectPlateauException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		jeu.piocher(eDroitier);
		jeu.piocher(eGaucher);
		
		Scanner myObj = new Scanner(System.in);
		
		System.out.println("0 = avancer, 1 = reculer, 2 = attaquer, 3 = annuler coup");
		System.out.println("4 = rejouer coup, 5 = taille de la pioche?, 6 = derniereCartePioche");
		
		jeu.afficherEtatJeu();
		while(jeu.winner == 0) {
			System.out.println("action collée à indice de la ou les cartes a jouer : ");
			String caj = myObj.nextLine();
			if(Integer.parseInt(String.valueOf(caj.charAt(0))) == 3) {
				jeu.annulerCoup();
			}else if(Integer.parseInt(String.valueOf(caj.charAt(0))) == 4) {
				jeu.rejouerCoupAnnule();
			}else if(Integer.parseInt(String.valueOf(caj.charAt(0))) == 7) {
				jeu.afficherEtatJeu();
			}else if(Integer.parseInt(String.valueOf(caj.charAt(0))) == 5) {
				System.out.println(deckPioche.nbCartes());
			}else if(Integer.parseInt(String.valueOf(caj.charAt(0))) == 6) {
				System.out.println(deckDefausse.consulterCarteVisible().getDistance());
			}else {
				Carte[] cartesAJouer = new Carte[caj.length()-1];
				for(int i = 1; i < caj.length(); i++) {
					if(jeu.getIsTourGaucher()) {
						cartesAJouer[i-1] = eGaucher.getCarte(Integer.parseInt(String.valueOf(caj.charAt(i))));
					}else {
						cartesAJouer[i-1] = eDroitier.getCarte(Integer.parseInt(String.valueOf(caj.charAt(i))));
					}
				}
				Coup c;
				if(jeu.getIsTourGaucher()) {
					c = new Coup(eGaucher, cartesAJouer, Integer.parseInt(String.valueOf(caj.charAt(0))));
				}else {
					c = new Coup(eDroitier, cartesAJouer, Integer.parseInt(String.valueOf(caj.charAt(0))));
				}
				jeu.jouer(c, false);
			}
			jeu.afficherEtatJeu();
		}
		if(jeu.winner == 1) {
			System.out.println("fin de manche, winner : gaucher");
		}else if(jeu.winner == 2){
			System.out.println("fin de manche, winner : droitier");
		}else {
			System.out.println("fin de manche, egalité");
		}
		
		
	}
}
