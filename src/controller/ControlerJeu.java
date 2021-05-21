package controller;

import java.io.IOException;

import java.util.ArrayList;

import model.Carte;
import model.DeckDefausse;
import model.DeckPioche;
import model.Escrimeur;
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

	public static void main(String[] args) {
		Carte cartes[] = new Carte[25];

		for (int i = 0; i < cartes.length; i++) {
			try {
				cartes[i] = new Carte((i % 5) + 1) ;
			} catch (IncorrectCarteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		DeckPioche deckPioche = new DeckPioche(cartes);
		deckPioche.melanger();
		DeckDefausse deckDefausse = new DeckDefausse();
		Escrimeur eGaucher = new Escrimeur("Gaucher", TypeEscrimeur.HUMAIN, true, 5);
		Escrimeur eDroitier = new Escrimeur("Droitier", TypeEscrimeur.HUMAIN, false, 5);
		Jeu jeu = null;
		try {
			jeu = new Jeu(true, new Plateau(23), deckPioche, deckDefausse, eGaucher, eDroitier);
		} catch (IncorrectPlateauException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ControlerJeu cJeu = new ControlerJeu(jeu);

		jeu.piocher(eDroitier);

		jeu.piocher(eGaucher);

		InterfaceGraphiqueJeu.demarrer(cJeu, jeu);
	}
}
