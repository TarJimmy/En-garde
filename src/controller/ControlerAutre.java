package controller;

import java.util.ArrayList;
import java.util.List;

import Global.Parametre;
import model.SauvegardeParametre;
import view.CollecteurEvenements;
import view.InterfaceGraphiqueFin;
//import view.InterfaceGraphiqueJeu;
import view.InterfaceGraphiqueMenu;
import view.InterfaceGraphiqueParametres;
import view.InterfaceGraphiqueRegles;

/**
 * 
 * @author La�titia & Delphine
 *
 */
public class ControlerAutre implements CollecteurEvenements {
	
	private List<String> settings;
	
	public ControlerAutre() {}
	
	@Override
	public boolean commande(String c) {
		switch(c) {
			case "menu":
				if (InterfaceGraphiqueParametres.close() || InterfaceGraphiqueFin.close()) {
				InterfaceGraphiqueMenu.demarrer(new ControlerAutre());}
				break;
			case "play":
				InterfaceGraphiqueMenu.close();
				//InterfaceGraphiqueJeu.demarrer(new ControlerJeu());
				break;
			case "chargeGame":
				InterfaceGraphiqueMenu.close();
				//InterfaceGraphiqueJeu.demarrer(new ControlerJeu());
				break;
			case "retry":
				break;
			case "settings":
				InterfaceGraphiqueMenu.close();
				Parametre.instance();
				SauvegardeParametre.chargerParametres();
				settings = SauvegardeParametre.getSettings();
				InterfaceGraphiqueParametres.demarrer(settings, new ControlerAutre());
				break;
			case "saveSettings":
				SauvegardeParametre.sauvegarderParametres(InterfaceGraphiqueParametres.getParametre("map"), InterfaceGraphiqueParametres.getParametre("nomJ1"), InterfaceGraphiqueParametres.getParametre("typeJ1"), InterfaceGraphiqueParametres.getParametre("posJ1"), InterfaceGraphiqueParametres.getParametre("nomJ2"), InterfaceGraphiqueParametres.getParametre("typeJ2"), InterfaceGraphiqueParametres.getParametre("posJ2"), InterfaceGraphiqueParametres.getParametre("modeAttaque"), InterfaceGraphiqueParametres.getParametre("manches"), InterfaceGraphiqueParametres.getParametre("carteMax"), InterfaceGraphiqueParametres.getParametre("carte1"), InterfaceGraphiqueParametres.getParametre("carte2"), InterfaceGraphiqueParametres.getParametre("carte3"), InterfaceGraphiqueParametres.getParametre("carte4"), InterfaceGraphiqueParametres.getParametre("carte5"));
				break;
			case "restoreSettings":
				SauvegardeParametre.chargerParametres();
				settings = SauvegardeParametre.getSettings();
				InterfaceGraphiqueParametres.majParametres(settings);
				break;
			case "tuto":
				break;
			case "rules":
				InterfaceGraphiqueRegles.demarrer();
				break;
			case "exit":
				System.exit(0);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + c);
		}
		return false;
	}

	@Override
	public boolean clickSouris(String s, int l, int c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean selectTypeCarte(String c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean jouerCartes(ArrayList<Integer> carteAAjouer) {
		// TODO Auto-generated method stub
		return false;
	}
}

