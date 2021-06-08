package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Database.SauvegarderPartie_DAO;
import Global.Parametre;
import model.Carte;
import model.DeckDefausse;
import model.DeckPioche;
import model.Escrimeur;
import model.IncorrectPlateauException;
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
	
	
	protected TypeEscrimeur getTypeEscrimeur(String text) {
		
		System.out.println(text + "   \n");
		switch (text) {
		case "HUMAIN":
			return TypeEscrimeur.HUMAIN;
		case "IA_FACILE":
			return TypeEscrimeur.IA_FACILE;
		case "IA_MOYENNE":
			return TypeEscrimeur.IA_MOYENNE;
		case "IA_DIFFICILE":
			return TypeEscrimeur.IA_DIFFICILE;
		default:
			System.out.println("passe dans null");
			return null;
		}
	}
	
	@Override
	public boolean chargerPartie(int id) {
		System.out.println("chargerPartie : " + id);
		SauvegarderPartie_DAO SPartie_DAO = new SauvegarderPartie_DAO();
		ResultSet PartieSauvegarde = SPartie_DAO.getIdJeu(id);
		
		try {
		
			int nbDalles = PartieSauvegarde.getInt("nbCasesJeu");
			
			String nameJ1 = PartieSauvegarde.getString("nomJoueurG");
			TypeEscrimeur typeJ1 = getTypeEscrimeur(PartieSauvegarde.getString("TypeEscrimeurG"));
			int posJ1 = PartieSauvegarde.getInt("posJoueurG");
			
			String nameJ2 = PartieSauvegarde.getString("nomJoueurD");
			TypeEscrimeur typeJ2 = getTypeEscrimeur(PartieSauvegarde.getString("TypeEscrimeurD"));
			int posJ2 = PartieSauvegarde.getInt("posJoueurD");
			
			Boolean modeSimple = PartieSauvegarde.getBoolean("modeSimple");
			int nbManches = PartieSauvegarde.getInt("nbManchesWin");
			int nbCarteMaxParJoueur = PartieSauvegarde.getInt("CartesMaxJoueur");
			
			Escrimeur eGaucher = new Escrimeur(nameJ1, typeJ1, Escrimeur.GAUCHER, nbCarteMaxParJoueur);
			JSONArray  mainGaucherJson = new JSONArray(PartieSauvegarde.getString("mainGaucherJSON"));
			eGaucher.setCartes(mainGaucherJson,nbCarteMaxParJoueur);
			eGaucher.setMancheGagner(PartieSauvegarde.getInt("mancheGagnerGauche"));
			
			Escrimeur eDroitier = new Escrimeur(nameJ2, typeJ2, Escrimeur.DROITIER, nbCarteMaxParJoueur);
			JSONArray  mainDroitierJson = new JSONArray(PartieSauvegarde.getString("mainDroitierJSON"));
			eDroitier.setCartes(mainDroitierJson,nbCarteMaxParJoueur);
			eDroitier.setMancheGagner(PartieSauvegarde.getInt("mancheGagnerDroit"));
	
			DeckPioche deckPioche = new DeckPioche();
			JSONArray  PiocheJson = new JSONArray(PartieSauvegarde.getString("piocheJSON"));
			deckPioche.setDeck(PiocheJson);
			
			DeckDefausse deckDefausse = new DeckDefausse();
			JSONArray  DefausseJson = new JSONArray(PartieSauvegarde.getString("DefausseJSON"));
			deckDefausse.setDeck(DefausseJson);
			
			Boolean animationAutoriser;
			
			animationAutoriser = PartieSauvegarde.getBoolean("AnimationAutoriser");
			
			int indiceCurrentEscrimeur = PartieSauvegarde.getInt("indiceCurrentJoueur");
			int indicePremierEscrimeur = PartieSauvegarde.getInt("indicePremierJoueur");
			
			int[] positionDepart = {PartieSauvegarde.getInt("posDepartGauche"), PartieSauvegarde.getInt("posDepartDroit")};
			Plateau plateau = new Plateau(posJ1, posJ2, nbDalles);
			int idPartie = PartieSauvegarde.getInt("idPartie");
			Jeu jeu = new Jeu(modeSimple, plateau, deckPioche, deckDefausse, nbManches, indiceCurrentEscrimeur, eGaucher, eDroitier, positionDepart,indicePremierEscrimeur,null, animationAutoriser,idPartie);
			SuiteChargerPartie(jeu);
			
		
		} catch (SQLException | IncorrectPlateauException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean supprimerPartie(int id) {
		SauvegarderPartie_DAO SPartie_DAO = new SauvegarderPartie_DAO();
		SPartie_DAO.supprimerPartieSauvegardee(id);
		return false;
	}
	
	
}
