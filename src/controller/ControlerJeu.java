package controller;

import java.io.IOException;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import Database.SauvegarderPartie_DAO;
import model.Carte;
import model.Coup;
import model.DeckDefausse;
import model.DeckPioche;
import model.Escrimeur;
import model.Historique;
import model.IncorrectCarteException;
import model.Jeu;
import model.Jeu.Action;
import model.Plateau;
import model.TypeEscrimeur;
import view.Animation;
import view.InterfaceGraphiqueActionAnnexe;
import view.InterfaceGraphiqueJeu;
import view.InterfaceGraphiqueMenu;

public class ControlerJeu extends Controler {
	private Jeu jeu;
	private LinkedList<Animation> animations;
	boolean animationsActives;
	private SauvegarderPartie_DAO partieSauvegardee;
	
	public ControlerJeu(Jeu jeu) {
		this.jeu = jeu;
		this.animations = new LinkedList<>();
		animationsActives = false;
		jeu.getDeckPioche().melanger();
		piocher(jeu.getEscrimeurGaucher());
		piocher(jeu.getEscrimeurDroitier());
		partieSauvegardee = new SauvegarderPartie_DAO();
		partieSauvegardee.sauvegardeJeu(jeu);
	}

	public void piocher(Escrimeur e) {
		jeu.piocher(e);
	}
	
	@Override
	public boolean clickCase(int x, int nbCartesAUtiliser) {
		int nbCartesAttaque = nbCartesAUtiliser;
		Coup dernierCoup = jeu.getHistorique().voirDernierCoup();
		Escrimeur currentEscrimeur = jeu.getCurrentEscrimeur();
		int indiceCurrentEscrimeur = jeu.getIndiceCurrentEscrimeur();
		Plateau plateau = jeu.getPlateau();
		int positionCurrentEscrimeur = plateau.getPosition(indiceCurrentEscrimeur);
		int positionNotCurrentEscrimeur = plateau.getPosition(((indiceCurrentEscrimeur + 1) % 2));
		Coup coupAJouer;
		Carte[] cartesAJouer;

		if (positionCurrentEscrimeur == x) {
			//il faut defendre
			int puissanceAttaque = jeu.getHistorique().voirDernierCoup().getCartes().length;
			cartesAJouer = new Carte[puissanceAttaque];
			int i = 0;
			int nbCartesCurrentEscrimeur = currentEscrimeur.getNbCartes();
			int distanceAttaque = plateau.getPosition(Escrimeur.DROITIER) - plateau.getPosition(Escrimeur.GAUCHER);
			while (i < nbCartesCurrentEscrimeur && puissanceAttaque > 0) {
				if (currentEscrimeur.getCarte(i) != null && currentEscrimeur.getCarte(i).getDistance() == distanceAttaque) {
					cartesAJouer[puissanceAttaque-1] = currentEscrimeur.getCarte(i);
					puissanceAttaque --;
				}
				i++;
			}
			coupAJouer = new Coup(currentEscrimeur, cartesAJouer, Coup.PARER);
		} else if(positionNotCurrentEscrimeur == x){
			//attaquer
			cartesAJouer = new Carte[nbCartesAttaque];
			int i = 0;
			int nbCartesCurrentEscrimeur = currentEscrimeur.getNbCartes();
			int distanceAttaque = plateau.getPosition(Escrimeur.DROITIER) - plateau.getPosition(Escrimeur.GAUCHER);
			while (i < nbCartesCurrentEscrimeur && nbCartesAttaque != 0) {
				if (currentEscrimeur.getCarte(i) != null && currentEscrimeur.getCarte(i).getDistance() == distanceAttaque) {
					cartesAJouer[nbCartesAttaque-1] = currentEscrimeur.getCarte(i);
					nbCartesAttaque--;
				}
				i++;
			}	
			int typeAttaque;
			if (dernierCoup != null && dernierCoup.getEscrimeur() == currentEscrimeur && dernierCoup.getAction() == Coup.AVANCER) {
				typeAttaque = Coup.ATTAQUEINDIRECTE;
			} else {
				typeAttaque = Coup.ATTAQUEDIRECTE;
			}
			coupAJouer = new Coup(currentEscrimeur, cartesAJouer, typeAttaque);
		} else if((currentEscrimeur.getIsGaucher() && x > positionCurrentEscrimeur) || (!currentEscrimeur.getIsGaucher() && x < positionCurrentEscrimeur)) {
			//avancer
			cartesAJouer = new Carte[1];
			int distanceClick = Math.abs(x - positionCurrentEscrimeur);
			int i = 0;
			int nbCartesCurrentEscrimeur = currentEscrimeur.getNbCartes();
			while(i < nbCartesCurrentEscrimeur && cartesAJouer[0] == null) {
				if(currentEscrimeur.getCarte(i) != null && currentEscrimeur.getCarte(i).getDistance() == distanceClick) {
					cartesAJouer[0] = currentEscrimeur.getCarte(i);
					System.out.println("Ajout de carte");
				}
				i++;
			}
			coupAJouer = new Coup(currentEscrimeur, cartesAJouer, Coup.AVANCER);
		} else {
			//reculer ou esquiver
			cartesAJouer = new Carte[1];
			int distanceClick = Math.abs(x - positionCurrentEscrimeur);
			int i = 0;
			int nbCartesCurrentEscrimeur = currentEscrimeur.getNbCartes();
			while (i < nbCartesCurrentEscrimeur && cartesAJouer[0] == null) {
				if (currentEscrimeur.getCarte(i) != null && currentEscrimeur.getCarte(i).getDistance() == distanceClick) {
					cartesAJouer[0] = currentEscrimeur.getCarte(i);
				}
				i++;
			}
			if (dernierCoup != null && dernierCoup.getAction() == Coup.ATTAQUEINDIRECTE) {
				coupAJouer = new Coup(currentEscrimeur, cartesAJouer, Coup.ESQUIVER);
			} else {
				coupAJouer = new Coup(currentEscrimeur, cartesAJouer, Coup.RECULER);
			}
		}
		jeu.jouer(coupAJouer, false);
		if (jeu.isDernierTour()) {
			finDeManche(null);
		} else {
			HashSet<Integer> cj = jeu.casesJouables();
			if ((!jeu.getDeckPioche().deckVide() && cj.isEmpty()) || (cj.isEmpty() && (jeu.getHistorique().voirDernierCoup().getAction() == Coup.ATTAQUEDIRECTE || jeu.getHistorique().voirDernierCoup().getAction() == Coup.ATTAQUEINDIRECTE))) {
				System.out.println("aucune case jouable");
				finDeManche(jeu.getNotCurrentEscrimeur());
			} else if (jeu.getDeckPioche().deckVide()) {
				finDeManche(null);
			}
		}
		HashSet<Integer> cases = jeu.casesJouables();
		if(cases.size() == 1 && cases.contains(-1)) {
			jeu.changerTour();
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
			if ((action == Coup.ATTAQUEDIRECTE  || action == Coup.ATTAQUEINDIRECTE) && !jeu.casesJouables().isEmpty()) {
					jeu.setDernierTour(true);
			} else {
				if (action == Coup.ATTAQUEDIRECTE  || action == Coup.ATTAQUEINDIRECTE) {
					winner = jeu.getHistorique().voirDernierCoup().getEscrimeur();
				} else {
					int avantageGaucher = 0;
					int nbCartes = gaucher.getNbCartes();
					int distanceAttaque = plateau.getPosition(Escrimeur.DROITIER) - plateau.getPosition(Escrimeur.GAUCHER);
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
						//checker le joueur qui a le plus avance
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
					jeu.setIndiceWinnerManche(winner.getIndice());
					if(winner.getMancheGagner() != jeu.getNbManchesPourVictoire()) {
						jeu.nouvelleManche();
					} else {
						//fin de partie, winner gagne
					}
				} else {
					jeu.setIndiceWinnerManche(jeu.NONE);
					jeu.nouvelleManche();
				}
			}
		} else {
			winner.addMancheGagnee();
			jeu.setIndiceWinnerManche(winner.getIndice());
			if(winner.getMancheGagner() != jeu.getNbManchesPourVictoire()) {
				jeu.nouvelleManche();
			}else {
				//fin de partie, winner gagne
			}
		}
	}
	
	@Override
	public boolean animation(String commande, Animation anim) {
		switch (commande) {
			case "Ajouter":
				animations.add(anim);
				jeu.modifieVue(Action.ANIMATION_LANCER);
				commande("ActionTerminer");
				return true;
			case "Terminer":
				animations.remove(anim);
				animationsActives = false;
				commande("ActionTerminer");
				return true;
			case "Lancer":
				if (!animations.isEmpty() && !animationsActives) {
					animationsActives = true;
					animations.peek().demarre();
				}
				return true;
			default:
				System.out.println("Animation pas traité : " + commande);
				break;
			}
		return false;
	}

	@Override
	public boolean commande(String c) {
		switch (c) {
			case "ActionTerminer":
				jeu.actionTerminer();
				return true;
			case "ActionLancer":
				jeu.demarreActionSuivante();
				return true;
			case "PasserTour":
				jeu.changerTour();
				return true;
			case "Menu":
				InterfaceGraphiqueActionAnnexe.close();
				InterfaceGraphiqueJeu.close();
				InterfaceGraphiqueMenu.demarrer(new ControlerAutre());
				return true;
			case "QuitterJeu":
				System.exit(0);
				return false;
			case "nouvellePartie":
				return true;
			case "sauvPartie":
				return false;
			case "chargePartie":
				return true;
			case "annuleCoup":
				jeu.getHistorique().annulerCoup();
				return false;
			case "refaireCoup":
				jeu.getHistorique().rejouerCoupAnnule();
				return true;
			case "close":
				InterfaceGraphiqueActionAnnexe.close();
				return false;
			default:
				System.out.println("Commande pas traitee : " + c);
				return false;
		}
	}
}
