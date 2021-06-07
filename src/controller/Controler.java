package controller;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import Database.SauvegarderPartie_DAO;
import Global.Parametre;
import model.Carte;
import model.DeckDefausse;
import model.DeckPioche;
import model.Escrimeur;
import model.Jeu;
import model.Plateau;
import model.SauvegardeParametre;
import model.TypeEscrimeur;
import view.Animation;
import view.CollecteurEvenements;

public abstract class Controler implements CollecteurEvenements {


	@Override
	public boolean animation(String typeAnimation, Animation anim) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean commande(String c) {
		// TODO Auto-generated method stub
		System.out.println("commande not found");
		return false;
	}

	@Override
	public boolean clickCase(int x, int nbCarte) {
		// TODO Auto-generated method stub
		return false;
	}

	public abstract void SuiteChargerPartie(Jeu jeu);
	
	
	@Override
	public boolean chargerPartie(int id) {
		System.out.println("chargerPartie : " + id);
		SauvegarderPartie_DAO SPartie_DAO = new SauvegarderPartie_DAO();
		ResultSet PartieSauvegarde = SPartie_DAO.getIdJeu(id);

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		Parametre.instance();
//		SauvegardeParametre.chargerParametres();
//		settings = SauvegardeParametre.getSettings();
//		Iterator<String> it = settings.iterator();
//		int nbDalles = Integer.parseInt(it.next());
//		String nameJ1 = it.next();
//		TypeEscrimeur typeJ1 = getTypeEscrimeur(it.next());
//		int posJ1 = Integer.parseInt(it.next());
//		
//		String nameJ2 = it.next();
//		TypeEscrimeur typeJ2 = getTypeEscrimeur(it.next());
//		int posJ2 = Integer.parseInt(it.next());
//		
//		Boolean modeSimple = it.next().equals("Basique");
//		int nbManches = Integer.parseInt(it.next());
//		int nbCarteMaxParJoueur = Integer.parseInt(it.next());
//		Escrimeur eGaucher = new Escrimeur(nameJ1, typeJ1, Escrimeur.GAUCHER, nbCarteMaxParJoueur);
//		Escrimeur eDroitier = new Escrimeur(nameJ2, typeJ2, Escrimeur.DROITIER, nbCarteMaxParJoueur);
//		
//		ArrayList<Carte> cartesPrepare = new ArrayList<>();
//		int nbCarteTotal = 0;
//		
//		for(int i = 0; i < 5; i++) {
//			int nbCarteCourant = Integer.parseInt(it.next());
//			for (int j = 0; j < nbCarteCourant; j++) {
//				cartesPrepare.add(new Carte(i + 1));
//				nbCarteTotal++;
//			}
//			
//		}
//		Carte[] cartesDeck = new Carte[nbCarteTotal];
//		int i = 0;
//		for (Carte c1 : cartesPrepare) {
//			cartesDeck[i] = c1;
//			i++;
//		}
//		DeckPioche deckPioche = new DeckPioche(cartesDeck);
//		DeckDefausse deckDefausse = new DeckDefausse();
//		
//		String anim = it.next();
//		Boolean animationAutoriser;
//		if (anim.equals("Actif")) { 
//			animationAutoriser = true;
//		} else {
//			animationAutoriser = false;
//		}
//		int[] positonDepart = {posJ1, posJ2};
//		Plateau plateau = new Plateau(posJ1, posJ2, nbDalles);
//		Jeu jeu = new Jeu(modeSimple, plateau, deckPioche, deckDefausse, nbManches, Escrimeur.GAUCHER, eGaucher, eDroitier, positonDepart, animationAutoriser);
//		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		SuiteChargerPartie(jeu)
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
