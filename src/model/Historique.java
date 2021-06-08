package model;

import java.util.HashSet;
import java.util.Stack;

import model.Jeu.Action;

public class Historique {
	Jeu jeu;
	private Stack<Coup> historique;
	private Stack<Coup> coupsAnnules;
	
	public Historique(Jeu jeu) {
		this.jeu = jeu;
		historique = new Stack<>();
		coupsAnnules = new Stack <>();
	}
	
	public void ajouterCoup(Coup c) {
		historique.push(c);
	}
	
	public void vider() {
		historique.clear();
		coupsAnnules.clear();
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
		
		
		Boolean res = jeu.jouer(c,true);
		if (res == false) {
			return false;
		}
		HashSet<Integer> cases = jeu.casesJouables();
		int action = c.getAction();
		if ((action == Coup.ATTAQUEDIRECTE  || action == Coup.ATTAQUEINDIRECTE) && !cases.isEmpty() && jeu.getDeckPioche().deckVide()) {
			jeu.setDernierTour(true);
		}
		if(cases.size() == 1 && cases.contains(-1)) {
			jeu.changerTour();
		}
		return true;
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
		Coup dernierCoup = depilerDernierCoup();
		if(dernierCoup == null) {
			System.err.println("impossible d'annuler un coup, l'historique est vide");
			return false;
		}
		System.out.println("annuler coup :");
		dernierCoup.printCoup();
		Escrimeur escrimeurCoup = dernierCoup.getEscrimeur();
		if(escrimeurCoup != jeu.getCurrentEscrimeur()) {
			//compter les CartesPioch�es
			Coup coupSauvegarde = depilerDernierCoup();
			int nb = dernierCoup.getCartes().length;
			if(coupSauvegarde != null && coupSauvegarde.getEscrimeur() == escrimeurCoup) { //defense ou mouvement avant attaque indirecte
				nb += coupSauvegarde.getCartes().length;
				if(voirDernierCoup() != null && voirDernierCoup().getEscrimeur() == escrimeurCoup) { //defense avant mouvement avant attaque indirecte
					nb+= voirDernierCoup().getCartes().length;
				}
			}
			ajouterCoup(coupSauvegarde);
			//reposer nb cartes
			int boucle = escrimeurCoup.getNbCartes()-1;
			while (boucle >= 0 && nb > 0) {
				jeu.getDeckPioche().reposerCarte(escrimeurCoup.getCarte(boucle));
				escrimeurCoup.getCartes()[boucle] = null;
				nb--;
				boucle--;
			}
			jeu.modifieVue(Action.ACTUALISER_DECK);
		}
		//recuperer les cartes dans la defausse
		dernierCoup.remettreCartesDansLordre();
		int departParcours = dernierCoup.getIndicesCartesJouees().size()-1;
		for (int i = departParcours; i >= 0; i--) {
			//on recupere chaque carte jou�e dans la defausse et on la remet a sa place
			if(!escrimeurCoup.ajouterCarteAIndiceVide(jeu.getDeckDefausse().reprendreDerniereCarte(), dernierCoup.getIndicesCartesJouees().get(i))) {
				System.err.println("erreur lors de l'annulation du coup");
			}
		}
		switch(dernierCoup.getAction()) {
			case Coup.AVANCER :
				jeu.getPlateau().deplacerEscrimeur(escrimeurCoup, -dernierCoup.getCartes()[0].getDistance());
				jeu.modifieVueAnimation(Action.ANIMATION_DEPLACER_ESCRIMEUR);
				break;
			case Coup.RECULER :
			case Coup.ESQUIVER :
				jeu.getPlateau().deplacerEscrimeur(escrimeurCoup, dernierCoup.getCartes()[0].getDistance());
				jeu.modifieVueAnimation(Action.ANIMATION_DEPLACER_ESCRIMEUR);
				break;
			default :
				break;
		}
		dernierCoup.viderCartesJouees();
		ajouterCoupAnnule(dernierCoup);
		if (jeu.isDernierTour()) {
			jeu.setDernierTour(false);
		}
		jeu.setIndiceCurrentEscrimeur(escrimeurCoup.getIndice());
        jeu.modifieVue(Action.ACTUALISER_ESCRIMEUR);
        jeu.modifieVue(Action.ACTUALISER_DECK);
        jeu.modifieVue(Action.ACTUALISER_PLATEAU);
		
		return false;
	}

	public Historique copySimple(Jeu jeu) {
		Historique newHistorique = new Historique(jeu);
		newHistorique.historique = new Stack<>();
		for (Coup c: historique) {
			newHistorique.historique.add(c.copySimple(jeu.getEscrimeurs()));
		}
		return newHistorique;
	}
}
