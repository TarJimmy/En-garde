package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import controller.Controler;
import controller.ControlerIA;
import controller.ControlerIA.JeuIA;
import controller.ControlerJeu;

public class IA_Difficile extends IA{
	
	Coup coup;
	Coup coupAdverse;
	Plateau plateau;
	Deck deck;
	HashMap<JeuIA, Float> memoisation;
	
	public static final int PROFONDEURMAX = 10;
	
	public IA_Difficile(Jeu jeu){
		super(jeu);
		memoisation = new HashMap<>();
	}
	
	/**NON
	 * rangerCartes :l'indice du premier tableau correspond a la valeur (+1), a  cette indice ce trouve dans un tableau les indice des cartes de cette valeur.
	 * exemple :  [ 1:[4,3] , 2:[5] , 3:[], 4:[1,2] ]
	 * Parametre : les cartes de la main du joueur.
	 * Retour : un tableau de tableau qui contient des int.
	 */	
	public int [][] rangerCartes(Carte[] cartes){
		int[] a = {1};
		int[][] res = {a};
		return res;
	}
	
	/**NON
	 * totalCartes : total des carte se trouvant dans la main du joueur.
	 * Parametre : les cartes de la main du joueur.
	 * Retour : un int representant le total.
	 */	
	public int totalCartes(Carte[] cartes){
		return 0;
	}
	
	/**NON
	 * max : trouve la carte de valeur la plus grande en ignorant les cartes doubles.
	 * Parametre : (Carte[]) cartes, (int) valeurCarteDouble
	 * Retour : (int) max
	 */	
	public int max(Carte[] cartes, int valeurCarteDouble) {
		return 0;
	}
	
	/**NON
	 * min : trouve la carte de valeur la plus petite en ignorant les cartes doubles.
	 * Parametre : (Carte[]) cartes, (int) valeurCarteDouble
	 * Retour : (int) min
	 */	
	public int min(Carte[] cartes, int valeurCarteDouble) {
		return 0;
	}
	
	/**NON
	 * gestionCartes : permet de choisir les cartes adequate selon la strategie de l'IA moyenne.
	 * Parametre : ...
	 * Retour : un tableau de int contenant les valeurs des cartes interessantes a jouer.
	 */
	public int[] gestionCartes() {
		int valeurCarteChoisie = 0, i=0, valeurMilieu = 16 ,valeurCarteDouble = 0;
		int [] cartesInteressantes = {valeurCarteChoisie};
		
		Carte[] cartes = coup.getCartes();
		int [][] cartesRangees = rangerCartes(cartes);
		//  [ 1:[4,3] , 2:[5] , 3:[], 4:[1,2] ]
		
		for( int k=0 ; k < cartesRangees.length ; k++) {
			if( cartesRangees[k].length > 2 ) { // si on a plus de 2 cartes de meme valeur
				for (int j=0 ; j< cartesRangees[k].length -2 ; j++) { // pour chaque cartes apres 2 cartes de meme valeur
					cartesInteressantes[i] = cartes[cartesRangees[k][j]].getDistance();// on ajoute la valeur de la carte au tableau de retour (cartesRangees[k][j] correspond a un indice dans le tableau presente en commentaire au dessus).
					i++;
				}
			}
		}
		
		if( totalCartes(cartes) > valeurMilieu ) {
			cartesInteressantes[i] = max(cartes, valeurCarteDouble); // si valeurCarteDouble = 0 : prendre max,  si valeurCarteDouble = max :prendre 2eme max , sinon prendre max.
		}else if( totalCartes(cartes) <= valeurMilieu ) {
			cartesInteressantes[i] = min(cartes, valeurCarteDouble); // si valeurCarteDouble = 0 : prendre min,  si valeurCarteDouble = min :prendre 2eme min , sinon prendre min.
		}
		
		return cartesInteressantes;
	}
	
	/**NON
	 * probaExploitable : definit le moment ou les proba faites sur les cartes deviennent exploitables / utiles.
	 * Parametre : (int) nbCartesPioche, le nombre de cartes restantes dans la pioche actuellement.
	 * retour : true si les proba deviennent interessantes, false sinon.
	 */
	public boolean probaExploitable( int nbCartesPioche ) {
		int taillePioche = 15;
		// A complexifier ...
		
		return nbCartesPioche < (taillePioche/2); 
	}
	
	/**OK
	 * estToucheAdverse : permet de savoir si le coup precedent etait une touche adverse.
	 * Parametre : ...
	 * Retour : true si le coup precedent etait une touche, false sinon.
	 */
	public boolean estToucheAdverse () {
		return (coupAdverse.getAction() == 2 || coupAdverse.getAction() == 3 );
	}
	
	/**OK
	 * distanceApresCoup : permet de connaitre la distance entre les deux escrimeurs apres un hypothetique coup jouer grace a l'action "action" et la carte "valeurCarte".
	 * Parametre : (int) action , (int) valeurCarte.
	 * Retour : un int representant la distance entre les deux escrimeurs.
	 */
	public int distanceApresCoup(int action, int valeurCarte) {
		int escrimeurD = 23;
		int escrimeurG = 1;
		if( action == 0 || action == 2 ) {
			escrimeurD = plateau.getPosition(1);
			escrimeurG = plateau.getPosition(0) + valeurCarte ;
		}
		if (action == 1) {
			escrimeurD = plateau.getPosition(1);
			escrimeurG = plateau.getPosition(0) - valeurCarte ;			
		}
		return escrimeurD - escrimeurG;
	}
	
	/**OK
	 * cartesEtActionsPossibles : permet de connaitre toutes les actions associées a une carte qui sont jouable actuellement par l'IA.
	 * Parametre : (Coup) coup , (Plateau) plateau
	 * Retour : Un tableau contenenant des tableaux de 2 int : (int) action et (int)valeurCarte. 
	 * (Represente toutes les combinaison d'actions et de cartes jouable).
	 */
	public int [][] cartesEtActionsPossibles (Coup coup , Plateau plateau, int phase){
		int[] tuple1 = { 0, 0 };
		int[] tuple2 = { 0, 0 };
		int k = 0;
		int valPlusGrandeCarte = 5 ;
		Carte[] cartes = coup.getCartes();
		int [][] res = {tuple1, tuple2 };
		for (int action = 0; action < 3 ; action++ ) { // 
			for( int indiceCarte = 0 ; indiceCarte < coup.escrimeur.getNbCartes() ; indiceCarte++ ) {
				if (estBonCoup (coup.escrimeur, cartes[indiceCarte].getDistance() , action)) {
					if(phase == 1) { // durant la phase 1 on souhaite rester a plus de 5 cases de l'adversaire.
						if(distanceApresCoup(action,cartes[indiceCarte].getDistance()) > valPlusGrandeCarte ) {
							res[k][0] = action;
							res[k][1] = cartes[indiceCarte].getDistance();
							k++;	
						}
						
					}else {
						res[k][0] = action;
						res[k][1] = cartes[indiceCarte].getDistance();
						k++;
					}
				}
			}
		}
		// attaque indirecte
		for (int premiereCarte = 0; premiereCarte < coup.escrimeur.getNbCartes() ; premiereCarte ++) {
			for(int deuxiemeCarte = 0; deuxiemeCarte < coup.escrimeur.getNbCartes() ; deuxiemeCarte ++) {
				int valeurCarte1 = cartes[premiereCarte].getDistance();
				int valeurCarte2 = cartes[deuxiemeCarte].getDistance();
				
				if ( (plateau.getPosition(1) - valeurCarte1 - valeurCarte2) == plateau.getPosition(0) ) {
					res[k][0] =  3;
					res[k][1] = valeurCarte1;
					res[k][2] = valeurCarte2;
				}
			}
		}
		return res;
	}
	
	/**OK
	 * choix : permet de choisir aleatoirement parmis les coups interessants a jouer.
	 * Parametre : int [][] tabRes (un tableau qui contient toutes les actions associee a leur cartes interessantes a jouer.)
	 * Retour : un tableau de int contenant l'action associee a la carte choisie.
	 */
	public int [] choix (int [][] tabRes) {
		Random rand = new Random();
	    int indice = rand.nextInt(tabRes.length);
	    
		return tabRes[indice];
	}
	
	/**OK
	 * choixPhase : permet de choisir dans quelle phase se trouve la partie.
	 * Parametre : /
	 * Retour : le numero de la phase actuelle.
	 */
	public int choixPhase() {
		int nbCartesPioche = deck.nbCartes();
		
		if (!probaExploitable( nbCartesPioche ) ) {
			return 1;
		}
		if( !estToucheAdverse() && nbCartesPioche > 1 ) {
			return 2;
		}
		if ( estToucheAdverse() ) {
			return 3;
		}
		if ( nbCartesPioche >= 1 ) {
			return 4;
		}
		return -1;
	}
	
	/**OK
	 * jouerPhase1 : permet d'appliquer la strategie de l'IA moyenne durant la phase 1.
	 * Parametre : (Coup) coup, (Plateau) plateau.
	 * Retour : un tableau de deux int : (int)action et (int)valeurCarteChoisie. (un int representant l'action a effectuer, compris entre 0 et 3 
	 * et un int representant la carte jouee.)
	 * ATTENTION : LE RETOUR EST A CHANGER EN FONCTION DE CE QUI EST ATTENDU PAR LE JEU.
	 */
	public int[] jouerPhase1(Coup coup, Plateau plateau){
		int k = 0;
		int [] res;
		int [][] resultat = {{0,0}};
		
		int [][] tabRes = cartesEtActionsPossibles(coup, plateau, 1);
		int [] cartesInteressantes = gestionCartes();
		
		for (int i = 0; i <cartesInteressantes.length; i++ ) {
			for (int j = 0; j < tabRes.length ; j++) {
				if (cartesInteressantes[i] == tabRes[j][1] ) {
					resultat[k] = tabRes[j];
					k++;
				}
			}
		}
		
		res = choix (resultat);		
		
		// Rester a plus de 5 en avancant / reculant.
		
		return res ;
	}
	
	/**NON
	 * jouerPhase2 : permet d'appliquer la strategie de l'IA moyenne durant la phase 2.
	 * Parametre : (Coup) coup, (Plateau) plateau.
	 * Retour : un tableau de deux int : (int)action et (int)valeurCarteChoisie. (un int representant l'action a effectuer, compris entre 0 et 3 
	 * et un int representant la carte jouee.)
	 * ATTENTION : LE RETOUR EST A CHANGER EN FONCTION DE CE QUI EST ATTENDU PAR LE JEU.
	 */
	public int[] jouerPhase2(Coup coup, Plateau plateau){
		int [] res = {0,0}; 
		
		// A completer ...
		
		return res ;
	}
	
	/**NON
	 * jouerPhase3 : permet d'appliquer la strategie de l'IA moyenne durant la phase 3.
	 * Parametre : (Coup) coup, (Plateau) plateau.
	 * Retour : un tableau de deux int : (int)action et (int)valeurCarteChoisie. (un int representant l'action a effectuer, compris entre 0 et 3 
	 * et un int representant la carte jouee.)
	 * ATTENTION : LE RETOUR EST A CHANGER EN FONCTION DE CE QUI EST ATTENDU PAR LE JEU.
	 */
	public int[] jouerPhase3(Coup coup, Plateau plateau){
		int [] res = {0,0}; 
		
		// A completer ...
		
		return res ;
	}
	
	/**NON
	 * jouerPhase4 : permet d'appliquer la strategie de l'IA moyenne durant la phase 4.
	 * Parametre : (Coup) coup, (Plateau) plateau.
	 * Retour : un tableau de deux int : (int)action et (int)valeurCarteChoisie. (un int representant l'action a effectuer, compris entre 0 et 3 
	 * et un int representant la carte jouee.)
	 * ATTENTION : LE RETOUR EST A CHANGER EN FONCTION DE CE QUI EST ATTENDU PAR LE JEU.
	 */
	public int[] jouerPhase4(Coup coup, Plateau plateau){
		int [] res = {0,0}; 
		
		// A completer ...
		
		return res ;
	}
	
	/**OK
	 * actionIA : fonction de retour globale ( permet de choisir la phase et d'envoyer les informations attendu par le jeu pour jouer un tour / coup).
	 * Parametre : (Coup)coup.
	 * Retour : un tableau de deux int : (int)action et (int)valeurCarteChoisie. (un int representant l'action a effectuer, compris entre 0 et 3 
	 * et un int representant la carte jouee.) 
	 * ATTENTION : LE RETOUR EST A CHANGER EN FONCTION DE CE QUI EST ATTENDU PAR LE JEU.
	 */
	public int[] actionIA( Coup coup, Plateau plateau ){
		int action = 0 , valeurCarteChoisie = 0;
		int [] res = {action, valeurCarteChoisie};
		
		int phase = choixPhase();
		
		switch(phase) {
		case 1 :
			res = jouerPhase1( coup,  plateau);
			break;
		case 2 :
			res = jouerPhase2( coup,  plateau);
			break;
		case 3 :
			res = jouerPhase3( coup,  plateau);
			break;
		case 4 :
			res = jouerPhase4( coup,  plateau);
			break;
		default :
			System.err.println("Erreur");
			break;
		}
		
		return res;
	}
	
	
	/**OK (peut etre inutile grace a cartesEtActionsPossibles )
	 * estBonCoup : permet de verifier que l'action selectionnee est correct.
	 * Parametre : Escrimeur, valeur de la carte, action
	 * Retour : boolean. (True si action possible, false sinon.) 
	 */
	public boolean estBonCoup (Escrimeur e, int valeurCarte, int action) {
		
		if( action == 1 || action == 0) {
			if (action == 1) {
				valeurCarte = valeurCarte * -1;
			}
			return plateau.deplacerEscrimeur( e, valeurCarte);
		}
		return plateau.escrimeurPeutAttaquer(e, valeurCarte);
		
	}
	

	
	
	
	
	/*public static void main(String[] args) throws IncorrectCarteException, IncorrectPlateauException {
		Plateau p = new Plateau(7, 9, 24) ;
		
		Escrimeur esc1 = new Escrimeur("IA", TypeEscrimeur.IAFACILE, true , 5);
		Carte[] cartes1 = {new Carte(2), new Carte(2), new Carte(2),new Carte(2),new Carte(2) };
		Coup coup1 = new Coup(esc1,cartes1, 0);
	
		
		Escrimeur esc2 = new Escrimeur("Humain", TypeEscrimeur.HUMAIN, false, 5);
		Carte[] cartes2 = {new Carte(1), new Carte(2), new Carte(3),new Carte(4),new Carte(5) };
		Coup coup2 = new Coup(esc2, cartes2, 0);
		
		IA_facile IA = new IA_facile(coup1, p);
		int[] act_IA = IA.actionIA(coup1);
		
		System.out.println("action choisie :" + act_IA[0] + "carte choisie :"+act_IA[1]);
	} */
	
	public int[] getCarteDansPioche(int[] totalCartes, Carte[] carteEGauche, Carte[] carteEDroitier, DeckDefausse defausse) {
        int[] cartesRes = new int[]{totalCartes[0],totalCartes[1],totalCartes[2],totalCartes[3],totalCartes[4]};
        for (Carte c: carteEGauche) { cartesRes[c.getDistance()-1]--; }
        for (Carte c: carteEDroitier) { cartesRes[c.getDistance()-1]--; }
        for (int i=0; i<defausse.cartes.size();i++) { cartesRes[defausse.cartes.elementAt(i).getDistance()-1]--; }
        return cartesRes;
    }
    
    public CarteProbable getCarteProbable(int[] cartesDisponible, int nbCarte) throws IncorrectCarteException {
        float probMax = 0;
        int distance = 0;
        for (int i=0; i<cartesDisponible.length; i++){
            float probaCourante = cartesDisponible[i]/nbCarte;
            if (probaCourante>probMax) {
                probMax = probaCourante;
                distance = i+1;
            }
        }
        return new CarteProbable(distance, probMax);
    }
    
    public class CarteProbable extends Carte {
        public float proba;
        
        public CarteProbable(int distance, float proba) throws IncorrectCarteException {
            super(distance);
            this.proba = proba; 
        }
    }

	@Override
	public int[] getChoixCoup() {
		if (jeu.getDeckPioche().nbCartes() > 10) {
			return new IA_Moyenne(jeu).getChoixCoup();
		}else {
			ControlerIA controlerIA = new ControlerIA(jeu);
			ArrayList<int[]> coups = getCoupsPossibles(controlerIA);
			/*System.out.println("nombre de coups possibles :" + coups.size());
			System.out.print("coupsPossibles : ");
			for (int i = 0; i < coups.size(); i++) {
				System.out.print(coups.get(i)[0] +"_"+ coups.get(i)[1] +", ");
			}
			System.out.println("");*/
			Iterator<int[]> it = coups.iterator();
			int[] meilleurCoup = new int[2];
			if(coups.size() == 1) {
				meilleurCoup = coups.get(0);
				return meilleurCoup;
			}
			float scoreMax = 0;
			ControlerIA copie = new ControlerIA(controlerIA.generateNewJeuIA(controlerIA.getJeu(), jeu.getIndiceCurrentEscrimeur()));
			while (it.hasNext()) {
				int[] currentCoup = it.next();
				copie.clickCase(currentCoup[0],currentCoup[1]);
				//System.out.println("clickCase : " + currentCoup[0] + " "+ currentCoup[1]);
				//copie.getJeu().afficherEtatJeu();
				float scoreCoup = scoreConfig(copie, 0);
				if (scoreCoup > scoreMax){
					scoreMax = scoreCoup;
					meilleurCoup = currentCoup;
				}
			}
			if(meilleurCoup[0] == 0){
	            it = coups.iterator();
	            meilleurCoup = (int[])it.next();
			}
			return meilleurCoup;
		}
	}
	
	public ArrayList<int[]> getCoupsPossibles(ControlerIA config) {
		ArrayList<int[]> res = new ArrayList<>();
		Iterator<Integer> itCases = config.getJeu().casesJouables().iterator();
		while (itCases.hasNext()) {
			int caseCliquee = itCases.next();
			if (caseCliquee == -1) {
				int[] monCoup = new int[2];
				monCoup[0] = caseCliquee;
				monCoup[1] = 0;
				res.add(monCoup);
			}else {
				int distance = (Math.abs(caseCliquee - config.getJeu().getPlateau().getPosition(config.getJeu().getIndiceCurrentEscrimeur())));
				if(caseCliquee == config.getJeu().getPlateau().getPosition(config.getJeu().getNotCurrentEscrimeur().getIndice())) {
					for(int i = 1; i <= config.getJeu().getCurrentEscrimeur().getNbCartes(distance); i++) {
						int[] monCoup = new int[2];
						monCoup[0] = caseCliquee;
						monCoup[1] = i;
						res.add(monCoup);
					}
				} else {
					int[] monCoup = new int[2];
					monCoup[0] = caseCliquee;
					monCoup[1] = 1;
					res.add(monCoup);
				}
			}
		}
		return res;
	}
	
	/**
	 *
	 * @param copie est la config qu'on veut tester
	 * @return retourne un score a cette config, ce score est la probabilité de victoire estimée pour le currentEscrimeur de la config
	 */
	public float scoreConfig(ControlerIA copie,int profondeur) {
		//memoisation
		if (memoisation.containsKey((JeuIA)copie.getJeu())) {
			//pour ça il faut avoir override les methode equals et peut etre Hash jsp quoi Sde Jeu pour que 2 jeux equivalents soient egaux meme
			//si les cartes des joueurs sont pas dans le meme ordre etc
			return memoisation.get((JeuIA)copie.getJeu());
		}else {
			//cas de base
			if (copie.getEWinner() != Jeu.NONE) {
				//je sais pas trop comment tester ça mais peut etre avec jeu.getWinnerManche par exemple
				if(copie.getEWinner() == copie.getJeu().getIndiceCurrentEscrimeur()) {
					/*if (copie.getEWinner() == jeu.getIndiceCurrentEscrimeur()) {
						System.out.println("on gagne");
					}else {
						System.out.println("on perd");
					}*/
					//System.out.println("fin de manche");
					memoisation.put((JeuIA)copie.getJeu(),(float)1);
					return memoisation.get((JeuIA)copie.getJeu());
				}else if(copie.getEWinner() == Jeu.EGALITE){
					/*System.out.println("egalite");
					System.out.println("fin de manche");*/
					memoisation.put((JeuIA)copie.getJeu(),(float)0.1);
					return memoisation.get((JeuIA)copie.getJeu());
				}else {
					/*if (copie.getEWinner() == jeu.getIndiceCurrentEscrimeur()) {
						System.out.println("on gagne");
					}else {
						System.out.println("on perd");
					}
					System.out.println("fin de manche");*/
					memoisation.put((JeuIA)copie.getJeu(),(float)0);
					return memoisation.get((JeuIA)copie.getJeu());
				}
			}else if(profondeur > IA_Difficile.PROFONDEURMAX){
				return approximationScore((JeuIA)copie.getJeu());
			}else {
				//recurrence
				
				ArrayList<int[]> coups = getCoupsPossibles(copie);
				/*System.out.println("nombre de coups possibles :" + coups.size());
				System.out.print("coupsPossibles : ");
				for (int i = 0; i < coups.size(); i++) {
					System.out.print(coups.get(i)[0] +"_"+ coups.get(i)[1] +", ");
				}
				System.out.println("");*/
				Iterator<int[]> it = coups.iterator();
				float scoreMax = 0;
				while (it.hasNext()){
					int[] currentCoup = it.next();
					ControlerIA copie2 = new ControlerIA(copie.generateNewJeuIA(copie.getJeu()));
					copie2.clickCase(currentCoup[0],currentCoup[1]);
					//System.out.println("clickCase : " + currentCoup[0] + " "+ currentCoup[1]);
					//copie2.getJeu().afficherEtatJeu();
					float score;
					if(copie2.getJeu().getIndiceCurrentEscrimeur() == copie2.getJeu().getIndiceCurrentEscrimeur()) {
						score = scoreConfig(copie2, profondeur+1);
					}else {
						score = 1 - scoreConfig(copie2, profondeur+1);
					}
					if(score > scoreMax) {
						scoreMax = score;
					}
				}
				memoisation.put((JeuIA)copie.getJeu(),scoreMax);
				return memoisation.get((JeuIA)copie.getJeu());

			}
		}
	}

	/**
	 *
	 * @param j
	 * @return le score estimé de la config donnée en parametre
	 */
	public float approximationScore(Jeu j) {
		//System.out.println("estimation coup ia");
		return (float)0.2;
	}
	
	public static void main(String[]args) {
		int nbCase = 23;
		int nbCarteParJoueur = 5;
		int indiceCurrentEscrimeur = Escrimeur.GAUCHER;
		
		int[] carteDefausse = new int[] {};
		
		int[] cartePioche = new int[] {1,1,1,1,1, 2,2,2,2,2, 3,3,3,3,3, 4,4,4,4,4, 5,5,5,5,5};
		
		int[] carteGaucher = new int[] {};
		
		int[] carteDroitier = new int[] {};
		
		int[] positionsDepart = new int[] {1, 23};
		
		System.out.println("Total cartes : " + (carteDefausse.length + cartePioche.length + carteGaucher.length + carteDroitier.length));
		Carte[] cartesDefausse = new Carte[carteDefausse.length];
		Carte[] cartesPioche = new Carte[cartePioche.length];
		Carte[] cartesGaucher = new Carte[carteGaucher.length];
		Carte[] cartesDroitier = new Carte[carteDroitier.length];
		try {
			for (int i = 0; i < cartesDefausse.length; i++) {
				cartesDefausse[i] = new Carte(carteDefausse[i]);
			}
			for (int i = 0; i < cartePioche.length; i++) {
				cartesPioche[i] = new Carte(cartePioche[i]);
			}
			for (int i = 0; i < cartesGaucher.length; i++) {
				cartesGaucher[i] = new Carte(carteGaucher[i]);
			}
			for (int i = 0; i < cartesDroitier.length; i++) {
				cartesDroitier[i] = new Carte(carteDroitier[i]);
			}
		} catch (IncorrectCarteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Jeu jeu;
		try {
			Escrimeur eG = new Escrimeur("IA_DIFFCILE", TypeEscrimeur.IA_DIFFICILE, 0, 5);
			eG.setCartes(cartesGaucher);
			Escrimeur eD = new Escrimeur("IA_FACILE", TypeEscrimeur.IA_FACILE, 1, 5);
			eD.setCartes(cartesDroitier);
			for (int i = 0; i < 1; i++) {
				jeu = new Jeu(false, new Plateau(nbCase), new DeckPioche(cartesPioche), new DeckDefausse(cartesDefausse), nbCarteParJoueur, indiceCurrentEscrimeur, eG, eD, positionsDepart, false, 0);
				ControlerJeu controlerJeu = new ControlerIA(new Jeu(false, new Plateau(nbCase), new DeckPioche(cartesPioche), new DeckDefausse(cartesDefausse), 5, indiceCurrentEscrimeur, eG, eD, positionsDepart, false, 0));
				controlerJeu.nouvellePartie();
			}
			System.out.println("Fin");
		} catch (IncorrectPlateauException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}