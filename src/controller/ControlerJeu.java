package controller;

import java.io.IOException;


import java.util.ArrayList;

import model.Carte;
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
	
	/*
	public void finDeManche(int attaqueEnCours) {
		//1 - si une attaque est en cours (si le dernier coup de l'historique est une attaque)
		a faire 
		//2 - checker le joueur qui a le plus de carte permettant une attaque directe en main
		if (winner == 2) {
			int avantageGaucher = 0;
			for (int i = 0; i < escrimeurs[0].getNbCartes(); i++) {
				if (escrimeurs[0].getCarte(i) != null && escrimeurs[0].getCarte(i).getDistance() == distanceAttaque) {
					avantageGaucher ++;
				}
			}
			for (int i = 0; i < escrimeurs[1].getNbCartes(); i++) {
				if (escrimeurs[1].getCarte(i) != null && escrimeurs[1].getCarte(i).getDistance() == distanceAttaque) {
					avantageGaucher --;
				}
			}
			if(avantageGaucher > 0) {
				winner = 0;
			}else if(avantageGaucher < 0){
				winner = 1;
			}
			
			//3 - checker le joueur qui a le plus avancé
			if (winner == 2) {
				avantageGaucher = (plateau.getPosition(EGAUCHER)-1) - (plateau.getNbCase() - plateau.getPosition(EDROITIER));
				if(avantageGaucher > 0) {
					winner = 0;
				}else if(avantageGaucher < 0){
					winner = 1;
				}
				
				//4 - si toujours aucun gagnant, match nul
				if (winner == 2) {
					winner = 3;
				}
			}
		}
	}*/

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
		Escrimeur eGaucher = new Escrimeur("Gaucher", TypeEscrimeur.HUMAIN, Escrimeur.GAUCHER, 3);
		eGaucher.addMancheGagner();
		eGaucher.addMancheGagner();
		eGaucher.addMancheGagner();
		Escrimeur eDroitier = new Escrimeur("Droitier", TypeEscrimeur.HUMAIN, Escrimeur.DROITIER, 7);
		eDroitier.addMancheGagner();
		Jeu jeu = null;
		try {
			jeu = new Jeu(true, new Plateau(23), deckPioche, deckDefausse, eGaucher, eDroitier);
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
