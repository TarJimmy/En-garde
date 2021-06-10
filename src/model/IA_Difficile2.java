package model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import controller.ControlerIA;
import controller.ControlerIA.JeuIA;



public class IA_Difficile2 extends IA{
	
	/*
	 * TODO : Ne pas oublier de decommenter insertJoueur avant de push
	 * 
	 * 
	 */
	
	static boolean doitFaireAttaqueIndirecte;
	static boolean doitContreAttaquer;
	
	public IA_Difficile2(Jeu jeu) {
		super(jeu);
		doitFaireAttaqueIndirecte = false;
		doitContreAttaquer = false;
		// TODO Auto-generated constructor stub
	}
	
	/**???? Utile ????
	 * choixCarteIA : permet a l'IA de choisir une carte parmis sa main.
	 * Parametre : (Coup)coup.
	 * Retour : (int)valeurCarte. (un int representant la valeur de la carte choisie.) 
	 */
	public int choixCarteIA() {
		int valeurCarte = 0;

		// A completer ...
		
		return valeurCarte;
	}
	
	/**OK
	 * rangerCartes : range les cartes dans l'ordres croissant.
	 * Parametre : les cartes de la main du joueur.
	 * Retour : un tableau de tableau qui contient des int.
	 */	
	public int [][] rangerCartes(Carte[] cartes){
		   
	    int taille = cartes.length;  
	    int [][] tab = new int[taille][2];
	    int tmp = 0;  
	    for (int k = 0; k < taille; k++) {
	    	tab[k][0] = cartes[k].getDistance();
	    	tab[k][1] = k;
	    }
	    for(int i=0; i < taille; i++) 
	    {
	            for(int j=1; j < (taille-i); j++)
	            {  
	                    if(tab[j-1][0] > tab[j][0])
	                    {
	                            //échanges des elements
	                                tmp = tab[j-1][0];  
	                                tab[j-1][0] = tab[j][0];  
	                                tab[j][0] = tmp;  
	                        }
	 
	                }
	        }
		   return tab;
	}
	
	/**OK
	 * totalCartes : total des carte se trouvant dans la main du joueur.
	 * Parametre : les cartes de la main du joueur.
	 * Retour : un int representant le total.
	 */	
	public int totalCartes(Carte[] cartes){
		int total = 0;
		
		for (int k=0; k < cartes.length ; k++) {
			total = total + cartes[k].getDistance();
		}
		return total;
	}
	
	/**OK
	 * max : trouve la carte de valeur la plus grande en ignorant les cartes doubles.
	 * Parametre : (Carte[]) cartes, (int) valeurCarteDouble
	 * Retour : (int) max
	 */	
	public int max(Carte[] cartes, int valeurCarteDouble) {// si valeurCarteDouble = 0 : prendre max,  si valeurCarteDouble = max :prendre 2eme max , sinon prendre max.
		int max = 0;
		//System.out.println("Je suis dans MAX");
		for( int k = 0; k < cartes.length ; k++) {
			if ( (cartes[k].getDistance() > max) && (cartes[k].getDistance() != valeurCarteDouble) ) {
				
				max = cartes[k].getDistance();
				//System.out.println("max = " + max);
			}
		}
		return max;
	}
	
	/**OK
	 * min : trouve la carte de valeur la plus petite en ignorant les cartes doubles.
	 * Parametre : (Carte[]) cartes, (int) valeurCarteDouble
	 * Retour : (int) min
	 */	
	public int min(Carte[] cartes, int valeurCarteDouble) {
		int min = 6;
		//System.out.println("Je suis dans MIN");
		for( int k = 0; k < cartes.length ; k++) {
			if ( (cartes[k].getDistance() < min) && (cartes[k].getDistance() != valeurCarteDouble) ) {
				min = cartes[k].getDistance();
				//System.out.println("min = " + min);
			}
		}
		return min;
	}
	
	/**OK
	 * gestionCartes : permet de choisir les cartes adequate selon la strategie de l'IA moyenne.
	 * Parametre : /
	 * Retour : un tableau de int contenant les valeurs des cartes interessantes a jouer.
	 */
	public int[] gestionCartes(JeuIA jeu) {
		int i=0, valeurMilieu = 15 ,valeurCarteDouble = 0;
		int [] cartesInteressantes2 = new int[50];
		int [] cartesInteressantes; 
		Carte[] cartes = jeu.getCurrentEscrimeur().getCartes();
		int [][] cartesRangees = rangerCartes(cartes);
		
		
		for(int k = 0; k < cartesRangees.length; k++) {
			//System.out.println("cartes testée : " + cartesRangees[k][0]);
			if(k < (cartesRangees.length -2) && cartesRangees[k][0] == cartesRangees[k+1][0] && cartesRangees[k][0] == cartesRangees[k+2][0]) {
				//System.out.println("On a 3 fois un "+ cartesRangees[k][0]);
				cartesInteressantes2[i] = cartesRangees[k+2][0];
				valeurCarteDouble = cartesRangees[k+2][0];
				i++;
			}
		}
		
		if( totalCartes(cartes) > valeurMilieu ) {
			cartesInteressantes2[i] = max(cartes, valeurCarteDouble); // si valeurCarteDouble = 0 : prendre max,  si valeurCarteDouble = max :prendre 2eme max , sinon prendre max.
			i++;
		}else if( totalCartes(cartes) <= valeurMilieu ) {
			cartesInteressantes2[i] = min(cartes, valeurCarteDouble); // si valeurCarteDouble = 0 : prendre min,  si valeurCarteDouble = min :prendre 2eme min , sinon prendre min.
			i++;
		}
		cartesInteressantes = new int[i];
		
		for( int i2 = 0 ; i2 <i; i2++) {
			cartesInteressantes[i2] = cartesInteressantes2[i2];
			//System.out.println("VALEUR de cartes = " + cartesInteressantes[i2]);
		}
		
		return cartesInteressantes;
	}
	
	/**NON
	 * probaExploitable : definit le moment ou les proba faites sur les cartes deviennent exploitables / utiles.
	 * Parametre : (int) nbCartesPioche, le nombre de cartes restantes dans la pioche actuellement.
	 * retour : true si les proba deviennent interessantes, false sinon.
	 */
	/*public boolean probaExploitable( int nbCartesPioche ) {
		//int nbCartesTotale = nbCartesPioche + jeu.getDeckDefausse().nbCartes() + coup.getCartes().length + coupAdverse.getCartes().length;
		//System.out.println("(nbCartesTotale/2) =  " + (nbCartesTotale/2));
		//System.out.println("nbCartesPioche =  " + nbCartesPioche);
		
		return nbCartesPioche < (nbCartesTotale/2); 
	}*/
	
	/**OK?
	 * estToucheAdverse : permet de savoir si le coup precedent etait une touche adverse.
	 * Parametre : ...
	 * Retour : true si le coup precedent etait une touche, false sinon.
	 */
	public boolean estToucheAdverse (JeuIA jeu) {
		Coup c = jeu.getHistorique().voirDernierCoup();
		if(c != null) {
			return c.getAction() == Coup.ATTAQUEDIRECTE || c.getAction() == Coup.ATTAQUEINDIRECTE;
		}else {
			return false;
		}
		
	}
	
	/**OK
	 * distanceApresCoup : permet de connaitre la distance entre les deux escrimeurs apres un hypothetique coup jouer grace a l'action "action" et la carte "valeurCarte".
	 * Parametre : (int) action , (int) valeurCarte.
	 * Retour : un int representant la distance entre les deux escrimeurs.
	 */
	public int distanceApresCoup(int action, int valeurCarte, JeuIA jeu) {
		int escrimeurD = 23;
		int escrimeurG = 1;
		if( action == 0 || action == 2 ) {
			escrimeurD = jeu.getPlateau().getPosition(1);
			escrimeurG = jeu.getPlateau().getPosition(0) + valeurCarte ;
		}
		if (action == 1) {
			escrimeurD = jeu.getPlateau().getPosition(1);
			escrimeurG = jeu.getPlateau().getPosition(0) - valeurCarte ;			
		}
		return escrimeurD - escrimeurG;
	}
	
	/**OK
	 * choisirComplementaire : permet de choisir la deuxieme carte de l attaque indirecte.
	 * Parametre : int valeurCarte , int distanceManquante
	 * Retour : (int) carteChoisie : la valeur de la carte choisie si elle existe, une valeur negative sinon.
	 */
	public int choisirComplementaire(int valeurCarte , int distanceManquante, JeuIA jeu ) {
		
		Carte[] cartes = jeu.getCurrentEscrimeur().getCartes();
		int compt = 0, carteChoisie = -50, carte;
		for(int k=0 ; k < cartes.length ; k++) {
			carte = cartes[k].getDistance();
			
			if (carte == distanceManquante) {
				if (valeurCarte == carte && compt == 1 ) { // si elle a la meme valeur que la premiere mais que s en est une autre on peut la choisir
					return carte;
				}else if (valeurCarte == carte && compt == 0) {// si elle a la meme valeur que la premiere et que c est la premiere qu on voit on ne peut pas la choisir
					compt ++;
				}else { // c est la bonne carte et elle ne correspond pas a la premiere carte choisie, on peut la choisir.
					return carte;
				}
			}
		}
		return carteChoisie;
	}
	
	/**OK
	 * cartesEtActionsPossibles : permet de connaitre toutes les actions associées a une carte qui sont jouable actuellement par l'IA.
	 * Parametre : (int) phase
	 * Retour : Un tableau contenenant des tableaux de 2 int : (int) action et (int)valeurCarte. 
	 * (Represente toutes les combinaison d'actions et de cartes jouable).
	 */
	public int [][] cartesEtActionsPossibles ( int phase, JeuIA jeu){
		int k = 0;
		int valPlusGrandeCarte = 5 ;
		Carte[] cartes = jeu.getCurrentEscrimeur().getCartes();
		int [][] res = new int [50][3];
		int valeurTotale = 0, valeurCarteChoisie2 =0; 
	    int posG = jeu.getPlateau().getPosition(0);
	    int posD = jeu.getPlateau().getPosition(1);
		
		for (int action = 0; action < 4 ; action++ ) { 
			for( int indiceCarte = 0 ; indiceCarte < jeu.getCurrentEscrimeur().getNbCartes() ; indiceCarte++ ) {
			    if (action == 3) {
			    	valeurCarteChoisie2 = choisirComplementaire(cartes[indiceCarte].getDistance() , posD - posG - cartes[indiceCarte].getDistance(), jeu );
			    }
			    valeurTotale = cartes[indiceCarte].getDistance() + valeurCarteChoisie2;
				//System.out.println("DUO = "+ action + " ; " +cartes[indiceCarte].getDistance()+ " ; " +valeurCarteChoisie2 );
				if (estBonCoup (jeu.getCurrentEscrimeur(), valeurTotale , action, jeu)) {
					
					if(phase == 1) { // durant la phase 1 on souhaite rester a plus de 5 cases de l'adversaire.
						//System.out.println("valeurCarte = "+ cartes[indiceCarte].getDistance()  + " ; " +  action );
						if(distanceApresCoup(action,cartes[indiceCarte].getDistance(), jeu) > valPlusGrandeCarte ) {
							res[k][1] =cartes[indiceCarte].getDistance();
							res[k][0] = action;
							k++;
							//System.out.println("PHASE 1 dans if");
						}//System.out.println("PHASE 1");
						
					}else {
						res[k][0] = action;
						res[k][1] = cartes[indiceCarte].getDistance();
						res[k][2] = valeurCarteChoisie2;
						//System.out.println("DEUXIEME CARTE : "+ res[k][2]);
						k++;
						//System.out.println("PHASE autres"); 
						//System.out.println("DUO = "+ res[k-1][0] + " ; " +res[k-1][1] );
					}
				}
			}
	
		}
		
		int[][] resFinal= new int[k][3];
		//System.out.println("taille res = "+ k);
		for( int i = 0; i < k ; i++) {
			//System.out.println("valeurCarte = "+ res[i][1]  + " ; " +  res[i][0] );
			resFinal[i][0] = res[i][0];
			resFinal[i][1] = res[i][1];
			resFinal[i][2] = res[i][2];
			
		}
		return resFinal;
	}
	
	/**OK
	 * choix : permet de choisir aleatoirement parmis les coups interessants a jouer.
	 * Parametre : int [][] tabRes (un tableau qui contient toutes les actions associee a leur cartes interessantes a jouer.)
	 * Retour : un tableau de int contenant l'action associee a la carte choisie.
	 */
	public int [] choix (int [][] tabRes, int [][] choixActions, JeuIA jeu) {
		Random rand = new Random();
		int indice;
		//System.out.println(" taille : " + tabRes.length);
		if (tabRes.length == 0 && choixActions.length == 0) {
			// si aucune action interessantes na ete trouvee parmis 
			// les actions possible de la phase 1 
			// et si aucune action possible de la phase 1 na ete trouvee
			// alors on choisie une action possible tout court

			tabRes = cartesEtActionsPossibles(2, jeu);
			indice = rand.nextInt(tabRes.length);
			return  tabRes[indice];
		}else if (tabRes.length == 0) {
			// si aucune action interessantes na ete trouvee parmis 
			// les actions possible de la phase 1 alors on choisie une action possible au hasard			

			indice = rand.nextInt(choixActions.length);
			return choixActions[indice];		
		}else {
			indice = rand.nextInt(tabRes.length);
			return tabRes[indice];
		}
	}
	/**NON
	 * estCoupFort : permet de savoir si l attaque possible de l IA va reussir 
	 * ( probabilite que lautre joueur ai assez de carte de la meme valeur dans sa main pour pouvoir contrer l attaque OU PIRE  de contrer et riposter)
	 * Parametre : (int) valeurCarte1 (la valeur de la carte qu'on va jouer pour attaquer si valeurCarte2 est null)
	 * 				(int) valeurCarte2 (la valeur de la carte qu'on va jouer pour attaquer )
	 * 				(int) nbCartes (le nombre de carte qu on jou en cas d attaque renforcee)
	 * Retour : (boolean) true si on a 0% de chance de perdre en lancant cette attaque ou si on a plus de 60% de chance de victoire. 
	 */
	public boolean estCoupFort(int valeurCarte1 , int valeurCarte2  , int nbCartes, Carte[] cartes, JeuIA jeu) {
		
		int[] ordreOpti = new int[cartes.length];
		for(int k = 0; k <5 ; k++) {
			ordreOpti[k] = nbCartesValeur(k+1,0, jeu.getDeckDefausse().defausseTableau()) 
					+ nbCartesValeur(k+1,0,jeu.getCurrentEscrimeur().getCartes()) ;
		}
		
		if (nbCartes > nbCartesValeur(valeurCarte1, 0, jeu.getNotCurrentEscrimeur().getCartes())) {
			return true;
		}
		
		return false;
	}
	
	
	/**OK
	 * nbCartesValeur : calcule le nb de carte que l ia a dans sa main de cette valeur
	 * Parametre :int valeur1, int valeur2.
	 * Retour : (int) le nombre de carte de la valeur1 si valeur2 est null, sinon le nombre de carte de la valeur2.  
	 */
	public int nbCartesValeur(int valeur1, int valeur2, Carte[] cartes) {
		

		int [][] cartesRangees = rangerCartes(cartes);
		int nb = 0 ;
		for(int i = 0; i <cartesRangees.length ; i++) {
			if(valeur2 == 0) {
				if(cartesRangees[i][0] == valeur1) {
					nb = nb + 1;
				}
			}else {
				if(cartesRangees[i][0] == valeur2) {
					nb = nb + 1;
				}
			}
		}
		
		return nb;
	}
	
	/**OK
	 * carteMoinsProbable : regarde la defausse et la main de l IA : plus une carte est tombee moins elle a de chance d etre dans la main adverse 
	 * on veux donc aller a cette distance de l adversaire. 
	 * Parametre : Carte[] cartes
	 * Retour : un int : la valeur de la carte a jouer pour etre a la bonne distance de l adversaire
	 */
	public int carteMoinsProbable(Carte[] cartes, JeuIA jeu) {
		int distanceOpti = 0;
		int tmp = 0;
		int carteOpti =0;
		int[] ordreOpti = new int[cartes.length];
		for(int k = 0; k <5 ; k++) {
			ordreOpti[k] = nbCartesValeur(k+1,0, jeu.getDeckDefausse().defausseTableau()) 
					+ nbCartesValeur(k+1,0,jeu.getCurrentEscrimeur().getCartes()) ;
		}
		// Max proba
		for( int l = 0; l <ordreOpti.length; l++ ) {
			//max 
			for (int k = 0; k <ordreOpti.length; k++) {
				// Ne pas utiliser la carte *2 ? 
				if (ordreOpti[k] > distanceOpti) {
					distanceOpti = ordreOpti[k];
					tmp = k;
					carteOpti = k+1;
					}
			}
			// distance apres coup == max 
			for( int i = 0; i <cartes.length;i++) {
						
				if (this.distanceApresCoup(1,cartes[i].getDistance(), jeu) == carteOpti) {
					return cartes[i].getDistance();
				}
			}
		ordreOpti[tmp] = -1;
		distanceOpti = 0;
		}
		
		return cartes[0].getDistance();
	}	
	
	/**
	 * transfoEnCase : permet de transformer une action et une valeurCarte en une case d'arrivee de l escrimeur.
	 * Parametre : (int) action, (int) valeurCarte, (int) valeurCarte2, (boolean) estGaucher, (int) positionG, (int) positionD.
	 * Retour : (int) caseArrivee : la case atteinte apres avoir effectuer l action "action" avec la carte "valeurCarte". 
	 */	
	public int[] transfoEnCase(int action, int valeurCarte, int valeurCarte2, boolean estGaucher, int positionG, int positionD) {
		int position;
		int[] res = new int [2];
		if(action == 4) {
			position = jeu.getPlateau().getPosition(jeu.getIndiceCurrentEscrimeur());
		}
		if (estGaucher) {
			if (action == Coup.RECULER) { // reculer
				position = positionG - valeurCarte;
			}else { // autres actions qui ne font qu avancer
				position = positionG + valeurCarte;
			}
		}else {
			if (action == 1) { // reculer
				position = positionD + valeurCarte;
			}else { // autres actions qui ne font qu avancer
				position = positionD - valeurCarte;
			}
		}
		if(action == Coup.ATTAQUEDIRECTE) {
			res[0] = nbCartesValeur(valeurCarte, 0, jeu.getCurrentEscrimeur().getCartes());
		}else {
			res[1] = 1;
		}
		res[0] = position;
		return res;
	}
		

	/**OK
	 * choixPhase : permet de choisir dans quelle phase se trouve la partie.
	 * Parametre : /
	 * Retour : le numero de la phase actuelle.
	 */
	public int choixPhase(JeuIA jeu) {
		int nbCartesPioche = jeu.getDeckPioche().nbCartes();
		
		if ( estToucheAdverse(jeu) ) {
			System.out.println("Phase 3");
			return 3;
		}
		if (jeu.getDeckPioche().nbCartes() < 10 ) {
			System.out.println("Phase 1");
			return 1;
		}
		if( nbCartesPioche > 1 ) {
			System.out.println("Phase 2");
			return 2;
		}
		if ( nbCartesPioche == 1 ) {
			System.out.println("Phase 4");
			return 4;
		}
		return -1;
	}
	
	/**OK
	 * jouerPhase1 : permet d'appliquer la strategie de l'IA moyenne durant la phase 1.
	 * Parametre : /
	 * Retour : un tableau de deux int : (int)action et (int)valeurCarteChoisie. (un int representant l'action a effectuer, compris entre 0 et 3 
	 * et un int representant la carte jouee.)
	 */
	public int[] jouerPhase1(JeuIA jeu){
		int k = 0;
		int [] res;
		int [][] resultat1 = new int[50][3];
		
		int [][] tabRes = cartesEtActionsPossibles(1, jeu);
		int [] cartesInteressantes = gestionCartes(jeu);
		
		for (int i = 0; i <cartesInteressantes.length; i++ ) {
			//System.out.println("JouerPhase1 : cartesInteressantes[i] " + cartesInteressantes[i]);
			for (int j = 0; j < tabRes.length ; j++) {
				//System.out.println("JouerPhase1 :tabRes[j][1]  " + tabRes[j][1] );
				if (cartesInteressantes[i] == tabRes[j][1] ) {
					resultat1[k] = tabRes[j];
					
					k++;
				}
			}
		}
		int i = 0;
		int [][] resultat = new int[k][3];
		//System.out.println("JouerPhase1 : resultat1[i][0]  " +  resultat1[i][1]);
		while (resultat1[i][1] != 0 ) {
			//System.out.println("JouerPhase1 : resultat1[i][0] " +   resultat1[i][1]);
			resultat[i] = resultat1[i];
			i++;
		}
		res = choix (resultat, tabRes, jeu);		
		
		// Rester a plus de 5 en avancant / reculant.
		
		return res ;
	}
	
	/**OK
	 * jouerPhase2 : permet d'appliquer la strategie de l'IA moyenne durant la phase 2.
	 * Parametre : /
	 * Retour : un tableau de deux int : (int)action et (int)valeurCarteChoisie. (un int representant l'action a effectuer, compris entre 0 et 3 
	 * et un int representant la carte jouee.)
	 */
	public int[] jouerPhase2(JeuIA jeu){
		int [] res =new int[3]; 
		//TODO : faire monter res jusqua 6 pour pouvoir faire des attaque directe et indirecte renforcee 
		int [][] tabRes = cartesEtActionsPossibles(2, jeu);		
		
		for (int j = 0; j < tabRes.length ; j++) {	// parcours les coups possibles
			if( tabRes[j][0] == 2 || tabRes[j][0] == 3 ) { // si le coup est une attaque directe ou indirecte 
				//System.out.println("ATTAQUE INDIRECTE");
				if( estCoupFort(tabRes[j][1], tabRes[j][2], nbCartesValeur(tabRes[j][1], tabRes[j][2], jeu.getCurrentEscrimeur().getCartes()), jeu.getCurrentEscrimeur().getCartes(), jeu) ) { // on verifie que c est un bon coup a jouer 
					res[0]=  tabRes[j][0];
					res[1]=  tabRes[j][1];
					res[2]=  tabRes[j][2];
					if(res[0] == 3) {
						doitFaireAttaqueIndirecte = true;
					}
					return res;
				}
			}		
		}
		res = jouerPhase1(jeu); // sinon on n attaque pas on continue a rester en retrait.
		
		
		return res ;
	}
	
	/**OK mais attention a valeurCarteAttaque et AJOUTER UNE CONTRE ATTAQUE 
	 * jouerPhase3 : permet d'appliquer la strategie de l'IA moyenne durant la phase 3.
	 * Parametre : (Coup) coup, (Plateau) plateau.
	 * Retour : un tableau de deux int : (int)action et (int)valeurCarteChoisie. (un int representant l'action a effectuer, compris entre 0 et 3 
	 * et un int representant la carte jouee.)
	 */
	public int[] jouerPhase3(JeuIA jeu){
		int [] res = {0,0,0}; 
		Carte[] cartes = jeu.getCurrentEscrimeur().getCartes();
		int[][] cartesEtActions = cartesEtActionsPossibles(3, jeu);
		int valeurCarteAttaque = jeu.getHistorique().voirDernierCoup().getCartes()[0].getDistance(); //ATTENTION VALEUR A CHANGER EN FONCTION DE LA CARTE JOUEE PAR L ADVERSAIRE
		if( nbCartesValeur(valeurCarteAttaque,0, jeu.getCurrentEscrimeur().getCartes())>= 1) { // si on a une carte de la meme valeur que l'attaque on pare avec cette carte.
			doitContreAttaquer = true;
			res[0] = 4;
			res[1] = valeurCarteAttaque;
			return res ;
		}else {
			for(int k=0; k < cartesEtActions.length ; k++) {
				if(cartesEtActions[k][0] == 1) {
					if( distanceApresCoup(1 , max( cartes, 0), jeu ) > 5 ) {// si la plus grande carte a jouer amene a distance de plus de 5 de l adversaire, la jouer
						res[0] = 1;
						res[1] = max( cartes, 0);
						return res ;
					}else {// sinon jouer une carte pour arriver a une cases a distance de l autre la moins probable quil ai cette carte... 
						//(ne pas se faire battre au prochain coup)
						res[0] = 1;
						res[1] = carteMoinsProbable(cartes, jeu);
						return res ;
						}
						
					}
					
				}
				
			}
			
		//SINON : PASSER TOUR.
		
		
		return res ;
	}
	
	/**NON
	 * jouerPhase4 : permet d'appliquer la strategie de l'IA moyenne durant la phase 4.
	 * Parametre : (Coup) coup, (Plateau) plateau.
	 * Retour : un tableau de deux int : (int)action et (int)valeurCarteChoisie. (un int representant l'action a effectuer, compris entre 0 et 3 
	 * et un int representant la carte jouee.)
	 */
	public int[] jouerPhase4(JeuIA jeu){
		int [] res = new int[3]; 
		int positionJoueurIA = jeu.getPlateau().getPosition(jeu.getIndiceCurrentEscrimeur());
		int positionAutreJoueur = jeu.getPlateau().getPosition(jeu.getNotCurrentEscrimeur().getIndice());
		
		HashSet<Integer> cj = jeu.casesJouables();
		Iterator<Integer> it = cj.iterator();
		while(it.hasNext()) {
			int caseCoup = it.next();
			boolean onGagneParDistance;
			if(jeu.getCurrentEscrimeur().getIsGaucher()) {
				onGagneParDistance = (positionJoueurIA - 1) > (jeu.getPlateau().getNbCase() - positionAutreJoueur);
			}else {
				onGagneParDistance = (positionAutreJoueur - 1) > (jeu.getPlateau().getNbCase() - positionJoueurIA);
			}
			if(onGagneParDistance && Math.abs(caseCoup - positionAutreJoueur) > 5) {
				if (jeu.getCurrentEscrimeur().getIsGaucher()) {
					res[0] = (positionJoueurIA < caseCoup ? Coup.AVANCER : Coup.RECULER);
				}else {
					res[0] = (positionJoueurIA < caseCoup ? Coup.RECULER : Coup.AVANCER);
				}
				res[1] = Math.abs(caseCoup - positionJoueurIA);
				return res;
			}
		}
		
		it = cj.iterator();
		int max = -7;
		while (it.hasNext()) {
			int caseCoup = it.next();
			int avantageAttaque = nbCartesValeur(Math.abs(caseCoup - positionJoueurIA), 0, jeu.getCurrentEscrimeur().getCartes()) - 1 - nbCartesValeur(Math.abs(caseCoup - positionJoueurIA), 0, jeu.getNotCurrentEscrimeur().getCartes());
			if (caseCoup == positionAutreJoueur) {
				avantageAttaque ++;
			}
			if (avantageAttaque > max) {
				if(caseCoup == positionAutreJoueur) {
					res[0] = Coup.ATTAQUEDIRECTE;
				}else if (jeu.getCurrentEscrimeur().getIsGaucher()) {
					res[0] = (positionJoueurIA < caseCoup ? Coup.AVANCER : Coup.RECULER);
				}else {
					res[0] = (positionJoueurIA < caseCoup ? Coup.RECULER : Coup.AVANCER);
				}
				res[1] = Math.abs(caseCoup - positionJoueurIA);
			}
		}
		
		return res ;
	}
	
	/**OK
	 * actionIA : fonction de retour globale ( permet de choisir la phase et d'envoyer les informations attendu par le jeu pour jouer un tour / coup).
	 * Parametre : /
	 * Retour : un tableau de deux int : (int)case et (int)nbCarte. (un int representant la case d arrivee de l IA,
	 * et un int representant le nombre de carte a jouer.) 
	 */
	public int[] actionIA( JeuIA jeu ){
		int action = 0 , valeurCarteChoisie = 0, valeurCarteChoisie2 = 0;
		int [] res = {action, valeurCarteChoisie,valeurCarteChoisie2};
	    int posG = jeu.getPlateau().getPosition(0);
	    int posD = jeu.getPlateau().getPosition(1);
		
		int phase = choixPhase(jeu);
		
		switch(phase) {
		case 1 :
			res = jouerPhase1(jeu);
			
			break;
		case 2 :
			res = jouerPhase2(jeu);
			break;
		case 3 :
			res = jouerPhase3(jeu);
			break;
		case 4 :
			res = jouerPhase4(jeu);
			break;
		default :
			System.out.println("Erreur");
			break;
		}
		System.out.println("Valeur de cartes = "+ res[1] +" Valeur de cartes2 = "+ res[2] + " action = "+ res[0]); 
		return transfoEnCase(res[0], res[1], res[2], jeu.getCurrentEscrimeur().getIsGaucher(), posG , posD);
	}
	
	
	/**OK (peut etre inutile grace a cartesEtActionsPossibles )
	 * estBonCoup : permet de verifier que l'action selectionnee est correct.
	 * Parametre : Escrimeur, valeur de la carte, action
	 * Retour : boolean. (True si action possible, false sinon.) 
	 */
	public boolean estBonCoup (Escrimeur e, int valeurCarte, int action, JeuIA jeu) {
		
		boolean var;
		if( action == 1 || action == 0) {
			if (action == 1) {
				valeurCarte = valeurCarte * -1;
			}
			
			var =  jeu.getPlateau().mouvementPossible( e, valeurCarte);
			//System.out.println("valeurCarte = "+ valeurCarte + " ; " +  action + " var : "+var );
			return var;
		}
		var =  jeu.getPlateau().escrimeurPeutAttaquer( e, valeurCarte);
		return var;
		
	}
	
	/*public static void main(String[] args) throws IncorrectCarteException, IncorrectPlateauException {
		Plateau p = new Plateau(5, 11, 23) ;

		DeckPioche pioche ;
		Carte[] cartesPioche = {new Carte(2)};
		pioche = new DeckPioche(cartesPioche);

		DeckDefausse defausse;
		Carte[] cartesDefausse = {new Carte(2),new Carte(1),new Carte(2),new Carte(3),new Carte(5), new Carte(5),new Carte(5),new Carte(3)
				,new Carte(2), new Carte(1),new Carte(3), new Carte(4), new Carte(4),new Carte(4),new Carte(4)};
		defausse = new DeckDefausse(cartesDefausse);
				
		Escrimeur esc1 = new Escrimeur("IA", TypeEscrimeur.IA_MOYENNE, 0 , 5);//0 si l'escrimeur est gaucher, 1 sinon
		Carte[] cartes1 = { new Carte(5),new Carte(5),new Carte(3), new Carte(1),new Carte(1)};
		Coup coup1 = new Coup(esc1,cartes1, 0);
		
		Escrimeur esc2 = new Escrimeur("Humain", TypeEscrimeur.HUMAIN, 1, 5);//0 si l'escrimeur est gaucher, 1 sinon
		Carte[] cartes2 = {  new Carte(3),new Carte(4),new Carte(3),new Carte(2), new Carte(1) };
		Coup coup2 = new Coup(esc2, cartes2, 0);

		
		IA_Difficile2 IA = new IA_Difficile2();
		
		//************* GLOBALE*************
		int[] res = IA.actionIA();
		System.out.println("Nombre de carte a jouer  = "+ res[1] + " Case d arrivee = "+ res[0]); 
		
		//************* PHASE 1*************
		
		// test de jouerPhase1()
		//int[] res = IA.jouerPhase1();
		//System.out.println("Valeur de cartes = "+ res[1] + "action = "+ res[0]); 
		 
		 
		// test de cartesEtActionsPossibles
		/*int [][] res = IA.cartesEtActionsPossibles(2);
		System.out.println("taille res = "+ res.length);
		for ( int k = 0;  res.length > k ; k++) {
			System.out.println("Valeur de cartes = "+ res[k][1] + "  Valeur de cartes2 = "+ res[k][2] + ", action = " + res[k][0]);
			
		}*/
		
		
		//test de gestionCartes
		/*int[] res = IA.gestionCartes();
		
		for( int k =0; k < res.length ; k++) {
			System.out.println( res[k] );
		}
		System.out.println("Taille res : " + res.length);*/
		
		
		 
		//test de rangerCartes
		/*int [][] res = IA.rangerCartes( cartes1 );
		for( int k = 0; k < 7; k++) {
			System.out.print("Valeur de cartes = "+ k + ", indices = ");
			for (int i = 0 ; i< res.length; i++) {
				if (res[i][0] == k ) {
					System.out.print(res[i][1]+", ");
				}
			}
			System.out.println(";");
		}*/
		
		//************* PHASE 2*************
		
		// test de jouerPhase2()
		//int[] res = IA.jouerPhase2();
		//System.out.println("Valeur de cartes = "+ res[1] +" Valeur de cartes2 = "+ res[2] + " action = "+ res[0]); 
		
		//test de nbCartesValeur
		/*int nb = IA.nbCartesValeur(4, 3);
		System.out.println("nb = " + nb);*/
		
		
		//************* PHASE 3*************
		// test de jouerPhase3()
		//int[] res = IA.jouerPhase3();
		//System.out.println("Valeur de cartes = "+ res[1] +" Valeur de cartes2 = "+ res[2] + " action = "+ res[0]); 
		
		//test de carteMoinsProbable
		/*int res = IA.carteMoinsProbable(cartes1);
		System.out.print("Valeur carte a jouer : " + res);*/
		
		//************* PHASE 4*************
		//int[] res = IA.jouerPhase4();
		//System.out.println("Valeur de cartes = "+ res[1] +" Valeur de cartes2 = "+ res[2] + " action = "+ res[0]); 
	//}
	
	public int[] getAttaqueIndirecte() {
		int[] res = new int[2];
		res[0] = jeu.getPlateau().getPosition(jeu.getNotCurrentEscrimeur().getIndice());
		res[1] = 1;
		return res;
	}
	
	public int[] getAttaqueMax() {
		int distanceEntreLesJoueurs = jeu.getPlateau().getPosition(Escrimeur.DROITIER) - jeu.getPlateau().getPosition(Escrimeur.GAUCHER);
		int[] res = new int[2];
		res[0] = jeu.getPlateau().getPosition(jeu.getNotCurrentEscrimeur().getIndice());
		res[1] = nbCartesValeur(distanceEntreLesJoueurs, 0, jeu.getCurrentEscrimeur().getCartes());
		return res;
	}

	@Override
	public int[] getChoixCoup() {
		// TODO Auto-generated method stub
		HashSet<Integer> cj = jeu.casesJouables();
		if (cj.contains(-1) && doitFaireAttaqueIndirecte) {
				doitFaireAttaqueIndirecte = false;
				return getAttaqueIndirecte();
		}else if(cj.contains(-1)){
			int [] passeTour= new int[2];
			passeTour[0] = -1;	
			return passeTour;
		}
		
		if (doitContreAttaquer) {
			doitContreAttaquer = false;
			return getAttaqueMax();
		}
		
		ControlerIA controlerIA = new ControlerIA(jeu);
		
		
		JeuIA jeuEtatCourant = controlerIA.generateNewJeuIA(jeu,jeu.getIndiceCurrentEscrimeur());
		return actionIA(controlerIA.generateNewJeuIA(jeu));
	} 
}
