package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

import controller.ControlerJeu;
import view.InterfaceGraphiqueRegles;

public abstract class IA {
	Jeu jeu;
	
	public IA(Jeu jeu) {
		this.jeu = jeu;
	}
	
	public abstract int[] getChoixCoup(int indiceEscrimeur, int typeCoup, int valeurDefense);
	
	/**
	 * 
	 * @param typeCoup : 0x1 => avancer, 0x10 => reculer/esquiver, 0x100 => attaquer, 0x1000 => defendre, 0x10000 = passer son tour
	 * @param valeurDefense
	 * @return la liste des cases accessibles
	 */
	protected HashSet<Integer> getCasesAccessibles(int indiceEscrimeur, int typeCoup, int valeurDefense) {
		 return jeu.getPlateau().casesJouables(jeu.getEscrimeurs()[indiceEscrimeur], typeCoup, valeurDefense);
	}
	
	/**
	 * 
	 * @param typeCoup
	 * @param valeurDefense
	 * @return un hashmap avec comme clé la valeur de la case accessible, et comme valeur un tableau int comme [distance, nbExemplaire]
	 */
	protected HashMap<Integer, int[]> getCasesByCartes(int indiceEscrimeur, int typeCoup, int valeurDefense) {
		 HashSet<Integer> casesAccessibles = getCasesAccessibles(typeCoup, valeurDefense, valeurDefense);
		 HashMap<Integer, int[]> caseByCartes = new HashMap<>();
		 
		 Iterator<Integer> it = casesAccessibles.iterator();
		 int positionEscrimeur = jeu.getPlateau().getPosition(indiceEscrimeur);
		 Escrimeur e = jeu.getEscrimeurs()[indiceEscrimeur];
		 while(it.hasNext()) {
			 int numCase = it.next();
			 int valeurDistance = Math.abs(numCase - positionEscrimeur);
			 caseByCartes.put(numCase, new int[] {valeurDistance, e.getNbCartes(valeurDistance)});
		 }
		 
		 return caseByCartes;
	}
		
	public int[] getCarteDansPioche(int[] totalCartes, Carte[] carteEGauche, Carte[] carteEDroitier, DeckDefausse defausse) {
		int[] cartesRes = new int[]{totalCartes[0],totalCartes[1],totalCartes[2],totalCartes[3],totalCartes[4]};
		for (Carte c: carteEGauche) { cartesRes[c.getDistance()-1]--; }
		for (Carte c: carteEDroitier) { cartesRes[c.getDistance()-1]--; }
		for (int i=0; i<defausse.cartes.size();i++) { cartesRes[defausse.cartes.elementAt(i).getDistance()-1]--; }
		return cartesRes;
	}
	
	public Escrimeur getEscrimeur(int indice) {
		return jeu.getEscrimeurs()[indice];
	}
	
	public Plateau getPlateau() {
		return jeu.getPlateau();
	}
	
	public DeckDefausse getDefausse() {
		return jeu.getDeckDefausse();
	}
}
