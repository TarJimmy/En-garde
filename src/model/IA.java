package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public abstract class IA {
	protected Escrimeur e;
	protected Plateau p;
	
	public IA(Escrimeur e, Plateau p) {
		this.e = e;
		this.p = p;
	}
	
	public abstract int[] getChoixCoup(int typeCoup, int valeurDefense);
	
	/**
	 * 
	 * @param typeCoup : 0x1 => avancer, 0x10 => reculer/esquiver, 0x100 => attaquer, 0x1000 => defendre, 0x10000 = passer son tour
	 * @param valeurDefense
	 * @return la liste des cases accessibles
	 */
	protected HashSet<Integer> getCasesAccessibles(int typeCoup, int valeurDefense) {
		return p.casesJouables(e, typeCoup, valeurDefense);
	}
	
	/**
	 * 
	 * @param typeCoup
	 * @param valeurDefense
	 * @return un hashmap avec comme cl� la valeur de la case accessible, et comme valeur un tableau int comme [distance, nbExemplaire]
	 */
	protected HashMap<Integer, int[]> getCasesByCartes(int typeCoup, int valeurDefense) {
		 HashSet<Integer> casesAccessibles = getCasesAccessibles(typeCoup, valeurDefense);
		 HashMap<Integer, int[]> caseByCartes = new HashMap<>();
		 
		 Iterator<Integer> it = casesAccessibles.iterator();
		 int positionEscrimeur = p.getPosition(e.getIndice());
		 while(it.hasNext()) {
			 int numCase = it.next();
			 int valeurDistance = Math.abs(numCase - positionEscrimeur);
			 caseByCartes.put(numCase, new int[] {valeurDistance, e.getNbCartes(valeurDistance)});
		 }
		 
		 return caseByCartes;
	}
}
