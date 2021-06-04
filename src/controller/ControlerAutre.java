package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Global.Parametre;
import model.Carte;
import model.DeckDefausse;
import model.DeckPioche;
import model.Escrimeur;
import model.IncorrectCarteException;
import model.IncorrectPlateauException;
import model.Jeu;
import model.Plateau;
import model.SauvegardeParametre;
import model.TypeEscrimeur;
import view.InterfaceGraphiqueChargerPartie;
import view.InterfaceGraphiqueFin;
import view.InterfaceGraphiqueJeu;
import view.InterfaceGraphiqueMenu;
import view.InterfaceGraphiqueParametres;
import view.InterfaceGraphiqueRegles;

/**
 * 
 * @author Laetitia & Delphine
 *
 */
public class ControlerAutre extends Controler {
	
	private List<String> settings;
	
	public ControlerAutre() {}
	
	@Override
	public boolean commande(String c) {
		switch(c) {
			case "annuler":
				InterfaceGraphiqueChargerPartie.close();
			case "menu":
				InterfaceGraphiqueParametres.close();
				InterfaceGraphiqueFin.close();
				InterfaceGraphiqueMenu.demarrer(new ControlerAutre());
				break;
			case "jouerMenu":
				InterfaceGraphiqueMenu.close();
			case "jouer":
				try {
					Parametre.instance();
					SauvegardeParametre.chargerParametres();
					settings = SauvegardeParametre.getSettings();
					Iterator<String> it = settings.iterator();
					int nbDalles = Integer.parseInt(it.next());
					String nameJ1 = it.next();
					TypeEscrimeur typeJ1 = getTypeEscrimeur(it.next());
					int posJ1 = Integer.parseInt(it.next());
					
					String nameJ2 = it.next();
					TypeEscrimeur typeJ2 = getTypeEscrimeur(it.next());
					int posJ2 = Integer.parseInt(it.next());
					
					Boolean modeSimple = it.next().equals("Basique");
					int nbManches = Integer.parseInt(it.next());
					int nbCarteMaxParJoueur = Integer.parseInt(it.next());
					Escrimeur eGaucher = new Escrimeur(nameJ1, typeJ1, Escrimeur.GAUCHER, nbCarteMaxParJoueur);
					Escrimeur eDroitier = new Escrimeur(nameJ2, typeJ2, Escrimeur.DROITIER, nbCarteMaxParJoueur);
					
					ArrayList<Carte> cartesPrepare = new ArrayList<>();
					int nbCarteTotal = 0;
					
					for(int i = 0; i < 5; i++) {
						int nbCarteCourant = Integer.parseInt(it.next());
						for (int j = 0; j < nbCarteCourant; j++) {
							cartesPrepare.add(new Carte(i + 1));
							nbCarteTotal++;
						}
						
					}
					Carte[] cartesDeck = new Carte[nbCarteTotal];
					int i = 0;
					for (Carte c1 : cartesPrepare) {
						cartesDeck[i] = c1;
						i++;
					}
					DeckPioche deckPioche = new DeckPioche(cartesDeck);
					DeckDefausse deckDefausse = new DeckDefausse();
					
					Plateau plateau = new Plateau(posJ1, posJ2, nbDalles);
					Jeu jeu = new Jeu(modeSimple, plateau, deckPioche, deckDefausse, nbManches, Escrimeur.GAUCHER, eGaucher, eDroitier);
					
					ControlerJeu cJeu = new ControlerJeu(jeu);
					
					InterfaceGraphiqueJeu.demarrer(cJeu, jeu);
				} catch (IncorrectPlateauException e) {
					e.printStackTrace();
				} catch (IncorrectCarteException e) {
					e.printStackTrace();
				}
				break;
			case "nouvellePartie":
				commande("jouer");
				break;
			case "chargePartieMenu":
				InterfaceGraphiqueMenu.close();
				InterfaceGraphiqueChargerPartie.demarrer(new ControlerAutre());
				break;
			case "rejouer":
				break;
			case "parametres":
				InterfaceGraphiqueMenu.close();
				Parametre.instance();
				SauvegardeParametre.chargerParametres();
				settings = SauvegardeParametre.getSettings();
				InterfaceGraphiqueParametres.demarrer(settings, new ControlerAutre());
				break;
			case "sauvePara":
				SauvegardeParametre.sauvegarderParametres(InterfaceGraphiqueParametres.getParametre("map"), InterfaceGraphiqueParametres.getParametre("nomJ1"), InterfaceGraphiqueParametres.getParametre("typeJ1"), InterfaceGraphiqueParametres.getParametre("posJ1"), InterfaceGraphiqueParametres.getParametre("nomJ2"), InterfaceGraphiqueParametres.getParametre("typeJ2"), InterfaceGraphiqueParametres.getParametre("posJ2"), InterfaceGraphiqueParametres.getParametre("modeAttaque"), InterfaceGraphiqueParametres.getParametre("manches"), InterfaceGraphiqueParametres.getParametre("carteMax"), InterfaceGraphiqueParametres.getParametre("carte1"), InterfaceGraphiqueParametres.getParametre("carte2"), InterfaceGraphiqueParametres.getParametre("carte3"), InterfaceGraphiqueParametres.getParametre("carte4"), InterfaceGraphiqueParametres.getParametre("carte5"));
				break;
			case "restaurePara":
				SauvegardeParametre.chargerParametres();
				settings = SauvegardeParametre.getSettings();
				InterfaceGraphiqueParametres.majParametres(settings);
				break;
			case "tuto":
				break;
			case "regles":
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
	private TypeEscrimeur getTypeEscrimeur(String text) {
		switch (text) {
		case "Humain":
			return TypeEscrimeur.HUMAIN;
		case "IA Facile":
			return TypeEscrimeur.IA_FACILE;
		case "IA Moyenne":
			return TypeEscrimeur.IA_MOYENNE;
		case "IA Difficile":
			return TypeEscrimeur.IA_DIFFICILE;
		default:
			return null;
		}
	}
}

