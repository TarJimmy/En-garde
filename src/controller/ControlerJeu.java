package controller;

import java.io.IOException;


import java.util.ArrayList;

import model.Carte;
import model.Coup;
import model.DeckDefausse;
import model.DeckPioche;
import model.Escrimeur;
import model.Historique;
import model.IncorrectCarteException;
import model.IncorrectPlateauException;
import model.Jeu;
import model.Plateau;
import model.TypeEscrimeur;
import view.InterfaceGraphiqueJeu;

public class ControlerJeu extends Controler {
	private Jeu jeu;

	public ControlerJeu(Jeu jeu) {
		this.jeu = jeu;
	}

	@Override
	public boolean jouerCartes(ArrayList<Integer> cartesAjouer) {
		return true;
	}

	public void piocher(Escrimeur e) {
		jeu.piocher(e);
	}

	@Override
	public boolean clickCase(int x) {
		// TODO Auto-generated method stub
		System.out.println("Il a cliquer sur la case " + x);
		Coup coupAJouer;
		jeu.jouer(coupAJouer, false);
		if ((!jeu.getDeckPioche().deckVide() && jeu.casesJouables().isEmpty()) || (jeu.casesJouables().isEmpty() && (coupAJouer.getAction() == Coup.ATTAQUEDIRECTE || coupAJouer.getAction() == Coup.ATTAQUEINDIRECTE))) {
			jeu.changerTour();
			finDeManche(jeu.getCurrentEscrimeur());
		}
		if (jeu.isDernierTour() || jeu.getDeckPioche().deckVide()) {
			finDeManche(null);
		}
		
		return false;
	}
	
	public void finDeManche(Escrimeur w) {
		Escrimeur winner = w;
		if (winner == null) {
			int action = jeu.getHistorique().voirDernierCoup().getAction();
			Escrimeur gaucher = jeu.getEscrimeurGaucher();
			Escrimeur droitier = jeu.getEscrimeurDroitier();
			Plateau plateau = jeu.getPlateau();
			if (action == Coup.ATTAQUEDIRECTE  || action == Coup.ATTAQUEINDIRECTE) {
				jeu.setDernierTour(true);
			} else {
				
				int avantageGaucher = 0;
				int nbCartes = gaucher.getNbCartes();
				int distanceAttaque = plateau.getPosition(Escrimeur.GAUCHER) - plateau.getPosition(Escrimeur.DROITIER);
				Carte[] cartesGaucher = gaucher.getCartes();
				//checker le joueur qui a le plus de carte permettant une attaque directe en main
				for (int i = 0; i < nbCartes; i++) {
					if (cartesGaucher[i] != null && cartesGaucher[i].getDistance() == distanceAttaque) {
						avantageGaucher ++;
					}
				}
				nbCartes = droitier.getNbCartes();
				Carte[] cartesDroitier = droitier.getCartes();
				for (int i = 0; i < nbCartes; i++) {
					if (cartesDroitier[i] != null && cartesDroitier[i].getDistance() == distanceAttaque) {
						avantageGaucher --;
					}
				}
				if (avantageGaucher == 0) {
					//checker le joueur qui a le plus avancé
					avantageGaucher = (plateau.getPosition(Escrimeur.GAUCHER)-1) - (plateau.getNbCase() - plateau.getPosition(Escrimeur.DROITIER));
				}
				if (avantageGaucher > 0) {
					winner = gaucher;
				} else if (avantageGaucher < 0) {
					winner = droitier;
				}
		}
		if (winner != null) {
			winner.addMancheGagnee();
		}
		jeu.nouvelleManche();
		}
	} 
	
	public static void main(String[] args) {
		Carte cartes[] = new Carte[25];

		for (int i = 0; i < cartes.length; i++) {
			try {
				cartes[i] = new Carte((i % 5) + 1);
			} catch (IncorrectCarteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		DeckPioche deckPioche = new DeckPioche(cartes);
		deckPioche.melanger();
		DeckDefausse deckDefausse = new DeckDefausse();
		for(int i = 0; i < 5; i ++) {
			deckDefausse.addCarte(deckPioche.piocher());
		}
		Escrimeur eGaucher = new Escrimeur("Gaucher", TypeEscrimeur.HUMAIN, Escrimeur.GAUCHER, 7);
		eGaucher.addMancheGagnee();
		eGaucher.addMancheGagnee();
		eGaucher.addMancheGagnee();
		Escrimeur eDroitier = new Escrimeur("Droitier", TypeEscrimeur.HUMAIN, Escrimeur.DROITIER, 7);
		eDroitier.addMancheGagnee();
		Jeu jeu = null;
		try {
			jeu = new Jeu(true, new Plateau(25), deckPioche, deckDefausse, eGaucher, eDroitier);
		} catch (IncorrectPlateauException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jeu.setHistorique(new Historique(jeu));
		
		ControlerJeu cJeu = new ControlerJeu(jeu);

		jeu.piocher(eDroitier);
		jeu.piocher(eGaucher);
		
		InterfaceGraphiqueJeu.demarrer(cJeu, jeu);
	}
}
