package model;

import java.util.Arrays;
import java.util.HashSet;

public class IA_Moyenne extends IA {

	public IA_Moyenne(Jeu jeu) {
		super(jeu);
		System.out.println("IA Moyenne créer");
	}

	/*
	 * Dans l'ordre de choix s c'est possible:
	 * Attaque direct
	 * Attaque indirecte
	 * Avance
	 * Recule
	 * 
	 * Defense:
	 * Pare
	 * Recule
	 */
	@Override
	public int[] getChoixCoup() {
		System.out.println("Lancement choix meilleur coup");
		Plateau p = getPlateau();
		Escrimeur eCurrent = jeu.getCurrentEscrimeur();
		int indiceCurrent = eCurrent.getIndice();
		Escrimeur eAdv = jeu.getNotCurrentEscrimeur();
		// Ataque indirect
		int posAdv = p.getPosition(eAdv.getIndice());
		int posCurrent = p.getPosition(indiceCurrent);
		Carte[] cartes = eCurrent.getCartes();
		HashSet<Integer> casesPossibles = getCasesAccessibles();
		System.out.println("Case possible :" + Arrays.toString(casesPossibles.toArray()));
		int[] choixCaseAttaqueIndirect = null;
		int[] choixCaseAvancerMax = new int[] {(indiceCurrent == Escrimeur.GAUCHER ? -1 : p.getNbCase() + 1), 1};
		int[] choixCaseReculerMax = new int[] {(indiceCurrent == Escrimeur.GAUCHER ? p.getNbCase() + 1 : -1), 1};
		for (int i = 0; i < cartes.length; i++) {
			if (cartes[i] != null) {
				int distanceCurrent = cartes[i].getDistance();
				int numCaseAvance = posCurrent + p.getValeurOrientation(eCurrent, distanceCurrent);
				int numCaseRecule = posCurrent - p.getValeurOrientation(eCurrent, distanceCurrent);
				// On prépare pour reculer le plus loin possible en 1 fois
				if (casesPossibles.contains(numCaseRecule) && ((indiceCurrent == Escrimeur.GAUCHER && choixCaseReculerMax[0] > numCaseRecule) || (indiceCurrent == Escrimeur.DROITIER && choixCaseReculerMax[0] < numCaseRecule))) {
					choixCaseReculerMax[0] = numCaseRecule;
				}
				// Si la case peut avancer
				if (casesPossibles.contains(numCaseAvance)) {
					//Attaque directe si possible
					if (numCaseAvance == posAdv) {
						return new int[] {numCaseAvance, eCurrent.getNbCartes(distanceCurrent)};
					}
					// On prépare pour avancer le plus loin possible en 1 fois
					System.out.println((indiceCurrent == Escrimeur.GAUCHER && choixCaseAvancerMax[0] < numCaseAvance) + " " + (indiceCurrent == Escrimeur.DROITIER && choixCaseAvancerMax[0] > numCaseAvance) + " " + choixCaseAvancerMax[0] + " " + numCaseAvance);
					if ((indiceCurrent == Escrimeur.GAUCHER && choixCaseAvancerMax[0] < numCaseAvance) || (indiceCurrent == Escrimeur.DROITIER && choixCaseAvancerMax[0] > numCaseAvance)) {
						choixCaseAvancerMax[0] = numCaseAvance;
					}
					// Si on est pas en mode simple ou que l'escrimeur n'est plus loin qu'une attaque indirect de distance 5, on regarde si on peut faire une attaque indirect
					if (!jeu.getModeSimple()) {
						// On regarde si on peut faire une attaque indirecte
						for (int j = i + 1; j < cartes.length; j++) {
							if (cartes[j] != null) {
								int distanceAjouter = cartes[j].getDistance();
								int numCaseAttaque = numCaseAvance + p.getValeurOrientation(eCurrent, distanceAjouter);
								if (numCaseAttaque == posAdv) {
									if (choixCaseAttaqueIndirect == null) {
										choixCaseAttaqueIndirect = new int[] {numCaseAvance, eCurrent.getNbCartes(distanceAjouter)}; 
									} else {
										int newNbCartePourAttaquer = eCurrent.getNbCartes(distanceAjouter) - (distanceAjouter == distanceCurrent ? 1 : 0);
										if (newNbCartePourAttaquer > choixCaseAttaqueIndirect[1]) {
											choixCaseAttaqueIndirect[0] = numCaseAvance;
											choixCaseAttaqueIndirect[1] = newNbCartePourAttaquer;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		if (choixCaseAttaqueIndirect != null) {
			return choixCaseAttaqueIndirect;
		}
		
		// On renvoie la carte la plus avancer où on peut aller
		if (casesPossibles.contains(choixCaseAvancerMax[0])) {
			return choixCaseAvancerMax;
		}
		// On pare si on peut
		if (casesPossibles.contains(posCurrent)) {
			return new int[] {posCurrent, 1};
		}
		// on recule le plus loin possible
		return choixCaseReculerMax;
	}
}
