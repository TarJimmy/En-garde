package model;

import java.util.Random;

public class IA_facile {
	Coup coup;
	Plateau plateau;
	
	public IA_facile(Coup c, Plateau p) {
		coup = c;
		plateau = p;
	}
	
	/**
	 * choixCarteIA : permet a l'IA de choisir une carte parmis sa main.
	 * Parametre :/
	 * Retour :  valeurCarte. (un int representant la valeur de la carte choisie.) 
	 */
	public int choixCarteIA() {
		Carte[] cartes = coup.getCartes();
		Random rand = new Random();
	    int indice = rand.nextInt(coup.escrimeur.getNbCartes());
	    return cartes[indice].getDistance();
	}
	
	/**
	 * estBonCoup : permet de verifier que l'action selectionnee est correct.
	 * Parametre : Escrimeur, valeur de la carte, action
	 * Retour : boolean. (True si action possible, false sinon.) 
	 */
	public boolean estBonCoup (Escrimeur e, int valeurCarte, int action) {
		boolean var;
		if( action == 1 || action == 0) {
			if (action == 1) {
				valeurCarte = valeurCarte * -1;
			}
			var =  plateau.mouvementPossible( e, valeurCarte);
			return var;
		}
		var =  plateau.escrimeurPeutAttaquer( e, valeurCarte);
		return var;
		
	}
	
	/**
	 * transfoEnCase : permet de transformer une action et une valeurCarte en une case d'arrivee de l escrimeur.
	 * Parametre : (int) action, (int) valeurCarte, (int) valeurCarte2, (boolean) estGaucher, (int) positionG, (int) positionD.
	 * Retour : (int) caseArrivee : la case atteinte apres avoir effectuer l action "action" avec la carte "valeurCarte". 
	 */	
	public int transfoEnCase(int action, int valeurCarte, int valeurCarte2, boolean estGaucher, int positionG, int positionD) {
		int position;
		if (estGaucher) {
			if (action == 1) { // reculer
				position = positionG - valeurCarte - valeurCarte2;
			}else { // autres actions qui ne font qu avancer
				position = positionG + valeurCarte + valeurCarte2;
			}
		}else {
			if (action == 1) { // reculer
				position = positionD + valeurCarte + valeurCarte2;
			}else { // autres actions qui ne font qu avancer
				position = positionD - valeurCarte - valeurCarte2;
			}
		}
		return position;
	}

	/**
	 * choisirComplementaire : permet de choisir la deuxieme carte de l attaque indirecte.
	 * Parametre : int valeurCarte , int distanceManquante
	 * Retour : (int) carteChoisie : la valeur de la carte choisie si elle existe, une valeur negative sinon.
	 */
	public int choisirComplementaire(int valeurCarte , int distanceManquante ) {
		Carte[] cartes = coup.cartes;
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
	
	/**
	 * actionIA : permet a l'IA de choisir une action a jouer en fonction de la carte choisie.
	 * Parametre : /
	 * Retour : un int : la case choisie par l'IA.
	 */
	public int actionIA( ){
		int valeurCarteChoisie, valeurCarteChoisie2=0, valeurTotale;
		Random rand = new Random();
	    int action = rand.nextInt(4);
	    int posG = plateau.getPosition(0);
	    int posD = plateau.getPosition(1);
	    valeurCarteChoisie = choixCarteIA();
	    if (action == 3) {
	    	valeurCarteChoisie2 = choisirComplementaire(valeurCarteChoisie , posD - posG - valeurCarteChoisie );
	    }
	    valeurTotale = valeurCarteChoisie + valeurCarteChoisie2;
	    while (!estBonCoup(coup.escrimeur, valeurTotale , action) ) {
	    	action = rand.nextInt(4);
	    	valeurCarteChoisie = choixCarteIA();
	    	valeurCarteChoisie2 = 0;
		    if (action == 3) {
		    	valeurCarteChoisie2 =  choisirComplementaire(valeurCarteChoisie , posD - posG - valeurCarteChoisie );
		    }
		    valeurTotale = valeurCarteChoisie + valeurCarteChoisie2;
	    }
	    System.out.println("action choisie :" + action + "   carte choisie :"+valeurCarteChoisie + "   carte choisie2 :"+valeurCarteChoisie2);

		return transfoEnCase(action, valeurCarteChoisie, valeurCarteChoisie2, coup.escrimeur.getIsGaucher(), posG , posD) ;
	}
	
	//TODO : parades ??
	
	public static void main(String[] args) throws IncorrectCarteException, IncorrectPlateauException {
		Plateau p = new Plateau(18, 20, 24) ;
	
		Escrimeur esc1 = new Escrimeur("IA", TypeEscrimeur.IA_FACILE, 1 , 5);
		Carte[] cartes1 = {new Carte(1), new Carte(1), new Carte(1),new Carte(2),new Carte(2) };
		Coup coup1 = new Coup(esc1,cartes1, 0);
	
		
		Escrimeur esc2 = new Escrimeur("Humain", TypeEscrimeur.HUMAIN, 0, 5);
		Carte[] cartes2 = {new Carte(1), new Carte(2), new Carte(1),new Carte(3),new Carte(2) };
		Coup coup2 = new Coup(esc2, cartes2, 0);
		
		IA_facile IA = new IA_facile(coup1, p);
		int case_IA = IA.actionIA();
		
		System.out.println("case d arrivee :"+ case_IA );
	} 
}
