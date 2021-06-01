package model;

import java.util.Random;

public class IA_facile implements IA{
	Coup coup;
	Plateau plateau;
	
	public IA_facile(Coup c, Plateau p) {
		coup = c;
		plateau = p;
	}
	
	/**
	 * choixCarteIA : permet a l'IA de choisir une carte parmis sa main.
	 * Parametre : (Coup)coup.
	 * Retour : (int)valeurCarte. (un int representant la valeur de la carte choisie.) 
	 */
	public int choixCarteIA(Coup coup) {
		Carte[] cartes = coup.getCartes();
		Random rand = new Random();
	    int indice = rand.nextInt(coup.escrimeur.getNbCartes());	
	    return cartes[indice].getDistance();
	}
	
	/**
	 * actionIA : permet a l'IA de choisir une action a jouer en fonction de la carte choisie.
	 * Parametre : (Coup)coup.
	 * Retour : un tableau de deux int : (int)action et (int)valeurCarteChoisie. (un int representant l'action a effectuer, compris entre 0 et 3 
	 * et un int representant la carte jouee.) 
	 */
	public int[] actionIA( Coup coup ){
		int[] intArray = new int[2];
		int valeurCarteChoisie;
		Random rand = new Random();
	    int action = rand.nextInt(4);
	    valeurCarteChoisie = choixCarteIA(coup);
	    
	    while (!estBonCoup(coup.escrimeur, valeurCarteChoisie , action)) {
	    	action = rand.nextInt(4);
	    	valeurCarteChoisie = choixCarteIA(coup);
	    }
	    intArray[0] = action;
	    intArray[1] = valeurCarteChoisie;
	    
		return intArray ;
	}
	
	/**
	 * estBonCoup : permet de verifier que l'action selectionnee est correct.
	 * Parametre : Escrimeur, valeur de la carte, action
	 * Retour : boolean. (True si action possible, false sinon.) 
	 */
	public boolean estBonCoup (Escrimeur e, int valeurCarte, int action) {
		
		if( action == 1 || action == 0) {
			if (action == 1) {
				valeurCarte = valeurCarte * -1;
			}
			System.out.println("verif : " + plateau.deplacerEscrimeur( e, valeurCarte));
			return plateau.deplacerEscrimeur( e, valeurCarte);
		}
		System.out.println("verif : " + plateau.attaquerEscrimeur(e, valeurCarte));
		return plateau.attaquerEscrimeur(e, valeurCarte);
		
	}
	
	//TODO : possibilite d attaques indirectes, parades.
	
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
}
