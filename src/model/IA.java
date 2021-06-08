package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import controller.ControlerIA;
import controller.ControlerJeu;

public abstract class IA {
	Jeu jeu;
	
	public IA(Jeu jeu) {
		this.jeu = jeu;
	}
	
	public abstract int[] getChoixCoup();
	
	/**
	 * 
	 * @param typeCoup : 0x1 => avancer, 0x10 => reculer/esquiver, 0x100 => attaquer, 0x1000 => defendre, 0x10000 = passer son tour
	 * @param valeurDefense
	 * @return la liste des cases accessibles
	 */
	protected HashSet<Integer> getCasesAccessibles() {
		 return jeu.casesJouables();
	}
	
	/**
	 * 
	 * @param typeCoup
	 * @param valeurDefense
	 * @return un hashmap avec comme clé la valeur de la case accessible, et comme valeur un tableau int comme [distance, nbExemplaire]
	 */
	protected HashMap<Integer, int[]> getCasesByCartes() {
		 HashSet<Integer> casesAccessibles = getCasesAccessibles();
		 HashMap<Integer, int[]> caseByCartes = new HashMap<>();
		 
		 Iterator<Integer> it = casesAccessibles.iterator();
		 int positionEscrimeur = jeu.getPlateau().getPosition(jeu.getIndiceCurrentEscrimeur());
		 Escrimeur e = jeu.getCurrentEscrimeur();
		 while(it.hasNext()) {
			 int numCase = it.next();
			 int valeurDistance = Math.abs(numCase - positionEscrimeur);
			 caseByCartes.put(numCase, new int[] {valeurDistance, e.getNbCartes(valeurDistance)});
		 }
		 
		 return caseByCartes;
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
