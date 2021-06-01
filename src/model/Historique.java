package model;

import java.util.Stack;

public class Historique {
	Jeu jeu;
	private Stack<Coup> historique;
	private Stack<Coup> coupsAnnules;
	
	public Historique(Jeu jeu) {
		jeu = jeu;
		historique = new Stack<>();
		coupsAnnules = new Stack <>();
	}
	
	public void ajouterCoup(Coup c) {
		historique.push(c);
	}
	
	public void ajouterCoupAnnule(Coup c) {
		coupsAnnules.push(c);
	}
	
	public void viderCoupsAnnules() {
		this.coupsAnnules = new Stack<>();
	}
	
	public boolean rejouerCoupAnnule() {
		if(coupsAnnules.empty()) {
			System.err.println("aucun coup annulé");
			return false;
		}else {
			System.out.println("coup rejoué :");
		}
		Coup c = coupsAnnules.pop();
		return jeu.jouer(c,true);
	}
	
	public Coup voirDernierCoup() {
		return historique.isEmpty() ? null : historique.peek();
	}
	
	public Coup depilerDernierCoup() {
		return historique.isEmpty() ? null : historique.pop();
	}
	
	public Coup voirDernierCoupAnnule() {
		return coupsAnnules.isEmpty() ? null : coupsAnnules.peek();
	}
	
	public Coup depilerDernierCoupAnnule() {
		return coupsAnnules.isEmpty() ? null : coupsAnnules.pop();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public boolean annulerCoup() {
		return false;
	}
	
	
	/*
	FONCTION A REFAIRE 5ON PEUT S'INSPIRER DE CELLE ECRITE MAIS IL DOIT Y AVOIR FULL BULLSHIT
	
	public boolean annulerCoup() {
	 /!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\
	 ATTENTION A SI IL FAUT ANNULER UN PIOCHAGE ET SI OUI COMBIEN DE CARTES
	 /!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\
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
		c.viderCartesJouees();
		metAJour();
		historique.ajouterCoupAnnule(c);
		return true;
	}
	
	*/

}
