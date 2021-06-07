package model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class IA_Facile extends IA {
	
	public IA_Facile(Escrimeur e, Plateau p) {
		super(e, p);
	}
	
	/**
	 * 
	 * @param typeCoup
	 * @param valeurDefense
	 * @return un tableau de int comme [numero de la case, nombre de carte utilisé]
	 */
	@Override
	public int[] getChoixCoup(int typeCoup, int valeurDefense) {
		HashSet<Integer> casesAccessibles = getCasesAccessibles(typeCoup, valeurDefense);
		
	    int indiceChoisi = new Random().nextInt(casesAccessibles.size());
	    Iterator<Integer> it = casesAccessibles.iterator();
	    for(int i = 0; i < indiceChoisi - 1; i++) {
	    	it.next();
	    }
	    int numCase = it.next();

		return new int[] {numCase, e.getNbCartes(Math.abs(numCase - p.getPosition(e.getIndice()))) } ;
	}
}
